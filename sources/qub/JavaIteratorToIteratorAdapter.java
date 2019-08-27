package qub;

/**
 * An adapter class that converts a java.util.Iterator object into a qub.Iterator.
 * @param <T> The type of values that the iterator returns.
 */
public class JavaIteratorToIteratorAdapter<T> implements Iterator<T>
{
    private final java.util.Iterator<T> javaIterator;
    private boolean hasStarted;
    private boolean hasCurrent;
    private T current;

    private JavaIteratorToIteratorAdapter(java.util.Iterator<T> javaIterator)
    {
        PreCondition.assertNotNull(javaIterator, "javaIterator");

        this.javaIterator = javaIterator;
    }

    /**
     * Create a new JavaIteratorToIteratorAdapter from the provided java.util.Iterator object.
     * @param javaIterator The java.util.Iterator object to adapt.
     * @param <U> The type of values that are returned by the new iterator.
     * @return The new iterator adapter.
     */
    public static <U> JavaIteratorToIteratorAdapter<U> create(java.util.Iterator<U> javaIterator)
    {
        PreCondition.assertNotNull(javaIterator, "javaIterator");

        final JavaIteratorToIteratorAdapter<U> result = new JavaIteratorToIteratorAdapter<>(javaIterator);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasCurrent;
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return current;
    }

    @Override
    public boolean next()
    {
        hasStarted = true;

        hasCurrent = javaIterator.hasNext();
        current = !hasCurrent ? null : javaIterator.next();

        return hasCurrent;
    }
}
