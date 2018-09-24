package qub;

class InstanceOfIterator<TInner,TOuter> extends IteratorBase<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Class<TOuter> type;

    InstanceOfIterator(Iterator<TInner> innerIterator, Class<TOuter> type)
    {
        this.innerIterator = innerIterator;
        this.type = type;
    }

    private boolean skipToMatch()
    {
        boolean foundMatch = false;

        if (type != null)
        {
            while (innerIterator.hasCurrent())
            {
                final TInner innerCurrent = innerIterator.getCurrent();
                if (innerCurrent != null && Types.instanceOf(innerCurrent, type))
                {
                    foundMatch = true;
                    break;
                }
                else
                {
                    innerIterator.next();
                }
            }
        }

        return foundMatch;
    }

    @Override
    public boolean hasStarted()
    {
        return innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return skipToMatch();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TOuter getCurrent()
    {
        return !hasCurrent() ? null : (TOuter)innerIterator.getCurrent();
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
            innerIterator.next();
            result = skipToMatch();
        }

        return result;
    }
}
