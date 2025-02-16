package persistance.api.exception;

import java.io.IOException;

/**
 * Signals that an error has occurred when trying to connect to a server. Specifically, this exception is thrown when
 * the server is unreachable due to external circumstances.
 */
public final class UnreachableServerException extends ApiException {
    /**
     * Constructs a UnreachableServerException indicating that a certain server is unreachable.
     *
     * @param url The URL that the program was trying to access. This is used to build a predefined detail message.
     * @param cause The original exception signalling the unreachable server, which should be an IOException.
     */
    public UnreachableServerException(String url, IOException cause) {
        super("The server at " + url + " can't be reached.", cause, url);
    }
}
