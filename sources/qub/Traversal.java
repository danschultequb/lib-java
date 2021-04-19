package qub;

public interface Traversal<TNode,TValue>
{
    /**
     * Create a depth-first-search traversal using the provided visit node function.
     * @param visitNodeFunction The function that will be invoked when visiting each traversed
     *                          node.
     * @param <TNode> The type of the nodes that will be traversed.
     * @param <TValue> The type of values that will be returned by the traversal.
     * @return The new traversal.
     */
    static <TNode,TValue> DepthFirstSearchTraversal<TNode,TValue> createDepthFirstSearch(Action2<TraversalActions<TNode,TValue>,TNode> visitNodeFunction)
    {
        PreCondition.assertNotNull(visitNodeFunction, "visitNodeFunction");

        return DepthFirstSearchTraversal.create(visitNodeFunction);
    }

    default Iterator<TValue> iterate(TNode startNode)
    {
        return this.iterate(Iterator.create(startNode));
    }

    default Iterator<TValue> iterate(Iterable<TNode> startNodes)
    {
        PreCondition.assertNotNull(startNodes, "startNodes");

        return this.iterate(startNodes.iterate());
    }

    Iterator<TValue> iterate(Iterator<TNode> startNodes);
}
