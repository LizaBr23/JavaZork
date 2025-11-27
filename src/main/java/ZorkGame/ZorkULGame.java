package ZorkGame;/* This game is a classic text-based adventure set in a university environment.
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

        Item beer = new  Item("beer", "Pint of Guinness on the wooden table", pub, 10);

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



        pub.addItem(beer);


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
                "lavender");

        bob.addToInventory("knife", 0);
        bob.addToInventory("shovel", 0);
        bob.addToInventory("pickaxe", 0);
        bob.addToInventory("secateurs", 0);
        bob.addToInventory("sandpaper", 0);

        alice.addToInventory("silverRainTonic", 0);
        alice.addToInventory("wardensBrew", 0);
        alice.addToInventory("morningBloomElixir", 0);

        pub.addNPC(bob);
        tallWoods.addNPC(alice);


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

    private boolean processCommand(Command command) {
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
        System.out.println("You are lost. You are alone. You wander around the university.");
        System.out.print("Your command words are: ");
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
        GameSaver.saveGame(player, filename);
    }

    private void loadGame(Command command) {
        String filename = command.hasSecondWord() ?
                command.getSecondWord() + ".sav" : "savegame.sav";
        Character loadedPlayer = GameSaver.loadGame(filename);
        if (loadedPlayer != null) {
            player = loadedPlayer;
            System.out.println(player.getCurrentRoom().getLongDescription());
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
        }
    }
}
