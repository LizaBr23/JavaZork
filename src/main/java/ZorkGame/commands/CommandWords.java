package ZorkGame.commands;

import ZorkGame.enums.CommandType;

public class CommandWords {

    public boolean isCommand(String commandWord) {
        return CommandType.fromString(commandWord) == null;
    }

    public void showAll() {
        System.out.print("Valid commands are: ");
        for (CommandType type : CommandType.values()) {
            System.out.print(type.getCommandWord() + " ");
        }
        System.out.println();
    }
}
