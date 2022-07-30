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

    @SafeVarargs
    public static <T> JavaArrayList<T> create(T... initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final JavaArrayList<T> result = JavaArrayList.create();
        result.addAll(initialValues);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(initialValues.length, result.getCount(), "result.getCount()");

        return result;
    }

    public static <T> JavaArrayList<T> create(Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final JavaArrayList<T> result = JavaArrayList.create();
        result.addAll(initialValues);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(initialValues.getCount(), result.getCount(), "result.getCount()");

        return result;
    }

    public static <T> JavaArrayList<T> create(Iterator<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final JavaArrayList<T> result = JavaArrayList.create();
        result.addAll(initialValues);

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
