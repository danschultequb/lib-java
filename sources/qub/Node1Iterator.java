package qub;

public class Node1Iterator<T> implements Iterator<T>
{
    private boolean hasStarted;
    private Node1<T> currentNode;

    public Node1Iterator(Node1<T> node)
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
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

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
            currentNode = currentNode.getNode1();
        }

        return hasCurrent();
    }
}
