package qub;

public class SkipUntilIterable<T> implements Iterable<T>
{
    private final Iterable<T> iterable;
    private final Function1<T,Boolean> condition;

    private SkipUntilIterable(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(iterable, "iterable");
        PreCondition.assertNotNull(condition, "condition");

        this.iterable = iterable;
        this.condition = condition;
    }

    public static <T> SkipUntilIterable<T> create(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return new SkipUntilIterable<>(iterable, condition);
    }

    @Override
    public Iterator<T> iterate()
    {
        return SkipUntilIterator.create(this.iterable.iterate(), this.condition);
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
