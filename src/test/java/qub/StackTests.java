package qub;

public class StackTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Stack<T>", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Stack<Double> stack = new Stack<>();
                        test.assertFalse(stack.any());
                        test.assertEqual(0, stack.getCount());
                    }
                });

                runner.test("push()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Stack<Double> stack = new Stack<>();
                        stack.push(5.6);
                        test.assertTrue(stack.any());
                        test.assertEqual(1, stack.getCount());
                        test.assertEqual(5.6, stack.peek());
                    }
                });

                runner.testGroup("pop()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Stack<Double> stack = new Stack<>();
                                test.assertNull(stack.pop());
                            }
                        });

                        runner.test("when not empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Stack<Double> stack = new Stack<>();
                                stack.push(1.2);
                                stack.push(3.4);
                                test.assertEqual(3.4, stack.pop());
                                test.assertEqual(1, stack.getCount());

                                test.assertEqual(1.2, stack.pop());
                                test.assertEqual(0, stack.getCount());

                                test.assertNull(stack.pop());
                            }
                        });
                    }
                });

                runner.testGroup("peek()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Stack<Double> stack = new Stack<>();
                                test.assertNull(stack.peek());
                            }
                        });

                        runner.test("when not empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Stack<Double> stack = new Stack<>();

                                stack.push(1.2);
                                for (int i = 0; i < 3; ++i)
                                {
                                    test.assertEqual(1.2, stack.peek());
                                }

                                stack.push(3.4);
                                for (int i = 0; i < 3; ++i)
                                {
                                    test.assertEqual(3.4, stack.peek());
                                }

                                stack.pop();
                                for (int i = 0; i < 3; ++i)
                                {
                                    test.assertEqual(1.2, stack.peek());
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
