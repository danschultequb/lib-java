package qub;

/**
 * An Iterator that converts the values of type TInner from an inner iterator into new values of
 * type TOuter.
 * @param <TInner> The type of values returned from the inner iterator.
 * @param <TOuter> The type of values returned from this iterator.
 */
class MapIterator<TInner,TOuter> implements Iterator<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Function1<TInner,TOuter> conversion;

    MapIterator(Iterator<TInner> innerIterator, Function1<TInner,TOuter> conversion)
    {
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
        return conversion != null && innerIterator.hasCurrent();
    }

    @Override
    public TOuter getCurrent()
    {
        TOuter result = null;
        if (hasCurrent() && conversion != null)
        {
            result = conversion.run(innerIterator.getCurrent());
        }
        return result;
    }

    @Override
    public boolean next()
    {
        boolean result;
        if (conversion == null)
        {
            result = false;
            if (!innerIterator.hasStarted())
            {
                innerIterator.next();
            }
        }
        else
        {
            result = innerIterator.next();
        }

        return result;
    }
}
