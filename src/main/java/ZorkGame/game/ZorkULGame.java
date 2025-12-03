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
import ZorkGame.enums.RecipeType;
import ZorkGame.models.Character;
import ZorkGame.models.Item;
import ZorkGame.models.GenericItem;
import ZorkGame.models.NPC;
import ZorkGame.models.RawMaterial;
import ZorkGame.models.Room;
import java.util.List;

public class ZorkULGame {
    private final Parser parser;
    private Character player;
    private final String name;

    public ZorkULGame(String name) {
        this.name = name;
        createRooms();
        parser = new Parser();
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
                chilcroftwood, 200, "secateurs", "fernTips");
        RawMaterial lavenderBush = new RawMaterial("lavenderBush", "Purple flowering bush",
                sunfield, 201, "knife", "lavender");
        RawMaterial messyGrass = new RawMaterial("messyGrass", "Overgrown sweet-smelling grass",
                stillgrass, 202, "secateurs", "sweetGrass");

        RawMaterial ashRoot = new RawMaterial("ashRoot", "Thick root buried in ash",
                ashgrove, 203, "shovel", "ashRootSpice");
        RawMaterial sharpThorn = new RawMaterial("sharpThorn", "Pointed thorny branch",
                greasyBog, 204, "sandpaper", "thornPowder");
        RawMaterial darkStone = new RawMaterial("darkStone", "Heavy dark stone",
                hedgehogAlp, 205, "sandpaper", "stoneDust");

        RawMaterial saltStone = new RawMaterial("saltStone", "Crystalline salt deposit",
                wildWills, 206, "pickaxe", "saltCrystal");
        RawMaterial shinyNut = new RawMaterial("shinyNut", "Hard nut with shiny shell",
                hazelwood, 207, "pickaxe", "nutShell");
        RawMaterial foxberry = new RawMaterial("foxberry", "Cluster of tiny red berries",
                foxwood, 208, "hands", "foxberryJuice");

//        Recipe morningBloomElixir = new Recipe("morningBloomElixir", "Potion that grants stealth and night vision", sunfield, 1, 3, 0,
//                List.of("fernTips", "lavender", "sweetGrass"));
//        Recipe wardensBrew = new Recipe("wardensBrew", "Potion that restores stamina and inner warmth", sunfield, 2, 3, 0,
//                List.of("ashRootSpice", "thornPowder", "stoneDust"));
//        Recipe silverRainTonic = new Recipe("silverRainTonic", "Potion that grants cold resistance and calm focus", sunfield, 3, 3, 0,
//                List.of("saltCrystal", "nutShell", "foxberryJuice"));
//
//
//        sunfield.addItem(morningBloomElixir);
//        sunfield.addItem(wardensBrew);
//        sunfield.addItem(silverRainTonic);
//
//        Tool shovel = new Shovel("shovel", "To dig something", sunfield, 3,
//                List.of("ashRootSpice"));
//        Tool knife = new Knife("knife", "Sharp", sunfield, 3,
//                List.of("lavender"));
//        Tool pickaxe = new Pickaxe("pickaxe", "To mine something", sunfield, 3,
//                List.of("saltCrystal", "nutShell"));
//        Tool secateurs = new Secateurs("secateurs", "To cut something", sunfield, 3,
//                List.of("fernTips", "sweetGrass"));
//        Tool sandpaper = new Sandpaper("sandpaper", "To dig something", sunfield, 3,
//                List.of("thornPowder","stoneDust"));
//


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
        System.out.println("Welcome to the University adventure!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    // Public method to print current room (for GUI)
    public void printCurrentRoom() {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    // Get current room name for GUI background
    public String getCurrentRoomName() {
        String description = player.getCurrentRoom().getDescription();
        // Extract room name from "in the [roomname]"
        return description.replace("in the ", "").trim();
    }

    // Get player for GUI
    public Character getPlayer() {
        return player;
    }

    // Changed from private to public for GUI integration
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
            case QUIT:
                if (command.hasSecondWord()) {
                    System.out.println("Quit what?");
                    return false;
                } else {
                    return true; // signal to quit
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
            Character loadedPlayer = GameSaver.loadGame(filename);
            player = loadedPlayer;
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
        }
    }
}
