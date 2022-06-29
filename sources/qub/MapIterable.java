package qub;

public class MapIterable<TInner,TOuter> implements Iterable<TOuter>
{
    private final Iterable<TInner> innerIterable;
    private final Function1<TInner,TOuter> conversion;

    private MapIterable(Iterable<TInner> innerIterable, Function1<TInner,TOuter> conversion)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertNotNull(conversion, "conversion");

        this.innerIterable = innerIterable;
        this.conversion = conversion;
    }

    public static <TInner,TOuter> MapIterable<TInner,TOuter> create(Iterable<TInner> innerIterable, Function1<TInner,TOuter> conversion)
    {
        return new MapIterable<>(innerIterable, conversion);
    }

    @Override
    public Iterator<TOuter> iterate()
    {
        return MapIterator.create(this.innerIterable.iterate(), this.conversion);
    }

    @Override
    public boolean any()
    {
        return this.innerIterable.any();
    }

    @Override
    public int getCount()
    {
        return this.innerIterable.getCount();
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
