package qub;

/**
 * An {@link Iterator} that converts values of type {@link TInner} from an inner {@link Iterator}
 * into new values of type {@link TOuter}.
 * @param <TInner> The type of values returned from the inner {@link Iterator}.
 * @param <TOuter> The type of values returned from this {@link Iterator}.
 */
public class MapIterator<TInner,TOuter> implements Iterator<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Function1<TInner,TOuter> conversion;

    private MapIterator(Iterator<TInner> innerIterator, Function1<TInner,TOuter> conversion)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(conversion, "conversion");

        this.innerIterator = innerIterator;
        this.conversion = conversion;
    }

    public static <TInner,TOuter> MapIterator<TInner,TOuter> create(Iterator<TInner> innerIterator, Function1<TInner,TOuter> conversion)
    {
        return new MapIterator<>(innerIterator, conversion);
    }

    @Override
    public boolean hasStarted()
    {
        return this.innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.innerIterator.hasCurrent();
    }

    @Override
    public TOuter getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.conversion.run(this.innerIterator.getCurrent());
    }

    @Override
    public boolean next()
    {
        return this.innerIterator.next();
    }
}
