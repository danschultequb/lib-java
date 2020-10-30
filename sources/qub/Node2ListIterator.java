package qub;

public class Node2ListIterator<T> implements Iterator<T>
{
    private boolean hasStarted;
    private Node2<T> currentNode;

    private Node2ListIterator(Node2<T> node)
    {
        this.hasStarted = false;
        this.currentNode = node;
    }

    public static <T> Node2ListIterator<T> create(Node2<T> node)
    {
        return new Node2ListIterator<>(node);
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
            this.currentNode = this.currentNode.getNode2();
        }

        return this.hasCurrent();
    }
}
