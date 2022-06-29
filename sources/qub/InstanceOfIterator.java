package qub;

public class InstanceOfIterator<TInner,TOuter> implements Iterator<TOuter>
{
    private final Iterator<TInner> innerIterator;
    private final Class<TOuter> type;

    private InstanceOfIterator(Iterator<TInner> innerIterator, Class<TOuter> type)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(type, "type");

        this.innerIterator = innerIterator;
        this.type = type;
    }

    public static <TInner,TOuter> InstanceOfIterator<TInner,TOuter> create(Iterator<TInner> innerIterator, Class<TOuter> type)
    {
        return new InstanceOfIterator<>(innerIterator, type);
    }

    private boolean skipToMatch()
    {
        boolean foundMatch = false;

        while (this.innerIterator.hasCurrent())
        {
            if (Types.instanceOf(this.innerIterator.getCurrent(), this.type))
            {
                foundMatch = true;
                break;
            }
            else
            {
                this.innerIterator.next();
            }
        }

        return foundMatch;
    }

    @Override
    public boolean hasStarted()
    {
        return this.innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.skipToMatch();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TOuter getCurrent()
    {
        return (TOuter)this.innerIterator.getCurrent();
    }

    @Override
    public boolean next()
    {
        this.innerIterator.next();
        return this.skipToMatch();
    }
}
