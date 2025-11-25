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

        // initialise room exits
        sunfield.setExit("east", stillgrass);
        sunfield.setExit("south", pub);
        sunfield.setExit("west", mapleThick);
        sunfield.setExit("north", hazelwood);

        pub.setExit("west", chilcroftwood);
        pub.setExit("north", sunfield);

        chilcroftwood.setExit("north", mapleThick);
        chilcroftwood.setExit("east", pub);

        mapleThick.setExit("south", chilcroftwood);
        mapleThick.setExit("east", sunfield);
        mapleThick.setExit("north", tallWoods);

        tallWoods.setExit("south", mapleThick);
        tallWoods.setExit("north", wildWills);

        wildWills.setExit("south", tallWoods);
        wildWills.setExit("east", greasyBog);

        greasyBog.setExit("west", wildWills);
        greasyBog.setExit("east", hedgehogAlp);

        hedgehogAlp.setExit("south", hazelwood);
        hedgehogAlp.setExit("west", greasyBog);
        hazelwood.setExit("east", ashgrove);

        ashgrove.setExit("south", foxwood);
        ashgrove.setExit("west", hedgehogAlp);

        foxwood.setExit("south", stillgrass);
        foxwood.setExit("west", hazelwood);
        foxwood.setExit("north", ashgrove);

        stillgrass.setExit("north", foxwood);
        stillgrass.setExit("west", sunfield);

        hazelwood.setExit("north", hedgehogAlp);
        hazelwood.setExit("south", sunfield);
        hazelwood.setExit("east", foxwood);

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

        Recipe morningBloomElixir = new Recipe("morningBloomElixir", "Potion that grants stealth and night vision", sunfield, 1, 3, 0,
                List.of("fernTips", "lavender", "sweetGrass"));
        Recipe wardensBrew = new Recipe("wardensBrew", "Potion that restores stamina and inner warmth", sunfield, 2, 3, 0,
                List.of("ashRootSpice", "thornPowder", "stoneDust"));
        Recipe silverRainTonic = new Recipe("silverRainTonic", "Potion that grants cold resistance and calm focus", sunfield, 3, 3, 0,
                List.of("saltCrystal", "nutShell", "foxberryJuice"));


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

        sunfield.addItem(silverRainTonic);
        sunfield.addItem(wardensBrew);
        sunfield.addItem(morningBloomElixir);

        NPC bob = new NPC("Bob", pub, new BobDialogueHandler(), "beer");
        bob.addToInventory("knife", 0);
        bob.addToInventory("shovel", 0);
        pub.addNPC(bob);

        NPC alice = new NPC("Alice", tallWoods, new AliceDialogueHandler(), "lavender");
        alice.addToInventory("pickaxe", 0);
        alice.addToInventory("secateurs", 0);
        tallWoods.addNPC(alice);

        NPC markus = new NPC("Markus", greasyBog, new MarkusDialogueHandler(), "foxberryJuice");
        markus.addToInventory("sandpaper", 0);
        markus.addToInventory("knife", 0);
        greasyBog.addNPC(markus);


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
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            System.out.println("I don't understand your command...");
            return false;
        }

        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "save":
                saveGame(command);
                break;
            case "load":
                loadGame(command);
                break;
            case "pick":
            case "take":
                takeItem(command);
                break;
            case "use":
                useToolCommand(command);
                break;
            case "squeeze":
                squeezeCommand(command);
                break;
            case "map":
                player.showMap();
            case "inventory":
            case "inv":
                System.out.println(player.listInventory());
                break;
            case "drop":
                dropItem(command);
                break;
            case "recipes":
                player.checkRecipes();
                break;
            case "talk":
                player.talkToNPC(command);
            case "quit":
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
