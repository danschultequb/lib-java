package qub;

/**
 * The HTTP response sent from a HTTP server to a HTTP client as a result of a HTTP request.
 */
public class HttpResponse implements AsyncDisposable
{
    private final String httpVersion;
    private final int statusCode;
    private final String reasonPhrase;
    private final HttpHeaders headers;
    private final ByteReadStream body;

    public HttpResponse(String httpVersion, int statusCode, String reasonPhrase, HttpHeaders headers, ByteReadStream body)
    {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.headers = headers;
        this.body = body;
    }

    public String getHttpVersion()
    {
        return httpVersion;
    }

    /**
     * Get the status code sent from the HTTP server.
     * @return The status code sent from the HTTP server.
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    public String getReasonPhrase()
    {
        return reasonPhrase;
    }

    /**
     * Get the HTTP headers that were sent from the HTTP server.
     * @return The HTTP headers that were sent from the HTTP server.
     */
    public HttpHeaders getHeaders()
    {
        return headers;
    }

    /**
     * Get the body of this HttpResponse.
     * @return The body of this HttpResponse.
     */
    public ByteReadStream getBody()
    {
        return body;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return body.getAsyncRunner();
    }

    @Override
    public boolean isDisposed()
    {
        return body.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return body.dispose();
    }
}
