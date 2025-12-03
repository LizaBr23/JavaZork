package ZorkGame.exceptions;



public class InvalidMenuChoiceException extends Exception {
    public InvalidMenuChoiceException(String message) {
        super(message);
    }
    public InvalidMenuChoiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
