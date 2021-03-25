package qub;

/**
 * An attempt to use a socket fails because the socket is already closed/disposed.
 */
public class SocketClosedException extends RuntimeException
{
    /**
     * Create a new SocketClosedException.
     */
    public SocketClosedException()
    {
        super();
    }

    /**
     * Create a new SocketClosedException with the provided message.
     * @param message A message explaining the cause of this error.
     */
    public SocketClosedException(String message)
    {
        super(message);
    }

    /**
     * Create a new SocketClosedException.
     * @param innerException The exception that caused the SocketClosedException.
     */
    public SocketClosedException(Throwable innerException)
    {
        super(innerException);
    }

    /**
     * Create a new SocketClosedException with the provided message.
     * @param message A message explaining the cause of this error.
     * @param innerException The exception that caused the SocketClosedException.
     */
    public SocketClosedException(String message, Throwable innerException)
    {
        super(message, innerException);
    }
}
