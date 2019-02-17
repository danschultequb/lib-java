package qub;

/**
 * An Iterable that will skip over the first toSkip number of elements in its inner Iterable and
 * then return the remaining elements.
 * @param <T> The type of value that this Iterable returns.
 */
class SkipIterable<T> implements Iterable<T>
{
    private final Iterable<T> innerIterable;
    private final int toSkip;

    SkipIterable(Iterable<T> innerIterable, int toSkip)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertGreaterThanOrEqualTo(toSkip, 0, "toSkip");

        this.innerIterable = innerIterable;
        this.toSkip = toSkip;
    }

    @Override
    public Iterator<T> iterate()
    {
        return new SkipIterator<>(innerIterable.iterate(), toSkip);
    }

    @Override
    public boolean any()
    {
        return innerIterable.getCount() > toSkip;
    }

    @Override
    public int getCount()
    {
        return Math.maximum(0, innerIterable.getCount() - toSkip);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
