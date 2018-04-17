package qub;

/**
 * A collection of HTTP headers to be used in an HTTP request or response.
 */
public class HttpHeaders extends IterableBase<HttpHeader>
{
    private final Map<String,HttpHeader> headerMap;

    /**
     * Create a new empty HttpHeaders collection.
     */
    public HttpHeaders()
    {
        headerMap = new ListMap<String,HttpHeader>();
    }

    private static String getHeaderKey(HttpHeader header)
    {
        return getHeaderKey(header.getName());
    }

    private static String getHeaderKey(String headerName)
    {
        return headerName.toLowerCase();
    }

    /**
     * Set the provided header name and value within this HTTP headers collection.
     * @param headerName The name of the header to set.
     * @param headerValue The value of the header to set.
     * @return Whether or not the header was set.
     */
    public Result<Boolean> set(String headerName, String headerValue)
    {
        Result<Boolean> result;

        final Result<HttpHeader> headerResult = HttpHeader.create(headerName, headerValue);
        if (headerResult.hasError())
        {
            result = Result.done(false, headerResult.getError());
        }
        else
        {
            final HttpHeader header = headerResult.getValue();
            headerMap.set(getHeaderKey(header), header);
            result = Result.success(true);
        }

        return result;
    }

    /**
     * Get the header in this collection that has the provided header name.
     * @param headerName The name of the header to get.
     * @return The value of the header in this collection, if the header exists in this collection.
     */
    public Result<HttpHeader> get(String headerName)
    {
        Result<HttpHeader> result;

        if (Strings.isNullOrEmpty(headerName))
        {
            result = Result.error(new IllegalArgumentException("headerName cannot be null or empty."));
        }
        else
        {
            final String headerKey = getHeaderKey(headerName);
            final HttpHeader header = headerMap.get(headerKey);
            if (header == null)
            {
                result = Result.error(new KeyNotFoundException(headerName));
            }
            else {
                result = Result.success(header);
            }
        }

        return result;
    }

    @Override
    public Iterator<HttpHeader> iterate()
    {
        return headerMap.getValues().iterate();
    }
}
