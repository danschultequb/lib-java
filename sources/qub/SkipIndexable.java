package qub;

public class SkipIndexable<T> implements Indexable<T>
{
    private final Indexable<T> innerIndexable;
    private final int toSkip;

    private SkipIndexable(Indexable<T> innerIndexable, int toSkip)
    {
        PreCondition.assertNotNull(innerIndexable, "innerIndexable");
        PreCondition.assertGreaterThanOrEqualTo(toSkip, 0, "toSkip");

        this.innerIndexable = innerIndexable;
        this.toSkip = toSkip;
    }

    public static <T> SkipIndexable<T> create(Indexable<T> innerIndexable, int toSkip)
    {
        return new SkipIndexable<>(innerIndexable, toSkip);
    }

    @Override
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return this.innerIndexable.get(index + this.toSkip);
    }

    @Override
    public Iterator<T> iterate()
    {
        return this.innerIndexable.iterate().skip(this.toSkip);
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
