package qub;

class MapIterable<TInner,TOuter> extends IterableBase<TOuter>
{
    private final Iterable<TInner> innerIterable;
    private final Function1<TInner,TOuter> conversion;

    MapIterable(Iterable<TInner> innerIterable, Function1<TInner,TOuter> conversion)
    {
        this.innerIterable = innerIterable;
        this.conversion = conversion;
    }

    @Override
    public Iterator<TOuter> iterate()
    {
        return new MapIterator<>(innerIterable.iterate(), conversion);
    }

    @Override
    public boolean any()
    {
        return conversion != null && innerIterable.any();
    }

    @Override
    public int getCount()
    {
        return conversion == null ? 0 : innerIterable.getCount();
    }
}
