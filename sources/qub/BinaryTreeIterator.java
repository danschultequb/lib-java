package qub;

/**
 * An iterator that traverses a Node2 graph.
 * @param <T> The type of value returned from this iterator.
 */
public class BinaryTreeIterator<T> implements Iterator<T>
{
    private final Stack<Node2<T>> nodesToVisit;
    private Node2<T> currentNode;
    private boolean hasStarted;

    private BinaryTreeIterator(Node2<T> startNode)
    {
        PreCondition.assertNotNull(startNode, "startNode");

        this.nodesToVisit = Stack.create();
        this.addNodesToVisit(startNode);
    }

    public static <T> BinaryTreeIterator<T> create(Node2<T> startNode)
    {
        return new BinaryTreeIterator<>(startNode);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.currentNode != null;
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
        this.hasStarted = true;

        if (!this.nodesToVisit.any())
        {
            this.currentNode = null;
        }
        else
        {
            this.currentNode = this.nodesToVisit.pop().await();
            this.addNodesToVisit(this.currentNode.getNode2());
        }

        return this.hasCurrent();
    }

    private void addNodesToVisit(Node2<T> nodeToVisit)
    {
        while (nodeToVisit != null)
        {
            this.nodesToVisit.push(nodeToVisit);
            nodeToVisit = nodeToVisit.getNode1();
        }
    }
}
