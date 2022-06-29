package qub;

public class SkipUntilIterator<T> implements Iterator<T>
{
    private final Iterator<T> iterator;
    private final Function1<T,Boolean> condition;
    private boolean hasSkipped;

    private SkipUntilIterator(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(iterator, "iterator");
        PreCondition.assertNotNull(condition, "condition");

        this.iterator = iterator;
        this.condition = condition;
    }

    public static <T> SkipUntilIterator<T> create(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        return new SkipUntilIterator<>(iterator, condition);
    }

    private void skipToMatch()
    {
        hasSkipped = true;
        if (!iterator.hasStarted())
        {
            iterator.next();
        }

        while (iterator.hasCurrent() && !condition.run(iterator.getCurrent()))
        {
            iterator.next();
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
        return iterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        if (iterator.hasStarted() && !hasSkipped)
        {
            skipToMatch();
        }
        return iterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        if (!hasSkipped)
        {
            skipToMatch();
        }
        else
        {
            iterator.next();
        }
        return iterator.hasCurrent();
    }
}
