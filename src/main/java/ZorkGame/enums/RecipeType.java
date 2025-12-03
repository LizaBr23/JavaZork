package ZorkGame.enums;

import java.util.List;
import ZorkGame.models.Room;
import ZorkGame.models.Recipe;

public enum RecipeType {
    MORNING_BLOOM_ELIXIR(
        "morningBloomElixir",
        "Potion that grants stealth and night vision",
        3,
        0,
        List.of("fernTips", "lavender", "sweetGrass")
    ),
    WARDENS_BREW(
        "wardensBrew",
        "Potion that restores stamina and inner warmth",
        3,
        0,
        List.of("ashRootSpice", "thornPowder", "stoneDust")
    ),
    SILVER_RAIN_TONIC(
        "silverRainTonic",
        "Potion that grants cold resistance and calm focus",
        3,
        0,
        List.of("saltCrystal", "nutShell", "foxberryJuice")
    );

    private final String name;
    private final String description;
    private final int ingredientCount;
    private final int alreadyCollected;
    private final List<String> requiredIngredients;

    RecipeType(String name, String description, int ingredientCount, int alreadyCollected, List<String> requiredIngredients) {
        this.name = name;
        this.description = description;
        this.ingredientCount = ingredientCount;
        this.alreadyCollected = alreadyCollected;
        this.requiredIngredients = requiredIngredients;
    }

    public String getRecipeName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public int getAlreadyCollected() {
        return alreadyCollected;
    }

    public List<String> getRequiredIngredients() {
        return requiredIngredients;
    }


    public static RecipeType fromString(String text) {
        if (text == null) {
            return null;
        }

        String normalized = text.toLowerCase().trim();

        for (RecipeType type : RecipeType.values()) {
            if (type.name.equalsIgnoreCase(text) ||
                type.name.toLowerCase().equals(normalized)) {
                return type;
            }
        }
        return null;
    }

    public Recipe createRecipe(Room location) {
        return new Recipe(name, description, location, ordinal() + 1, ingredientCount, alreadyCollected, requiredIngredients);
    }
}
