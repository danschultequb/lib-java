package qub;

public class MutableMapTests
{
    public static void test(TestRunner runner, Function0<MutableMap<Integer,Boolean>> creator, boolean canHandleNullKeys, boolean canHandleNullValues)
    {
        runner.testGroup(MutableMap.class, () ->
        {
            runner.test("constructor()", test ->
            {
                final MutableMap<Integer,Boolean> map = creator.run();
                test.assertEqual(0, map.getCount());
            });

            runner.testGroup("get()", () ->
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

            runner.testGroup("set()", () ->
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
            });

            runner.testGroup("remove()", () ->
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

            runner.testGroup("getKeys()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertEqual(Iterable.create(), map.getKeys());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertSame(map, map.set(0, false));
                    test.assertSame(map, map.set(1, true));
                    test.assertEqual(Iterable.create(0, 1), map.getKeys());
                });
            });

            runner.testGroup("getValues()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertEqual(Iterable.create(), map.getValues());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(0, false);
                    map.set(1, true);
                    map.set(2, false);
                    test.assertEqual(Iterable.create(false, true, false), map.getValues());
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

                    final MapEntry<Integer,Boolean> entry = map.iterate().first();
                    final Integer expectedValue = 50;
                    final Integer actualValue = entry.getKey();
                    test.assertEqual(expectedValue, actualValue);
                    test.assertEqual(false, entry.getValue());
                });
            });
        });
    }
}
