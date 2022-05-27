package qub;

/**
 * A {@link NotFoundException} that is thrown when an operation is performed on an object that is
 * empty.
 */
public class EmptyException extends NotFoundException
{
    public EmptyException()
    {
        super();
    }

    public EmptyException(String message)
    {
        super(message);
    }

    public EmptyException(Throwable cause)
    {
        super(cause);
    }

    public EmptyException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
