package qub;

public class SkipUntilIterator<T> extends IteratorBase<T>
{
    private final Iterator<T> iterator;
    private final Function1<T,Boolean> condition;
    private boolean hasSkipped;

    public SkipUntilIterator(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        this.iterator = iterator;
        this.condition = condition;
    }

    private void skipToMatch()
    {
        hasSkipped = true;
        if (!iterator.hasStarted())
        {
            iterator.next();
        }

        if (condition != null)
        {
            while (iterator.hasCurrent() && !condition.run(iterator.getCurrent()))
            {
                iterator.next();
            }
        }
    }

    @Override
    public boolean hasStarted()
    {
        return iterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        if (iterator.hasCurrent() && !hasSkipped)
        {
            skipToMatch();
        }
        return condition != null && iterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        if (iterator.hasStarted() && !hasSkipped)
        {
            skipToMatch();
        }
        return condition == null ? null : iterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        if (!hasSkipped)
        {
            skipToMatch();
        }
        else if (condition != null)
        {
            iterator.next();
        }
        return condition != null && iterator.hasCurrent();
    }
}
