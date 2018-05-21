package qub;

/**
 * An exception that is thrown when a particular value isn't found.
 */
public class NotFoundException extends RuntimeException
{
    private final Object value;

    public NotFoundException(Object value)
    {
        this(value, "Could not find " + value + ".");
    }

    public NotFoundException(Object value, String message)
    {
        super(message);

        this.value = value;
    }

    /**
     * Get the value that was not found.
     * @return The value that was not found.
     */
    public Object getValue()
    {
        return value;
    }
}
