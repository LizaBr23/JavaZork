package ZorkGame.exceptions;


public class InvalidDialogueInputException extends Exception {
    public InvalidDialogueInputException(String message) {
        super(message);
    }
    public InvalidDialogueInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
