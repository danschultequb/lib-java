package qub;

/**
 * The HTTP response sent from a HTTP server to a HTTP client as a result of a HTTP request.
 */
public interface HttpResponse
{
    /**
     * Get the status code sent from the HTTP server.
     * @return The status code sent from the HTTP server.
     */
    int getStatusCode();

    /**
     * Get the HTTP headers that were sent from the HTTP server.
     * @return The HTTP headers that were sent from the HTTP server.
     */
    HttpHeaders getHeaders();

    /**
     * Get the body of this HttpResponse.
     * @return The body of this HttpResponse.
     */
    ByteReadStream getBody();
}
