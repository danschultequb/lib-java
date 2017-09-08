package qub;

class InstanceOfIterator<TInner,TOuter> extends IteratorBase<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Class<TOuter> type;

    InstanceOfIterator(Iterator<TInner> innerIterator, Class<TOuter> type)
    {
        this.innerIterator = innerIterator;
        this.type = type;

        if (type != null)
        {
            while (hasCurrent() && (getCurrent() == null || !type.isAssignableFrom(getCurrent().getClass())))
            {
                innerIterator.next();
            }
        }
    }

    @Override
    public boolean hasStarted()
    {
        return innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return type == null ? false : innerIterator.hasCurrent();
    }

    @Override
    public TOuter getCurrent()
    {
        return type == null ? null : (TOuter)innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        boolean result;
        if (type == null)
        {
            result = false;
            if (!innerIterator.hasStarted())
            {
                innerIterator.next();
            }
        }
        else
        {
            while (innerIterator.next() && (getCurrent() == null || !type.isAssignableFrom(getCurrent().getClass())))
            {
            }

            result = innerIterator.hasCurrent();
        }

        return result;
    }
}
