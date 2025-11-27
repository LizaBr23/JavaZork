package ZorkGame;

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
    TALK("talk", "Talk to an NPC");

    private final String commandWord;
    private final String description;

    // Constructor for enum
    CommandType(String commandWord, String description) {
        this.commandWord = commandWord;
        this.description = description;
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getDescription() {
        return description;
    }

    // Convert string to enum, returns null if not found
    public static CommandType fromString(String text) {
        if (text == null) {
            return null;
        }

        for (CommandType type : CommandType.values()) {
            if (type.commandWord.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
