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

    private final String NAME;
    private final String DESCRIPTION;
    private final int INGREDIENT_COUNT;
    private final int ALREADY_COLLECTED;
    private final List<String> REQUIRED_INGREDIENTS;

    RecipeType(String name, String description, int ingredientCount, int alreadyCollected, List<String> requiredIngredients) {
        this.NAME = name;
        this.DESCRIPTION = description;
        this.INGREDIENT_COUNT = ingredientCount;
        this.ALREADY_COLLECTED = alreadyCollected;
        this.REQUIRED_INGREDIENTS = requiredIngredients;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public static RecipeType fromString(String text) {
        if (text == null) {
            return null;
        }

        String normalized = text.toLowerCase().trim();

        for (RecipeType type : RecipeType.values()) {
            if (type.NAME.equalsIgnoreCase(text) ||
                type.NAME.toLowerCase().equals(normalized)) {
                return type;
            }
        }
        return null;
    }

    public Recipe createRecipe(Room location) {
        return new Recipe(NAME, DESCRIPTION, location, ordinal() + 1, INGREDIENT_COUNT, ALREADY_COLLECTED, REQUIRED_INGREDIENTS);
    }
}
