package qub;

public class Bytes
{
    Bytes()
    {
    }

    public static int toUnsignedInt(byte value)
    {
        return 0xFF & value;
    }
}
