package qub;

public interface GraphNodeTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(GraphNode.class, () ->
        {
            runner.testGroup("create(MutableGraph<T>,T)", () ->
            {
                runner.test("with null graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = null;
                    final Integer value = 20;
                    test.assertThrows(() -> GraphNode.create(graph, value),
                        new PreConditionFailure("graph cannot be null."));
                });

                runner.test("with graph that doesn't already contain the provided value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final Integer value = 20;
                    final GraphNode<Integer> node = GraphNode.create(graph, value);
                    test.assertNotNull(node);
                    test.assertFalse(graph.containsNode(node));
                    test.assertEqual(value, node.getValue());
                    test.assertEqual(Iterable.create(), node.iterateLinkedNodes().toList());
                });

                runner.test("with graph that already contains the provided value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final Integer value = 20;
                    graph.createNode(value);

                    test.assertThrows(() -> GraphNode.create(graph, value),
                        new PreConditionFailure("graph.containsValue(value) cannot be true."));

                    test.assertEqual(Iterable.create(value), graph.getNodes().map(GraphNode::getValue));
                });
            });

            runner.testGroup("iterateLinkedNodes().toList()", () ->
            {
                runner.test("with no other nodes", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(20);

                    test.assertEqual(Iterable.create(), node.iterateLinkedNodes().toList());
                });

                runner.test("with another non-linked node", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node1 = graph.createNode(1);
                    final GraphNode<Integer> node2 = graph.createNode(2);

                    test.assertEqual(Iterable.create(), node1.iterateLinkedNodes().toList());
                    test.assertEqual(Iterable.create(), node2.iterateLinkedNodes().toList());
                });

                runner.test("with another linked node", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToNode(node2);

                    test.assertEqual(Iterable.create(node2), node1.iterateLinkedNodes().toList());
                    test.assertEqual(Iterable.create(), node2.iterateLinkedNodes().toList());
                });
            });

            runner.testGroup("getLinkedNode(T)", () ->
            {
                runner.test("with no linked nodes and null value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);

                    test.assertThrows(() -> node.getLinkedNode(null).await(),
                            new NotFoundException("No GraphNode is linked to this GraphNode with the value null."));
                });

                runner.test("with no linked nodes and non-null value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);

                    test.assertThrows(() -> node.getLinkedNode(2).await(),
                            new NotFoundException("No GraphNode is linked to this GraphNode with the value 2."));
                });

                runner.test("with linked node and not found value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToNode(node2);

                    test.assertThrows(() -> node1.getLinkedNode(3).await(),
                            new NotFoundException("No GraphNode is linked to this GraphNode with the value 3."));
                });

                runner.test("with linked node and found value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToNode(node2);

                    test.assertSame(node2, node1.getLinkedNode(2).await());
                });
            });

            runner.testGroup("isLinkedTo(T)", () ->
            {
                runner.test("with no linked nodes and null value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);

                    test.assertFalse(node.isLinkedTo((Integer)null));
                });

                runner.test("with no linked nodes and non-null value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);

                    test.assertFalse(node.isLinkedTo(2));
                });

                runner.test("with linked node and not found value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToNode(node2);

                    test.assertFalse(node1.isLinkedTo(3));
                });

                runner.test("with linked node and found value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToNode(node2);

                    test.assertTrue(node1.isLinkedTo(2));
                });
            });
        });
    }
}
