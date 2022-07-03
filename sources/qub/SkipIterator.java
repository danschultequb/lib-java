package qub;

/**
 * An {@link Iterator} that will skip over the first toSkip number of elements and then iterate over
 * the remaining elements.
 * @param <T> The type of values that this {@link Iterator} returns.
 */
public class SkipIterator<T> extends IteratorWrapper<T>
{
    private final int toSkip;
    private int skipped;

    private SkipIterator(Iterator<T> innerIterator, int toSkip)
    {
        super(innerIterator);

        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertGreaterThanOrEqualTo(toSkip, 0, "toSkip");

        this.toSkip = toSkip;
        this.skipped = 0;
    }

    public static <T> SkipIterator<T> create(Iterator<T> innerIterator, int toSkip)
    {
        return new SkipIterator<>(innerIterator, toSkip);
    }

    private void skipValues()
    {
        while (this.skipped < this.toSkip)
        {
            if (super.next())
            {
                this.skipped++;
            }
            else
            {
                break;
            }
        }
    }

    @Override
    public boolean hasCurrent()
    {
        if (super.hasCurrent())
        {
            this.skipValues();
        }
        return super.hasCurrent();
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        if (super.hasCurrent())
        {
            this.skipValues();
        }
        return super.getCurrent();
    }

    @Override
    public boolean next()
    {
        this.skipValues();
        return super.next();
    }
}
