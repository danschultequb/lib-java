package qub;

/**
 * An {@link Iterator} that is always empty.
 * @param <T> The type of values that are returned by this {@link EmptyIterator}.
 */
public class EmptyIterator<T> implements Iterator<T>
{
    private boolean hasStarted;

    private EmptyIterator()
    {
    }

    /**
     * Create a new {@link EmptyIterator}.
     * @param <T> The type of values that are returned by the {@link EmptyIterator}.
     */
    public static <T> EmptyIterator<T> create()
    {
        return new EmptyIterator<>();
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return false;
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return null;
    }

    @Override
    public boolean next()
    {
        this.hasStarted = true;
        return false;
    }
}
