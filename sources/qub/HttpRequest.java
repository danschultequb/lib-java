package qub;

/**
 * A HTTP request that will be sent from a TCPClient to a HTTP server.
 */
public class HttpRequest
{
    private HttpMethod method;
    private URL url;
    private final MutableHttpHeaders headers;
    private int contentLength;
    private ByteReadStream body;

    private HttpRequest(HttpMethod method, URL url, Iterable<HttpHeader> headers, int contentLength, ByteReadStream body)
    {
        this.method = method;
        this.url = url;
        this.headers = new MutableHttpHeaders(headers);
        setBody(contentLength, body);
    }

    public static Result<HttpRequest> create(HttpMethod method, String urlString)
    {
        Result<HttpRequest> result;
        final Result<URL> url = URL.parse(urlString);
        if (url.hasError())
        {
            result = Result.error(url.getError());
        }
        else
        {
            result = create(method, url.getValue());
        }
        return result;
    }

    public static Result<HttpRequest> create(HttpMethod method, URL url)
    {
        return HttpRequest.create(method, url, null, 0, null);
    }

    public static Result<HttpRequest> create(HttpMethod method, String urlString, Iterable<HttpHeader> headers, int contentLength, ByteReadStream body)
    {
        Result<HttpRequest> result;
        final Result<URL> url = URL.parse(urlString);
        if (url.hasError())
        {
            result = Result.error(url.getError());
        }
        else
        {
            result = create(method, url.getValue(), headers, contentLength, body);
        }
        return result;
    }

    public static Result<HttpRequest> create(HttpMethod method, URL url, Iterable<HttpHeader> headers, int contentLength, ByteReadStream body)
    {
        Result<HttpRequest> result = Result.notNull(method, "method");
        if (result == null)
        {
            result = Result.notNull(url, "url");
            if (result == null)
            {
                result = Result.greaterThanOrEqualTo(contentLength, 0, "contentLength");
                if (result == null)
                {
                    if (contentLength == 0 && body != null)
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
                }
            }
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

    public URL getUrl()
    {
        return url;
    }

    public Result<Boolean> setUrl(String urlString)
    {
        Result<Boolean> result;
        final Result<URL> url = URL.parse(urlString);
        if (url.hasError())
        {
            result = Result.error(url.getError());
        }
        else
        {
            result = setUrl(url.getValue());
        }
        return result;
    }

    public Result<Boolean> setUrl(URL url)
    {
        Result<Boolean> result = Result.notNull(url, "url");
        if (result == null)
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
        setBody(contentLength, contentLength == 0 ? null : new InMemoryByteStream(bodyBytes).endOfStream());
    }

    public void setBody(String bodyText)
    {
        final Result<byte[]> bodyBytes = CharacterEncoding.US_ASCII.encode(bodyText);
        setBody(bodyBytes.hasError() ? null : bodyBytes.getValue());
    }
}
