package qub;

public class SkipIndexable<T> implements Indexable<T>
{
    private final Indexable<T> innerIndexable;
    private final int toSkip;

    public SkipIndexable(Indexable<T> innerIndexable, int toSkip)
    {
        this.innerIndexable = innerIndexable;
        this.toSkip = toSkip;
    }

    @Override
    public T get(int index)
    {
        return innerIndexable.get(index + toSkip);
    }

    @Override
    public Iterator<T> iterate()
    {
        return innerIndexable.iterate().skip(toSkip);
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
