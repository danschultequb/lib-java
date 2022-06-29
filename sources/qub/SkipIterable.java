package qub;

/**
 * An {@link Iterable} that will skip over the first toSkip number of elements in its inner
 * {@link Iterable} and then return the remaining elements.
 * @param <T> The type of value that this {@link Iterable} returns.
 */
public class SkipIterable<T> implements Iterable<T>
{
    private final Iterable<T> innerIterable;
    private final int toSkip;

    private SkipIterable(Iterable<T> innerIterable, int toSkip)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertGreaterThanOrEqualTo(toSkip, 0, "toSkip");

        this.innerIterable = innerIterable;
        this.toSkip = toSkip;
    }

    public static <T> SkipIterable<T> create(Iterable<T> innerIterable, int toSkip)
    {
        return new SkipIterable<>(innerIterable, toSkip);
    }

    @Override
    public Iterator<T> iterate()
    {
        return this.innerIterable.iterate().skip(this.toSkip);
    }

    @Override
    public boolean any()
    {
        return this.innerIterable.getCount() > this.toSkip;
    }

    @Override
    public int getCount()
    {
        return Math.maximum(0, this.innerIterable.getCount() - this.toSkip);
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
