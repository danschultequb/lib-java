package qub;

/**
 * When searching through a set of key and value pairs, no entry was found that isMatch the provided
 * key.
 */
public class NotFoundException extends RuntimeException
{
    /**
     * Create a new NotFoundException with the provided message.
     * @param message A message explaining the cause of this error.
     */
    public NotFoundException(String message)
    {
        super(message);
    }

    /**
     * Create a new NotFoundException with the provided message.
     * @param message A message explaining the cause of this error.
     * @param innerException The exception that caused the NotFoundException.
     */
    public NotFoundException(String message, Throwable innerException)
    {
        super(message, innerException);
    }
}
