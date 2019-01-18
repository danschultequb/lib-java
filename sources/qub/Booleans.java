package qub;

public class Booleans
{
    public static boolean isTrue(Boolean value)
    {
        return value != null && value;
    }

    public static boolean isFalse(Boolean value)
    {
        return value != null && !value;
    }

    public static String toString(boolean value)
    {
        return value ? "true" : "false";
    }

    public static String toString(Boolean value)
    {
        PreCondition.assertNotNull(value, "value");

        return toString(value.booleanValue());
    }
}
