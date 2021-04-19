package qub;

/**
 * An object that can be traversed.
 * @param <TNode> The type of the nodes that will be traversed.
 * @param <TValue> The type of the values that will be returned by the traversal.
 */
public interface Traversable<TNode,TValue>
{
    /**
     * Iterate through the values in this Traversable2 using the provided Traversal2.
     * @param traversal The Traversal2 to use to iterate through the values in this Traversable2.
     * @return An Iterator that will iterate through the values of this Traversable2.
     */
    Iterator<TValue> iterate(Traversal<TNode,TValue> traversal);
}
