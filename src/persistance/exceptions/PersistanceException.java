package persistance.exceptions;

/**
 * Custom exception for handling persistence-related errors.
 * This exception is thrown when issues occur while accessing or modifying persistent data.
 */
public class PersistanceException extends RuntimeException {

    /**
     * Constructs a new PersistanceException with a specified error message.
     *
     * @param message The error message describing the cause of the exception.
     */
    public PersistanceException(String message) {
        super(message);
    }

    /**
     * Constructs a new PersistanceException with a specified error message and cause.
     *
     * @param message The error message describing the cause of the exception.
     * @param cause   The underlying exception that caused this error.
     */
    public PersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
