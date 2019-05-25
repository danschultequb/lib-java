package qub;

public interface Doubles
{
    int bitCount = 64;

    int byteCount = 8;

    static int hashCode(double value)
    {
        return hashCode(Double.valueOf(value));
    }

    static int hashCode(Double value)
    {
        PreCondition.assertNotNull(value, "value");

        return value.hashCode();
    }

    static String toString(double value)
    {
        return toString(Double.valueOf(value));
    }

    static String toString(Double value)
    {
        PreCondition.assertNotNull(value, "value");

        return value.toString();
    }
}
