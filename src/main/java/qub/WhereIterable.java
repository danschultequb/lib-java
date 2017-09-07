package qub;

/**
 * An Iterable that only returns values that match a provided condition.
 * @param <T> The type of value that this Iterable returns.
 */
class WhereIterable<T> extends IterableBase<T>
{
    private final Iterable<T> innerIterable;
    private final Function1<T,Boolean> condition;

    WhereIterable(Iterable<T> innerIterable, Function1<T,Boolean> condition)
    {
        this.innerIterable = innerIterable;
        this.condition = condition;
    }

    @Override
    public Iterator<T> iterate()
    {
        return new WhereIterator<>(innerIterable.iterate(), condition);
    }

    @Override
    public boolean any()
    {
        return iterate().any();
    }

    @Override
    public int getCount()
    {
        return iterate().getCount();
    }
}
