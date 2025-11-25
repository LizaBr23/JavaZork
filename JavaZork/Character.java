import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private Room currentRoom;
    private List<Item> inventory = new ArrayList<>();
    private GameMap map;

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.map = new GameMap();
        this.map.visitRoom(startingRoom);
    }


    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
        this.map.visitRoom(room);
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
        List<NPC> npcs = currentRoom.getNPCs();

        //find the NPC
        NPC targetNPC = null;
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                targetNPC = npc;
                break;
            }
        }

        if (targetNPC == null) {
            System.out.println("There's no one named " + npcName + " here.");
            return;
        }

        startDialogue(targetNPC);
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
                System.out.println(npc.getName() + " has nothing more to say.");
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

            if (input.equalsIgnoreCase("exit") || input == "3") {
                System.out.println("\nYou end the conversation with " + npc.getName() + ".\n");
                inDialogue = false;
            } else {
                try {
                    int choice = Integer.parseInt(input);

                    if (choice < 1 || choice > options.size()) {
                        System.out.println("Invalid choice. Please choose a valid option.");
                        continue;
                    }

                    String response = npc.getDialogueResponse(choice, hasRequiredItem);
                    System.out.println(response);

                    if (choice == 1 && npc.isTalking()) {
                        showTradeMenu(npc);
                        inDialogue = false;
                    } else if (!hasRequiredItem) {
                        //if no beer, exit dialogue
                        inDialogue = false;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number or 'exit'.");
                }
            }
        }
    }

    private void showTradeMenu(NPC npc) {
        Scanner scanner = new Scanner(System.in);
        Set<String> items = npc.getAvailableItems();

        System.out.println("\n--- Available Items ---");
        List<String> itemList = new ArrayList<>(items);

        for (int i = 0; i < itemList.size(); i++) {
            String item = itemList.get(i);
            int price = npc.getItemPrice(item);

            String priceStr;
            if (price == 0) {
                priceStr = "FREE";
            } else {
                priceStr = price + " gold";
            }

            System.out.println((i + 1) + ". " + item + " (" + priceStr + ")");
        }

        System.out.println((itemList.size() + 1) + ". Exit shop");
        System.out.print("\nWhich item would you like? > ");

        try {
            String input = scanner.nextLine().trim();
            int choice = Integer.parseInt(input);

            if (choice == itemList.size() + 1) {
                System.out.println("You leave the shop.\n");
                return;
            }

            if (choice < 1 || choice > itemList.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            String selectedItem = itemList.get(choice - 1);
            buyToolFromNPC(npc, selectedItem);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }


    private void buyToolFromNPC(NPC npc, String toolName) {
        System.out.println("\n" + npc.getName() + " hands you a " + toolName + ".");
        System.out.println("\"Take good care of it.\"");

        //create and add the tool to inventory
        Tool tool = createToolByName(toolName);
        if (tool != null) {
            inventory.add(tool);
            System.out.println("You obtained: " + toolName + "\n");
        } else {
            System.out.println("Tool not found.\n");
        }
    }


    private Tool createToolByName(String toolName) {
        switch (toolName.toLowerCase()) {
            case "knife":
                return new Knife("knife", "A sharp cutting blade", currentRoom, 101,
                        List.of("lavender"));
            case "pickaxe":
                return new Pickaxe("pickaxe", "A heavy mining tool", currentRoom, 102,
                        List.of("saltCrystal", "nutShell"));
            case "shovel":
                return new Shovel("shovel", "A sturdy digging tool", currentRoom, 100,
                        List.of("ashRootSpice"));
            case "secateurs":
                return new Secateurs("secateurs", "Garden shears for cutting", currentRoom, 103,
                        List.of("fernTips", "sweetGrass"));
            case "sandpaper":
                return new Sandpaper("sandpaper", "Rough paper for grinding", currentRoom, 104,
                        List.of("thornPowder", "stoneDust"));
            default:
                return null;
        }
    }

    //check for a specific item
    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


    public void useTool(String toolName, String rawMaterialName) {
        Tool tool = null;
        for (Item item : inventory) {
            if (item instanceof Tool && item.getName().equalsIgnoreCase(toolName)) {
                tool = (Tool) item;
                break;
            }
        }

        if (tool == null) {
            System.out.println("You don't have a " + toolName + ".");
            return;
        }

        RawMaterial rawMaterial = null;
        for (Item item : currentRoom.getItems()) {
            if (item instanceof RawMaterial &&
                    item.getName().equalsIgnoreCase(rawMaterialName)) {
                rawMaterial = (RawMaterial) item;
                break;
            }
        }

        if (rawMaterial == null) {
            System.out.println("There's no " + rawMaterialName + " here.");
            return;
        }

        if (!tool.canUseOn(rawMaterial.getResultingIngredient())) {
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

        currentRoom.getItems().remove(rawMaterial);
        currentRoom.getItems().add(ingredient);

        System.out.println("You used " + toolName + " on " + rawMaterialName +
                " and dropped " + ingredientName + "!");
    }

    public void squeezeItem(String itemName) {
        // Find the item in the room
        Item foundItem = null;
        for (Item item : currentRoom.getItems()) {
            if (item.getName().toLowerCase().contains(itemName.toLowerCase())) {
                foundItem = item;
                break;
            }
        }

        if (foundItem == null) {
            System.out.println("There's no " + itemName + " here to squeeze.");
            return;
        }

        // Special handling for foxberry
        if (foundItem.getName().equalsIgnoreCase("foxberry")) {
            Ingredient juice = new Ingredient(
                    "foxberryJuice",
                    "Juice from tiny red berries that freezes water on contact",
                    currentRoom,
                    foundItem.getId() + 100
            );

            currentRoom.getItems().remove(foundItem);
            currentRoom.getItems().add(juice);

            System.out.println("You squeezed the foxberry and obtained foxberry juice!");
        } else {
            System.out.println("You can't squeeze that!");
        }
    }

    public void dropItem(String itemName) {
        List<Item> roomItems = currentRoom.getItems();

        if (itemName == null) {
            System.out.println("Drop what?");
            return;
        }

        Item itemToDrop = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemToDrop = item;
                break;
            }
        }

        if (itemToDrop != null) {

            if (itemToDrop instanceof Recipe) {
                System.out.println("You can't drop a recipe! You need it to complete your quest.");
                return;
            }

            if (itemToDrop instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) itemToDrop;

                for (Item item : inventory) {
                    if (item instanceof Recipe) {
                        Recipe recipe = (Recipe) item;
                        recipe.removeIngredient(ingredient.getName());
                    }
                }
            }

            inventory.remove(itemToDrop);
            roomItems.add(itemToDrop);
            System.out.println("You dropped the " + itemToDrop.getName() + ".");
        } else {
            System.out.println("You don’t have that item.");
        }
    }

    public void takeItem(String itemName){
        List<Item> roomItems = currentRoom.getItems();
        Item foundItem = null;

        for(Item item : roomItems){
            if(item.getName().toLowerCase().contains(itemName.toLowerCase())){
                foundItem = item;
                break;
            }
        }

        if(foundItem != null){
            inventory.add(foundItem);
            roomItems.remove(foundItem);

            boolean completedRecipe = false;
            String completedRecipeName = "";

            if (foundItem instanceof Recipe) {
                Recipe newRecipe = (Recipe) foundItem;

                // Check if we already have ingredients for this recipe in inventory
                for (Item item : inventory) {
                    if (item instanceof Ingredient) {
                        Ingredient ingredient = (Ingredient) item;
                        newRecipe.addIngredient(ingredient.getName());
                    }
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

            } else if (foundItem instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) foundItem;

                // Update all recipes that need this ingredient
                for (Item item : inventory){
                    if (item instanceof Recipe) {
                        Recipe recipe = (Recipe) item;
                        if (recipe.addIngredient(ingredient.getName())) {
                            // Check if THIS recipe is now complete
                            if(recipe.getCollected() >= recipe.getIngredients()) {
                                completedRecipe = true;
                                completedRecipeName = recipe.getName();
                            }
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

            // Check if ALL recipes are complete
            boolean allRecipesComplete = true;
            int recipeCount = 0;
            for (Item item : inventory) {
                if (item instanceof Recipe) {
                    recipeCount++;
                    Recipe recipe = (Recipe) item;
                    if (recipe.getCollected() < recipe.getIngredients()) {
                        allRecipesComplete = false;
                        break;
                    }
                }
            }

            if(recipeCount >= Recipe.getNumRecipes() && allRecipesComplete) {
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
        StringBuilder print = new StringBuilder();
        if(inventory.isEmpty()){
            return "\nYou have no items with you.";
        }
        else{
            for(Item item : inventory){
                print.append(item.getName() + " ");
            }
            return "\nYour inventory: " + print.toString();
        }
    }

    public void checkRecipes() {
        boolean hasRecipes = false;
        System.out.println("\n=== Recipe Progress ===");

        for (Item item : inventory) {
            if (item instanceof Recipe) {
                hasRecipes = true;
                Recipe recipe = (Recipe) item;
                System.out.println(recipe.getName() + ": " +
                        recipe.getCollected() + "/" + recipe.getIngredients() + " ingredients collected");

                System.out.print("  Needs: ");
                for (String ing : recipe.getIngredient()) {
                    System.out.print(ing + " ");
                }
                System.out.println();
            }
        }

        if (!hasRecipes) {
            System.out.println("You don't have any recipes yet.");
        }
        System.out.println();
    }

    public void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            System.out.println("You moved to: " + currentRoom.getDescription());
        } else {
            System.out.println("You can't go that way!");
        }
    }


}

