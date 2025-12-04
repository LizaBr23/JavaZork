package ZorkGame.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private final CommandWords commands;
    private Scanner scanner;

    public Parser() {
        commands = new CommandWords();
        scanner = new Scanner(System.in);
    }

    public Parser(boolean guiMode) {
        commands = new CommandWords();
        if (!guiMode) {
            scanner = new Scanner(System.in);
        }
    }

    public Command getCommand() {
        System.out.print("> ");
        String inputLine = scanner.nextLine();

        return parseCommandString(inputLine);
    }


    private String[] split(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (isWhitespace(c)) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }

        //add the last token if it exists
        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }

        return tokens.toArray(new String[0]);
    }

//\t = tab  \r = carriage return (old control character for cursor movement)
    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    public void showCommands() {
        commands.showAll();
    }

    public Command parseCommandString(String inputLine) {
        if (inputLine == null || inputLine.isBlank()) {
            return new Command(null, null);
        }

        inputLine = inputLine.replaceAll("[^a-zA-Z0-9\\s\"']", "");

        String[] words = split(inputLine);

        if (words.length == 0 || (words.length == 1 && words[0].isEmpty())) {
            return new Command(null, null);
        }

        String commandWord = words[0];
        String[] remainingWords = null;

        if (commands.isCommand(commandWord)) {
            commandWord = null;
        }

        if (words.length > 1) {
            remainingWords = new String[words.length - 1];
            System.arraycopy(words, 1, remainingWords, 0, words.length - 1);
        }

        return new Command(commandWord, remainingWords);
    }
}
