package ZorkGame.enums;

// Enum for cardinal directions in the game
public enum Direction {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west");

    private final String DIRECTION_NAME;

    Direction(String directionName) {
        this.DIRECTION_NAME = directionName;
    }

    public String getDirectionName() {
        return DIRECTION_NAME;
    }

    // Convert string to Direction enum
    public static Direction fromString(String text) {
        if (text == null) {
            return null;
        }

        for (Direction direction : Direction.values()) {
            if (direction.DIRECTION_NAME.equalsIgnoreCase(text)) {
                return direction;
            }
        }

        return switch (text.toLowerCase()) {
            case "n" -> NORTH;
            case "s" -> SOUTH;
            case "e" -> EAST;
            case "w" -> WEST;
            default -> null;
        };
    }

}
