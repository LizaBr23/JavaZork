import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private final Map<String, String> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
        validCommands.put("go", "Move to another room");
        validCommands.put("up", "Move up");
        validCommands.put("down", "Move down");
        validCommands.put("dig", "Dig the ground");
        validCommands.put("quit", "End the game");
        validCommands.put("help", "Show help");
        validCommands.put("look", "Look around");
        validCommands.put("eat", "Eat something");
        validCommands.put("drink", "Drink something");
        validCommands.put("pick", "pick up something");
        validCommands.put("drop", "drop something");
        validCommands.put("inventory", "Show inventory");
        validCommands.put("recipes", "Show recipe progress");
        validCommands.put("save", "Save your game");
        validCommands.put("load", "Load your game");
        validCommands.put("map", "Show map of the world");
    }
//is command in a list of valid commands
    public boolean isCommand(String commandWord) {
        return validCommands.containsKey(commandWord);
    }

    public void showAll() {
        System.out.print("Valid commands are: ");
        //for keys only
        for (String command : validCommands.keySet()) {
            System.out.print(command + " ");
        }
        System.out.println();
    }
}
