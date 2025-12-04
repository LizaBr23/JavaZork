package ZorkGame.enums;

public enum AchievementType {
    EXPLORER("Explorer", "Visit every location in the game"),
    SOCIALITE("Socialite", "Talk to every NPC"),
    TOOL_COLLECTOR("Tool Collector", "Buy every tool from Bob"),
    ALCHEMIST("Alchemist", "Convert all raw materials to ingredients"),
    RECIPE_COLLECTOR("Recipe Collector", "Get every recipe from Alice"),
    MASTER_CHEF("Master Chef", "Complete all recipes"),
    CARTOGRAPHER("Cartographer", "Open the map for the first time"),
    HOARDER("Hoarder", "Open inventory for the first time"),
    GATHERER("Gatherer", "Pick up an item for the first time"),
    MINIMALIST("Minimalist", "Drop an item for the first time"),
    NAVIGATOR("Navigator", "Use direction buttons for the first time");

    private final String NAME;
    private final String DESCRIPTION;

    AchievementType(String name, String description) {
        this.NAME = name;
        this.DESCRIPTION = description;
    }

    public String getName() {
        return NAME;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String toString() {
        return NAME + ": " + DESCRIPTION;
    }
}
