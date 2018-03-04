package qub;

class MapIndexable<TInner,TOuter> extends IndexableBase<TOuter>
{
    private final Indexable<TInner> innerIndexable;
    private final Function1<TInner,TOuter> conversion;

    MapIndexable(Indexable<TInner> innerIndexable, Function1<TInner,TOuter> conversion)
    {
        this.innerIndexable = innerIndexable;
        this.conversion = conversion;
    }

    @Override
    public Iterator<TOuter> iterate()
    {
        return new MapIterator<>(innerIndexable.iterate(), conversion);
    }

    @Override
    public boolean any()
    {
        return conversion != null && innerIndexable.any();
    }

    @Override
    public int getCount()
    {
        return conversion == null ? 0 : innerIndexable.getCount();
    }

    @Override
    public TOuter get(int index)
    {
        TOuter result = null;
        if (0 <= index && index < getCount())
        {
            result = conversion.run(innerIndexable.get(index));
        }
        return result;
    }
}
