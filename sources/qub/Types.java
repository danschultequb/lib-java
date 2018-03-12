package qub;

public class Types
{
    Types()
    {
    }

    public static boolean isSubTypeOf(Object value, Class<?> type)
    {
        return value != null && type != null && type.isAssignableFrom(value.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> T as(Object value, Class<T> type)
    {
        T result;
        if (Types.isSubTypeOf(value, type))
        {
            result = (T)value;
        }
        else
        {
            result = null;
        }
        return result;
    }
}
