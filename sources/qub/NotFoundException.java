package qub;

/**
 * An {@link RuntimeException} that is used when an operation can't find what it is looking for.
 */
public class NotFoundException extends RuntimeException
{
    /**
     * Create a new {@link NotFoundException}.
     */
    public NotFoundException()
    {
        super();
    }

    /**
     * Create a new {@link NotFoundException} with the provided message.
     * @param message A message explaining the cause of this error.
     */
    public NotFoundException(String message)
    {
        super(message);
    }

    /**
     * Create a new {@link NotFoundException}.
     * @param innerException The exception that caused the error.
     */
    public NotFoundException(Throwable innerException)
    {
        super(innerException);
    }

    /**
     * Create a new {@link NotFoundException} with the provided message.
     * @param message A message explaining the cause of this error.
     * @param innerException The exception that caused the error.
     */
    public NotFoundException(String message, Throwable innerException)
    {
        super(message, innerException);
    }
}
