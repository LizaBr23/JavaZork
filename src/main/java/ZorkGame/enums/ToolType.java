package ZorkGame.enums;

import java.util.Map;
import ZorkGame.models.Room;
import ZorkGame.models.Tool;

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

    private final String NAME;
    private final String DESCRIPTION;
    private final int ID;
    private final Map<String, String> CONVERSION_MAP;

    ToolType(String name, String description, int id, Map<String, String> conversionMap) {
        this.NAME = name;
        this.DESCRIPTION = description;
        this.ID = id;
        this.CONVERSION_MAP = conversionMap;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public static ToolType fromString(String text) {
        if (text == null) {
            return null;
        }

        for (ToolType type : ToolType.values()) {
            if (type.NAME.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    public Tool createTool(Room location) {
        return new Tool(NAME, DESCRIPTION, location, ID, CONVERSION_MAP);
    }
}
