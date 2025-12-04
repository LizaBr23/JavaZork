package ZorkGame.enums;

// Enum for types of items in the game
public enum ItemCategory {
    TOOL("Items used to gather or process materials"),
    INGREDIENT("Processed materials used in recipes"),
    RAW_MATERIAL("Unprocessed materials found in the world"),
    POTION("Consumable items with special effects"),
    QUEST_ITEM("Special items for quests or trading"),
    RECIPE("Instructions for creating potions");

    private final String DESCRIPTION;

    ItemCategory(String description) {
        this.DESCRIPTION = description;
    }

    public String getDescription() {
        return DESCRIPTION;
    }
}
