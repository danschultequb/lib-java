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
                        test.assertError(new NotFoundException("Could not find the provided key (null) in this Map."), map.get(null));
                    });

                    runner.test("with null existing key", test ->
                    {
                        final MutableMap<Integer,Boolean> map = creator.run();
                        map.set(null, true);
                        test.assertSuccess(true, map.get(null));
                    });
                }
                runner.test("with non-null non-existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertError(new NotFoundException("Could not find the provided key (20) in this Map."), map.get(20));
                });

                runner.test("with non-null existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(100, false);
                    test.assertSuccess(false, map.get(100));
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
                            test.assertSuccess(null, map.get(null));
                        });

                        runner.test("with null existing key and null value", test ->
                        {
                            final MutableMap<Integer,Boolean> map = creator.run();
                            map.set(null, true);

                            map.set(null, null);
                            test.assertEqual(1, map.getCount());
                            test.assertSuccess(null, map.get(null));
                        });
                    }

                    runner.test("with null non-existing key and non-null value", test ->
                    {
                        final MutableMap<Integer,Boolean> map = creator.run();
                        map.set(null, false);
                        test.assertEqual(1, map.getCount());
                        test.assertSuccess(false, map.get(null));
                    });

                    runner.test("with null existing key and non-null value", test ->
                    {
                        final MutableMap<Integer,Boolean> map = creator.run();
                        map.set(null, true);

                        map.set(null, false);
                        test.assertEqual(1, map.getCount());
                        test.assertSuccess(false, map.get(null));
                    });
                }

                if (canHandleNullValues)
                {
                    runner.test("with non-null non-existing key and null value", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        map.set(12, null);
                        test.assertEqual(1, map.getCount());
                        test.assertSuccess(null, map.get(12));
                    });

                    runner.test("with non-null existing key and null value", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        map.set(14, true);

                        map.set(14, null);
                        test.assertEqual(1, map.getCount());
                        test.assertSuccess(null, map.get(14));
                    });
                }

                runner.test("with non-null non-existing key and non-null value", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(13, false);
                    test.assertEqual(1, map.getCount());
                    test.assertSuccess(false, map.get(13));
                });

                runner.test("with non-null existing key and non-null value", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(15, true);

                    map.set(15, false);
                    test.assertEqual(1, map.getCount());
                    test.assertSuccess(false, map.get(15));
                });
            });

            runner.testGroup("remove()", () ->
            {
                if (canHandleNullKeys)
                {
                    runner.test("with null non-existing key", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        test.assertError(new NotFoundException("Could not find the provided key (null) in this Map."), map.remove(null));
                    });

                    runner.test("with null existing key", test ->
                    {
                        final MutableMap<Integer, Boolean> map = creator.run();
                        map.set(null, true);

                        test.assertSuccess(true, map.remove(null));
                        test.assertEqual(0, map.getCount());
                    });
                }

                runner.test("with non-null non-existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    test.assertError(new NotFoundException("Could not find the provided key (100) in this Map."), map.remove(100));
                });

                runner.test("with non-null existing key", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(101, false);

                    test.assertSuccess(false, map.remove(101));
                    test.assertEqual(0, map.getCount());
                });
            });

            runner.testGroup("getKeys()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    final Iterable<Integer> keys = map.getKeys();
                    test.assertNotNull(keys);
                    test.assertEqual(0, keys.getCount());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(0, false);
                    map.set(1, true);
                    final Iterable<Integer> keys = map.getKeys();
                    test.assertNotNull(keys);
                    test.assertEqual(2, keys.getCount());
                    test.assertEqual(new int[] { 0, 1 }, Array.toIntArray(keys));
                });
            });

            runner.testGroup("getValues()", () ->
            {
                runner.test("with empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    final Iterable<Boolean> values = map.getValues();
                    test.assertNotNull(values);
                    test.assertEqual(0, values.getCount());
                });

                runner.test("with non-empty map", test ->
                {
                    final MutableMap<Integer,Boolean> map = creator.run();
                    map.set(0, false);
                    map.set(1, true);
                    map.set(2, false);
                    final Iterable<Boolean> values = map.getValues();
                    test.assertNotNull(values);
                    test.assertEqual(3, values.getCount());
                    test.assertEqual(new boolean[] { false, true, false }, Array.toBooleanArray(values));
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
