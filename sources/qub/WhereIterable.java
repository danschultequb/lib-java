package qub;

/**
 * An Iterable that only returns values that match a provided condition.
 * @param <T> The type of value that this Iterable returns.
 */
class WhereIterable<T> implements Iterable<T>
{
    private final Iterable<T> innerIterable;
    private final Function1<T,Boolean> condition;

    WhereIterable(Iterable<T> innerIterable, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertNotNull(condition, "condition");

        this.innerIterable = innerIterable;
        this.condition = condition;
    }

    @Override
    public Iterator<T> iterate()
    {
        return new WhereIterator<>(innerIterable.iterate(), condition);
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
