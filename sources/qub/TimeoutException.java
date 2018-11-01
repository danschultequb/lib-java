package qub;

/**
 * An exception that is thrown when an operation takes too long.
 */
public class TimeoutException extends RuntimeException
{
    public TimeoutException()
    {
        super();
    }

    public TimeoutException(String message)
    {
        super(message);
    }
}
