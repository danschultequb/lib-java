package qub;

public class IndexableTests
{
    public static void test(final TestRunner runner, final Function1<Integer,Indexable<Integer>> createIndexable)
    {
        runner.testGroup("Indexable<T>", new Action0()
        {
            @Override
            public void run()
            {
                IterableTests.test(runner, new Function1<Integer,Iterable<Integer>>()
                {
                    @Override
                    public Iterable<Integer> run(Integer count)
                    {
                        return createIndexable.run(count);
                    }
                });

                runner.testGroup("get()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with negative index", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(0);
                                test.assertNull(indexable.get(-5));
                            }
                        });
                        
                        runner.test("with index equal to Indexable count", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(3);
                                test.assertNull(indexable.get(3));
                            }
                        });
                    }
                });
                
                runner.testGroup("indexOf()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty Indexable and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(0);
                                test.assertEqual(-1, indexable.indexOf((Function1<Integer,Boolean>)null));
                            }
                        });

                        runner.test("with empty Indexable and non-null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(0);
                                test.assertEqual(-1, indexable.indexOf(Math.isOdd));
                            }
                        });

                        runner.test("with non-empty Indexable and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(1);
                                test.assertEqual(-1, indexable.indexOf((Function1<Integer,Boolean>)null));
                            }
                        });

                        runner.test("with non-empty Indexable and non-matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(1);
                                test.assertEqual(-1, indexable.indexOf(Math.isOdd));
                            }
                        });

                        runner.test("with non-empty Indexable and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(7);
                                test.assertEqual(1, indexable.indexOf(Math.isOdd));
                            }
                        });

                        runner.test("with non-empty Indexable and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(2);
                                test.assertEqual(-1, indexable.indexOf((Integer)null));
                            }
                        });

                        runner.test("with non-empty Indexable and not found value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(2);
                                test.assertEqual(-1, indexable.indexOf(20));
                            }
                        });

                        runner.test("with non-empty Indexable and found value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Indexable<Integer> indexable = createIndexable.run(10);
                                test.assertEqual(4, indexable.indexOf(4));
                            }
                        });
                    }
                });
            }
        });
    }
}
