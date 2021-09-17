package qub;

public interface GraphTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Graph.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableGraph<Integer> graph = Graph.create();
                test.assertNotNull(graph);
                test.assertEqual(Iterable.create(), graph.getNodes());
            });

            runner.testGroup("getNode(T)", () ->
            {
                runner.test("with value that doesn't exist in the graph", (Test test) ->
                {
                    final Graph<Integer> graph = Graph.create();
                    test.assertThrows(() -> graph.getNode(5).await(),
                        new NotFoundException("No node exists in the Graph with the value 5."));
                });

                runner.test("with value that exists in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);
                    final GraphNode<Integer> getNodeResult = graph.getNode(1).await();
                    test.assertSame(node, getNodeResult);
                });
            });

            runner.testGroup("containsValue(T)", () ->
            {
                runner.test("with value that doesn't exist in the graph", (Test test) ->
                {
                    final Graph<Integer> graph = Graph.create();
                    test.assertFalse(graph.containsValue(5));
                });

                runner.test("with value that exists in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    graph.createNode(1);
                    test.assertTrue(graph.containsValue(1));
                });
            });

            runner.testGroup("containsNode(GraphNode<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Graph<Integer> graph = Graph.create();
                    test.assertThrows(() -> graph.containsNode(null),
                        new PreConditionFailure("node cannot be null."));
                });

                runner.test("with node from a different graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph1 = Graph.create();
                    final GraphNode<Integer> node1 = graph1.createNode(1);
                    final MutableGraph<Integer> graph2 = Graph.create();
                    final GraphNode<Integer> node2 = graph2.createNode(1);
                    test.assertFalse(graph1.containsNode(node2));
                });

                runner.test("with existing node", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);
                    test.assertTrue(graph.containsNode(node));
                });
            });
        });
    }
}
