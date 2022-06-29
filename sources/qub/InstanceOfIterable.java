package qub;

public class InstanceOfIterable<TInner,TOuter> implements Iterable<TOuter>
{
    private final Iterable<TInner> innerIterable;
    private final Class<TOuter> type;

    private InstanceOfIterable(Iterable<TInner> innerIterable, Class<TOuter> type)
    {
        PreCondition.assertNotNull(innerIterable, "innerIterable");
        PreCondition.assertNotNull(type, "type");

        this.innerIterable = innerIterable;
        this.type = type;
    }

    public static <TInner,TOuter> InstanceOfIterable<TInner,TOuter> create(Iterable<TInner> innerIterable, Class<TOuter> type)
    {
        return new InstanceOfIterable<>(innerIterable, type);
    }

    @Override
    public Iterator<TOuter> iterate()
    {
        return this.innerIterable.iterate().instanceOf(this.type);
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
