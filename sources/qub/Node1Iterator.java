package qub;

public class Node1Iterator<T> implements Iterator<T>
{
    private boolean hasStarted;
    private Node1<T> currentNode;

    private Node1Iterator(Node1<T> node)
    {
        this.hasStarted = false;
        this.currentNode = node;
    }

    public static <T> Node1Iterator<T> create(Node1<T> node)
    {
        return new Node1Iterator<>(node);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted && this.currentNode != null;
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.currentNode.getValue();
    }

    @Override
    public boolean next()
    {
        if (!this.hasStarted)
        {
            this.hasStarted = true;
        }
        else if (this.currentNode != null)
        {
            this.currentNode = this.currentNode.getNode1();
        }

        return this.hasCurrent();
    }
}
