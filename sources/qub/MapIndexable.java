package qub;

public class MapIndexable<TInner,TOuter> implements Indexable<TOuter>
{
    private final Indexable<TInner> innerIndexable;
    private final Function1<TInner,TOuter> conversion;

    private MapIndexable(Indexable<TInner> innerIndexable, Function1<TInner,TOuter> conversion)
    {
        PreCondition.assertNotNull(innerIndexable, "innerIndexable");
        PreCondition.assertNotNull(conversion, "conversion");

        this.innerIndexable = innerIndexable;
        this.conversion = conversion;
    }

    public static <TInner,TOuter> MapIndexable<TInner,TOuter> create(Indexable<TInner> innerIndexable, Function1<TInner,TOuter> conversion)
    {
        return new MapIndexable<>(innerIndexable, conversion);
    }

    @Override
    public Iterator<TOuter> iterate()
    {
        return MapIterator.create(this.innerIndexable.iterate(), this.conversion);
    }

    @Override
    public boolean any()
    {
        return this.innerIndexable.any();
    }

    @Override
    public int getCount()
    {
        return this.innerIndexable.getCount();
    }

    @Override
    public TOuter get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

        return this.conversion.run(this.innerIndexable.get(index));
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
