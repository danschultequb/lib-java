package qub;

public class SingleLinkNodeIterator<T> implements Iterator<T>
{
    private boolean hasStarted;
    private SingleLinkNode<T> currentNode;

    public SingleLinkNodeIterator(SingleLinkNode<T> node)
    {
        hasStarted = false;
        currentNode = node;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentNode != null;
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return currentNode.getValue();
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentNode != null)
        {
            currentNode = currentNode.getNext();
        }

        return hasCurrent();
    }
}
