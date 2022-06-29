package qub;

/**
 * An {@link Iterable} that only returns values that match a provided condition.
 * @param <T> The type of value that this {@link Iterable} returns.
 */
public class WhereIterable<T> implements Iterable<T>
{
    private final Iterable<T> innerIterable;
    private final Function1<T,Boolean> condition;

    private WhereIterable(Iterable<T> innerIterable, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertNotNull(condition, "condition");

        this.innerIterable = innerIterable;
        this.condition = condition;
    }

    public static <T> WhereIterable<T> create(Iterable<T> innerIterable, Function1<T,Boolean> condition)
    {
        return new WhereIterable<>(innerIterable, condition);
    }

    @Override
    public Iterator<T> iterate()
    {
        return WhereIterator.create(innerIterable.iterate(), condition);
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
