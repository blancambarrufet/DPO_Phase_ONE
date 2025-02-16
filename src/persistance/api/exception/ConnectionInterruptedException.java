package persistance.api.exception;

/**
 * Signals that an error has occurred when trying to connect to a server. Specifically, this exception is thrown when
 * the connection is interrupted due to external causes.
 */
public final class ConnectionInterruptedException extends ApiException {
    /**
     * Constructs a ConnectionInterruptedException indicating that the connection to a certain URL was interrupted.
     *
     * @param url The URL that the program was trying to access. This is used to build a predefined detail message.
     * @param cause The original exception signalling the interruption, which should be an InterruptedException.
     */
    public ConnectionInterruptedException(String url, InterruptedException cause) {
        super("The connection to the server at " + url + " was interrupted.", cause, url);
    }
}
