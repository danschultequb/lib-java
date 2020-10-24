package qub;

public class Node2List<T> implements List<T>
{
    private Node2<T> head;
    private Node2<T> tail;

    private Node2List()
    {
        this.head = null;
    }

    public static <T> Node2List<T> create()
    {
        return new Node2List<>();
    }

    @Override
    public Node2List<T> add(T value)
    {
        final Node2<T> nodeToAdd = Node2.create(value);
        if (this.head == null)
        {
            this.head = nodeToAdd;
            this.tail = nodeToAdd;
        }
        else
        {
            nodeToAdd.setNode1(tail);
            this.tail.setNode2(nodeToAdd);
            this.tail = nodeToAdd;
        }
        return this;
    }

    @Override
    public Node2List<T> insert(int insertIndex, T value)
    {
        PreCondition.assertBetween(0, insertIndex, this.getCount(), "insertIndex");

        if (insertIndex == this.getCount())
        {
            this.add(value);
        }
        else
        {
            final Node2<T> nodeToInsert = Node2.create(value);

            if (insertIndex == 0)
            {
                nodeToInsert.setNode2(this.head);
                this.head.setNode1(nodeToInsert);
                this.head = nodeToInsert;
            }
            else
            {
                Node2<T> currentNode = this.head;
                for (int currentNodeIndex = 0; currentNodeIndex < insertIndex; ++currentNodeIndex)
                {
                    currentNode = currentNode.getNode2();
                }

                currentNode.getNode1().setNode2(nodeToInsert);
                nodeToInsert.setNode1(currentNode.getNode1());

                currentNode.setNode1(nodeToInsert);
                nodeToInsert.setNode2(currentNode);
            }
        }

        return this;
    }

    private Node2<T> getNode(int index)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        Node2<T> result = this.head;
        for (int i = 0; i < index; ++i)
        {
            result = result.getNode2();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Node2List<T> set(int index, T value)
    {
        PreCondition.assertIndexAccess(index, this.getCount());

        this.getNode(index).setValue(value);

        return this;
    }

    @Override
    public T removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, this.getCount());

        final Node2<T> nodeToRemove = this.getNode(index);
        final T result = nodeToRemove.getValue();

        final Node2<T> previousNode = nodeToRemove.getNode1();
        final Node2<T> nextNode = nodeToRemove.getNode2();

        if (previousNode == null)
        {
            this.head = nextNode;
        }
        else
        {
            previousNode.setNode2(nextNode);
        }

        if (nextNode == null)
        {
            this.tail = previousNode;
        }
        else
        {
            nextNode.setNode1(previousNode);
        }

        nodeToRemove.setNode1(null);
        nodeToRemove.setNode2(null);
        nodeToRemove.setValue(null);

        return result;
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        T result = null;

        Node2<T> node = this.head;
        while (node != null)
        {
            final T value = node.getValue();
            final Node2<T> nextNode = node.getNode2();
            if (condition.run(value))
            {
                result = value;

                final Node2<T> previousNode = node.getNode1();

                if (previousNode == null)
                {
                    this.head = nextNode;
                }
                else
                {
                    previousNode.setNode2(nextNode);
                }

                if (nextNode == null)
                {
                    this.tail = previousNode;
                }
                else
                {
                    nextNode.setNode1(previousNode);
                }

                node.setNode1(null);
                node.setNode2(null);
                node.setValue(null);
                break;
            }
            else
            {
                node = node.getNode2();
            }
        }

        return result;
    }

    @Override
    public Node2List<T> clear()
    {
        Node2<T> node = this.head;
        while (node != null)
        {
            final Node2<T> nextNode = node.getNode2();
            node.setNode1(null);
            node.setNode2(null);
            node.setValue(null);
            node = nextNode;
        }

        this.head = null;
        this.tail = null;

        return this;
    }

    @Override
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, this.getCount());

        return this.getNode(index).getValue();
    }

    @Override
    public Iterator<T> iterate()
    {
        return head == null ? EmptyIterator.create() : Node2Iterator.create(this.head);
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
