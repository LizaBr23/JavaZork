package ZorkGame;

// Enum for different types of items in the game
public enum ItemCategory {
    TOOL("Tool", "Items used to gather or process materials"),
    INGREDIENT("Ingredient", "Processed materials used in recipes"),
    RAW_MATERIAL("Raw Material", "Unprocessed materials found in the world"),
    POTION("Potion", "Consumable items with special effects"),
    QUEST_ITEM("Quest Item", "Special items for quests or trading");

    private final String displayName;
    private final String description;

    ItemCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
