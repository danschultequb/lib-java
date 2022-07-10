package qub;

public interface MutableMapTests
{
    public static void test(TestRunner runner, Function0<MutableMap<Integer,Boolean>> creator, boolean canHandleNullKeys, boolean canHandleNullValues)
    {
        runner.testGroup(MutableMap.class, () ->
        {
            runner.test("creator", test ->
            {
                final MutableMap<Integer,Boolean> map = creator.run();
                test.assertEqual(0, map.getCount());
            });

            runner.testGroup("get(TKey)", () ->
            {
                if (canHandleNullKeys)
                {
                    runner.test("with null non-existing key", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        test.assertThrows(() -> map.get(null).await(),
                            new NotFoundException("Could not find the provided key (null) in this Map."));
                    });

                    runner.test("with null existing key", test ->
                    {
                        final MutableMap<Integer,Boolean> map = creator.run();
                        test.assertSame(map, map.set(null, true));
                        test.assertTrue(map.get(null).await());
                    });
                }
                runner.test("with non-null non-existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertThrows(() -> map.get(20).await(),
                        new NotFoundException("Could not find the provided key (20) in this Map."));
                });

                runner.test("with non-null existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertSame(map, map.set(100, false));
                    test.assertFalse(map.get(100).await());
                });
            });

            runner.testGroup("set(TKey,TValue)", () ->
            {
                if (canHandleNullKeys)
                {
                    if (canHandleNullValues)
                    {
                        runner.test("with null non-existing key and null value", test ->
                        {
                            final MutableMap<Integer, Boolean> map = creator.run();
                            map.set(null, null);
                            test.assertEqual(1, map.getCount());
                            test.assertNull(map.get(null).await());
                        });

                        runner.test("with null existing key and null value", test ->
                        {
                            final MutableMap<Integer,Boolean> map = creator.run();
                            map.set(null, true);

                            map.set(null, null);
                            test.assertEqual(1, map.getCount());
                            test.assertNull(map.get(null).await());
                        });
                    }

                    runner.test("with null non-existing key and non-null value", test ->
                    {
                        final MutableMap<Integer,Boolean> map = creator.run();
                        map.set(null, false);
                        test.assertEqual(1, map.getCount());
                        test.assertFalse(map.get(null).await());
                    });

                    runner.test("with null existing key and non-null value", test ->
                    {
                        final MutableMap<Integer,Boolean> map = creator.run();
                        map.set(null, true);

                        map.set(null, false);
                        test.assertEqual(1, map.getCount());
                        test.assertFalse(map.get(null).await());
                    });
                }

                if (canHandleNullValues)
                {
                    runner.test("with non-null non-existing key and null value", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        map.set(12, null);
                        test.assertEqual(1, map.getCount());
                        test.assertNull(map.get(12).await());
                    });

                    runner.test("with non-null existing key and null value", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        map.set(14, true);

                        map.set(14, null);
                        test.assertEqual(1, map.getCount());
                        test.assertNull(map.get(14).await());
                    });
                }

                runner.test("with non-null non-existing key and non-null value", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(13, false);
                    test.assertEqual(1, map.getCount());
                    test.assertFalse(map.get(13).await());
                });

                runner.test("with non-null existing key and non-null value", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(15, true);

                    map.set(15, false);
                    test.assertEqual(1, map.getCount());
                    test.assertFalse(map.get(15).await());
                });

                runner.test("with negative key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    final MutableMap<Integer,Boolean> setResult = map.set(-15, true);
                    test.assertSame(map, setResult);
                    test.assertEqual(true, map.get(-15).await());
                });
            });

            runner.testGroup("remove(TKey)", () ->
            {
                if (canHandleNullKeys)
                {
                    runner.test("with null non-existing key", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        test.assertThrows(() -> map.remove(null).await(),
                            new NotFoundException("Could not find the provided key (null) in this Map."));
                    });

                    runner.test("with null existing key", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        test.assertSame(map, map.set(null, true));

                        test.assertTrue(map.remove(null).await());
                        test.assertEqual(0, map.getCount());
                    });
                }

                runner.test("with non-null non-existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertThrows(() -> map.remove(100).await(),
                        new NotFoundException("Could not find the provided key (100) in this Map."));
                });

                runner.test("with non-null existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertSame(map, map.set(101, false));

                    test.assertFalse(map.remove(101).await());
                    test.assertEqual(0, map.getCount());
                });
            });

            runner.testGroup("clear()", () ->
            {
                final Action1<MutableMap<Integer,Boolean>> clearTest = (MutableMap<Integer,Boolean> map) ->
                {
                    runner.test("with " + map, (Test test) ->
                    {
                        final MutableMap<Integer,Boolean> clearResult = map.clear();
                        test.assertSame(map, clearResult);
                        test.assertEqual(Map.create(), map);
                    });
                };

                clearTest.run(creator.run());
                clearTest.run(creator.run().set(1, false));
                clearTest.run(creator.run().set(1, false).set(2, true));
            });

            runner.testGroup("iterateKeys()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertEqual(Iterable.create(), map.iterateKeys().toList());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertSame(map, map.set(0, false));
                    test.assertSame(map, map.set(1, true));
                    test.assertEqual(Iterable.create(0, 1), map.iterateKeys().toList());
                });
            });

            runner.testGroup("iterateValues()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertEqual(Iterable.create(), map.iterateValues().toList());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(0, false);
                    map.set(1, true);
                    map.set(2, false);
                    test.assertEqual(Iterable.create(false, true, false), map.iterateValues().toList());
                });
            });

            runner.testGroup("iterate()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    final Iterator<MapEntry<Integer,Boolean>> entries = map.iterate();
                    test.assertNotNull(entries);
                    test.assertEqual(0, entries.getCount());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(50, false);
                    final Iterator<MapEntry<Integer,Boolean>> entries = map.iterate();
                    test.assertNotNull(entries);
                    test.assertEqual(1, entries.getCount());

                    final MapEntry<Integer,Boolean> entry = map.iterate().first().await();
                    final Integer expectedValue = 50;
                    final Integer actualValue = entry.getKey();
                    test.assertEqual(expectedValue, actualValue);
                    test.assertEqual(false, entry.getValue());
                });
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<MutableMap<Integer,Boolean>,String> toStringTest = (MutableMap<Integer,Boolean> map, String expected) ->
                {
                    runner.test("with " + map.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, map.toString());
                    });
                };

                toStringTest.run(creator.run(), "[]");
                toStringTest.run(creator.run().set(1, true), "[1:true]");
                toStringTest.run(creator.run().set(1, true).set(2, false), "[1:true,2:false]");
                toStringTest.run(creator.run().set(1, true).set(2, false).set(3, true), "[1:true,2:false,3:true]");
                toStringTest.run(creator.run().set(1, true).set(2, false).set(3, true).set(1, false), "[1:false,2:false,3:true]");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<MutableMap<Integer,Boolean>,Object,Boolean> equalsTest = (MutableMap<Integer,Boolean> map, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(map.toString(), Objects.toString(rhs)), (Test test) ->
                    {
                        test.assertEqual(expected, map.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(), null, false);
                equalsTest.run(creator.run(), "hello", false);
                equalsTest.run(creator.run(), creator.run(), true);
                equalsTest.run(creator.run(), Map.create(), true);
                equalsTest.run(creator.run().set(1, true), Map.create(), false);
                equalsTest.run(creator.run().set(1, true), Map.create().set(1, true), true);
                equalsTest.run(creator.run().set(1, true), Map.create().set(1, false), false);
                equalsTest.run(creator.run().set(1, true), Map.create().set(2, true), false);
                equalsTest.run(creator.run().set(1, true), Map.create().set(2, false), false);
                equalsTest.run(creator.run().set(1, true).set(2, false), Map.create().set(1, true).set(2, false), true);
                equalsTest.run(creator.run().set(1, true).set(2, false), Map.create().set(2, false).set(1, true), true);
            });

            runner.testGroup("equals(Map<TKey,TValue>)", () ->
            {
                final Action3<MutableMap<Integer,Boolean>,Map<Integer,Boolean>,Boolean> equalsTest = (MutableMap<Integer,Boolean> map, Map<Integer,Boolean> rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(map.toString(), Objects.toString(rhs)), (Test test) ->
                    {
                        test.assertEqual(expected, map.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(), null, false);
                equalsTest.run(creator.run(), creator.run(), true);
                equalsTest.run(creator.run(), Map.<Integer,Boolean>create(), true);
                equalsTest.run(creator.run().set(1, true), Map.<Integer,Boolean>create(), false);
                equalsTest.run(creator.run().set(1, true), Map.<Integer,Boolean>create().set(1, true), true);
                equalsTest.run(creator.run().set(1, true), Map.<Integer,Boolean>create().set(1, false), false);
                equalsTest.run(creator.run().set(1, true), Map.<Integer,Boolean>create().set(2, true), false);
                equalsTest.run(creator.run().set(1, true), Map.<Integer,Boolean>create().set(2, false), false);
                equalsTest.run(creator.run().set(1, true).set(2, false), Map.<Integer,Boolean>create().set(1, true).set(2, false), true);
                equalsTest.run(creator.run().set(1, true).set(2, false), Map.<Integer,Boolean>create().set(2, false).set(1, true), true);
            });
        });
    }
}
