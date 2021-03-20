package qub;

public class Skip
{
    private final String message;

    private Skip(String message)
    {
        PreCondition.assertNotNull(message, "message");

        this.message = message;
    }

    public static Skip create()
    {
        return Skip.create("");
    }

    public static Skip create(String message)
    {
        PreCondition.assertNotNull(message, "message");

        return new Skip(message);
    }

    public String getMessage()
    {
        return this.message;
    }
}
