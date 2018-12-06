package qub;

/**
 * An exception that is created when a value already exists.
 */
public class AlreadyExistsException extends RuntimeException
{
    private final Object value;

    /**
     * Create a new AlreadyExistsException.
     * @param value The value that was already found.
     */
    public AlreadyExistsException(Object value)
    {
        this(value, "The value " + Objects.toString(value) + " already exists.");
    }

    /**
     * Create a new AlreadyExistsException.
     * @param value The value that was already found.
     * @param message The message that will be associated with this exception.
     */
    public AlreadyExistsException(Object value, String message)
    {
        super(message);

        PreCondition.assertNotNullAndNotEmpty(message, "message");

        this.value = value;
    }

    /**
     * Get the value that was already found.
     * @return The value that was already found.
     */
    public Object getValue()
    {
        return value;
    }
}
