package ZorkGame.networking;

import ZorkGame.networking.NetworkProtocol.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final String playerId;
    private final GameServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean running;
    private final BlockingQueue<Message> outgoingMessages;

    public ClientHandler(Socket socket, String playerId, GameServer server) {
        this.socket = socket;
        this.playerId = playerId;
        this.server = server;
        this.running = true;
        this.outgoingMessages = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            sendMessage(new Message(MessageType.ACK, "SERVER", "Connected as " + playerId));
            server.broadcastMessage(new Message(MessageType.PLAYER_JOIN, "SERVER", playerId + " joined"), this);

            Thread senderThread = new Thread(this::processSendQueue);
            senderThread.setDaemon(true);
            senderThread.start();

            while (running) {
                Message message = (Message) in.readObject();
                handleMessage(message);
            }
        } catch (Exception e) {
        } finally {
            cleanup();
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case CHAT_MESSAGE:
                server.broadcastMessage(message, null);
                break;
            case PLAYER_MOVE:
                server.broadcastMessage(new Message(MessageType.PLAYER_LOCATION, playerId, message.getData()), this);
                break;
            case PLAYER_ACTION:
                server.broadcastMessage(new Message(MessageType.PLAYER_ACTION, playerId, message.getData()), this);
                break;
            case ITEM_PICKUP:
                server.broadcastMessage(new Message(MessageType.ITEM_PICKUP, playerId, message.getData()), this);
                break;
            case ITEM_DROP:
                server.broadcastMessage(new Message(MessageType.ITEM_DROP, playerId, message.getData()), this);
                break;
            case DISCONNECT:
                running = false;
                break;
        }
    }

    private void processSendQueue() {
        while (running) {
            try {
                Message message = outgoingMessages.take();
                synchronized (out) {
                    out.writeObject(message);
                    out.flush();
                }
            } catch (Exception e) {
                running = false;
                break;
            }
        }
    }

    public void sendMessage(Message message) {
        outgoingMessages.offer(message);
    }

    public String getPlayerId() {
        return playerId;
    }

    public boolean isRunning() {
        return running && !socket.isClosed();
    }

    public void disconnect() {
        running = false;
        cleanup();
    }

    private void cleanup() {
        running = false;
        try { if (in != null) in.close(); } catch (Exception e) {}
        try { if (out != null) out.close(); } catch (Exception e) {}
        try { if (socket != null) socket.close(); } catch (Exception e) {}
        server.removeClient(this);
        server.broadcastMessage(new Message(MessageType.PLAYER_LEAVE, "SERVER", playerId + " left"), null);
    }
}
