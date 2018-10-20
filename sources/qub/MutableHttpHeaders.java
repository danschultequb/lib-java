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
        headerMap = new ListMap<String,HttpHeader>();
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
     * Set the provided header within this HTTP headers collection.
     * @param header The header to set.
     * @return Whether or not the header was set.
     */
    public Result<Boolean> set(HttpHeader header)
    {
        Result<Boolean> result;

        if (header == null)
        {
            result = Result.error(new IllegalArgumentException("header cannot be null."));
        }
        else
        {
            result = set(header.getName(), header.getValue());
        }

        return result;
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
