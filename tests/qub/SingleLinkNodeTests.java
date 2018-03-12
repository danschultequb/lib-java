package qub;

public class SingleLinkNodeTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SingleLinkNode.class, () ->
        {
            IterableTests.test(runner, (Integer count) ->
            {
                SingleLinkNode<Integer> head = null;
                if (count > 0)
                {
                    SingleLinkNode<Integer> tail = null;
                    for (int i = 0; i < count; ++i)
                    {
                        if(head == null)
                        {
                            head = new SingleLinkNode<>(i);
                            tail = head;
                        }
                        else
                        {
                            tail.setNext(new SingleLinkNode<>(i));
                            tail = tail.getNext();
                        }
                    }
                }
                return head;
            });

            runner.testGroup("constructor()", () ->
            {
                runner.test("with null", test ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(null);
                    test.assertNull(node.getValue());
                    test.assertNull(node.getNext());
                });

                runner.test("with non-null", test ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(55);
                    test.assertEqual(55, node.getValue());
                    test.assertNull(node.getNext());
                });
            });

            runner.testGroup("setValue()", () ->
            {
                runner.test("with null", test ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(33);
                    node.setValue(null);
                    test.assertNull(node.getValue());
                });

                runner.test("with non-null", test ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(33);
                    node.setValue(44);
                    test.assertEqual(44, node.getValue());
                });
            });

            runner.testGroup("setNext()", () ->
            {
                runner.test("with null", test ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(66);
                    node.setNext(null);
                    test.assertNull(node.getNext());
                });

                runner.test("with non-null", test ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(77);
                    node.setNext(node);
                    test.assertSame(node, node.getNext());
                });
            });
        });
    }
}
