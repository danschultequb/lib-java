package qub;

/**
 * An Iterable that will contains no more than a fixed number of values create an inner Iterable.
 * @param <T> The type of value contained in this Iterable.
 */
class TakeIterable<T> implements Iterable<T>
{
    private final Iterable<T> innerIterable;
    private final int toTake;

    TakeIterable(Iterable<T> innerIterable, int toTake)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertGreaterThanOrEqualTo(toTake, 0, "toTake");

        this.innerIterable = innerIterable;
        this.toTake = toTake;
    }

    @Override
    public Iterator<T> iterate()
    {
        return innerIterable.iterate().take(toTake);
    }

    @Override
    public boolean any()
    {
        return innerIterable.any();
    }

    @Override
    public int getCount()
    {
        return Math.minimum(toTake, innerIterable.getCount());
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
