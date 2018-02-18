package qub;

/**
 * An Iterator that only returns values that match a provided condition.
 * @param <T> The type of value that this Iterator returns.
 */
class WhereIterator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private final Function1<T,Boolean> condition;

    WhereIterator(Iterator<T> innerIterator, Function1<T,Boolean> condition)
    {
        this.innerIterator = innerIterator;
        this.condition = condition;
    }

    private void skipToMatch()
    {
        while (innerIterator.hasCurrent() && !condition.run(innerIterator.getCurrent())) {
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
        skipToMatch();
        return innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        skipToMatch();
        return innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        while (innerIterator.next() && !condition.run(innerIterator.getCurrent()))
        {
        }
        return this.hasCurrent();
    }
}
