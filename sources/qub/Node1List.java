package qub;

public class Node1List<T> implements List<T>
{
    private volatile Node1<T> head;

    private Node1List()
    {
        head = null;
    }

    public static <T> Node1List<T> create()
    {
        return new Node1List<>();
    }

    private Node1<T> getNode(int index)
    {
        Node1<T> result;
        if (index == -1)
        {
            result = null;
        }
        else
        {
            result = head;
            for (int i = 0; result != null && i < index; ++i)
            {
                result = result.getNode1();
            }
        }

        return result;
    }

    @Override
    public Node1List<T> insert(int insertIndex, T value)
    {
        PreCondition.assertBetween(0, insertIndex, getCount(), "insertIndex");

        final Node1<T> nodeToAdd = Node1.create(value);

        if (insertIndex == 0)
        {
            nodeToAdd.setNode1(head);
            head = nodeToAdd;
        }
        else
        {
            final Node1<T> nodeBeforeInsertIndex = this.getNode(insertIndex - 1);
            nodeToAdd.setNode1(nodeBeforeInsertIndex.getNode1());
            nodeBeforeInsertIndex.setNode1(nodeToAdd);
        }

        return this;
    }

    @Override
    public Node1List<T> set(int index, T value)
    {
        PreCondition.assertIndexAccess(index, getCount());

        this.getNode(index).setValue(value);
        return this;
    }

    @Override
    public T removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

        final Node1<T> nodeBeforeNodeToRemove = this.getNode(index - 1);
        final Node1<T> nodeToRemove = (nodeBeforeNodeToRemove == null ? head : nodeBeforeNodeToRemove.getNode1());

        if (nodeBeforeNodeToRemove == null)
        {
            head = nodeToRemove.getNode1();
        }
        else
        {
            nodeBeforeNodeToRemove.setNode1(nodeToRemove.getNode1());
        }

        nodeToRemove.setNode1(null);

        final T result = nodeToRemove.getValue();
        nodeToRemove.setValue(null);

        return result;
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        T result = null;

        Node1<T> previousNode = null;
        Node1<T> currentNode = head;
        while (currentNode != null)
        {
            final T nodeValue = currentNode.getValue();
            final Node1<T> nextNode = currentNode.getNode1();
            if (condition.run(nodeValue))
            {
                result = nodeValue;

                if (previousNode == null)
                {
                    head = nextNode;
                }
                else
                {
                    previousNode.setNode1(nextNode);
                }

                currentNode.setNode1(null);
                currentNode.setValue(null);
                break;
            }
            else
            {
                previousNode = currentNode;
                currentNode = nextNode;
            }
        }

        return result;
    }

    @Override
    public Node1List<T> clear()
    {
        Node1<T> currentNode = head;
        while (currentNode != null)
        {
            final Node1<T> nextNode = currentNode.getNode1();
            currentNode.setValue(null);
            currentNode.setNode1(null);
            currentNode = nextNode;
        }
        head = null;

        return this;
    }

    @Override
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

        return getNode(index).getValue();
    }

    @Override
    public Iterator<T> iterate()
    {
        return head == null ? EmptyIterator.create() : Node1Iterator.create(this.head);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
