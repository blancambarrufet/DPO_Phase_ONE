package persistance.api;

import persistance.api.exception.*;
import persistance.api.exception.status.IncorrectRequestException;
import persistance.api.exception.status.UnavailableApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Helper class with the responsibility of reading and posting Strings to an HTTPS API.
 */
public final class ApiHelper {
    private final HttpClient client;

    /**
     * Default constructor, where the client used for HTTPS communication is set up.
     * A connection to an auxiliary API URL is also performed to verify its status.
     *
     * @throws ApiException If the server can't be reached or the connection is interrupted.
     */
    public ApiHelper() throws ApiException {
        client = HttpClient.newHttpClient();
        getFromUrl("https://balandrau.salle.url.edu/dpoo/healthcheck");
    }

    /**
     * Method that reads the contents from a URL using the HTTPS protocol. Specifically, a GET request is sent.
     * Any parameters should be included in the URL.
     *
     * @param url A String representation of the URL to read from, which will be assumed to use HTTP/HTTPS.
     * @return The contents of the URL represented as text.
     * @throws ApiException If the URL is malformed, the server can't be reached or the connection is interrupted.
     */
    public String getFromUrl(String url) throws ApiException {
        HttpResponse<String> response;

        try {
            // Define the request
            // The default method is GET, so we don't need to specify it (but we could do so by calling .GET() before .build()
            // The HttpRequest.Builder pattern offers a ton of customization for the request (headers, body, HTTP version...)
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();

            // We use the default BodyHandler for Strings (so we can get the body of the response as a String)
            // Note we could also send the request asynchronously, but things would escalate in terms of coding complexity
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Exceptions are all bundled as ApiException subclasses
        } catch (URISyntaxException e) {
            throw new MalformedUrlException(url, e);
        } catch (IOException e) {
            throw new UnreachableServerException(url, e);
        } catch (InterruptedException e) {
            throw new ConnectionInterruptedException(url, e);
        }

        // Detect when a response was received, but it wasn't successful
        validateResponseStatus(url, response.statusCode());

        // Just return the body
        return response.body();
    }

    /**
     * Method that posts contents to a URL using the HTTPS protocol. Specifically, a POST request is sent.
     * The request body is set to the corresponding parameter, and the response body is returned just in case.
     *
     * @param url  A String representation of the URL to post to, which will be assumed to use HTTP/HTTPS.
     * @param body The content to post, which will be sent to the server in the request body.
     * @return The contents of the response, in case the server sends anything back after posting the content.
     * @throws ApiException If the URL is malformed, the server can't be reached or the connection is interrupted.
     */
    public String postToUrl(String url, String body) throws ApiException {
        HttpResponse<String> response;

        try {
            // Define the request
            // In this case, we have to use the .POST() and .headers() methods to define what we want (to send a string containing JSON data)
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).headers("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();

            // We could use a BodyHandler that discards the response body, but here we return the API's response
            // Note we could also send the request asynchronously, but things would escalate in terms of coding complexity
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Exceptions are all bundled as ApiException subclasses
        } catch (URISyntaxException e) {
            throw new MalformedUrlException(url, e);
        } catch (IOException e) {
            throw new UnreachableServerException(url, e);
        } catch (InterruptedException e) {
            throw new ConnectionInterruptedException(url, e);
        }

        // Detect when a response was received, but it wasn't successful
        validateResponseStatus(url, response.statusCode());

        return response.body();
    }

    /**
     * Method that removes the contents from a URL using the HTTPS protocol. Specifically, a DELETE request is sent.
     * Any parameters should be included in the URL.
     *
     * @param url A String representation of the URL to delete from, which will be assumed to use HTTP/HTTPS.
     * @return The contents of the response, in case the server sends anything back after deleting the content.
     * @throws ApiException If the URL is malformed, the server can't be reached or the connection is interrupted.
     */
    public String deleteFromUrl(String url) throws ApiException {
        HttpResponse<String> response;

        try {
            // Define the request
            // The default method is GET, so we don't need to specify it (but we could do so by calling .GET() before .build()
            // The HttpRequest.Builder pattern offers a ton of customization for the request (headers, body, HTTP version...)
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).DELETE().build();

            // We use the default BodyHandler for Strings (so we can get the body of the response as a String)
            // Note we could also send the request asynchronously, but things would escalate in terms of coding complexity
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Exceptions are all bundled as ApiException subclasses
        } catch (URISyntaxException e) {
            throw new MalformedUrlException(url, e);
        } catch (IOException e) {
            throw new UnreachableServerException(url, e);
        } catch (InterruptedException e) {
            throw new ConnectionInterruptedException(url, e);
        }

        // Detect when a response was received, but it wasn't successful
        validateResponseStatus(url, response.statusCode());

        return response.body();
    }

    // Helper method that throws an exception if a call returns a status code that isn't successful
    private void validateResponseStatus(String url, int statusCode) throws ApiException {
        if (statusCode >= 500) {
            throw new UnavailableApiException(url, statusCode);
        }
        // Redirects can happen if they try to access HTTP endpoints instead of HTTPS ones. We will consider them errors.
        if (statusCode >= 300) {
            throw new IncorrectRequestException(url, statusCode);
        }
    }
}
