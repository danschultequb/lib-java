package qub;

public class SingleLinkList<T> implements List<T>
{
    private volatile SingleLinkNode<T> head;
    private volatile SingleLinkNode<T> tail;

    public SingleLinkList() {
        head = null;
        tail = null;
    }

    private SingleLinkNode<T> getNode(int index)
    {
        SingleLinkNode<T> result;
        if (index == -1)
        {
            result = null;
        }
        else
        {
            result = head;
            for (int i = 0; result != null && i < index; ++i)
            {
                result = result.getNext();
            }
        }

        return result;
    }

    @Override
    public void insert(int insertIndex, T value)
    {
        validateInsertIndex(insertIndex);

        final SingleLinkNode<T> nodeToAdd = new SingleLinkNode<>(value);

        if (insertIndex == 0)
        {
            nodeToAdd.setNext(head);
            head = nodeToAdd;
        }
        else
        {
            final SingleLinkNode<T> nodeBeforeInsertIndex = getNode(insertIndex - 1);
            nodeToAdd.setNext(nodeBeforeInsertIndex.getNext());
            nodeBeforeInsertIndex.setNext(nodeToAdd);
        }
    }

    @Override
    public void set(int index, T value)
    {
        validateAccessIndex(index);

        getNode(index).setValue(value);
    }

    @Override
    public T removeAt(int index)
    {
        validateAccessIndex(index);

        final SingleLinkNode<T> nodeBeforeNodeToRemove = getNode(index - 1);
        final SingleLinkNode<T> nodeToRemove = (nodeBeforeNodeToRemove == null ? head : nodeBeforeNodeToRemove.getNext());

        if (nodeBeforeNodeToRemove == null)
        {
            head = nodeToRemove.getNext();
        }
        else
        {
            nodeBeforeNodeToRemove.setNext(nodeToRemove.getNext());
        }

        if (nodeToRemove == tail)
        {
            tail = nodeBeforeNodeToRemove;
        }
        else
        {
            nodeToRemove.setNext(null);
        }

        final T result = nodeToRemove.getValue();
        nodeToRemove.setValue(null);

        return result;
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        T result = null;

        SingleLinkNode<T> previousNode = null;
        SingleLinkNode<T> currentNode = head;
        while (currentNode != null)
        {
            final T nodeValue = currentNode.getValue();
            final SingleLinkNode<T> nextNode = currentNode.getNext();
            if (condition.run(nodeValue))
            {
                result = nodeValue;

                if (previousNode == null)
                {
                    head = nextNode;
                }
                else
                {
                    previousNode.setNext(nextNode);
                }

                if (nextNode == null)
                {
                    tail = previousNode;
                }

                currentNode.setNext(null);
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
    public void clear()
    {
        SingleLinkNode<T> currentNode = head;
        while (currentNode != null)
        {
            final SingleLinkNode<T> nextNode = currentNode.getNext();
            currentNode.setValue(null);
            currentNode.setNext(null);
            currentNode = nextNode;
        }
        head = null;
        tail = null;
    }

    @Override
    public T get(int index)
    {
        validateAccessIndex(index);

        return getNode(index).getValue();
    }

    @Override
    public Iterator<T> iterate()
    {
        return head == null ? new EmptyIterator<>() : head.iterate();
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
