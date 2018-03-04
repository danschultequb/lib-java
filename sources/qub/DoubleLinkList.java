package qub;

public class DoubleLinkList<T> extends ListBase<T>
{
    private DoubleLinkNode<T> head;
    private DoubleLinkNode<T> tail;

    public DoubleLinkList()
    {
        head = null;
        tail = null;
    }

    @Override
    public void add(T value)
    {
        final DoubleLinkNode<T> nodeToAdd = new DoubleLinkNode<>(value);
        if (head == null)
        {
            head = nodeToAdd;
            tail = nodeToAdd;
        }
        else
        {
            nodeToAdd.setPrevious(tail);
            tail.setNext(nodeToAdd);
            tail = nodeToAdd;
        }
    }

    private DoubleLinkNode<T> getNode(int index)
    {
        DoubleLinkNode<T> result = null;
        if (0 <= index)
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
    public void set(int index, T value)
    {
        final DoubleLinkNode<T> nodeToSet = getNode(index);
        if (nodeToSet != null)
        {
            nodeToSet.setValue(value);
        }
    }

    @Override
    public T removeAt(int index)
    {
        T result = null;
        if (index == 0)
        {
            result = removeFirst();
        }
        else
        {
            final DoubleLinkNode<T> nodeToRemove = getNode(index);
            if (nodeToRemove != null)
            {
                result = nodeToRemove.getValue();

                final DoubleLinkNode<T> previousNode = nodeToRemove.getPrevious();
                final DoubleLinkNode<T> nextNode = nodeToRemove.getNext();

                previousNode.setNext(nextNode);

                if (nextNode == null)
                {
                    tail = previousNode;
                }
                else
                {
                    nextNode.setPrevious(previousNode);
                }

                nodeToRemove.setPrevious(null);
                nodeToRemove.setNext(null);
                nodeToRemove.setValue(null);
            }
        }
        return result;
    }

    @Override
    public T removeFirst()
    {
        T result = null;

        final DoubleLinkNode<T> nodeToRemove = head;
        if (nodeToRemove != null)
        {
            result = nodeToRemove.getValue();

            final DoubleLinkNode<T> nextNode = nodeToRemove.getNext();

            head = nextNode;

            if (nextNode == null)
            {
                tail = null;
            }
            else
            {
                nextNode.setPrevious(null);
            }

            nodeToRemove.setNext(null);
            nodeToRemove.setValue(null);
        }

        return result;
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            DoubleLinkNode<T> node = head;
            while (node != null)
            {
                final T value = node.getValue();
                final DoubleLinkNode<T> nextNode = node.getNext();
                if (condition.run(value))
                {
                    result = value;

                    final DoubleLinkNode<T> previousNode = node.getPrevious();

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
                    else
                    {
                        nextNode.setPrevious(previousNode);
                    }

                    node.setPrevious(null);
                    node.setNext(null);
                    node.setValue(null);
                    break;
                }
                else
                {
                    node = node.getNext();
                }
            }
        }

        return result;
    }

    @Override
    public void clear()
    {
        DoubleLinkNode<T> node = head;
        while (node != null)
        {
            final DoubleLinkNode<T> nextNode = node.getNext();
            node.setPrevious(null);
            node.setNext(null);
            node.setValue(null);
            node = nextNode;
        }

        head = null;
        tail = null;
    }

    @Override
    public T get(int index)
    {
        final DoubleLinkNode<T> node = getNode(index);
        return node == null ? null : node.getValue();
    }

    @Override
    public Iterator<T> iterate()
    {
        return head == null ? new EmptyIterator<T>() : head.iterate();
    }
}
