package qub;

public interface DepthFirstSearchTraversalIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DepthFirstSearchTraversalIterator.class, () ->
        {
            TraversalIteratorTests.test(runner, (Iterable<Node2<Integer>> startNodes) ->
            {
                PreCondition.assertNotNull(startNodes, "startNodes");

                return DepthFirstSearchTraversalIterator.create(startNodes.iterate(), Node2::inOrderTraversal);
            });

            runner.testGroup("iteration", () ->
            {
                final Action4<String,Iterable<Node2<Integer>>,Action2<TraversalActions<Node2<Integer>,Integer>,Node2<Integer>>,Iterable<Integer>> iterationTest = (String testName, Iterable<Node2<Integer>> startNodes, Action2<TraversalActions<Node2<Integer>,Integer>,Node2<Integer>> visitNodeFunction, Iterable<Integer> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final DepthFirstSearchTraversalIterator<Node2<Integer>,Integer> iterator = DepthFirstSearchTraversalIterator.create(startNodes.iterate(), visitNodeFunction);
                        test.assertFalse(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertFalse(iterator.hasNodesToVisit());
                        test.assertFalse(iterator.hasValueToReturn());
                        test.assertEqual(startNodes.any(), iterator.hasActionsToRun());

                        final List<Integer> returnedValues = List.create();
                        while (iterator.next())
                        {
                            test.assertTrue(iterator.hasStarted());
                            test.assertTrue(iterator.hasCurrent());
                            test.assertTrue(iterator.hasValueToReturn());

                            returnedValues.add(iterator.getCurrent());
                        }

                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertFalse(iterator.hasNodesToVisit());
                        test.assertFalse(iterator.hasValueToReturn());
                        test.assertFalse(iterator.hasActionsToRun());

                        test.assertEqual(expected, returnedValues);
                    });
                };

                iterationTest.run("with no start nodes (pre-order traversal)",
                    Iterable.create(),
                    Node2::preOrderTraversal,
                    Iterable.create());

                iterationTest.run("with no start nodes (in-order traversal)",
                    Iterable.create(),
                    Node2::inOrderTraversal,
                    Iterable.create());

                iterationTest.run("with no start nodes (post-order traversal)",
                    Iterable.create(),
                    Node2::postOrderTraversal,
                    Iterable.create());

                iterationTest.run("with one single-node start node (pre-order traversal)",
                    Iterable.create(
                        Node2.create(10)),
                    Node2::preOrderTraversal,
                    Iterable.create(10));

                iterationTest.run("with one single-node start node (in-order traversal)",
                    Iterable.create(
                        Node2.create(10)),
                    Node2::inOrderTraversal,
                    Iterable.create(10));

                iterationTest.run("with one single-node start node (post-order traversal)",
                    Iterable.create(
                        Node2.create(10)),
                    Node2::postOrderTraversal,
                    Iterable.create(10));



                iterationTest.run("with one multiple-node start node (pre-order traversal)",
                    Iterable.create(
                        Node2.create(1)
                            .setNode1(Node2.create(2)
                                .setNode1(Node2.create(3))
                                .setNode2(Node2.create(4)))
                            .setNode2(Node2.create(5))),
                    Node2::preOrderTraversal,
                    Iterable.create(1, 2, 3, 4, 5));

                iterationTest.run("with one start node (in-order traversal)",
                    Iterable.create(
                        Node2.create(1)
                            .setNode1(Node2.create(2)
                                .setNode1(Node2.create(3))
                                .setNode2(Node2.create(4)))
                            .setNode2(Node2.create(5))),
                    Node2::inOrderTraversal,
                    Iterable.create(3, 2, 4, 1, 5));

                iterationTest.run("with one start node (post-order traversal)",
                    Iterable.create(
                        Node2.create(1)
                            .setNode1(Node2.create(2)
                                .setNode1(Node2.create(3))
                                .setNode2(Node2.create(4)))
                            .setNode2(Node2.create(5))),
                    Node2::postOrderTraversal,
                    Iterable.create(3, 4, 2, 5, 1));
            });
        });
    }
}
