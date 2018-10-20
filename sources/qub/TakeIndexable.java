package qub;

public class TakeIndexable<T> implements Indexable<T>
{
    private final Indexable<T> innerIndexable;
    private final int toTake;

    public TakeIndexable(Indexable<T> innerIndexable, int toTake)
    {
        this.innerIndexable = innerIndexable;
        this.toTake = toTake;
    }

    @Override
    public T get(int index)
    {
        PreCondition.assertBetween(0, index, getCount() - 1, "index");

        return innerIndexable.get(index);
    }

    @Override
    public Iterator<T> iterate()
    {
        return innerIndexable.iterate().take(toTake);
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
