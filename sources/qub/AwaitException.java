package qub;

/**
 * A RuntimeException that is thrown when an AsyncTask is awaited and the AsyncTask has an outgoing
 * exception.
 */
public class AwaitException extends RuntimeException
{
    public AwaitException(Throwable cause)
    {
        super(cause.getMessage(), cause);
    }
}
