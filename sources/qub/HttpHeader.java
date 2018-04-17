package qub;

/**
 * An individual header within a HTTP request.
 */
public class HttpHeader
{
    private final String name;
    private final String value;

    private HttpHeader(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * Create a new HTTP header with the provided name and value.
     * @param name The name of the HTTP header.
     * @param value The value of the HTTP header.
     * @return The created HttpHeader.
     */
    public static Result<HttpHeader> create(String name, String value)
    {
        Result<HttpHeader> result;
        if (Strings.isNullOrEmpty(name))
        {
            result = Result.error(new IllegalArgumentException("name cannot be null or empty."));
        }
        else if (Strings.isNullOrEmpty(value))
        {
            result = Result.error(new IllegalArgumentException("value cannot be null or empty."));
        }
        else
        {
            result = Result.success(new HttpHeader(name, value));
        }
        return result;
    }

    /**
     * Get the name of this HTTP header.
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get the value of this HTTP header.
     * @return
     */
    public String getValue()
    {
        return this.value;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof HttpHeader && equals((HttpHeader)rhs);
    }

    /**
     * Get whether or not this HttpHeader equals the provided HttpHeader.
     * @param rhs The HttpHeader to compare against this HttpHeader.
     * @return Whether or not this HttpHeader equals the provided HttpHeader.
     */
    public boolean equals(HttpHeader rhs)
    {
        return rhs != null &&
            this.name.equalsIgnoreCase(rhs.name) &&
            this.value.equals(rhs.value);
    }

    @Override
    public String toString()
    {
        return name + ": " + value;
    }
}
