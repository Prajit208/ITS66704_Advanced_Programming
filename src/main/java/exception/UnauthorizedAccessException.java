package exception;

public class UnauthorizedAccessException extends Exception {
    //Constructor
    public UnauthorizedAccessException(String message) {
        // Pass the message to the parent Exception constructor
        super(message);
    }
}