package qub;

/**
 * An exception that is created when a value already exists.
 */
public class AlreadyExistsException extends RuntimeException
{
    /**
     * Create a new AlreadyExistsException.
     * @param message The message that will be associated with this exception.
     */
    public AlreadyExistsException(String message)
    {
        super(message);

        PreCondition.assertNotNullAndNotEmpty(message, "message");
    }
}
