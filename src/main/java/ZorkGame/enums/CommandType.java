package ZorkGame.enums;

// Enum for all valid game commands
public enum CommandType {
    GO("go", "Move to another room"),
    QUIT("quit", "End the game"),
    HELP("help", "Show help"),
    PICK("pick", "Pick up something"),
    TAKE("take", "Take something"),
    DROP("drop", "Drop something"),
    INVENTORY("inventory", "Show inventory"),
    INV("inv", "Show inventory"),
    RECIPES("recipes", "Show recipe progress"),
    SAVE("save", "Save your game"),
    LOAD("load", "Load your game"),
    MAP("map", "Show map of the world"),
    USE("use", "Use a tool"),
    SQUEEZE("squeeze", "Make juice out of something"),
    TALK("talk", "Talk to an NPC"),
    ACHIEVEMENTS("achievements", "Show your achievements");

    private final String COMMAND_WORD;
    private final String DESCRIPTION;

    CommandType(String commandWord, String description) {
        this.COMMAND_WORD = commandWord;
        this.DESCRIPTION = description;
    }

    public String getCommandWord() {
        return COMMAND_WORD;
    }

    public String getDescription() {
        return DESCRIPTION;
    }


    public static CommandType fromString(String text) {
        if (text == null) {
            return null;
        }

        String normalized = text.toLowerCase().trim();

        if (normalized.isEmpty()) {
            return null;
        }

        for (CommandType type : CommandType.values()) {
            if (type.COMMAND_WORD.equalsIgnoreCase(text)) {
                return type;
            }
        }

        if (normalized.length() == 1) {
            switch (normalized.charAt(0)) {
                case 'i':
                    return INVENTORY;
                case 'h':
                    return HELP;
                case 'q':
                    return QUIT;
                case 'g':
                    return GO;
                case 't':
                    return TAKE;
                case 'd':
                    return DROP;
                case 'm':
                    return MAP;
                case 'u':
                    return USE;
                case 's':
                    return SAVE;
                case 'l':
                    return LOAD;
                case 'r':
                    return RECIPES;
            }
        }

        if (normalized.startsWith("inv") && normalized.length() <= "inventory".length()) {
            if ("inventory".startsWith(normalized)) {
                return INVENTORY;
            }
        }

        if (normalized.startsWith("rec") && normalized.length() <= "recipes".length()) {
            if ("recipes".startsWith(normalized)) {
                return RECIPES;
            }
        }

        if (normalized.startsWith("squ") && normalized.length() <= "squeeze".length()) {
            if ("squeeze".startsWith(normalized)) {
                return SQUEEZE;
            }
        }

        if (normalized.startsWith("tal") && normalized.length() <= "talk".length()) {
            if ("talk".startsWith(normalized)) {
                return TALK;
            }
        }

        if (normalized.length() >= 2) {
            for (CommandType type : CommandType.values()) {
                if (type.COMMAND_WORD.startsWith(normalized)) {
                    return type;
                }
            }
        }

        return null;
    }
}
