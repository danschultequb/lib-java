package qub;

public abstract class TraversalIteratorBase<TNode,TValue> implements TraversalIterator<TNode,TValue>
{
    private boolean started;

    @Override
    public boolean hasStarted()
    {
        return this.started;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasValueToReturn();
    }

    @Override
    public TValue getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.getValueToReturn();
    }

    @Override
    public boolean next()
    {
        this.started = true;

        while (!this.nextValueToReturn())
        {
            if (this.hasNodesToVisit())
            {
                this.visitNextNode();
            }
            else if (this.hasActionsToRun())
            {
                this.runNextAction();
            }
            else
            {
                break;
            }
        }

        return this.hasCurrent();
    }

    protected abstract boolean hasNodesToVisit();

    protected abstract void visitNextNode();

    protected abstract boolean hasActionsToRun();

    protected abstract void runNextAction();

    protected abstract boolean hasValueToReturn();

    protected abstract TValue getValueToReturn();

    protected abstract boolean nextValueToReturn();
}
