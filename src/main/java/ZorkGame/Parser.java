package ZorkGame;

import java.util.Scanner;

public class Parser {
    private final CommandWords commands;
    private final Scanner reader;

    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    public Command getCommand() {
        String inputLine;
        String commandWord = null;
        String[] remainingWords = null;

        System.out.print("> ");
        inputLine = reader.nextLine();
        String[] words = inputLine.trim().split("\\s+");

        if (words.length > 0) {
            commandWord = words[0];
            if (!commands.isCommand(commandWord)) {
                commandWord = null;
            }
            if (words.length > 1) {
                remainingWords = new String[words.length - 1];
                //copy from indx 1 words into remW [length]
                System.arraycopy(words, 1, remainingWords, 0, words.length - 1);
            }
        }

        return new Command(commandWord, remainingWords);
    }

    public void showCommands() {
        commands.showAll();
    }
}
