package qub;

class InstanceOfIterator<TInner,TOuter> implements Iterator<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Class<TOuter> type;

    InstanceOfIterator(Iterator<TInner> innerIterator, Class<TOuter> type)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(type, "type");

        this.innerIterator = innerIterator;
        this.type = type;
    }

    private boolean skipToMatch()
    {
        boolean foundMatch = false;

        while (innerIterator.hasCurrent())
        {
            if (Types.instanceOf(innerIterator.getCurrent(), type))
            {
                foundMatch = true;
                break;
            }
            else
            {
                innerIterator.next();
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
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return (TOuter)innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        innerIterator.next();
        return skipToMatch();
    }
}
