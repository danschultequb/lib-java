package qub;

/**
 * An exception that is used when the end of something is reached.
 */
public class EndException extends RuntimeException
{
    public EndException()
    {
        super();
    }

    public EndException(String message)
    {
        super(message);
    }

    public EndException(Throwable cause)
    {
        super(cause);
    }

    public EndException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
