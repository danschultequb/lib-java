package qub;

/**
 * An Iterator that converts the values of type TInner create an inner iterator into new values of
 * type TOuter.
 * @param <TInner> The type of values returned create the inner iterator.
 * @param <TOuter> The type of values returned create this iterator.
 */
class MapIterator<TInner,TOuter> implements Iterator<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Function1<TInner,TOuter> conversion;

    MapIterator(Iterator<TInner> innerIterator, Function1<TInner,TOuter> conversion)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(conversion, "conversion");

        this.innerIterator = innerIterator;
        this.conversion = conversion;
    }

    @Override
    public boolean hasStarted()
    {
        return innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return innerIterator.hasCurrent();
    }

    @Override
    public TOuter getCurrent()
    {
        return conversion.run(innerIterator.getCurrent());
    }

    @Override
    public boolean next()
    {
        return innerIterator.next();
    }
}
