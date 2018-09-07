package qub;

public class Objects
{
    public static int getHashCode(Object value)
    {
        return value == null ? 0 : value.hashCode();
    }

    public static String toString(Object value)
    {
        String result;
        if (value == null)
        {
            result = "null";
        }
        else if (value instanceof byte[])
        {
            result = Array.toString((byte[])value);
        }
        else if (value instanceof short[])
        {
            result = Array.toString((short[])value);
        }
        else if (value instanceof int[])
        {
            result = Array.toString((int[])value);
        }
        else if (value instanceof long[])
        {
            result = Array.toString((long[])value);
        }
        else if (value instanceof float[])
        {
            result = Array.toString((float[])value);
        }
        else if (value instanceof double[])
        {
            result = Array.toString((double[])value);
        }
        else if (value instanceof Object[])
        {
            result = Array.toString((Object[])value);
        }
        else
        {
            result = value.toString();
        }
        return result;
    }
}
