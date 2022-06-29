package qub;

/**
 * An {@link Iterator} that only returns values that match a provided condition.
 * @param <T> The type of value that this {@link Iterator} returns.
 */
public class WhereIterator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private final Function1<T,Boolean> condition;

    private WhereIterator(Iterator<T> innerIterator, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(condition, "condition");

        this.innerIterator = innerIterator;
        this.condition = condition;
    }

    public static <T> WhereIterator<T> create(Iterator<T> innerIterator, Function1<T,Boolean> condition)
    {
        return new WhereIterator<>(innerIterator, condition);
    }

    private void skipToMatch()
    {
        while (innerIterator.hasCurrent() && !condition.run(innerIterator.getCurrent()))
        {
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
        this.skipToMatch();
        return innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        this.skipToMatch();
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
