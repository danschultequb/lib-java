package qub;

public class Doubles
{
    Doubles()
    {
    }

    public static final int bitCount = 64;

    public static final int byteCount = 8;

    public static int hashCode(double value)
    {
        return hashCode(Double.valueOf(value));
    }

    public static int hashCode(Double value)
    {
        PreCondition.assertNotNull(value, "value");

        return value.hashCode();
    }

    public static String toString(double value)
    {
        return toString(Double.valueOf(value));
    }

    public static String toString(Double value)
    {
        PreCondition.assertNotNull(value, "value");

        return value.toString();
    }
}
