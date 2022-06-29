package qub;

/**
 * An Iterator that will skip over the first toSkip number of elements in this Iterator and then
 * iterate over the remaining elements.
 * @param <T> The type of value that this Iterator returns.
 */
public class SkipIterator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private final int toSkip;

    private int skipped;

    private SkipIterator(Iterator<T> innerIterator, int toSkip)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertGreaterThanOrEqualTo(toSkip, 0, "toSkip");

        this.innerIterator = innerIterator;
        this.toSkip = toSkip;
        skipped = 0;
    }

    public static <T> SkipIterator<T> create(Iterator<T> innerIterator, int toSkip)
    {
        return new SkipIterator<>(innerIterator, toSkip);
    }

    private void skipValues()
    {
        while (skipped < toSkip)
        {
            if (!innerIterator.next())
            {
                skipped = toSkip;
            }
            else
            {
                ++skipped;
            }
        }
    }

    @Override
    public boolean hasStarted()
    {
        return innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent() {
        if (innerIterator.hasCurrent())
        {
            skipValues();
        }
        return innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent() {
        if (innerIterator.hasCurrent())
        {
            skipValues();
        }
        return innerIterator.getCurrent();
    }

    @Override
    public boolean next() {
        skipValues();
        return innerIterator.next();
    }
}
