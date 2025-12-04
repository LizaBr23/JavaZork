package ZorkGame.commands;

import ZorkGame.enums.CommandType;
public class Command {
    private final CommandType commandType;
    private final String secondWord;
    private final String[] allWords;

    public Command(String firstWord, String[] remainingWords) {
        this.commandType = CommandType.fromString(firstWord);
        this.allWords = remainingWords;
        if (remainingWords != null && remainingWords.length > 0) {
            this.secondWord = remainingWords[0];
        } else {
            this.secondWord = null;
        }
    }

    // Get the command type enum
    public CommandType getCommandType() {
        return commandType;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public boolean hasSecondWord() {
        return secondWord != null;
    }

    public String[] getRemainingWords() {
        return allWords;
    }
}

