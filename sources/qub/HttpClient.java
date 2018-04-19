package qub;

/**
 * A class that can send HTTP requests and receive HTTP responses.
 */
public interface HttpClient
{
    /**
     * Send the provided HttpRequest and then wait for the target endpoint to return a HttpResponse.
     * @param request The HttpRequest to send.
     * @return The HttpResponse that the server returned.
     */
    Result<HttpResponse> send(HttpRequest request);
}
