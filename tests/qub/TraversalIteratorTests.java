package qub;

public interface TraversalIteratorTests
{
    static void test(TestRunner runner, Function1<Iterable<Node2<Integer>>,? extends TraversalIteratorBase<Node2<Integer>,Integer>> creator)
    {
        runner.testGroup(TraversalIteratorBase.class, () ->
        {
            TraversalActionsTests.test(runner, creator);
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                PreCondition.assertNotNull(count, "count");
                PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");
                PreCondition.assertNotNull(started, "started");

                final List<Node2<Integer>> startNodes = List.create();
                if (count >= 1)
                {
                    Node2<Integer> startNode = Node2.create(0);
                    Node2<Integer> currentNode = startNode;
                    for (int i = 1; i < count; ++i)
                    {
                        final Node2<Integer> nextNode = Node2.create(i);
                        currentNode.setNode2(nextNode);
                        currentNode = nextNode;
                    }
                    startNodes.add(startNode);
                }

                final Iterator<Integer> result = creator.run(startNodes);
                if (started)
                {
                    result.start();
                }

                PostCondition.assertNotNull(result, "result");

                return result;
            });
        });
    }
}
