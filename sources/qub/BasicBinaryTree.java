package qub;

public class BasicBinaryTree<T> implements BinaryTree<T>
{
    private final Function2<T,T,Comparison> comparer;
    private Node2<T> root;

    private BasicBinaryTree(Function2<T,T,Comparison> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        this.comparer = comparer;
    }

    /**
     * Create a new BinaryTree using the provided comparer.
     * @param comparer The function that will be used to compare the values of the nodes in this tree.
     * @param <T> The type of value stored in each node of this tree.
     * @return The new BinaryTree.
     */
    @SafeVarargs
    public static <T> BasicBinaryTree<T> create(Function2<T,T,Comparison> comparer, T... values)
    {
        return new BasicBinaryTree<>(comparer).addAll(values);
    }

    /**
     * Create a new BinaryTree using the provided comparer.
     * @param comparer The function that will be used to compare the values of the nodes in this tree.
     * @param <T> The type of value stored in each node of this tree.
     * @return The new BinaryTree.
     */
    public static <T> BasicBinaryTree<T> create(Function2<T,T,Comparison> comparer, Iterable<T> values)
    {
        return new BasicBinaryTree<>(comparer).addAll(values);
    }

    /**
     * Create a new BinaryTree using the values' compareTo() function.
     * @param <T> The type of value stored in this
     * @return The new BinaryTree.
     */
    @SafeVarargs
    public static <T extends java.lang.Comparable<T>> BasicBinaryTree<T> create(T... values)
    {
        return BasicBinaryTree.<T>create(Comparer::compare).addAll(values);
    }

    /**
     * Create a new BinaryTree using the values' compareTo() function.
     * @param <T> The type of value stored in this
     * @return The new BinaryTree.
     */
    @SuppressWarnings("unchecked")
    public static <T extends java.lang.Comparable<T>> BasicBinaryTree<T> create(Iterable<T> values)
    {
        return BasicBinaryTree.<T>create(Comparer::compare).addAll(values);
    }

    @Override
    public BasicBinaryTree<T> add(T value)
    {
        final Node2<T> nodeToAdd = Node2.create(value);

        if (this.root == null)
        {
            this.root = nodeToAdd;
        }
        else
        {
            Node2<T> parentNode = this.root;
            Node2<T> childNode;
            while (true)
            {
                final Comparison comparison = this.comparer.run(value, parentNode.getValue());
                if (comparison == Comparison.GreaterThan)
                {
                    childNode = parentNode.getNode2();
                    if (childNode == null)
                    {
                        parentNode.setNode2(nodeToAdd);
                        break;
                    }
                    else
                    {
                        parentNode = childNode;
                    }
                }
                else
                {
                    childNode = parentNode.getNode1();
                    if (childNode == null)
                    {
                        parentNode.setNode1(nodeToAdd);
                        break;
                    }
                    else
                    {
                        parentNode = childNode;
                    }
                }
            }
        }

        return this;
    }

    @SafeVarargs
    @Override
    public final BasicBinaryTree<T> addAll(T... values)
    {
        return (BasicBinaryTree<T>)BinaryTree.super.addAll(values);
    }

    @Override
    public BasicBinaryTree<T> addAll(Iterable<T> values)
    {
        return (BasicBinaryTree<T>)BinaryTree.super.addAll(values);
    }

    @Override
    public BasicBinaryTree<T> addAll(Iterator<T> values)
    {
        return (BasicBinaryTree<T>)BinaryTree.super.addAll(values);
    }

    @Override
    public boolean remove(T value)
    {
        boolean result = false;

        Node2<T> parentNode = null;
        Comparison parentComparison = null;
        Node2<T> currentNode = this.root;
        Comparison currentComparison = null;
        while (currentNode != null && !result)
        {
            currentComparison = this.comparer.run(value, currentNode.getValue());
            if (currentComparison == Comparison.Equal)
            {
                result = true;

                final Node2<T> leftChild = currentNode.getNode1();
                final Node2<T> rightChild = currentNode.getNode2();
                if (leftChild == null && rightChild == null)
                {
                    if (parentComparison == null)
                    {
                        this.root = null;
                    }
                    else if (parentComparison == Comparison.LessThan)
                    {
                        parentNode.setNode1(null);
                    }
                    else
                    {
                        parentNode.setNode2(null);
                    }
                }
                else if (leftChild == null)
                {
                    if (parentComparison == null)
                    {
                        this.root = rightChild;
                    }
                    else if (parentComparison == Comparison.LessThan)
                    {
                        parentNode.setNode1(rightChild);
                    }
                    else
                    {
                        parentNode.setNode2(rightChild);
                    }
                }
                else if (rightChild == null)
                {
                    if (parentComparison == null)
                    {
                        this.root = leftChild;
                    }
                    else if (parentComparison == Comparison.LessThan)
                    {
                        parentNode.setNode1(leftChild);
                    }
                    else
                    {
                        parentNode.setNode2(leftChild);
                    }
                }
                else
                {
                    final Node2<T> leftRightGrandchild = leftChild.getNode2();
                    leftChild.setNode2(rightChild);
                    if (leftRightGrandchild != null)
                    {
                        Node2<T> rightLeftDescendent = rightChild;
                        while (rightLeftDescendent.getNode1() != null)
                        {
                            rightLeftDescendent = rightLeftDescendent.getNode1();
                        }
                        rightLeftDescendent.setNode1(leftRightGrandchild);
                    }

                    if (parentComparison == null)
                    {
                        this.root = leftChild;
                    }
                    else if (parentComparison == Comparison.LessThan)
                    {
                        parentNode.setNode1(leftChild);
                    }
                    else
                    {
                        parentNode.setNode2(leftChild);
                    }
                }
            }
            else
            {
                parentNode = currentNode;
                parentComparison = currentComparison;
                currentNode = currentComparison == Comparison.LessThan
                    ? currentNode.getNode1()
                    : currentNode.getNode2();
            }
        }

        return result;
    }

    @Override
    public Iterator<T> iterate()
    {
        return this.root == null ? Iterator.create() : BinaryTreeIterator.create(this.root);
    }

    @Override
    public boolean contains(T value)
    {
        boolean result = false;

        Node2<T> currentNode = this.root;
        while (currentNode != null && !result)
        {
            switch (this.comparer.run(value, currentNode.getValue()))
            {
                case LessThan:
                    currentNode = currentNode.getNode1();
                    break;

                case Equal:
                    result = true;
                    break;

                default: // GreaterThan
                    currentNode = currentNode.getNode2();
                    break;
            }
        }

        return result;
    }
    
    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }
}
