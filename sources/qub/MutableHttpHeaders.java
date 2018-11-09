package qub;

/**
 * A collection of HTTP headers to be used in an HTTP request or response.
 */
public class MutableHttpHeaders implements HttpHeaders
{
    private final Map<String,HttpHeader> headerMap;

    /**
     * Create a new empty MutableHttpHeaders collection.
     */
    public MutableHttpHeaders()
    {
        headerMap = new ListMap<>();
    }

    public MutableHttpHeaders(Iterable<HttpHeader> headers)
    {
        this();

        if (headers != null && headers.any())
        {
            for (final HttpHeader header : headers)
            {
                set(header);
            }
        }
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
     * Remove all headers from this HTTP header collection.
     */
    public void clear()
    {
        headerMap.clear();
    }

    /**
     * Set the provided header within this HTTP headers collection.
     * @param header The header to set.
     */
    public void set(HttpHeader header)
    {
        PreCondition.assertNotNull(header, "header");

        set(header.getName(), header.getValue());
    }

    /**
     * Set the provided header name and value within this HTTP headers collection.
     * @param headerName The name of the header to set.
     * @param headerValue The value of the header to set.
     */
    public void set(String headerName, int headerValue)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        set(headerName, Integers.toString(headerValue));
    }

    /**
     * Set the provided header name and value within this HTTP headers collection.
     * @param headerName The name of the header to set.
     * @param headerValue The value of the header to set.
     */
    public void set(String headerName, long headerValue)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        set(headerName, Longs.toString(headerValue));
    }

    /**
     * Set the provided header name and value within this HTTP headers collection.
     * @param headerName The name of the header to set.
     * @param headerValue The value of the header to set.
     */
    public void set(String headerName, String headerValue)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");
        PreCondition.assertNotNullAndNotEmpty(headerValue, "headerValue");

        final HttpHeader header = new HttpHeader(headerName, headerValue);
        headerMap.set(getHeaderKey(header), header);
    }

    @Override
    public boolean contains(String headerName)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        return get(headerName).getValue() != null;
    }

    /**
     * Get the header in this collection that has the provided header name.
     * @param headerName The name of the header to get.
     * @return The value of the header in this collection, if the header exists in this collection.
     */
    public Result<HttpHeader> get(String headerName)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        final String headerKey = getHeaderKey(headerName);
        final HttpHeader header = headerMap.get(headerKey);

        Result<HttpHeader> result;
        if (header == null)
        {
            result = Result.error(new KeyNotFoundException(headerName));
        }
        else
        {
            result = Result.success(header);
        }

        return result;
    }

    public Result<String> getValue(String headerName)
    {
        Result<String> result;

        final Result<HttpHeader> getResult = get(headerName);
        if (getResult.hasError())
        {
            result = Result.error(getResult.getError());
        }
        else
        {
            result = Result.success(getResult.getValue().getValue());
        }

        return result;
    }

    public Result<Boolean> remove(String headerName)
    {
        Result<Boolean> result;
        if (headerName == null)
        {
            result = Result.error(new IllegalArgumentException("headerName cannot be null."));
        }
        else if (headerName.isEmpty())
        {
            result = Result.error(new IllegalArgumentException("headerName cannot be empty."));
        }
        else if (!headerMap.remove(getHeaderKey(headerName)))
        {
            result = Result.done(false, new KeyNotFoundException(headerName));
        }
        else {
            result = Result.successTrue();
        }
        return result;
    }

    @Override
    public Iterator<HttpHeader> iterate()
    {
        return headerMap.getValues().iterate();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
