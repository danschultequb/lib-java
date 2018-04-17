package qub;

public class HttpHeader
{
    private final String name;
    private final String value;

    private HttpHeader(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

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

    public String getName()
    {
        return this.name;
    }

    public String getValue()
    {
        return this.value;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof HttpHeader && equals((HttpHeader)rhs);
    }

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
