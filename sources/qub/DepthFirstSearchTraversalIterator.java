package qub;

public class DepthFirstSearchTraversalIterator<TNode,TValue> extends TraversalIteratorBase<TNode,TValue>
{
    private final Action2<TraversalActions<TNode,TValue>,TNode> visitNodeFunction;
    private final List<Action0> actionsToRun;
    private final List<TNode> nodesToVisit;
    private Iterator<TValue> valuesToReturn;
    private int actionsToRunInsertIndex;

    protected DepthFirstSearchTraversalIterator(Iterator<TNode> startNodes, Action2<TraversalActions<TNode,TValue>,TNode> visitNodeFunction)
    {
        PreCondition.assertNotNull(startNodes, "startNodes");
        PreCondition.assertNotNull(visitNodeFunction, "visitNodeFunction");

        this.visitNodeFunction = visitNodeFunction;
        this.actionsToRun = List.create();
        this.nodesToVisit = List.create();
        this.valuesToReturn = Iterator.create();

        this.visitNodes(startNodes);
    }

    public static <TNode,TValue> DepthFirstSearchTraversalIterator<TNode,TValue> create(Iterator<TNode> startNodes, Action2<TraversalActions<TNode,TValue>,TNode> visitNodeFunction)
    {
        return new DepthFirstSearchTraversalIterator<>(startNodes, visitNodeFunction);
    }

    @Override
    public void visitNodes(Iterator<TNode> nodes)
    {
        PreCondition.assertNotNull(nodes, "nodes");

        nodes.start();
        while (nodes.hasCurrent())
        {
            final TNode node = nodes.takeCurrent();
            this.actionsToRun.insert(this.actionsToRunInsertIndex++, () ->
            {
                this.nodesToVisit.add(node);
            });
        }
    }

    @Override
    public void returnValues(Iterator<TValue> values)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertFalse(this.valuesToReturn.any(), "this.valuesToReturn.any()");

        this.actionsToRun.insert(this.actionsToRunInsertIndex++, () ->
        {
            this.valuesToReturn = values;
        });

        PostCondition.assertFalse(this.valuesToReturn.any(), "this.valuesToReturn.any()");
    }

    @Override
    protected boolean hasActionsToRun()
    {
        return this.actionsToRun.any();
    }

    @Override
    protected void runNextAction()
    {
        final Action0 action = this.actionsToRun.removeFirst();
        action.run();
    }

    @Override
    protected boolean hasNodesToVisit()
    {
        return this.nodesToVisit.any();
    }

    @Override
    protected void visitNextNode()
    {
        PreCondition.assertTrue(this.hasNodesToVisit(), "this.hasNodesToVisit()");

        this.actionsToRunInsertIndex = 0;

        final TNode nodeToVisit = this.nodesToVisit.removeFirst();
        this.visitNodeFunction.run(this, nodeToVisit);
    }

    @Override
    protected boolean hasValueToReturn()
    {
        return this.valuesToReturn.hasCurrent();
    }

    @Override
    protected TValue getValueToReturn()
    {
        PreCondition.assertTrue(this.hasValueToReturn(), "this.hasValueToReturn()");

        return this.valuesToReturn.getCurrent();
    }

    @Override
    protected boolean nextValueToReturn()
    {
        return this.valuesToReturn.next();
    }
}
