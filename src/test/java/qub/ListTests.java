package qub;

public abstract class ListTests
{
    public static void test(final TestRunner runner, final Function1<Integer,List<Integer>> createList)
    {
        runner.testGroup("List<T>", new Action0()
        {
            @Override
            public void run()
            {
                IndexableTests.test(runner, new Function1<Integer,Indexable<Integer>>()
                {
                    @Override
                    public Indexable<Integer> run(Integer count)
                    {
                        return createList.run(count);
                    }
                });

                runner.testGroup("add()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("multiple values", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(0);
                                test.assertEqual(0, list.getCount());
                                test.assertFalse(list.any());
                                test.assertEqual(null, list.get(0));

                                for (int i = 0; i < 100; ++i)
                                {
                                    list.add(100 - i);
                                    test.assertEqual(i + 1, list.getCount());
                                    test.assertTrue(list.any());
                                    test.assertEqual(100 - i, list.get(i));
                                }
                            }
                        });

                        runner.test("after removing the only value in the List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(1);
                                list.removeFirst(Math.isEven);

                                list.add(70);
                                test.assertEqual(new int[] { 70 }, Array.toIntArray(list));
                            }
                        });
                    }
                });
                
                runner.test("addAll()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final List<Integer> list = createList.run(0);
                        test.assertEqual(0, list.getCount());
                        test.assertFalse(list.any());
                        test.assertEqual(null, list.get(0));

                        list.addAll();
                        test.assertEqual(0, list.getCount());
                        test.assertFalse(list.any());
                        test.assertEqual(null, list.get(0));

                        list.addAll(0);
                        test.assertEqual(1, list.getCount());
                        test.assertTrue(list.any());
                        test.assertEqual(0, list.get(0));

                        list.addAll(1, 2, 3, 4, 5);
                        test.assertEqual(6, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }

                        list.addAll((Iterator<Integer>)null);
                        test.assertEqual(6, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }

                        list.addAll(Array.<Integer>fromValues().iterate());
                        test.assertEqual(6, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }

                        list.addAll(Array.fromValues(6, 7, 8, 9).iterate());
                        test.assertEqual(10, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }

                        list.addAll((Iterable<Integer>)null);
                        test.assertEqual(10, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }

                        list.addAll(Array.<Integer>fromValues());
                        test.assertEqual(10, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }

                        list.addAll(Array.fromValues(10, 11));
                        test.assertEqual(12, list.getCount());
                        test.assertTrue(list.any());
                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(i, list.get(i));
                        }
                    }
                });
                
                runner.test("set()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final List<Integer> list = createList.run(0);
                        for (int i = -1; i <= 1; ++i)
                        {
                            list.set(i, i);
                            test.assertNull(list.get(i));
                        }

                        for (int i = 0; i < 10; ++i)
                        {
                            list.add(i);
                        }

                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            list.set(i, -list.get(i));
                        }

                        for (int i = 0; i < list.getCount(); ++i)
                        {
                            test.assertEqual(-i, list.get(i));
                        }
                    }
                });

                runner.testGroup("remove()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(10);
                                test.assertFalse(list.remove(null));
                                test.assertEqual(10, list.getCount());
                            }
                        });

                        runner.test("with not found", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(10);
                                test.assertFalse(list.remove(20));
                                test.assertEqual(10, list.getCount());
                            }
                        });

                        runner.test("with found", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(5);
                                test.assertTrue(list.remove(3));
                                test.assertEqual(4, list.getCount());
                                test.assertEqual(Array.fromValues(0, 1, 2, 4), list);
                            }
                        });
                    }
                });

                runner.test("removeAt()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final List<Integer> list = createList.run(0);
                        for (int i = -1; i <= 1; ++i)
                        {
                            test.assertNull(list.removeAt(i));
                        }
                        test.assertEqual(0, list.getCount());

                        for (int i = 0; i < 10; ++i)
                        {
                            list.add(i);
                        }
                        test.assertEqual(10, list.getCount());

                        test.assertEqual(0, list.removeAt(0));
                        test.assertEqual(9, list.getCount());

                        test.assertEqual(9, list.removeAt(8));
                        test.assertEqual(8, list.getCount());

                        test.assertEqual(5, list.removeAt(4));
                        test.assertEqual(7, list.getCount());
                    }
                });

                runner.testGroup("removeFirst()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(0);
                                test.assertNull(list.removeFirst());
                            }
                        });

                        runner.test("with single value List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(1);
                                test.assertEqual(0, list.removeFirst());
                                test.assertNull(list.removeFirst());
                            }
                        });

                        runner.test("with multiple value List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(3);
                                test.assertEqual(0, list.removeFirst());
                                test.assertEqual(1, list.removeFirst());
                                test.assertEqual(2, list.removeFirst());
                                test.assertNull(list.removeFirst());
                            }
                        });
                    }
                });

                runner.testGroup("removeFirst() with condition", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null condition and empty List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(0);
                                test.assertNull(list.removeFirst(null));
                            }
                        });

                        runner.test("with null condition and non-empty List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(4);
                                test.assertNull(list.removeFirst(null));
                            }
                        });

                        runner.test("with non-matching condition and empty List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(0);
                                test.assertNull(list.removeFirst(Math.isOdd));
                            }
                        });

                        runner.test("with non-matching condition and non-empty List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(1);
                                test.assertNull(list.removeFirst(Math.isOdd));
                            }
                        });

                        runner.test("with matching condition and non-empty List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(4);
                                test.assertEqual(1, list.removeFirst(Math.isOdd));
                                test.assertEqual(new int[] { 0, 2, 3 }, Array.toIntArray(list));
                            }
                        });

                        runner.test("with matching condition and single-value List", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Integer> list = createList.run(1);
                                test.assertEqual(0, list.removeFirst(Math.isEven));
                                test.assertFalse(list.any());
                            }
                        });
                    }
                });

                runner.test("clear()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final List<Integer> list = createList.run(0);
                        list.clear();
                        test.assertEqual(0, list.getCount());

                        for (int i = 0; i < 5; ++i)
                        {
                            list.add(i);
                        }
                        test.assertEqual(5, list.getCount());

                        list.clear();
                        test.assertEqual(0, list.getCount());
                    }
                });
            }
        });
    }
}
