package qub;

public class SingleLinkNodeTests extends IterableTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("SingleLinkNode<T>", new Action0()
        {
            @Override
            public void run()
            {
                IterableTests.test(runner, new Function1<Integer, Iterable<Integer>>()
                {
                    @Override
                    public Iterable<Integer> run(Integer count)
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
                    }
                });
                
                runner.testGroup("constructor()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SingleLinkNode<Integer> node = new SingleLinkNode<>(null);
                                test.assertNull(node.getValue());
                                test.assertNull(node.getNext());
                            }
                        });
                        
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SingleLinkNode<Integer> node = new SingleLinkNode<>(55);
                                test.assertEqual(55, node.getValue());
                                test.assertNull(node.getNext());
                            }
                        });
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
                                final SingleLinkNode<Integer> node = new SingleLinkNode<>(33);
                                node.setValue(null);
                                test.assertNull(node.getValue());
                            }
                        });

                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SingleLinkNode<Integer> node = new SingleLinkNode<>(33);
                                node.setValue(44);
                                test.assertEqual(44, node.getValue());
                            }
                        });
                    }
                });

                runner.testGroup("setNext()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SingleLinkNode<Integer> node = new SingleLinkNode<>(66);
                                node.setNext(null);
                                test.assertNull(node.getNext());
                            }
                        });

                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SingleLinkNode<Integer> node = new SingleLinkNode<>(77);
                                node.setNext(node);
                                test.assertSame(node, node.getNext());
                            }
                        });
                    }
                });
            }
        });
    }
}
