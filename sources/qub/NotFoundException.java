package qub;

/**
 * When searching through a set of key and value pairs, no entry was found that matches the provided
 * key.
 */
public class NotFoundException extends RuntimeException
{
    /**
     * The value that wasn't found.
     */
    private final Object value;

    /**
     * Create a new NotFoundException with the provided value that wasn't found.
     * @param value The value that wasn't found.
     */
    public NotFoundException(Object value)
    {
        this(value, "Could not find the value: " + value);
    }

    /**
     * Create a new NotFoundException with the provided value that wasn't found.
     * @param value The value that wasn't found.
     * @param message A message that explains the cause of this error.
     */
    public NotFoundException(Object value, String message)
    {
        super(message);

        this.value = value;
    }

    /**
     * Get the value that wasn't found.
     * @return The value that wasn't found.
     */
    public Object getValue()
    {
        return value;
    }
}
