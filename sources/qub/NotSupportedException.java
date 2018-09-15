package qub;

/**
 * An exception that is thrown when functionality is requested that isn't supported yet.
 */
public class NotSupportedException extends RuntimeException
{
    /**
     * Create a new NotSupportedException.
     */
    public NotSupportedException()
    {
        this("This feature is not yet supported.");
    }

    /**
     * Create a new NotSupportedException.
     * @param message The message that will be associated with this exception.
     */
    public NotSupportedException(String message)
    {
        super(message);
    }
}
