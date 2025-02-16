package persistance.api.exception.status;

import persistance.api.exception.ApiException;

/**
 * Signals that the program could connect to a server, but the request wasn't successful. This class is the general
 * class of exceptions produced as a result of a non-successful status code.
 */
public abstract class StatusCodeException extends ApiException {
    private final int statusCode;
    /**
     * Constructs an ApiException with the specified url, detail message and status code.
     * Note that the detail message associated with cause is not automatically incorporated into this exception's detail message.
     *
     * @param message    The detail message (which is saved for later retrieval by the getMessage() method)
     * @param statusCode The status code returned by the API when processing an incorrect request.
     * @param url        The URL which was trying to be accessed (which is saved for later retrieval by the getUrl() method)
     */
    public StatusCodeException(String message, int statusCode, String url) {
        super(message, null, url);
        this.statusCode = statusCode;
    }

    /**
     * Returns the status code which the server returned, causing the exception to be thrown.
     *
     * @return The status code which the server returned, causing the exception to be thrown.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
