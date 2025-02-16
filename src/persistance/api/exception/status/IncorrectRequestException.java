package persistance.api.exception.status;

/**
 * Signals that the program could connect to a server, but the request wasn't successful. Specifically, this exception
 * is thrown when the problem arose due to an incorrect request, such as trying to connect to a non-existing endpoint.
 */
public final class IncorrectRequestException extends StatusCodeException {
    /**
     * Constructs an IncorrectRequestException indicating that the API is available, but the request was incorrect.
     *
     * @param url The URL that the program was trying to access. This is used to build a predefined detail message.
     * @param statusCode The status code returned by the API, which can be useful when diagnosing errors. This is used to build a predefined detail message.
     */
    public IncorrectRequestException(String url, int statusCode) {
        super("The server at " + url + " is reachable, but your request was incorrect (status code: " + statusCode + "). Googling this status code may help debug the issue.", statusCode, url);
    }
}
