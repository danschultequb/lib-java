package qub;

public class DoubleLinkNodeTests extends IterableTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("DoubleLinkNode<T>", new Action0()
        {
            @Override
            public void run()
            {
                IterableTests.test(runner, new Function1<Integer, Iterable<Integer>>()
                {
                    @Override
                    public Iterable<Integer> run(Integer count)
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
                    }
                });
                
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(20);
                        test.assertEqual(20, node.getValue());
                        test.assertNull(node.getPrevious());
                        test.assertNull(node.getNext());
                    }
                });
                
                runner.testGroup("setValue()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(10);
                                node.setValue(null);
                                test.assertNull(node.getValue());
                            }
                        });
                        
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(10);
                                node.setValue(20);
                                test.assertEqual(20, node.getValue());
                            }
                        });
                    }
                });
                
                runner.test("setPrevious()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(11);
                        node.setPrevious(node);
                        test.assertSame(node, node.getPrevious());
                    }
                });

                runner.test("setNext()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final DoubleLinkNode<Integer> node = new DoubleLinkNode<>(11);
                        node.setNext(node);
                        test.assertSame(node, node.getNext());
                    }
                });
            }
        });
    }
}
