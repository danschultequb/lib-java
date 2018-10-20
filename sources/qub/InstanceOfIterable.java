package qub;

class InstanceOfIterable<TInner,TOuter> implements Iterable<TOuter>
{
    private final Iterable<TInner> innerIterable;
    private final Class<TOuter> type;

    InstanceOfIterable(Iterable<TInner> innerIterable, Class<TOuter> type)
    {
        this.innerIterable = innerIterable;
        this.type = type;
    }

    @Override
    public Iterator<TOuter> iterate()
    {
        return new InstanceOfIterator<>(innerIterable.iterate(), type);
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
