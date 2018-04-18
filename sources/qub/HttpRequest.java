package qub;

/**
 * A HTTP request that will be sent from a TCPClient to a HTTP server.
 */
public class HttpRequest
{
    private HttpMethod method;
    private String url;
    private final HttpHeaders headers;

    public HttpRequest(HttpMethod method, String url, HttpHeaders headers)
    {
        this.method = method;
        this.url = url;
        this.headers = new HttpHeaders(headers);
    }
}
