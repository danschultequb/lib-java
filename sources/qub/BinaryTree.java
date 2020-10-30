package qub;

public interface BinaryTree<T> extends Iterable<T>
{
    /**
     * Create a new BinaryTree using the provided comparer.
     * @param comparer The function that will be used to compare the values of the nodes in this tree.
     * @param <T> The type of value stored in each node of this tree.
     * @return The new BinaryTree.
     */
    @SafeVarargs
    static <T> BinaryTree<T> create(Function2<T,T,Comparison> comparer, T... values)
    {
        return BasicBinaryTree.create(comparer, values);
    }

    /**
     * Create a new BinaryTree using the provided comparer.
     * @param comparer The function that will be used to compare the values of the nodes in this tree.
     * @param <T> The type of value stored in each node of this tree.
     * @return The new BinaryTree.
     */
    static <T> BinaryTree<T> create(Function2<T,T,Comparison> comparer, Iterable<T> values)
    {
        return BasicBinaryTree.create(comparer, values);
    }

    /**
     * Create a new BinaryTree using the values' compareTo() function.
     * @param <T> The type of value stored in this
     * @return The new BinaryTree.
     */
    @SafeVarargs
    static <T extends java.lang.Comparable<T>> BinaryTree<T> create(T... values)
    {
        return BinaryTree.create(Comparer::compare, values);
    }

    /**
     * Create a new BinaryTree using the values' compareTo() function.
     * @param <T> The type of value stored in this
     * @return The new BinaryTree.
     */
    static <T extends java.lang.Comparable<T>> BinaryTree<T> create(Iterable<T> values)
    {
        return BinaryTree.create(Comparer::compare, values);
    }

    /**
     * Add the provided value to this BinaryTree.
     * @param value The value to add to this BinaryTree.
     * @return This object for method chaining.
     */
    BinaryTree<T> add(T value);

    @SuppressWarnings("unchecked")
    default BinaryTree<T> addAll(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.add(value);
        }

        return this;
    }

    default BinaryTree<T> addAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.add(value);
        }

        return this;
    }

    default BinaryTree<T> addAll(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.add(value);
        }

        return this;
    }

    /**
     * Remove the first value in this BinaryTree that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this BinaryTree was found and removed.
     */
    boolean remove(T value);
}
