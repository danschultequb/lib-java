package qub;

public interface DoubleLinkNodeTests
{
    static void test(final TestRunner runner)
    {
        runner.testGroup(DoubleLinkNode.class, () ->
        {
            IterableTests.test(runner, (Integer count) ->
            {
                DoubleLinkNode<Integer> result = null;

                if (count > 0)
                {
                    result = new DoubleLinkNode<>(0);
                    DoubleLinkNode<Integer> currentNode = result;
                    for (int i = 1; i < count; ++i)
                    {
                        final DoubleLinkNode<Integer> nextNode = new DoubleLinkNode<>(i);
                        currentNode.setNext(nextNode);
                        nextNode.setPrevious(currentNode);

                        currentNode = nextNode;
                    }
                }

                return result;
            });
            
            runner.test("constructor()", (Test test) ->
            {
                final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(20);
                test.assertEqual(20, node.getValue());
                test.assertNull(node.getPrevious());
                test.assertNull(node.getNext());
            });
            
            runner.testGroup("setValue()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(10);
                    node.setValue(null);
                    test.assertNull(node.getValue());
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(10);
                    node.setValue(20);
                    test.assertEqual(20, node.getValue());
                });
            });
            
            runner.test("setPrevious()", (Test test) ->
            {
                final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(11);
                node.setPrevious(node);
                test.assertSame(node, node.getPrevious());
            });

            runner.test("setNext()", (Test test) ->
            {
                final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(11);
                node.setNext(node);
                test.assertSame(node, node.getNext());
            });
        });
    }
}
