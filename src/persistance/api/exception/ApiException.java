package persistance.api.exception;

import java.io.IOException;

/**
 * Signals that an error has occurred when trying to connect to a server. This class is the general class of exceptions
 * produced when connecting to the API.
 */
public abstract class ApiException extends IOException {

    private final String url;

    /**
     * Constructs an ApiException with the specified url, detail message and cause.
     * Note that the detail message associated with cause is not automatically incorporated into this exception's detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause The cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @param url The URL which was trying to be accessed (which is saved for later retrieval by the getUrl() method)
     */
    public ApiException(String message, Throwable cause, String url) {
        super(message, cause);
        this.url = url;
    }

    /**
     * Returns the URL which was trying to be accessed when the exception was thrown.
     *
     * @return The URL which was trying to be accessed at the time of the exception.
     */
    public String getUrl() {
        return url;
    }
}
