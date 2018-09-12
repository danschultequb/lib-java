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
        return Double.valueOf(value).hashCode();
    }
}
