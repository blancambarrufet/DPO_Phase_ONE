package persistance.api.exception.status;

/**
 * Signals that the program could connect to a server, but the request wasn't successful. Specifically, this exception
 * is thrown when the server is reachable, but the API is unavailable due to internal errors.
 */
public final class UnavailableApiException extends StatusCodeException {
    /**
     * Constructs a ApiUnavailableException indicating that a certain API is unavailable, despite the server being reachable.
     *
     * @param url The URL that the program was trying to access. This is used to build a predefined detail message.
     * @param statusCode The status code returned by the API, which can be useful when diagnosing errors. This is used to build a predefined detail message.
     */
    public UnavailableApiException(String url, int statusCode) {
        super("The server at " + url + " is reachable, but the API isn't available (status code: " + statusCode + "). Contact the subject coordinator.", statusCode, url);
    }
}
