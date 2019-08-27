package qub;

class MapIndexable<TInner,TOuter> implements Indexable<TOuter>
{
    private final Indexable<TInner> innerIndexable;
    private final Function1<TInner,TOuter> conversion;

    MapIndexable(Indexable<TInner> innerIndexable, Function1<TInner,TOuter> conversion)
    {
        PreCondition.assertNotNull(innerIndexable, "innerIndexable");
        PreCondition.assertNotNull(conversion, "conversion");

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
        return innerIndexable.any();
    }

    @Override
    public int getCount()
    {
        return innerIndexable.getCount();
    }

    @Override
    public TOuter get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

        return conversion.run(innerIndexable.get(index));
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
