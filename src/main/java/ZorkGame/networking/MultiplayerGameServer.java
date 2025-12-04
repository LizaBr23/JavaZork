package ZorkGame.networking;

import ZorkGame.game.ZorkULGame;
import ZorkGame.commands.Command;
import ZorkGame.commands.Parser;
import ZorkGame.models.Character;
import ZorkGame.models.Room;
import ZorkGame.networking.NetworkProtocol.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

public class MultiplayerGameServer {
    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_PLAYERS = 4;
    private static final int SOCKET_TIMEOUT = 1000;

    private final int port;
    private final int maxPlayers;
    private ServerSocket serverSocket;
    private volatile boolean running;
    private final Map<String, MultiplayerClientHandler> clients;
    private final Map<String, Character> players;
    private final ExecutorService clientExecutor;
    private final Parser parser;
    private ZorkULGame sharedWorld;
    private int nextPlayerId;

    public MultiplayerGameServer(int port, int maxPlayers) {
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.clients = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        this.clientExecutor = Executors.newCachedThreadPool();
        this.parser = new Parser(true);
        this.nextPlayerId = 1;
        this.running = false;
    }

    public MultiplayerGameServer() {
        this(DEFAULT_PORT, MAX_PLAYERS);
    }

    public void start() {
        if (running) return;

        sharedWorld = new ZorkULGame("SharedWorld");

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
                Room startingRoom = sharedWorld.getPlayer().getCurrentRoom();
                Character playerChar = new Character(playerId, startingRoom);
                players.put(playerId, playerChar);

                MultiplayerClientHandler handler = new MultiplayerClientHandler(clientSocket, playerId, this);
                clients.put(playerId, handler);
                clientExecutor.submit(handler);
            } catch (IOException e) {
            }
        }
    }

    public synchronized String processPlayerCommand(String playerId, String commandString) {
        Character player = players.get(playerId);
        if (player == null) return "Error";

        Command command = parser.parseCommandString(commandString);
        if (command == null || command.getCommandType() == null) return "Invalid command";

        String result = executeCommandForPlayer(player, command);
        broadcastPlayerAction(playerId, commandString);
        return result;
    }

    private String executeCommandForPlayer(Character player, Command command) {
        return switch (command.getCommandType()) {
            case GO -> handlePlayerMove(player, command.getSecondWord());
            case TAKE -> handlePlayerTake(player, command.getSecondWord());
            case DROP -> handlePlayerDrop(player, command.getSecondWord());
            case INVENTORY, INV -> player.listInventory();
            case HELP -> "go/take/drop/inventory/help";
            default -> "Not supported";
        };
    }

    private String handlePlayerMove(Character player, String direction) {
        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) return "Can't go that way";
        player.setCurrentRoom(nextRoom);
        return "Moved " + direction + "\n" + nextRoom.getLongDescription();
    }

    private String handlePlayerTake(Character player, String itemName) {
        if (itemName == null || itemName.isEmpty()) return "Take what?";
        player.takeItem(itemName);
        return "OK";
    }

    private String handlePlayerDrop(Character player, String itemName) {
        if (itemName == null || itemName.isEmpty()) return "Drop what?";
        player.dropItem(itemName);
        return "OK";
    }

    private void broadcastPlayerAction(String playerId, String action) {
        Character player = players.get(playerId);
        if (player == null) return;

        Room playerRoom = player.getCurrentRoom();
        for (Map.Entry<String, Character> entry : players.entrySet()) {
            if (!entry.getKey().equals(playerId) && entry.getValue().getCurrentRoom() == playerRoom) {
                MultiplayerClientHandler client = clients.get(entry.getKey());
                if (client != null) {
                    client.sendMessage(new Message(MessageType.PLAYER_ACTION, playerId, action));
                }
            }
        }
    }

    public String getPlayersInRoom(String playerId) {
        Character player = players.get(playerId);
        if (player == null) return "";

        Room playerRoom = player.getCurrentRoom();
        StringBuilder sb = new StringBuilder("\nPlayers here: ");
        int count = 0;

        for (Map.Entry<String, Character> entry : players.entrySet()) {
            if (entry.getValue().getCurrentRoom() == playerRoom) {
                if (count > 0) sb.append(", ");
                sb.append(entry.getKey());
                count++;
            }
        }
        return count > 1 ? sb.toString() : "";
    }

    public void broadcastMessage(Message message, String excludePlayerId) {
        for (Map.Entry<String, MultiplayerClientHandler> entry : clients.entrySet()) {
            if (!entry.getKey().equals(excludePlayerId)) {
                entry.getValue().sendMessage(message);
            }
        }
    }

    public void removeClient(String playerId) {
        clients.remove(playerId);
        players.remove(playerId);
    }

    public void stop() {
        if (!running) return;
        running = false;

        for (MultiplayerClientHandler client : clients.values()) {
            client.disconnect();
        }
        clients.clear();
        players.clear();

        clientExecutor.shutdown();
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) {}
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length >= 1) {
            try { port = Integer.parseInt(args[0]); } catch (Exception e) {}
        }

        MultiplayerGameServer server = new MultiplayerGameServer(port, MAX_PLAYERS);
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}
