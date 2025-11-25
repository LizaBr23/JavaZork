package StartPackage;

public class Command {
    private final String commandWord;
    private final String secondWord;
    private String[] allWords;

    public Command(String firstWord, String secondWord) {
        this.commandWord = firstWord;
        this.secondWord = secondWord;
        this.allWords = new String[]{secondWord};
    }

    public Command(String firstWord, String[] remainingWords) {
        this.commandWord = firstWord;
        this.allWords = remainingWords;
        if (remainingWords != null && remainingWords.length > 0) {
            this.secondWord = remainingWords[0];
        } else {
            this.secondWord = null;
        }
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public boolean isUnknown() {
        return commandWord == null;
    }

    public boolean hasSecondWord() {
        return secondWord != null;
    }

    public String[] getRemainingWords() {
        return allWords;
    }
}
