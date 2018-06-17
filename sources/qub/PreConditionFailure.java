package qub;

/**
 * The failure exception that is thrown when a pre-condition fails.
 */
public class PreConditionFailure extends RuntimeException
{
    public PreConditionFailure(String message)
    {
        super(message);
    }
}
