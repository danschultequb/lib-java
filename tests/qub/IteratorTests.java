package qub;

public abstract class IteratorTests
{
    public static void test(final TestRunner runner, final Function2<Integer,Boolean,Iterator<Integer>> createIterator)
    {
        runner.testGroup("Iterator<T>", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("takeCurrent()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                test.assertNull(iterator.takeCurrent());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                    }
                });

                runner.testGroup("any()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertFalse(iterator.any());
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertFalse(iterator.any());
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(10, false);
                                assertIterator(test, iterator, false, null);
                                test.assertTrue(iterator.any());
                                assertIterator(test, iterator, true, 0);
                            }
                        });

                        runner.test("with non-empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(10, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertTrue(iterator.any());
                                assertIterator(test, iterator, true, 0);
                            }
                        });
                        
                        runner.test("with non-empty Iterator at second element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(10, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertTrue(iterator.next());
                                assertIterator(test, iterator, true, 1);
                                test.assertTrue(iterator.any());
                                assertIterator(test, iterator, true, 1);
                            }
                        });
                    }
                });
                
                runner.testGroup("getCount()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertEqual(3, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty Iterator at first value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(3, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty Iterator at second value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertTrue(iterator.next());
                                assertIterator(test, iterator, true, 1);
                                test.assertEqual(2, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                                test.assertEqual(0, iterator.getCount());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("first()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.first());
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.first());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertEqual(0, iterator.first());
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(0, iterator.first());
                                assertIterator(test, iterator, true, 0);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(0, iterator.getCurrent());
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(0, iterator.first());
                                assertIterator(test, iterator, true, 0);
                            }
                        });
                    }
                });

                runner.testGroup("first() with condition", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.first(null));
                                assertIterator(test, iterator, false, null);
                            }
                        });

                        runner.test("with empty started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.first(null));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.first(null));
                                assertIterator(test, iterator, false, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertNull(iterator.first(null));
                                assertIterator(test, iterator, true, 0);
                            }
                        });

                        runner.test("with empty non-started Iterator and condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.first(Math.isOdd));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with empty started Iterator and condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.first(Math.isOdd));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertEqual(1, iterator.first(Math.isOdd));
                                assertIterator(test, iterator, true, 1);
                                test.assertEqual(1, iterator.first(Math.isOdd));
                                assertIterator(test, iterator, true, 1);
                            }
                        });

                        runner.test("with non-empty started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(0, iterator.first(Math.isEven));
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(0, iterator.first(Math.isEven));
                                assertIterator(test, iterator, true, 0);
                            }
                        });
                    }
                });

                runner.testGroup("last()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.last());
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last());
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertEqual(2, iterator.last());
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last());
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(2, iterator.last());
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last());
                                assertIterator(test, iterator, true, null);
                            }
                        });
                    }
                });

                runner.testGroup("last() with condition", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.last(null));
                                assertIterator(test, iterator, false, null);
                            }
                        });

                        runner.test("with empty started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last(null));
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.last(null));
                                assertIterator(test, iterator, false, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertNull(iterator.last(null));
                                assertIterator(test, iterator, true, 0);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertNull(iterator.last(Math.isOdd));
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with empty started Iterator and condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last(Math.isOdd));
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, false);
                                assertIterator(test, iterator, false, null);
                                test.assertEqual(1, iterator.last(Math.isOdd));
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last(Math.isOdd));
                                assertIterator(test, iterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertEqual(2, iterator.last(Math.isEven));
                                assertIterator(test, iterator, true, null);
                                test.assertNull(iterator.last(Math.isEven));
                                assertIterator(test, iterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("contains()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);
                                test.assertFalse(iterator.contains(3));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);
                                test.assertFalse(iterator.contains(3));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and not found value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(2, false);
                                assertIterator(test, iterator, false, null);
                                test.assertFalse(iterator.contains(3));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and found value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);
                                test.assertTrue(iterator.contains(3));
                                assertIterator(test, iterator, true, 3);
                                test.assertTrue(iterator.contains(3));
                                assertIterator(test, iterator, true, 3);
                            }
                        });

                        runner.test("with non-empty started Iterator and not found value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(2, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertFalse(iterator.contains(3));
                                assertIterator(test, iterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and found value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(20, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertTrue(iterator.contains(12));
                                assertIterator(test, iterator, true, 12);
                                test.assertTrue(iterator.contains(12));
                                assertIterator(test, iterator, true, 12);
                            }
                        });
                    }
                });

                runner.testGroup("take()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and negative toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> takeIterator = iterator.take(-3);
                                assertIterator(test, takeIterator, false, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and zero toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> takeIterator = iterator.take(0);
                                assertIterator(test, takeIterator, false, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and positive toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> takeIterator = iterator.take(2);
                                assertIterator(test, takeIterator, false, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and negative toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> takeIterator = iterator.take(-3);
                                assertIterator(test, takeIterator, false, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and zero toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> takeIterator = iterator.take(0);
                                assertIterator(test, takeIterator, false, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and positive toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> takeIterator = iterator.take(2);
                                assertIterator(test, takeIterator, false, null);

                                for (int i = 0; i < 2; ++i)
                                {
                                    test.assertTrue(takeIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, takeIterator, true, i);
                                }

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, 1);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and negative toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> takeIterator = iterator.take(-3);
                                assertIterator(test, takeIterator, true, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and zero toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> takeIterator = iterator.take(0);
                                assertIterator(test, takeIterator, true, null);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and positive but less than Iterator count toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> takeIterator = iterator.take(2);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, takeIterator, true, 0);

                                test.assertTrue(takeIterator.next());
                                assertIterator(test, iterator, true, 1);
                                assertIterator(test, takeIterator, true, 1);

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, 1);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and positive equal to Iterator count toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> takeIterator = iterator.take(5);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, takeIterator, true, 0);

                                for (int i = 1; i < 5; ++i) {
                                    test.assertTrue(takeIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, takeIterator, true, i);
                                }

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, 4);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and positive greater than Iterator count toTake value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> takeIterator = iterator.take(100);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, takeIterator, true, 0);

                                for (int i = 1; i < 5; ++i) {
                                    test.assertTrue(takeIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, takeIterator, true, i);
                                }

                                test.assertFalse(takeIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, takeIterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("skip()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and negative toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipIterator = iterator.skip(-3);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipIterator, false, null);

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and zero toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipIterator = iterator.skip(0);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipIterator, false, null);

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and positive toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipIterator = iterator.skip(2);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipIterator, false, null);

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and negative toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipIterator = iterator.skip(-3);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipIterator, false, null);

                                for (int i = 0; i < 5; ++i)
                                {
                                    test.assertTrue(skipIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipIterator, true, i);
                                }

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and zero toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipIterator = iterator.skip(0);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipIterator, false, null);

                                for (int i = 0; i < 5; ++i)
                                {
                                    test.assertTrue(skipIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipIterator, true, i);
                                }

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and positive toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipIterator = iterator.skip(2);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipIterator, false, null);

                                for (int i = 2; i < 5; ++i)
                                {
                                    test.assertTrue(skipIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipIterator, true, i);
                                }

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and negative toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipIterator = iterator.skip(-3);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipIterator, true, 0);

                                for (int i = 1; i < 5; ++i)
                                {
                                    test.assertTrue(skipIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipIterator, true, i);
                                }

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and zero toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipIterator = iterator.skip(0);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipIterator, true, 0);

                                for (int i = 1; i < 5; ++i)
                                {
                                    test.assertTrue(skipIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipIterator, true, i);
                                }

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and positive less than Iterator count toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipIterator = iterator.skip(2);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipIterator, true, 2);
                                assertIterator(test, iterator, true, 2);

                                for (int i = 3; i < 5; ++i)
                                {
                                    test.assertTrue(skipIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipIterator, true, i);
                                }

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and positive equal to Iterator count toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipIterator = iterator.skip(5);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipIterator, true, null);
                                assertIterator(test, iterator, true, null);

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and positive greater than Iterator count toSkip value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipIterator = iterator.skip(100);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipIterator, true, null);
                                assertIterator(test, iterator, true, null);

                                test.assertFalse(skipIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipIterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("skipUntil() with condition", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(null);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipUntilIterator, false, null);

                                test.assertFalse(skipUntilIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipUntilIterator, true, null);
                            }
                        });

                        runner.test("with empty non-started Iterator and condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipUntilIterator, false, null);

                                test.assertFalse(skipUntilIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipUntilIterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(null);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipUntilIterator, false, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and non-matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(6, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Comparer.equal(20));
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipUntilIterator, false, null);

                                test.assertFalse(skipUntilIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipUntilIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, skipUntilIterator, false, null);

                                for (int i = 1; i < 5; ++i)
                                {
                                    test.assertTrue(skipUntilIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipUntilIterator, true, i);
                                }

                                test.assertFalse(skipUntilIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipUntilIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(null);
                                assertIterator(test, iterator, true, 0);
                                test.assertNull(skipUntilIterator.getCurrent());
                                assertIterator(test, skipUntilIterator, true, null);
                                assertIterator(test, iterator, true, 0);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and non-matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipUntilIterator, true, 1);
                                assertIterator(test, iterator, true, 1);

                                test.assertTrue(skipUntilIterator.next());
                                assertIterator(test, iterator, true, 2);
                                assertIterator(test, skipUntilIterator, true, 2);

                                test.assertFalse(skipUntilIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipUntilIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, skipUntilIterator, true, 1);
                                assertIterator(test, iterator, true, 1);

                                for (int i = 2; i < 5; ++i)
                                {
                                    test.assertTrue(skipUntilIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, skipUntilIterator, true, i);
                                }

                                test.assertFalse(skipUntilIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, skipUntilIterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("where()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> whereIterator = iterator.where(null);
                                assertIterator(test, iterator, false, null);
                                test.assertSame(iterator, whereIterator);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, whereIterator, false, null);

                                test.assertFalse(whereIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, whereIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> whereIterator = iterator.where(null);
                                assertIterator(test, iterator, false, null);
                                test.assertSame(iterator, whereIterator);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and non-matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(6, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> whereIterator = iterator.where(Comparer.equal(20));
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, whereIterator, false, null);

                                test.assertFalse(whereIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, whereIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, whereIterator, false, null);

                                for (int i = 1; i < 5; i += 2)
                                {
                                    test.assertTrue(whereIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, whereIterator, true, i);
                                }

                                test.assertFalse(whereIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, whereIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and null condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> whereIterator = iterator.where(null);
                                assertIterator(test, iterator, true, 0);
                                test.assertSame(iterator, whereIterator);
                            }
                        });

                        runner.test("with non-empty started Iterator and non-matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(3, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, whereIterator, true, 1);
                                assertIterator(test, iterator, true, 1);

                                test.assertFalse(whereIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, whereIterator, true, null);
                            }
                        });

                        runner.test("with started non-empty Iterator and matching condition", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, whereIterator, true, 1);
                                assertIterator(test, iterator, true, 1);

                                test.assertTrue(whereIterator.next());
                                assertIterator(test, iterator, true, 3);
                                assertIterator(test, whereIterator, true, 3);

                                test.assertFalse(whereIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, whereIterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("map()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started iterator and null conversion", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> mapIterator = iterator.map(null);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, mapIterator, false, null);

                                test.assertFalse(mapIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, mapIterator, true, null);
                            }
                        });
                        
                        runner.test("with empty non-started Iterator and conversion", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, mapIterator, false, null);

                                test.assertFalse(mapIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, mapIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and null conversion", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Boolean> mapIterator = iterator.map(null);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, mapIterator, false, null);

                                test.assertFalse(mapIterator.next());
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, mapIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and conversion", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, mapIterator, false, null);

                                for (int i = 0; i < 5; ++i)
                                {
                                    test.assertTrue(mapIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, mapIterator, true, i % 2 == 1);
                                }

                                test.assertFalse(mapIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, mapIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and null conversion", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Boolean> mapIterator = iterator.map(null);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, mapIterator, true, null);

                                test.assertFalse(mapIterator.next());
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, mapIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and conversion", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, mapIterator, true, false);

                                for (int i = 1; i < 5; ++i)
                                {
                                    test.assertTrue(mapIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, mapIterator, true, i % 2 == 1);
                                }

                                test.assertFalse(mapIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, mapIterator, true, null);
                            }
                        });
                    }
                });
                
                runner.testGroup("instanceOf()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator and null type", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, instanceOfIterator, false, null);

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });

                        runner.test("with empty non-started Iterator and type", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Boolean> instanceOfIterator = iterator.instanceOf(Boolean.class);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, instanceOfIterator, false, null);

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and null type", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, instanceOfIterator, false, null);

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });

                        runner.test("with non-empty non-started Iterator and no matches", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, false);
                                assertIterator(test, iterator, false, null);

                                final Iterator<Double> instanceOfIterator = iterator.instanceOf(Double.class);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, instanceOfIterator, false, null);

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });
                        
                        runner.test("with non-empty non-started Iterator and matches", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Number> iterator = createIterator.run(5, false).map(new Function1<Integer, Number>()
                                {
                                    @Override
                                    public Number run(Integer value)
                                    {
                                        return value;
                                    }
                                });
                                assertIterator(test, iterator, false, null);

                                final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
                                assertIterator(test, iterator, false, null);
                                assertIterator(test, instanceOfIterator, false, null);

                                for (int i = 0; i < 5; ++i)
                                {
                                    test.assertTrue(instanceOfIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, instanceOfIterator, true, i);
                                }

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and null type", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(5, true);
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, instanceOfIterator, true, null);
                                assertIterator(test, iterator, true, 0);

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and no matches", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Number> iterator = createIterator.run(5, true).map(new Function1<Integer, Number>()
                                {
                                    @Override
                                    public Number run(Integer value)
                                    {
                                        return value;
                                    }
                                });
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Float> instanceOfIterator = iterator.instanceOf(Float.class);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, instanceOfIterator, true, null);
                                assertIterator(test, iterator, true, null);

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });

                        runner.test("with non-empty started Iterator and matches", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Number> iterator = createIterator.run(5, true).map(new Function1<Integer, Number>()
                                {
                                    @Override
                                    public Number run(Integer value)
                                    {
                                        return value;
                                    }
                                });
                                assertIterator(test, iterator, true, 0);

                                final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
                                assertIterator(test, iterator, true, 0);
                                assertIterator(test, instanceOfIterator, true, 0);
                                assertIterator(test, iterator, true, 0);

                                for (int i = 1; i < 5; ++i)
                                {
                                    test.assertTrue(instanceOfIterator.next());
                                    assertIterator(test, iterator, true, i);
                                    assertIterator(test, instanceOfIterator, true, i);
                                }

                                test.assertFalse(instanceOfIterator.next());
                                assertIterator(test, iterator, true, null);
                                assertIterator(test, instanceOfIterator, true, null);
                            }
                        });
                    }
                });

                runner.testGroup("for each", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, false);
                                assertIterator(test, iterator, false, null);

                                int elementCount = 0;
                                for (final Integer ignored : iterator)
                                {
                                    ++elementCount;
                                }
                                assertIterator(test, iterator, true, null);

                                test.assertEqual(0, elementCount);
                            }
                        });

                        runner.test("with empty started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(0, true);
                                assertIterator(test, iterator, true, null);

                                int elementCount = 0;
                                for (final Integer ignored : iterator)
                                {
                                    ++elementCount;
                                }
                                assertIterator(test, iterator, true, null);

                                test.assertEqual(0, elementCount);
                            }
                        });

                        runner.test("with non-empty non-started Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(10, false);
                                assertIterator(test, iterator, false, null);

                                int i = 0;
                                for (final Integer element : iterator)
                                {
                                    test.assertEqual(i, element);
                                    ++i;
                                }
                                assertIterator(test, iterator, true, null);

                                test.assertEqual(10, i);
                            }
                        });

                        runner.test("with non-empty started Iterator at first value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(10, true);
                                assertIterator(test, iterator, true, 0);

                                int i = 0;
                                for (final Integer element : iterator)
                                {
                                    test.assertEqual(i, element);
                                    ++i;
                                }
                                assertIterator(test, iterator, true, null);

                                test.assertEqual(10, i);
                            }
                        });

                        runner.test("with non-empty started Iterator at second value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Iterator<Integer> iterator = createIterator.run(10, true);
                                assertIterator(test, iterator, true, 0);
                                test.assertTrue(iterator.next());
                                assertIterator(test, iterator, true, 1);

                                int i = 0;
                                for (final Integer element : iterator)
                                {
                                    test.assertEqual(i + 1, element);
                                    ++i;
                                }
                                assertIterator(test, iterator, true, null);

                                test.assertEqual(9, i);
                            }
                        });
                    }
                });
            }
        });
    }

    private static <T> void assertIterator(Test test, Iterator<T> iterator, boolean expectedHasStarted, T expectedCurrent)
    {
        test.assertEqual(expectedHasStarted, iterator.hasStarted(), "Wrong hasStarted()");
        test.assertEqual(expectedCurrent != null, iterator.hasCurrent(), "Wrong hasCurrent()");
        test.assertEqual(expectedCurrent, iterator.getCurrent(), "Wrong getCurrent()");
    }
}
