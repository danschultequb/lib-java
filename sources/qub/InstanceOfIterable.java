package qub;

class InstanceOfIterable<TInner,TOuter> extends IterableBase<TOuter>
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
}
