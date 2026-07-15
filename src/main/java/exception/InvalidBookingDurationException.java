package exception;

public class InvalidBookingDurationException extends Exception {
    // Constructor
    public InvalidBookingDurationException(String message) {
        // Call parent class Exception and pass the error message to it
        super(message);
    }
}