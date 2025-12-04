package ZorkGame.networking;

import ZorkGame.networking.NetworkProtocol.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_PLAYERS = 4;
    private static final int SOCKET_TIMEOUT = 1000;

    private final int port;
    private final int maxPlayers;
    private ServerSocket serverSocket;
    private volatile boolean running;
    private final List<ClientHandler> clients;
    private final ExecutorService clientExecutor;
    private int nextPlayerId;

    public GameServer(int port, int maxPlayers) {
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.clients = new CopyOnWriteArrayList<>();
        this.clientExecutor = Executors.newCachedThreadPool();
        this.nextPlayerId = 1;
        this.running = false;
    }

    public void start() {
        if (running) return;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(SOCKET_TIMEOUT);
            running = true;

            System.out.println("[SERVER] Started on port " + port);

            Thread acceptThread = new Thread(this::acceptClients);
            acceptThread.setDaemon(false);
            acceptThread.start();
        } catch (IOException e) {
            running = false;
        }
    }

    private void acceptClients() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();

                if (clients.size() >= maxPlayers) {
                    clientSocket.close();
                    continue;
                }

                String playerId = "Player" + nextPlayerId++;
                ClientHandler clientHandler = new ClientHandler(clientSocket, playerId, this);
                clients.add(clientHandler);
                clientExecutor.submit(clientHandler);
            } catch (IOException e) {
            }
        }
    }

    public void broadcastMessage(Message message, ClientHandler exclude) {
        for (ClientHandler client : clients) {
            if (client != exclude && client.isRunning()) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void stop() {
        if (!running) return;
        running = false;

        for (ClientHandler client : clients) {
            client.disconnect();
        }
        clients.clear();

        clientExecutor.shutdown();
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) {}
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length >= 1) {
            try { port = Integer.parseInt(args[0]); } catch (Exception e) {}
        }

        GameServer server = new GameServer(port, MAX_PLAYERS);
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}
