package qub;

/**
 * A array-based List data structure that can expand when it gets full.
 * @param <T> The type of element stored in this List.
 */
public class ArrayList<T> extends JavaList<T>
{
    public ArrayList()
    {
        super(new java.util.ArrayList<T>());
    }

    public static <T> ArrayList<T> fromValues(T... values)
    {
        final ArrayList<T> result = new ArrayList<>();
        result.addAll(values);
        return result;
    }

    public static <T> ArrayList<T> fromValues(Iterator<T> values)
    {
        final ArrayList<T> result = new ArrayList<>();
        result.addAll(values);
        return result;
    }

    public static <T> ArrayList<T> fromValues(Iterable<T> values)
    {
        final ArrayList<T> result = new ArrayList<>();
        result.addAll(values);
        return result;
    }
}
