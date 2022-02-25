package qub;

/**
 * An {@link Iterator} that returns values from an inner {@link Iterator} that satisfy a provided
 * condition.
 * @param <T> The type of values returned by this {@link Iterator}.
 */
public class TakeUntilIterator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private final Function1<T,Boolean> condition;
    private boolean hasCurrent;

    private TakeUntilIterator(Iterator<T> innerIterator, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(condition, "condition");

        this.innerIterator = innerIterator;
        this.condition = condition;
        this.updateHasCurrent();
    }

    public static <T> TakeUntilIterator<T> create(Iterator<T> innerIterator, Function1<T,Boolean> condition)
    {
        return new TakeUntilIterator<>(innerIterator, condition);
    }

    private void updateHasCurrent()
    {
        this.hasCurrent = this.innerIterator.hasCurrent() && this.condition.run(this.innerIterator.getCurrent());
    }

    @Override
    public boolean hasStarted()
    {
        return this.innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasCurrent;
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
        if (!this.hasStarted() || this.hasCurrent())
        {
            this.innerIterator.next();
            this.updateHasCurrent();
        }
        return this.hasCurrent();
    }
}
