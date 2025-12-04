package ZorkGame.models;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Recipe extends Item {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int ingredients;
    private int collected;
    private List<String> ingredient;
    private final Set<String> COLLECTED_iNGREDIENTS;
    private static int recepies;

    public Recipe(String name, String description, Room location, int id, int ingredients, int collected, List<String> ingredient) {
        super(name, description, location, id);
        this.ingredients = ingredients;
        this.collected = collected;
        this.ingredient = ingredient != null ? ingredient : new ArrayList<>();
        this.COLLECTED_iNGREDIENTS = new HashSet<>();
        recepies++;
    }

    public static int getNumRecipes(){return recepies;}

    public boolean addIngredient(String ingredientName) {
        if (ingredient.contains(ingredientName) && !COLLECTED_iNGREDIENTS.contains(ingredientName)) {
            COLLECTED_iNGREDIENTS.add(ingredientName);
            this.collected++;
            return true;
        }
        return false;
    }

    public void removeIngredient(String ingredientName) {
        if (COLLECTED_iNGREDIENTS.contains(ingredientName)) {
            COLLECTED_iNGREDIENTS.remove(ingredientName);
            if (this.collected > 0) {
                this.collected--;
            }
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
