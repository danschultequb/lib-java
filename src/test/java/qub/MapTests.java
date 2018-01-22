package qub;

public class MapTests
{
    public static void test(final TestRunner runner, final Function0<Map<Integer,Boolean>> creator)
    {
        runner.testGroup("Map<K,V>", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Map<Integer,Boolean> map = creator.run();
                        test.assertEqual(0, map.getCount());
                    }
                });
                
                runner.testGroup("get()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null non-existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                test.assertNull(map.get(null));
                            }
                        });
                        
                        runner.test("with non-null non-existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                test.assertNull(map.get(20));
                            }
                        });
                        
                        runner.test("with null existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(null, true);
                                test.assertTrue(map.get(null));
                            }
                        });
                        
                        runner.test("with non-null existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(100, false);
                                test.assertFalse(map.get(100));
                            }
                        });
                    }
                });
                
                runner.testGroup("set()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null non-existing key and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(null, null);
                                test.assertEqual(1, map.getCount());
                                test.assertNull(map.get(null));
                            }
                        });
                        
                        runner.test("with null non-existing key and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(null, false);
                                test.assertEqual(1, map.getCount());
                                test.assertEqual(false, map.get(null));
                            }
                        });
                        
                        runner.test("with null existing key and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(null, true);

                                map.set(null, null);
                                test.assertEqual(1, map.getCount());
                                test.assertNull(map.get(null));
                            }
                        });
                        
                        runner.test("with null existing key and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(null, true);

                                map.set(null, false);
                                test.assertEqual(1, map.getCount());
                                test.assertEqual(false, map.get(null));
                            }
                        });
                        
                        runner.test("with non-null non-existing key and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(12, null);
                                test.assertEqual(1, map.getCount());
                                test.assertNull(map.get(12));
                            }
                        });
                        
                        runner.test("with non-null non-existing key and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(13, false);
                                test.assertEqual(1, map.getCount());
                                test.assertEqual(false, map.get(13));
                            }
                        });
                        
                        runner.test("with non-null existing key and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(14, true);

                                map.set(14, null);
                                test.assertEqual(1, map.getCount());
                                test.assertNull(map.get(14));
                            }
                        });
                        
                        runner.test("with non-null existing key and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(15, true);

                                map.set(15, false);
                                test.assertEqual(1, map.getCount());
                                test.assertEqual(false, map.get(15));
                            }
                        });
                    }
                });
                
                runner.testGroup("remove()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null non-existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                test.assertFalse(map.remove(null));
                            }
                        });
                        
                        runner.test("with null existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(null, true);

                                test.assertTrue(map.remove(null));
                                test.assertEqual(0, map.getCount());
                            }
                        });
                        
                        runner.test("with non-null non-existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                test.assertFalse(map.remove(100));
                            }
                        });
                        
                        runner.test("with non-null existing key", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(101, false);

                                test.assertTrue(map.remove(101));
                                test.assertEqual(0, map.getCount());
                            }
                        });
                    }
                });
                
                runner.testGroup("getKeys()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty map", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                final Iterable<Integer> keys = map.getKeys();
                                test.assertNotNull(keys);
                                test.assertEqual(0, keys.getCount());
                            }
                        });
                        
                        runner.test("with non-empty map", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(0, false);
                                map.set(1, true);
                                final Iterable<Integer> keys = map.getKeys();
                                test.assertNotNull(keys);
                                test.assertEqual(2, keys.getCount());
                                test.assertEqual(new int[] { 0, 1 }, Array.toIntArray(keys));
                            }
                        });
                    }
                });

                runner.testGroup("getValues()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty map", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                final Iterable<Boolean> values = map.getValues();
                                test.assertNotNull(values);
                                test.assertEqual(0, values.getCount());
                            }
                        });

                        runner.test("with non-empty map", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(0, false);
                                map.set(1, true);
                                map.set(2, false);
                                final Iterable<Boolean> values = map.getValues();
                                test.assertNotNull(values);
                                test.assertEqual(3, values.getCount());
                                test.assertEqual(new boolean[] { false, true, false }, Array.toBooleanArray(values));
                            }
                        });
                    }
                });

                runner.testGroup("iterate()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty map", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                final Iterator<MapEntry<Integer,Boolean>> entries = map.iterate();
                                test.assertNotNull(entries);
                                test.assertEqual(0, entries.getCount());
                            }
                        });

                        runner.test("with non-empty map", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Map<Integer,Boolean> map = creator.run();
                                map.set(50, false);
                                final Iterator<MapEntry<Integer,Boolean>> entries = map.iterate();
                                test.assertNotNull(entries);
                                test.assertEqual(1, entries.getCount());

                                final MapEntry<Integer,Boolean> entry = map.iterate().first();
                                final Integer expectedValue = 50;
                                final Integer actualValue = entry.getKey();
                                test.assertEqual(expectedValue, actualValue);
                                test.assertEqual(false, entry.getValue());
                            }
                        });
                    }
                });
            }
        });
    }
}
