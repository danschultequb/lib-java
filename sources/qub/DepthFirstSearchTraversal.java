package qub;

public class DepthFirstSearchTraversal<TNode,TValue> implements Traversal<TNode,TValue>
{
    protected final Action2<TraversalActions<TNode,TValue>,TNode> visitNodeFunction;

    protected DepthFirstSearchTraversal(Action2<TraversalActions<TNode, TValue>, TNode> visitNodeFunction)
    {
        PreCondition.assertNotNull(visitNodeFunction, "visitNodeFunction");

        this.visitNodeFunction = visitNodeFunction;
    }

    public static <TNode,TValue> DepthFirstSearchTraversal<TNode,TValue> create(Action2<TraversalActions<TNode,TValue>,TNode> visitNodeFunction)
    {
        PreCondition.assertNotNull(visitNodeFunction, "visitNodeFunction");

        return new DepthFirstSearchTraversal<>(visitNodeFunction);
    }

    @Override
    public Iterator<TValue> iterate(Iterator<TNode> startNodes)
    {
        PreCondition.assertNotNull(startNodes, "startNodes");

        return DepthFirstSearchTraversalIterator.create(startNodes, this.visitNodeFunction);
    }
}
