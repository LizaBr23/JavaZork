import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private Room currentRoom;
    private List<Item> inventory = new ArrayList<>();
    private GameMap map;
    private List<Room> allRooms = ZorkULGame.getAllRooms();

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.map = new GameMap(allRooms);
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

