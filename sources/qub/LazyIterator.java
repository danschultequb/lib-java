package qub;

/**
 * An Iterator decorator that will only create its inner iterator when the first value is requested.
 * @param <T> The type of value returned by this Iterator.
 */
public class LazyIterator<T> implements Iterator<T>
{
    private final Function0<? extends Iterator<T>> innerIteratorCreator;
    private Iterator<T> innerIterator;
    private boolean started;

    private LazyIterator(Function0<? extends Iterator<T>> innerIteratorCreator)
    {
        PreCondition.assertNotNull(innerIteratorCreator, "innerIteratorCreator");

        this.innerIteratorCreator = innerIteratorCreator;
    }

    public static <T> LazyIterator<T> create(Function0<? extends Iterator<T>> innerIteratorCreator)
    {
        return new LazyIterator<>(innerIteratorCreator);
    }

    @Override
    public boolean hasStarted()
    {
        return this.started;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted() && this.innerIterator != null && this.innerIterator.hasCurrent();
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
        if (!this.hasStarted())
        {
            this.started = true;
            this.innerIterator = this.innerIteratorCreator.run();
        }

        if (this.innerIterator != null)
        {
            this.innerIterator.next();
        }

        return this.hasCurrent();
    }
}
