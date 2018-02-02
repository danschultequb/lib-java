package qub;

/**
 * An Iterable that will contains no more than a fixed number of values from an inner Iterable.
 * @param <T> The type of value contained in this Iterable.
 */
class TakeIterable<T> extends IterableBase<T>
{
    private final Iterable<T> innerIterable;
    private final int toTake;

    TakeIterable(Iterable<T> innerIterable, int toTake)
    {
        this.innerIterable = innerIterable;
        this.toTake = toTake;
    }

    @Override
    public Iterator<T> iterate() {
        return new TakeIterator<>(innerIterable.iterate(), toTake);
    }

    @Override
    public boolean any() {
        return toTake > 0 && innerIterable.any();
    }

    @Override
    public int getCount() {
        int result;
        if (toTake <= 0)
        {
            result = 0;
        }
        else
        {
            result = innerIterable.getCount();
            if (result > toTake)
            {
                result = toTake;
            }
        }
        return result;
    }
}
