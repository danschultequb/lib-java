package qub;

public class DoubleLinkNodeIterator<T> implements Iterator<T>
{
    private boolean hasStarted;
    private DoubleLinkNode<T> currentNode;

    public DoubleLinkNodeIterator(DoubleLinkNode<T> node)
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
        return hasCurrent() ? currentNode.getValue() : null;
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

        return currentNode != null;
    }
}
