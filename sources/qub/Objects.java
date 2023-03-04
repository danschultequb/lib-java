package qub;

/**
 * A collection of functions that help when interacting with {@link Object}s.
 */
public interface Objects
{
    /**
     * Get the {@link String} representation of the provided {@link Object}.
     * @param value The {@link Object} to get the {@link String} representation of.
     */
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
        else if (value instanceof char[])
        {
            result = Array.toString((char[])value);
        }
        else if (value instanceof boolean[])
        {
            result = Array.toString((boolean[])value);
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

    /**
     * Get the first non-null value in the provided values.<br/>
     * If a {@link Function0} is provided, it will be invoked and its return value will be checked.<br/>
     * If a {@link Value} is provided, it's inner value will be checked.
     * @param values The values to search through.
     * @param <T> The type of values to search through and return.
     */
    @SafeVarargs
    public static <T> T coalesce(T... values)
    {
        T result = null;

        if (values != null)
        {
            for (T value : values)
            {
                if (value != null)
                {
                    result = value;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Get the first non-null return value from the provided {@link Function0}s.
     * @param functions The functions that will be invoked to see if their return values are
     *                  non-null.
     * @param <T> The type of return values to search through and return.
     */
    @SafeVarargs
    public static <T> T coalesce(Function0<? extends T>... functions)
    {
        T result = null;

        if (functions != null)
        {
            for (Function0<? extends T> function : functions)
            {
                if (function != null)
                {
                    result = function.run();
                    if (result != null)
                    {
                        break;
                    }
                }
            }
        }

        return result;
    }
}
