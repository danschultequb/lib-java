package qub;

/**
 * A HTTP request that will be sent from a TCPClient to a HTTP server.
 */
public class HttpRequest
{
    private HttpMethod method;
    private String url;
    private final MutableHttpHeaders headers;
    private ByteReadStream body;

    private HttpRequest(HttpMethod method, String url, Iterable<HttpHeader> headers, ByteReadStream body)
    {
        this.method = method;
        this.url = url;
        this.headers = new MutableHttpHeaders(headers);
        this.body = body;
    }

    public static Result<HttpRequest> create(HttpMethod method, String url)
    {
        return HttpRequest.create(method, url, null, null);
    }

    public static Result<HttpRequest> create(HttpMethod method, String url, Iterable<HttpHeader> headers, ByteReadStream body)
    {
        Result<HttpRequest> result;

        if (method == null)
        {
            result = Result.error(new IllegalArgumentException("method cannot be null."));
        }
        else if (url == null)
        {
            result = Result.error(new IllegalArgumentException("url cannot be null."));
        }
        else if (url.isEmpty())
        {
            result = Result.error(new IllegalArgumentException("url cannot be empty."));
        }
        else
        {
            result = Result.success(new HttpRequest(method, url, headers, body));
        }

        return result;
    }

    public HttpMethod getMethod()
    {
        return method;
    }

    public Result<Boolean> setMethod(HttpMethod method)
    {
        Result<Boolean> result;
        if (method == null)
        {
            result = Result.error(new IllegalArgumentException("method cannot be null."));
        }
        else
        {
            this.method = method;
            result = Result.successTrue();
        }
        return result;
    }

    public String getUrl()
    {
        return url;
    }

    public Result<Boolean> setUrl(String url)
    {
        Result<Boolean> result;
        if (url == null)
        {
            result = Result.error(new IllegalArgumentException("url cannot be null."));
        }
        else if (url.isEmpty())
        {
            result = Result.error(new IllegalArgumentException("url cannot be empty."));
        }
        else
        {
            this.url = url;
            result = Result.successTrue();
        }
        return result;
    }

    public MutableHttpHeaders getHeaders()
    {
        return headers;
    }

    public ByteReadStream getBody()
    {
        return body;
    }

    public void setBody(ByteReadStream body)
    {
        this.body = body;
    }

    public void setBody(byte[] bodyBytes)
    {
        setBody(bodyBytes == null || bodyBytes.length == 0 ? null : new InMemoryByteReadStream(bodyBytes));
    }

    public void setBody(String bodyText)
    {
        setBody(Strings.isNullOrEmpty(bodyText) ? null : CharacterEncoding.UTF_8.encode(bodyText));
    }
}
