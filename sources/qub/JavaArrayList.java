package qub;

/**
 * A array-based {@link List} data structure that can expand when it gets full.
 * @param <T> The type of element stored in this {@link JavaArrayList}.
 */
public class JavaArrayList<T> extends JavaList<T>
{
    private JavaArrayList()
    {
        super(new java.util.ArrayList<>());
    }

    public static <T> JavaArrayList<T> create()
    {
        return new JavaArrayList<>();
    }
}
