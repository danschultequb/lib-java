package qub;

/**
 * A collection of HTTP headers to be used in an HTTP request or response.
 */
public class MutableHttpHeaders implements HttpHeaders
{
    private final MutableMap<String,HttpHeader> headerMap;

    /**
     * Create a new empty MutableHttpHeaders collection.
     */
    public MutableHttpHeaders()
    {
        headerMap = Map.create();
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

        headerMap.set(getHeaderKey(headerName), new HttpHeader(headerName, headerValue));
    }

    @Override
    public boolean contains(String headerName)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        return !get(headerName).hasError();
    }

    /**
     * Get the header in this collection that has the provided header name.
     * @param headerName The name of the header to get.
     * @return The value of the header in this collection, if the header exists in this collection.
     */
    public Result<HttpHeader> get(String headerName)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        return headerMap.get(getHeaderKey(headerName))
            .catchErrorResult(NotFoundException.class, () -> createNotFoundResult(headerName));
    }

    public Result<String> getValue(String headerName)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        return get(headerName).then(HttpHeader::getValue);
    }

    public Result<HttpHeader> remove(String headerName)
    {
        PreCondition.assertNotNullAndNotEmpty(headerName, "headerName");

        return headerMap.remove(getHeaderKey(headerName))
            .catchErrorResult(NotFoundException.class, () -> createNotFoundResult(headerName));
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

    private static <T> Result<T> createNotFoundResult(String headerName)
    {
        return Result.error(new NotFoundException(headerName));
    }
}
