package qub;

/**
 * An {@link Iterator} that wraps around another {@link Iterator}.
 * @param <T> The type of values that are returned by this {@link Iterator}.
 */
public abstract class IteratorWrapper<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;

    protected IteratorWrapper(Iterator<T> innerIterator)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");

        this.innerIterator = innerIterator;
    }

    /**
     * Get the inner {@link Iterator} that this {@link IteratorWrapper} wraps around.
     */
    protected Iterator<T> getInnerIterator()
    {
        return this.innerIterator;
    }

    @Override
    public boolean hasStarted()
    {
        return this.innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        return this.innerIterator.next();
    }
}
