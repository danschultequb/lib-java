package qub;

public class TakeIndexable<T> implements Indexable<T>
{
    private final Indexable<T> innerIndexable;
    private final int toTake;

    private TakeIndexable(Indexable<T> innerIndexable, int toTake)
    {
        PreCondition.assertNotNull(innerIndexable, "innerIndexable");
        PreCondition.assertGreaterThanOrEqualTo(toTake, 0, "toTake");

        this.innerIndexable = innerIndexable;
        this.toTake = toTake;
    }

    public static <T> TakeIndexable<T> create(Indexable<T> innerIndexable, int toTake)
    {
        return new TakeIndexable<>(innerIndexable, toTake);
    }

    @Override
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

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
