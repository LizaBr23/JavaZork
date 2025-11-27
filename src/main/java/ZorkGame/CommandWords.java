package ZorkGame;

public class CommandWords {

    // Check if command string is valid by converting to enum
    public boolean isCommand(String commandWord) {
        return CommandType.fromString(commandWord) != null;
    }

    // Show all valid commands from enum
    public void showAll() {
        System.out.print("Valid commands are: ");
        for (CommandType type : CommandType.values()) {
            System.out.print(type.getCommandWord() + " ");
        }
        System.out.println();
    }
}
