package qub;

/**
 * An Iterator that will iterate over no more than a fixed number of values create an inner Iterator.
 * @param <T> The type of value that this Iterator returns.
 */
public class TakeIterator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private final int toTake;

    private int taken;

    private TakeIterator(Iterator<T> innerIterator, int toTake)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertGreaterThanOrEqualTo(toTake, 0, "toTake");

        this.innerIterator = innerIterator;
        this.toTake = toTake;
        taken = innerIterator.hasCurrent() ? 1 : 0;
    }

    public static <T> TakeIterator<T> create(Iterator<T> innerIterator, int toTake)
    {
        return new TakeIterator<>(innerIterator, toTake);
    }

    @Override
    public boolean hasStarted()
    {
        return taken > 0 || innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return taken <= toTake && innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        if (taken <= toTake)
        {
            ++taken;
        }
        return taken <= toTake && innerIterator.next();
    }
}
