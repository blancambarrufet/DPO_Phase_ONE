package persistance.api.exception;

import java.net.URISyntaxException;

/**
 * Signals that an error has occurred when trying to connect to a server. Specifically, this exception is thrown when
 * the corresponding URL is malformed and the connection to the API can't be started.
 */
public final class MalformedUrlException extends ApiException {
    /**
     * Constructs a MalformedUrlException indicating that a certain URL was malformed.
     *
     * @param url The URL that the program was trying to access. This is used to build a predefined detail message.
     * @param cause The original exception signalling the malformed URL, which should be an URISyntaxException.
     */
    public MalformedUrlException(String url, URISyntaxException cause) {
        super("Malformed URL: " + url, cause, url);
    }
}
