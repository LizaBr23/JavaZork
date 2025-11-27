package ZorkGame;

// Enum for cardinal directions in the game
public enum Direction {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west");

    private final String directionName;

    Direction(String directionName) {
        this.directionName = directionName;
    }

    public String getDirectionName() {
        return directionName;
    }

    // Convert string to Direction enum
    public static Direction fromString(String text) {
        if (text == null) {
            return null;
        }

        for (Direction direction : Direction.values()) {
            if (direction.directionName.equalsIgnoreCase(text)) {
                return direction;
            }
        }
        return null;
    }

    // Get opposite direction - useful for automatically creating two-way connections between rooms
    // Example: if you go NORTH from roomA to roomB, roomB's SOUTH exit should point back to roomA
    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}
