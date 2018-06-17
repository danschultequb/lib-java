package qub;

/**
 * The failure exception that is thrown when a post-condition fails.
 */
public class PostConditionFailure extends RuntimeException
{
    public PostConditionFailure(String message)
    {
        super(message);
    }
}
