package ZorkGame;

import java.util.Map;

// Enum for all tool types and their conversion mappings
public enum ToolType {
    KNIFE(
        "knife",
        "A sharp cutting blade",
        101,
        Map.of("lavenderBush", "lavender")
    ),
    PICKAXE(
        "pickaxe",
        "A heavy mining tool",
        102,
        Map.of("saltStone", "saltCrystal", "shinyNut", "nutShell")
    ),
    SHOVEL(
        "shovel",
        "A sturdy digging tool",
        100,
        Map.of("ashRoot", "ashRootSpice")
    ),
    SECATEURS(
        "secateurs",
        "Garden shears for cutting",
        103,
        Map.of("fern", "fernTips", "messyGrass", "sweetGrass")
    ),
    SANDPAPER(
        "sandpaper",
        "Rough paper for grinding",
        104,
        Map.of("sharpThorn", "thornPowder", "darkStone", "stoneDust")
    );

    private final String name;
    private final String description;
    private final int id;
    private final Map<String, String> conversionMap;

    ToolType(String name, String description, int id, Map<String, String> conversionMap) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.conversionMap = conversionMap;
    }

    public String getToolName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Map<String, String> getConversionMap() {
        return conversionMap;
    }

    // Convert string to ToolType enum
    public static ToolType fromString(String text) {
        if (text == null) {
            return null;
        }

        for (ToolType type : ToolType.values()) {
            if (type.name.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    // Create a Tool instance from this enum
    public Tool createTool(Room location) {
        return new Tool(name, description, location, id, conversionMap);
    }
}
