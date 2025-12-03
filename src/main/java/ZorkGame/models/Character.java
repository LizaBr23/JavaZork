package ZorkGame.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import ZorkGame.commands.Command;
import ZorkGame.enums.RecipeType;
import ZorkGame.enums.ToolType;
import ZorkGame.exceptions.InvalidDialogueInputException;
import ZorkGame.exceptions.InvalidMenuChoiceException;
import ZorkGame.game.Level2;
import ZorkGame.utils.GameMap;
import ZorkGame.utils.GenericClass;

public class Character implements GameEntity, Locatable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static boolean guiMode = false;

    private final String name;
    private Room currentRoom;
    private GenericClass<Item> inventory = new GenericClass<>();
    private GameMap map;
    private int coins;
    private int hp;

    public static void setGuiMode(boolean mode) {
        guiMode = mode;
    }

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.map = new GameMap();
        this.map.visitRoom(startingRoom);
        this.coins = 0;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Character: " + name;
    }

    @Override
    public Room getLocation() {
        return currentRoom;
    }

    @Override
    public void setLocation(Room location) {
        setCurrentRoom(location);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
        if(!map.isVisited(room)) {
            coins++;
        }
        this.map.visitRoom(room);
    }

    public int getCoins() {
        return coins;
    }

    private void decrementCoins() {
        this.coins--;
    }

    private void spendCoins(int amount) {
        this.coins -= amount;
    }

    public void showMap() {
        map.showMap(currentRoom);
    }

    public void talkToNPC(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Talk to whom?");
            return;
        }

        String npcName = command.getSecondWord();
        GenericClass<NPC> npcCollection = new GenericClass<>(currentRoom.getNPCs());

        NPC targetNPC = npcCollection.findByName(npcName);

        if (targetNPC == null) {
            System.out.println("There's no one named " + npcName + " here.");
            return;
        }

        if (guiMode) {
            System.out.println("DIALOGUE_START:" + npcName);
        } else {
            startDialogue(targetNPC);
        }
    }

    private void startDialogue(NPC npc) {
        Scanner scanner = new Scanner(System.in);
        boolean inDialogue = true;

        String itemWanted = npc.getItemWantedToTalk();
        boolean hasRequiredItem = hasItem(itemWanted);

        System.out.println("\n=== Dialogue with " + npc.getName() + " ===");
        System.out.println(npc.getDescription(hasRequiredItem));

        while (inDialogue) {
            List<String> options = npc.getDialogueOptions(hasRequiredItem);

            if (options.isEmpty()) {
                System.out.println(npc.getName() + " has nothing to say.");
                break;
            }
            //show options
            System.out.println();
            for (String option : options) {
                System.out.println(option);
            }
            System.out.println("Type 'exit' to end conversation\n");

            //get player choice
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("\nYou end the conversation with " + npc.getName() + ".\n");
                inDialogue = false;
            } else {
                try {
                    // Try to parse the input as a number
                    int choice = Integer.parseInt(input);

                    // Validate the choice is within valid range
                    if (choice < 1 || choice > options.size()) {
                        throw new InvalidDialogueInputException(
                            "Choice " + choice + " is out of range. Please choose between 1 and " + options.size() + ".");
                    }

                    String response = npc.getDialogueResponse(choice, hasRequiredItem);
                    System.out.println(response);
                    if (response.contains("Shop")) {
                        showTradeMenu(npc);
                        inDialogue = false;
                    }
                    else if (response.toLowerCase().contains("goodbye") || response.toLowerCase().contains("farewell")||
                            response.toLowerCase().contains("walk away") || response.toLowerCase().contains("you left")) {
                        inDialogue = false;
                    }
                    else if (!hasRequiredItem) {
                        //if no beer, exit dialogue
                        inDialogue = false;
                    }

                    if(!inDialogue) {
                        System.out.println("You decide to explore the world more.");
                    }

                } catch (NumberFormatException e) {
                   //input cannot be converted to an integer
                    try {
                        throw new InvalidDialogueInputException(
                            "Input '" + input + "' is not a valid number. Please enter a number or 'exit'.", e);
                    } catch (InvalidDialogueInputException ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (InvalidDialogueInputException e) {
                    //out of range choices
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void showTradeMenu(NPC npc) {
        Scanner scanner = new Scanner(System.in);
        Set<String> items = npc.getAvailableItems();
        boolean inShop = true;

        while (inShop) {
            System.out.println("\n--- Available Items ---");
            System.out.println("Your coins: " + coins);
            List<String> itemList = new ArrayList<>(items);

            for (int i = 0; i < itemList.size(); i++) {
                String item = itemList.get(i);
                int price = npc.getItemPrice(item);

                String priceStr;
                if (price == -1) {
                    priceStr = "Item not found";
                } else if (price == 0) {
                    priceStr = "FREE";
                } else {
                    priceStr = price + " gold";
                }

                System.out.println((i + 1) + ". " + item + " (" + priceStr + ")");
            }

            System.out.println((itemList.size() + 1) + ". Exit shop");
            System.out.print("\nWhich item would you like? > ");

            try {
                //changed from nextInt to avoid \n in the next input
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                //Validating choice
                if (choice == itemList.size() + 1) {
                    System.out.println("You leave the shop.\n");
                    inShop = false;
                } else if (choice < 1 || choice > itemList.size()) {
                    throw new InvalidMenuChoiceException(
                        "Choice " + choice + " is out of range. Please choose between 1 and " + (itemList.size() + 1) + ".");
                } else {
                    String selectedItem = itemList.get(choice - 1);

                    buyToolFromNPC(npc, selectedItem);
                }

            } catch (NumberFormatException e) {
                try {
                    throw new InvalidMenuChoiceException(
                        "Invalid input. Please enter a valid number.", e);
                } catch (InvalidMenuChoiceException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (InvalidMenuChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void buyToolFromNPC(NPC npc, String toolName) {
        // Get the price of the item
        int price = npc.getItemPrice(toolName);

        if (price == -1) {
            System.out.println("Item not found.\n");
            return;
        }

        // Check if player has enough coins
        if (coins < price) {
            System.out.println("You don't have enough coins! You need " + price + " coins but only have " + coins + ".\n");
            return;
        }

        //try to create a tool first
        Tool tool = createToolByName(toolName);
        if (tool != null) {
            inventory.add(tool);
            npc.removeFromInventory(toolName);
            spendCoins(price);
            System.out.println("You obtained: " + toolName + " from " + npc.getName() + " for " + price + " coins.\n");
            System.out.println("Coins remaining: " + coins + "\n");
        } else {
            //if not a tool, try to create an item
            Item item = createRecipeByName(toolName);
            if (item != null) {
                inventory.add(item);
                npc.removeFromInventory(toolName);
                spendCoins(price);
                System.out.println("You obtained: " + toolName + " from " + npc.getName() + " for " + price + " coins.\n");
                System.out.println("Coins remaining: " + coins + "\n");
            } else {
                System.out.println("Item not found.\n");
            }
        }
    }


    private Tool createToolByName(String toolName) {
        ToolType toolType = ToolType.fromString(toolName);
        if (toolType != null) {
            return toolType.createTool(currentRoom);
        }
        return null;
    }

    private Item createRecipeByName(String itemName) {
        RecipeType recipeType = RecipeType.fromString(itemName);
        if (recipeType != null) {
            return recipeType.createRecipe(currentRoom);
        }
        return null;
    }

    //check for a specific item
    public boolean hasItem(String itemName) {
        for (Item item : inventory.getAll()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


    public void useTool(String toolName, String rawMaterialName) {
        Tool tool = inventory.findByTypeAndName(Tool.class, toolName);

        if (tool == null) {
            System.out.println("You don't have a " + toolName + ".");
            return;
        }

        GenericClass<Item> roomItems = new GenericClass<>(currentRoom.getItems());
        RawMaterial rawMaterial = roomItems.findByTypeAndName(RawMaterial.class, rawMaterialName);

        if (rawMaterial == null) {
            System.out.println("There's no " + rawMaterialName + " here.");
            return;
        }

        if (!tool.canUseOn(rawMaterial.getName())) {
            System.out.println("You can't use " + toolName + " on " + rawMaterialName + ".");
            return;
        }

        String ingredientName = tool.getResultIngredient(rawMaterialName);
        if (ingredientName == null) {
            ingredientName = rawMaterial.getResultingIngredient();
        }

        Ingredient ingredient = new Ingredient(
                ingredientName,
                "Processed from " + rawMaterialName,
                currentRoom,
                rawMaterial.getId() + 100
        );

        currentRoom.removeItem(rawMaterial);
        currentRoom.addItem(ingredient);

        System.out.println("You used " + toolName + " on " + rawMaterialName +
                " and dropped " + ingredientName + "!");
    }

    public void squeezeItem(String itemName) {
        GenericClass<Item> roomItems = new GenericClass<>(currentRoom.getItems());
        Item foundItem = roomItems.findByPartialName(itemName);

        if (foundItem == null) {
            System.out.println("There's no " + itemName + " here to squeeze.");
            return;
        }

        if (foundItem.getName().equalsIgnoreCase("foxberry")) {
            Ingredient juice = new Ingredient(
                    "foxberryJuice",
                    "Juice from tiny red berries that freezes water on contact",
                    currentRoom,
                    foundItem.getId() + 100
            );

            currentRoom.removeItem(foundItem);
            currentRoom.addItem(juice);

            System.out.println("You squeezed the foxberry and obtained foxberry juice!");
        } else {
            System.out.println("You can't squeeze that!");
        }
    }

    public void dropItem(String itemName) {
        if (itemName == null) {
            System.out.println("Drop what?");
            return;
        }

        Item itemToDrop = inventory.findByName(itemName);

        if (itemToDrop != null) {

            if (itemToDrop instanceof Recipe) {
                System.out.println("You can't drop a recipe! You need it to complete your quest.");
                return;
            }

            if (itemToDrop instanceof Ingredient ingredient) {
                List<Recipe> recipes = inventory.findAllByType(Recipe.class);
                for (Recipe recipe : recipes) {
                    recipe.removeIngredient(ingredient.getName());
                }
            }

            inventory.remove(itemToDrop);
            currentRoom.addItem(itemToDrop);
            System.out.println("You dropped the " + itemToDrop.getName() + ".");
        } else {
            System.out.println("You don't have that item.");
        }
    }

    public void takeItem(String itemName){
        GenericClass<Item> roomItems = new GenericClass<>(currentRoom.getItems());
        Item foundItem = roomItems.findByPartialName(itemName);

        if(foundItem != null){
            inventory.add(foundItem);
            currentRoom.removeItem(foundItem);

            boolean completedRecipe = false;
            String completedRecipeName = "";

            if (foundItem instanceof Recipe newRecipe) {

                List<Ingredient> ingredients = inventory.findAllByType(Ingredient.class);
                for (Ingredient ingredient : ingredients) {
                    newRecipe.addIngredient(ingredient.getName());
                }

                // Check if this recipe is now complete
                if(newRecipe.getCollected() >= newRecipe.getIngredients()) {
                    completedRecipe = true;
                    completedRecipeName = newRecipe.getName();
                }

                System.out.println("\nYou picked up the potion Recipe " + foundItem.getName() + ".\n");

                if(completedRecipe){
                    System.out.println("You've completed the recipe for " + completedRecipeName + "!");
                }

            } else if (foundItem instanceof Ingredient ingredient) {

                List<Recipe> recipes = inventory.findAllByType(Recipe.class);
                for (Recipe recipe : recipes) {
                    if (recipe.addIngredient(ingredient.getName())) {
                        if(recipe.getCollected() >= recipe.getIngredients()) {
                            completedRecipe = true;
                            completedRecipeName = recipe.getName();
                        }
                    }
                }

                System.out.println("\nYou picked up " + foundItem.getName() + ".\n");

                if(completedRecipe){
                    System.out.println("You've completed the recipe for " + completedRecipeName + "!");
                }
            } else {
                // Regular item
                System.out.println("\nYou picked up " + foundItem.getName() + ".\n");
            }

            List<Recipe> allRecipes = inventory.findAllByType(Recipe.class);
            boolean allRecipesComplete = true;
            int recipeCount = allRecipes.size();

            for (Recipe recipe : allRecipes) {
                if (recipe.getCollected() < recipe.getIngredients()) {
                    allRecipesComplete = false;
                    break;
                }
            }

            if(recipeCount > 0 && recipeCount >= Recipe.getNumRecipes() && allRecipesComplete) {
                System.out.println("\n*** Congratulations! You've completed all recipes! ***");
                System.out.println("You are moving to level 2.\n");
                Level2 level2 = new Level2();
            }
        }
        else{
            System.out.println("There's no such item here.");
        }
    }

    public String listInventory(){
        if(inventory.isEmpty()){
            return "\nYou have no items with you.";
        }
        else{
            //count items
            Map<String, Integer> itemCounts = new HashMap<>();
            for(Item item : inventory.getAll()){
                String itemName = item.getName();
                itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + 1);
            }

            StringBuilder print = new StringBuilder();
            for(Map.Entry<String, Integer> entry : itemCounts.entrySet()){
                String itemName = entry.getKey();
                int count = entry.getValue();

                if(count > 1){
                    print.append(itemName + " x" + count + " ");
                } else {
                    print.append(itemName + " ");
                }
            }
            return "\nYour inventory: " + print.toString();
        }
    }

    public void checkRecipes() {
        List<Recipe> recipes = inventory.findAllByType(Recipe.class);
        System.out.println("\n=== Recipe Progress ===");

        for (Recipe recipe : recipes) {
            System.out.println(recipe.getName() + ": " +
                    recipe.getCollected() + "/" + recipe.getIngredients() + " ingredients collected");

            System.out.print("  Needs: ");
            for (String ing : recipe.getIngredient()) {
                System.out.print(ing + " ");
            }
            System.out.println();
        }

        if (recipes.isEmpty()) {
            System.out.println("You don't have any recipes yet.");
        }
        System.out.println();
    }

    public void checkRecipesParallel() {
        checkRecipes();
    }
}

