package qub;

/**
 * When searching through a set of key and value pairs, no entry was found that isMatch the provided
 * key.
 */
public class NotFoundException extends RuntimeException
{
    /**
     * Create a new NotFoundException with the provided message.
     * @param message A message explaning the cause of this error.
     */
    public NotFoundException(String message)
    {
        super(message);
    }
}
