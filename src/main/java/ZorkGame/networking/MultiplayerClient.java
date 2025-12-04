package ZorkGame.networking;

import ZorkGame.networking.NetworkProtocol.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiplayerClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean running;
    private String playerId;
    private final BlockingQueue<Message> outgoingMessages;

    public MultiplayerClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.running = false;
        this.outgoingMessages = new LinkedBlockingQueue<>();
    }

    public MultiplayerClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            running = true;

            Thread receiverThread = new Thread(this::receiveMessages);
            receiverThread.setDaemon(false);
            receiverThread.start();

            Thread senderThread = new Thread(this::sendMessages);
            senderThread.setDaemon(false);
            senderThread.start();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void startGameLoop() {

        try (Scanner scanner = new Scanner(System.in)) {
            while (playerId == null && running) {
                try {
                    Thread.sleep(100);
                } catch (Exception ignored) {
                }
            }
            if (playerId == null) {
                System.err.println("Failed to connect to server");
                disconnect();
                return;
            }
            System.out.println("Connected! Type commands or /quit to exit.");
            while (running) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                if (input.startsWith("/")) {
                    handleClientCommand(input);
                    continue;
                }

                sendMessage(new Message(MessageType.PLAYER_ACTION, playerId, input));
            }
        } catch (Exception e) {
            System.err.println("Input error: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void handleClientCommand(String input) {
        String[] parts = input.substring(1).split("\\s+", 2);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "chat":
            case "say":
                if (parts.length > 1) {
                    System.out.println("[CHAT] You: " + parts[1]);
                    sendMessage(new Message(MessageType.CHAT_MESSAGE, playerId, parts[1]));
                }
                break;
            case "quit":
            case "disconnect":
                disconnect();
                break;
        }
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
                String data = message.getData();
                if (data.startsWith("Welcome")) {
                    String[] lines = data.split("\n");
                    for (String line : lines) {
                        if (line.startsWith("You are: ")) {
                            playerId = line.substring(9);
                        }
                    }
                }
                System.out.println("\n" + data);
                System.out.print("> ");
                break;
            case PLAYER_JOIN:
                System.out.println("\n[+] " + message.getData());
                System.out.print("> ");
                break;
            case PLAYER_LEAVE:
                System.out.println("\n[-] " + message.getData());
                System.out.print("> ");
                break;
            case CHAT_MESSAGE:
                System.out.println("\n[CHAT] " + message.getSenderId() + ": " + message.getData());
                System.out.print("> ");
                break;
            case PLAYER_LOCATION:
                System.out.println(message.getData());
                System.out.print("> ");
                break;
            case PLAYER_ACTION:
                System.out.println("\n[" + message.getSenderId() + "] " + message.getData());
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

        try { if (out != null) sendMessage(new Message(MessageType.DISCONNECT, playerId, "Goodbye")); } catch (Exception e) {}
        try { if (in != null) in.close(); } catch (Exception e) {}
        try { if (out != null) out.close(); } catch (Exception e) {}
        try { if (socket != null) socket.close(); } catch (Exception e) {}
    }

    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try { port = Integer.parseInt(args[1]); } catch (Exception e) {}
        }

        MultiplayerClient client = new MultiplayerClient(host, port);
        if (client.connect()) {
            Runtime.getRuntime().addShutdownHook(new Thread(client::disconnect));
            client.startGameLoop();
        }
    }
}
