package qub;

/**
 * A HTTP request that will be sent from a TCPClient to a HTTP server.
 */
public class HttpRequest
{
    private HttpMethod method;
    private String url;
    private final MutableHttpHeaders headers;
    private int contentLength;
    private ByteReadStream body;

    private HttpRequest(HttpMethod method, String url, Iterable<HttpHeader> headers, int contentLength, ByteReadStream body)
    {
        this.method = method;
        this.url = url;
        this.headers = new MutableHttpHeaders(headers);
        setBody(contentLength, body);
    }

    public static Result<HttpRequest> create(HttpMethod method, String url)
    {
        return HttpRequest.create(method, url, null, 0, null);
    }

    public static Result<HttpRequest> create(HttpMethod method, String url, Iterable<HttpHeader> headers, int contentLength, ByteReadStream body)
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
        else if (contentLength < 0)
        {
            result = Result.error(new IllegalArgumentException("contentLength must be greater than or equal to 0."));
        }
        else if (contentLength == 0 && body != null)
        {
            result = Result.error(new IllegalArgumentException("If contentLength is 0, then body must be null."));
        }
        else if (contentLength > 0 && body == null)
        {
            result = Result.error(new IllegalArgumentException("If contentLength is greater than 0, then body must be non-null."));
        }
        else
        {
            result = Result.success(new HttpRequest(method, url, headers, contentLength, body));
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

    public int getContentLength()
    {
        return contentLength;
    }

    public ByteReadStream getBody()
    {
        return body;
    }

    public Result<Boolean> setBody(int contentLength, ByteReadStream body)
    {
        Result<Boolean> result;
        if (contentLength < 0)
        {
            result = Result.error(new IllegalArgumentException("contentLength must be greater than or equal to 0."));
        }
        else if (contentLength == 0 && body != null)
        {
            result = Result.error(new IllegalArgumentException("If contentLength is 0, then body must be null."));
        }
        else if (contentLength > 0 && body == null)
        {
            result = Result.error(new IllegalArgumentException("If contentLength is greater than 0, then body must be not null."));
        }
        else
        {
            this.contentLength = contentLength;
            this.body = body;

            if (this.contentLength == 0)
            {
                this.headers.remove("Content-Length");
            }
            else
            {
                this.headers.set("Content-Length", Integer.toString(contentLength));
            }

            result = Result.successTrue();
        }
        return result;
    }

    public void setBody(byte[] bodyBytes)
    {
        final int contentLength = bodyBytes == null ? 0 : bodyBytes.length;
        setBody(contentLength, contentLength == 0 ? null : new InMemoryByteReadStream(bodyBytes));
    }

    public void setBody(String bodyText)
    {
        setBody(Strings.isNullOrEmpty(bodyText) ? null : CharacterEncoding.UTF_8.encode(bodyText));
    }
}
