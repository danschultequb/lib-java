package qub;

public interface TraversalActions<TNode,TValue>
{
    /**
     * Schedule an action to visit the provided node.
     * @param node The node to visit.
     */
    default void visitNode(TNode node)
    {
        this.visitNodes(Iterator.create(node));
    }

    /**
     * Schedule an action to visit the provided nodes.
     * @param nodes The nodes to visit.
     */
    void visitNodes(Iterator<TNode> nodes);

    /**
     * Schedule an action to visit the provided nodes.
     * @param nodes The nodes to visit.
     */
    default void visitNodes(Iterable<TNode> nodes)
    {
        PreCondition.assertNotNull(nodes, "nodes");

        this.visitNodes(nodes.iterate());
    }

    /**
     * Schedule an action to return the provided value.
     * @param value The value to return.
     */
    default void returnValue(TValue value)
    {
        this.returnValues(Iterator.create(value));
    }

    /**
     * Schedule an action to return the provided values.
     * @param values The values to return.
     */
    void returnValues(Iterator<TValue> values);

    /**
     * Schedule an action to return the provided values.
     * @param values The values to return.
     */
    default void returnValues(Iterable<TValue> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.returnValues(values.iterate());
    }
}
