package StartPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Recipe extends Item  {
    private static final long serialVersionUID = 1L;
    private final int ingredients;
    private int collected;
    private List<String> ingredient = new ArrayList<>();
    private Set<String> collectedIngredients = new HashSet<>();
    private static int recepies;

    public Recipe(String name, String description, Room location, int id, int ingredients, int collected, List<String> ingredient) {
        super(name, description, location, id);
        this.ingredients = ingredients;
        this.collected = collected;
        this.ingredient = ingredient;
        recepies++;
    }

    public static int getNumRecipes(){return recepies;}

    public boolean addIngredient(String ingredientName) {
        if (ingredient.contains(ingredientName) && !collectedIngredients.contains(ingredientName)) {
            collectedIngredients.add(ingredientName);
            this.collected++;
            return true;
        }
        return false;
    }

    public boolean removeIngredient(String ingredientName) {
        if (collectedIngredients.contains(ingredientName)) {
            collectedIngredients.remove(ingredientName);
            if (this.collected > 0) {
                this.collected--;
            }
            return true;
        }
        return false;
    }

    public void incrementCollected(){
        this.collected++;
    }

    public void decrementCollected(){
        if(this.collected > 0) {
            this.collected--;
        }
    }

    public int getCollected(){
        return this.collected;
    }

    public int getIngredients(){
        return this.ingredients;
    }

    public List<String> getIngredient() {return ingredient;}


}
