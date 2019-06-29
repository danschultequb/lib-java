package qub;

/**
 * An error that is returned when a parser encounters an unexpected value.
 */
public class ParseException extends RuntimeException
{
    /**
     * Create a new ParseException with the provided message.
     * @param message The message that explains why the parser failed.
     */
    public ParseException(String message)
    {
        super(message);

        PreCondition.assertNotNullAndNotEmpty(message, "message");
    }
}
