package ZorkGame.networking;

import ZorkGame.networking.NetworkProtocol.*;
import ZorkGame.game.ZorkULGame;
import ZorkGame.commands.Command;
import ZorkGame.commands.Parser;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean running;
    private String playerId;
    private ZorkULGame game;
    private Parser parser;
    private final BlockingQueue<Message> outgoingMessages;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.running = false;
        this.outgoingMessages = new LinkedBlockingQueue<>();
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            running = true;

            Thread receiverThread = new Thread(this::receiveMessages);
            receiverThread.setDaemon(true);
            receiverThread.start();

            Thread senderThread = new Thread(this::sendMessages);
            senderThread.setDaemon(true);
            senderThread.start();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void initializeGame(String playerName) {
        this.playerId = playerName;
        this.game = new ZorkULGame(playerName);
        this.parser = new Parser();
        sendMessage(new Message(MessageType.CONNECT, playerId, "Joined"));
        System.out.println(game.getPlayer().getCurrentRoom().getLongDescription());
    }

    public void startGameLoop() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            if (input.startsWith("/")) {
                handleMultiplayerCommand(input);
                continue;
            }

            Command command = parser.getCommand();
            if (command != null) {
                boolean finished = game.processCommand(command);
                if (finished) {
                    disconnect();
                    break;
                }
                notifyPlayerAction();
            }
        }
        scanner.close();
    }

    private void handleMultiplayerCommand(String input) {
        String[] parts = input.substring(1).split("\\s+", 2);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "chat":
            case "say":
                if (parts.length > 1) {
                    sendChatMessage(parts[1]);
                }
                break;
            case "disconnect":
            case "quit":
                disconnect();
                break;
        }
    }

    private void sendChatMessage(String message) {
        sendMessage(new Message(MessageType.CHAT_MESSAGE, playerId, message));
    }

    private void notifyPlayerAction() {
        String roomDesc = game.getPlayer().getCurrentRoom().getDescription();
        sendMessage(new Message(MessageType.PLAYER_MOVE, playerId, roomDesc));
    }

    public void sendMessage(Message message) {
        outgoingMessages.offer(message);
    }

    private void sendMessages() {
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

    private void receiveMessages() {
        while (running) {
            try {
                Message message = (Message) in.readObject();
                handleServerMessage(message);
            } catch (Exception e) {
                running = false;
                break;
            }
        }
    }

    private void handleServerMessage(Message message) {
        switch (message.getType()) {
            case ACK:
                System.out.println("[SERVER] " + message.getData());
                break;
            case PLAYER_JOIN:
            case PLAYER_LEAVE:
                System.out.println("\n[*] " + message.getData());
                break;
            case CHAT_MESSAGE:
                System.out.println("\n[" + message.getSenderId() + "] " + message.getData());
                System.out.print("> ");
                break;
            case PLAYER_LOCATION:
                System.out.println("\n[*] " + message.getSenderId() + " moved to: " + message.getData());
                System.out.print("> ");
                break;
            case PLAYER_ACTION:
                System.out.println("\n[*] " + message.getSenderId() + " performed: " + message.getData());
                System.out.print("> ");
                break;
            case DISCONNECT:
                running = false;
                break;
        }
    }

    public void disconnect() {
        if (!running) return;
        running = false;

        try { if (out != null) sendMessage(new Message(MessageType.DISCONNECT, playerId, "Leaving")); } catch (Exception e) {}
        try { if (in != null) in.close(); } catch (Exception e) {}
        try { if (out != null) out.close(); } catch (Exception e) {}
        try { if (socket != null) socket.close(); } catch (Exception e) {}
    }

    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        String playerName = "Player";

        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try { port = Integer.parseInt(args[1]); } catch (Exception e) {}
        }
        if (args.length >= 3) playerName = args[2];

        NetworkClient client = new NetworkClient(host, port);
        if (client.connect()) {
            Runtime.getRuntime().addShutdownHook(new Thread(client::disconnect));
            client.initializeGame(playerName);
            client.startGameLoop();
        }
    }
}
