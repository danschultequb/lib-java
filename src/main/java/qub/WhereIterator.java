package qub;

/**
 * An Iterator that only returns values that match a provided condition.
 * @param <T> The type of value that this Iterator returns.
 */
class WhereIterator<T> extends IteratorBase<T>
{
    private final Iterator<T> innerIterator;
    private final Function1<T,Boolean> condition;

    WhereIterator(Iterator<T> innerIterator, Function1<T,Boolean> condition)
    {
        this.innerIterator = innerIterator;
        this.condition = condition;

        while (hasCurrent() && !condition.run(getCurrent())) {
            innerIterator.next();
        }
    }

    @Override
    public boolean hasStarted()
    {
        return innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        return innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        while (innerIterator.next() && !condition.run(getCurrent()))
        {
        }

        return this.hasCurrent();
    }
}
