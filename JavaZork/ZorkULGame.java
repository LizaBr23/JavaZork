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

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;



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

        Ingredient fernTips = new Ingredient("fernTips", "Dark moss that grows only under moonlight", chilcroftwood, 1);
        Ingredient lavender = new Ingredient("lavender", "Pale flower that opens silently at night, releasing a calming scent", sunfield, 2);
        Ingredient sweetGrass = new Ingredient("sweetGrass", "Fresh blades of grass with a sweet aroma, ideal for soothing teas", stillgrass, 3);

        Ingredient ashRootSpice = new Ingredient("ashRootSpice", "Red-veined root that stays warm to the touch and adds a spicy kick", ashgrove, 4);
        Ingredient thornPowder = new Ingredient("thornPowder", "Flaking bark that sparks when scraped, useful for ignition or magic", greasyBog, 5);
        Ingredient stoneDust = new Ingredient("stoneDust", "Golden thistle dust crushed to a fine powder for strength potions", hedgehogAlp, 6);

        Ingredient saltCrystal = new Ingredient("saltCrystal", "Translucent crystal that never melts, used for preservation and cooling effects", wildWills, 7);
        Ingredient nutShell = new Ingredient("nutShell", "Hard shell from forest nuts, crushed for grounding potions", hazelwood, 8);
        Ingredient foxberryJuice = new Ingredient("foxberryJuice", "Juice from tiny red berries that freezes water on contact", foxwood, 9);


        Recipe morningBloomElixir = new Recipe("morningBloomElixir", "Potion that grants stealth and night vision", sunfield, 1, 3, 0,
                List.of("fernTips", "lavender", "sweetGrass"));

        Recipe wardensBrew = new Recipe("wardensBrew", "Potion that restores stamina and inner warmth", sunfield, 2, 3, 0,
                List.of("ashRootSpice", "thornPowder", "stoneDust"));

        Recipe silverRainTonic = new Recipe("silverRainTonic", "Potion that grants cold resistance and calm focus", sunfield, 3, 3, 0,
                List.of("saltCrystal", "nutShell", "foxberryJuice"));


        Tool shovel = new Shovel("shovel", "To dig something", sunfield, 3,
                List.of("ashRootSpice"));
        Tool knife = new Knife("knife", "Sharp", sunfield, 3,
                List.of("lavender"));
        Tool pickaxe = new Pickaxe("pickaxe", "To mine something", sunfield, 3,
                List.of("saltCrystal", "nutShell"));
        Tool secateurs = new Secateurs("secateurs", "To cut something", sunfield, 3,
                List.of("fernTips", "sweetGrass"));
        Tool sandpaper = new Sandpaper("sandpaper", "To dig something", sunfield, 3,
                List.of("thornPowder","stoneDust"));

        pub.addItem(beer);

//        sunfield.addItem(fernTips);
//        sunfield.addItem(lavender);
//        sunfield.addItem(sweetGrass);
//
//        sunfield.addItem(ashRootSpice);
//        sunfield.addItem(thornPowder);
//        sunfield.addItem(stoneDust);
//
//        sunfield.addItem(saltCrystal);
//        sunfield.addItem(nutShell);
//        sunfield.addItem(foxberryJuice);

        chilcroftwood.addItem(fernTips);
        sunfield.addItem(lavender);
        stillgrass.addItem(sweetGrass);

        ashgrove.addItem(ashRootSpice);
        greasyBog.addItem(thornPowder);
        hedgehogAlp.addItem(stoneDust);

        wildWills.addItem(saltCrystal);
        hazelwood.addItem(nutShell);
        foxwood.addItem(foxberryJuice);

        sunfield.addItem(silverRainTonic);
        sunfield.addItem(wardensBrew);
        sunfield.addItem(morningBloomElixir);


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
                takeItem(command);
                break;
            case "map":
                player.showMap();
            case "inventory":
                System.out.println(player.listInventory());
                break;
            case "drop":
                dropItem(command);
                break;
            case "recipes":
                player.checkRecipes();
                break;
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
