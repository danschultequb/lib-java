package qub;

public class SingleLinkList<T> extends ListBase<T>
{
    private SingleLinkNode<T> head;
    private SingleLinkNode<T> tail;

    public SingleLinkList() {
        head = null;
        tail = null;
    }

    private SingleLinkNode<T> getNode(int index)
    {
        SingleLinkNode<T> currentNode = head;
        for (int i = 0; currentNode != null && i < index; ++i)
        {
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }

    public void add(T value)
    {
        final SingleLinkNode<T> newNode = new SingleLinkNode<>(value);
        if (head == null)
        {
            head = newNode;
            tail = newNode;
        }
        else
        {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    @Override
    public void set(int index, T value)
    {
        final SingleLinkNode<T> node = getNode(index);
        if (node != null)
        {
            node.setValue(value);
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
        else if (index > 0)
        {
            final SingleLinkNode<T> nodeBeforeNodeToRemove = getNode(index - 1);
            if (nodeBeforeNodeToRemove != null)
            {
                final SingleLinkNode<T> nodeToRemove = nodeBeforeNodeToRemove.getNext();
                if (nodeToRemove != null)
                {
                    if (nodeToRemove == tail)
                    {
                        tail = nodeBeforeNodeToRemove;
                    }

                    nodeBeforeNodeToRemove.setNext(nodeToRemove.getNext());
                    nodeToRemove.setNext(null);

                    result = nodeToRemove.getValue();
                    nodeToRemove.setValue(null);
                }
            }
        }

        return result;
    }

    @Override
    public T removeFirst()
    {
        T result = null;

        if (head != null)
        {
            final SingleLinkNode<T> nodeToRemove = head;

            if (head == tail)
            {
                tail = null;
            }

            head = nodeToRemove.getNext();
            nodeToRemove.setNext(null);

            result = nodeToRemove.getValue();
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
        final SingleLinkNode<T> node = getNode(index);
        return node == null ? null : node.getValue();
    }

    @Override
    public Iterator<T> iterate()
    {
        return head == null ? new EmptyIterator<T>() : head.iterate();
    }

    public static <T> SingleLinkList<T> fromValues(T[] values)
    {
        final SingleLinkList<T> result = new SingleLinkList<>();
        result.addAll(values);
        return result;
    }

    public static <T> SingleLinkList<T> fromValues(Iterator<T> values)
    {
        final SingleLinkList<T> result = new SingleLinkList<>();
        result.addAll(values);
        return result;
    }

    public static <T> SingleLinkList<T> fromValues(Iterable<T> values)
    {
        final SingleLinkList<T> result = new SingleLinkList<>();
        result.addAll(values);
        return result;
    }
}
