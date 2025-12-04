package ZorkGame.game;
/* This game is a classic text-based adventure set in a university environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   lecture theatre, campus pub, computing lab, and admin office using simple text commands (e.g., "go east", "go west").
    The game provides descriptions of each location and lists possible exits.

Key features include:
Room navigation: Moving among interconnected rooms with named exits.
Simple command parser: Recognizes a limited set of commands like "go", "help", and "quit".
Player character: Tracks current location and handles moving between rooms.
Text descriptions: Provides immersive text output describing the player's surroundings and available options.
Help system: Lists valid commands to guide the player.
Overall, it recreates the classic Zork interactive fiction experience with a university-themed setting, 
emphasizing exploration and simple command-driven gameplay
*/

import ZorkGame.commands.Parser;
import ZorkGame.commands.Command;
import ZorkGame.enums.CommandType;
import ZorkGame.exceptions.GameLoadException;
import ZorkGame.exceptions.GameSaveException;
import ZorkGame.utils.GameSaver;
import ZorkGame.dialogue.GenericDialogHandler;
import ZorkGame.enums.Direction;
import ZorkGame.enums.NPCType;
import ZorkGame.models.Character;
import ZorkGame.models.GenericItem;
import ZorkGame.models.NPC;
import ZorkGame.models.RawMaterial;
import ZorkGame.models.Room;
import ZorkGame.threading.TimedEventManager;
import ZorkGame.threading.HintEvent;
import ZorkGame.threading.NPCMovementThread;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;


public class ZorkULGame {
    private final Parser parser;
    private Character player;
    private final String name;
    private TimedEventManager eventManager;
    private List<NPCMovementThread> npcThreads;

    public ZorkULGame(String name) {
        this.name = name;
        createRooms();
        parser = new Parser();
        initializeThreading();
    }

    private void initializeThreading() {
        // Initialize event manager for timed events
        eventManager = new TimedEventManager();
        npcThreads = new ArrayList<>();

        // Schedule periodic hint event (every 2 minutes)
        HintEvent hintEvent = new HintEvent(player, 120000); // 120000ms = 2 minutes
        eventManager.scheduleRepeating(hintEvent);

        // Start NPC movement threads
        startNPCMovement();
    }

    private void startNPCMovement() {
        // Get all rooms for NPC movement
        List<Room> allRooms = getAllRooms();

        // Find Bob and start his movement thread
        for (Room room : allRooms) {
            for (NPC npc : room.getNPCs()) {
                // Create movement thread for each NPC (moves every 45 seconds)
                NPCMovementThread movementThread = new NPCMovementThread(npc, allRooms, 45000);
                movementThread.start();
                npcThreads.add(movementThread);
            }
        }
    }

    private List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        Room currentRoom = player.getCurrentRoom();

        // Traverse all connected rooms using BFS
        Set<Room> visited = new java.util.HashSet<>();
        java.util.Queue<Room> queue = new java.util.LinkedList<>();
        queue.add(currentRoom);
        visited.add(currentRoom);

        while (!queue.isEmpty()) {
            Room room = queue.poll();
            rooms.add(room);

            // Check all directions
            for (Direction direction : Direction.values()) {
                Room neighbor = room.getExit(direction.getDirectionName());
                if (neighbor != null && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return rooms;
    }

    public void shutdown() {
        // Stop all NPC movement threads
        if (npcThreads != null) {
            for (NPCMovementThread thread : npcThreads) {
                thread.stopMovement();
            }
        }

        // Shutdown event manager
        if (eventManager != null) {
            eventManager.shutdown();
        }
    }

    private void createRooms() {
        Room sunfield, hazelwood, pub, hedgehogAlp, ashgrove,foxwood, stillgrass, greasyBog,wildWills,tallWoods,mapleThick,chilcroftwood;

        // create rooms
        sunfield = new Room("in the Sunfield");
        pub = new Room("in the abandoned pub");
        hazelwood = new Room("in the hazelwood");
        hedgehogAlp = new Room("in the hedgehog alp");
        ashgrove = new Room("in the ashgrove");
        foxwood = new Room("in the foxwood");
        stillgrass = new Room("in the stillgrass");
        greasyBog = new Room("in the greasy bog");
        wildWills = new Room("in the wild wills");
        tallWoods = new Room("in the tall woods");
        mapleThick = new Room("in the maple thick");
        chilcroftwood = new Room("in the chilcroftwood");

        // initialise room exits using Direction enum
        sunfield.setExit(Direction.EAST, stillgrass);
        sunfield.setExit(Direction.SOUTH, pub);
        sunfield.setExit(Direction.WEST, mapleThick);
        sunfield.setExit(Direction.NORTH, hazelwood);

        pub.setExit(Direction.WEST, chilcroftwood);
        pub.setExit(Direction.NORTH, sunfield);

        chilcroftwood.setExit(Direction.NORTH, mapleThick);
        chilcroftwood.setExit(Direction.EAST, pub);

        mapleThick.setExit(Direction.SOUTH, chilcroftwood);
        mapleThick.setExit(Direction.EAST, sunfield);
        mapleThick.setExit(Direction.NORTH, tallWoods);

        tallWoods.setExit(Direction.SOUTH, mapleThick);
        tallWoods.setExit(Direction.NORTH, wildWills);

        wildWills.setExit(Direction.SOUTH, tallWoods);
        wildWills.setExit(Direction.EAST, greasyBog);

        greasyBog.setExit(Direction.WEST, wildWills);
        greasyBog.setExit(Direction.EAST, hedgehogAlp);

        hedgehogAlp.setExit(Direction.SOUTH, hazelwood);
        hedgehogAlp.setExit(Direction.WEST, greasyBog);
        hazelwood.setExit(Direction.EAST, ashgrove);

        ashgrove.setExit(Direction.SOUTH, foxwood);
        ashgrove.setExit(Direction.WEST, hedgehogAlp);

        foxwood.setExit(Direction.SOUTH, stillgrass);
        foxwood.setExit(Direction.WEST, hazelwood);
        foxwood.setExit(Direction.NORTH, ashgrove);

        stillgrass.setExit(Direction.NORTH, foxwood);
        stillgrass.setExit(Direction.WEST, sunfield);

        hazelwood.setExit(Direction.NORTH, hedgehogAlp);
        hazelwood.setExit(Direction.SOUTH, sunfield);
        hazelwood.setExit(Direction.EAST, foxwood);

        GenericItem beer = new GenericItem("beer", "Pint of Guinness on the wooden table", pub, 10);
        GenericItem grapes = new GenericItem("grapes", "Mysterious grapes which attract fairies", pub, 10);

        RawMaterial fern = new RawMaterial("fern", "Green fern plant",
                chilcroftwood, 200, "fernTips");
        RawMaterial lavenderBush = new RawMaterial("lavenderBush", "Purple flowering bush",
                sunfield, 201, "lavender");
        RawMaterial messyGrass = new RawMaterial("messyGrass", "Overgrown sweet-smelling grass",
                stillgrass, 202, "sweetGrass");

        RawMaterial ashRoot = new RawMaterial("ashRoot", "Thick root buried in ash",
                ashgrove, 203, "ashRootSpice");
        RawMaterial sharpThorn = new RawMaterial("sharpThorn", "Pointed thorny branch",
                greasyBog, 204, "thornPowder");
        RawMaterial darkStone = new RawMaterial("darkStone", "Heavy dark stone",
                hedgehogAlp, 205, "stoneDust");

        RawMaterial saltStone = new RawMaterial("saltStone", "Crystalline salt deposit",
                wildWills, 206, "saltCrystal");
        RawMaterial shinyNut = new RawMaterial("shinyNut", "Hard nut with shiny shell",
                hazelwood, 207, "nutShell");
        RawMaterial foxberry = new RawMaterial("foxberry", "Cluster of tiny red berries",
                foxwood, 208, "foxberryJuice");

        pub.addItem(beer);
        sunfield.addItem(grapes);


        chilcroftwood.addItem(fern);
        sunfield.addItem(lavenderBush);
        stillgrass.addItem(messyGrass);

        ashgrove.addItem(ashRoot);
        greasyBog.addItem(sharpThorn);
        hedgehogAlp.addItem(darkStone);

        wildWills.addItem(saltStone);
        hazelwood.addItem(shinyNut);
        foxwood.addItem(foxberry);


        NPC bob = new NPC("Bob", pub,
                GenericDialogHandler.fromNPCType(NPCType.BOB),
                "beer");
        NPC alice = new NPC("Alice", tallWoods,
                GenericDialogHandler.fromNPCType(NPCType.ALICE),
                "grapes");

        bob.addToInventory("knife", 1);
        bob.addToInventory("shovel", 1);
        bob.addToInventory("pickaxe", 1);
        bob.addToInventory("secateurs", 1);
        bob.addToInventory("sandpaper", 1);

        alice.addToInventory("silverRainTonic", 3);
        alice.addToInventory("wardensBrew", 1);
        alice.addToInventory("morningBloomElixir", 2);

        pub.addNPC(bob);
        sunfield.addNPC(alice);


        // create the player character and start outside
        player = new Character(name, sunfield);
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the mysterious Hecate adventure!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    public void printCurrentRoom() {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    public String getCurrentRoomName() {
        String description = player.getCurrentRoom().getDescription();
        return description.replace("in the ", "").trim();
    }

    public Character getPlayer() {
        return player;
    }

    public boolean processCommand(Command command) {
        CommandType commandType = command.getCommandType();

        if (commandType == null) {
            System.out.println("I don't understand your command...");
            return false;
        }

        switch (commandType) {
            case HELP:
                printHelp();
                break;
            case GO:
                goRoom(command);
                break;
            case SAVE:
                saveGame(command);
                break;
            case LOAD:
                loadGame(command);
                break;
            case PICK:
            case TAKE:
                takeItem(command);
                break;
            case USE:
                useToolCommand(command);
                break;
            case SQUEEZE:
                squeezeCommand(command);
                break;
            case MAP:
                player.showMap();
                break;
            case INVENTORY:
            case INV:
                System.out.println(player.listInventory());
                break;
            case DROP:
                dropItem(command);
                break;
            case RECIPES:
                player.checkRecipes();
                break;
            case TALK:
                player.talkToNPC(command);
                break;
            case ACHIEVEMENTS:
                System.out.println(player.showAchievements());
                break;
            case QUIT:
                if (command.hasSecondWord()) {
                    System.out.println("Quit what?");
                    return false;
                } else {
                    shutdown();
                    return true;
                }
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }


    private void printHelp() {
        System.out.println("""
        === HELP MENU ===
        Move using:
          go north | go south | go east | go west
        pick/take <item>    => pick up something in the room
        drop <item>         => drop an item from your inventory
        use <tool> <item>   => use a tool on a raw material
        squeeze <berry>     => create juice if possible
        inventory / inv     => show your items
        recipes             => check potion progress
        Raw materials must be processed with the right tools.
          secateurs => fern, grass
          knife     => lavender
        Combine processed ingredients to craft potions.
        talk <name>         => speak with NPCs
        map                 => show the world map
        save <filename>     => save the game (default: savegame.sav)
        load <filename>     => load a saved game
        NPCs may require specific items before helping you.
        """);

        parser.showCommands();

    }

    private void useToolCommand(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Use what? (Format: use <tool> <item>)");
            return;
        }

        String[] parts = command.getRemainingWords();
        if (parts.length < 2) {
            System.out.println("Use tool on what? (Format: use <tool> <item>)");
            return;
        }

        String toolName = parts[0];
        String itemName = parts[1];
        player.useTool(toolName, itemName);
    }

    private void squeezeCommand(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Squeeze what?");
            return;
        }

        String itemName = command.getSecondWord();
        player.squeezeItem(itemName);
    }

    private void takeItem(Command command){
        if (!command.hasSecondWord()){
            System.out.println("Take what?");
            return;
        }
        String itemName = command.getSecondWord();
        player.takeItem(itemName);
    }

    private void dropItem(Command command){
        if (!command.hasSecondWord()){
            System.out.println("Drop what?");
            return;
        }
        String itemName = command.getSecondWord();
        player.dropItem(itemName);
    }

    private void saveGame(Command command) {
        String filename = command.hasSecondWord() ?
                command.getSecondWord() + ".sav" : "savegame.sav";

        try {
            GameSaver.saveGame(player, filename);
        } catch (GameSaveException e) {
            System.err.println("Save failed: " + e.getMessage());

            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private void loadGame(Command command) {
        String filename = command.hasSecondWord() ?
                command.getSecondWord() + ".sav" : "savegame.sav";

        try {
            player = GameSaver.loadGame(filename);
            System.out.println(player.getCurrentRoom().getLongDescription());
        } catch (GameLoadException e) {
            System.err.println("Load failed: " + e.getMessage());

            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
        }
    }


    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
            System.out.println("\nYour score is "+ player.getCoins());
            player.checkExplorerAchievement(12); // 12 total rooms
        }
    }
}
