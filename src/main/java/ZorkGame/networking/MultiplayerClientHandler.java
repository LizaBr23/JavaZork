package ZorkGame.networking;

import ZorkGame.networking.NetworkProtocol.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiplayerClientHandler implements Runnable {
    private final Socket socket;
    private final String playerId;
    private final MultiplayerGameServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean running;
    private final BlockingQueue<Message> outgoingMessages;

    public MultiplayerClientHandler(Socket socket, String playerId, MultiplayerGameServer server) {
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

            sendMessage(new Message(MessageType.ACK, "SERVER", "Welcome!\nYou are: " + playerId));

            String playersHere = server.getPlayersInRoom(playerId);
            if (!playersHere.isEmpty()) {
                sendMessage(new Message(MessageType.PLAYER_LOCATION, "SERVER", playersHere));
            }

            Thread senderThread = new Thread(this::processSendQueue);
            senderThread.setDaemon(true);
            senderThread.start();

            server.broadcastMessage(new Message(MessageType.PLAYER_JOIN, "SERVER", playerId + " joined"), playerId);

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
                server.broadcastMessage(new Message(MessageType.CHAT_MESSAGE, playerId, message.getData()), playerId);
                break;
            case PLAYER_MOVE:
            case PLAYER_ACTION:
                String result = server.processPlayerCommand(playerId, message.getData());
                sendMessage(new Message(MessageType.ACK, "SERVER", result));
                String playersHere = server.getPlayersInRoom(playerId);
                if (!playersHere.isEmpty()) {
                    sendMessage(new Message(MessageType.PLAYER_LOCATION, "SERVER", playersHere));
                }
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

    public void disconnect() {
        running = false;
        cleanup();
    }

    private void cleanup() {
        running = false;
        try { if (in != null) in.close(); } catch (Exception e) {}
        try { if (out != null) out.close(); } catch (Exception e) {}
        try { if (socket != null) socket.close(); } catch (Exception e) {}
        server.removeClient(playerId);
        server.broadcastMessage(new Message(MessageType.PLAYER_LEAVE, "SERVER", playerId + " left"), null);
    }
}
