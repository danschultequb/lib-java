package qub;

public interface MutableSetTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableSet.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableSet<Boolean> set = MutableSet.create();
                test.assertNotNull(set);
                test.assertEqual(Iterable.create(), set);
            });

            runner.test("create(T...)", (Test test) ->
            {
                final MutableSet<String> set = MutableSet.create("a", "b", "c");
                test.assertNotNull(set);
                test.assertEqual(Iterable.create("a", "b", "c"), set);
            });
        });
    }

    public static void test(TestRunner runner, Function1<Integer,? extends MutableSet<Integer>> creator)
    {
        runner.testGroup(MutableSet.class, () ->
        {
            Set2Tests.test(runner, creator);

            runner.testGroup("add()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);

                    set.add(null);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains((Integer)null));

                    set.add(null);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains((Integer)null));
                });

                runner.test("with single non-null value", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);

                    set.add(5);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains(5));

                    set.add(5);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains(5));
                });

                runner.test("with multiple non-null values", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);

                    set.add(5);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains(5));

                    set.add(6);
                    test.assertEqual(2, set.getCount());
                    test.assertTrue(set.contains(5));
                    test.assertTrue(set.contains(6));

                    test.assertFalse(set.contains(7));
                });
            });

            runner.testGroup("addAll(T...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll();
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with null array", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll((Integer[])null);
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(new Integer[0]);
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(1, 2, 3);
                    test.assertEqual(Iterable.create(1, 2, 3), set);
                });

                runner.test("with non-empty array with duplicates", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(1, 2, 2, 1);
                    test.assertEqual(Iterable.create(1, 2), set);
                });
            });

            runner.testGroup("addAll(Iterator<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertThrows(() -> set.addAll((Iterator<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertFalse(set.addAll(Iterator.create()));
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertTrue(set.addAll(Iterator.create(1, 2, 3)));
                    test.assertEqual(Iterable.create(1, 2, 3), set);
                });

                runner.test("with non-empty with duplicates", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertTrue(set.addAll(Iterator.create(1, 2, 2, 1)));
                    test.assertEqual(Iterable.create(1, 2), set);
                });
            });

            runner.testGroup("addAll(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertThrows(() -> set.addAll((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(Iterable.create());
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(IntegerArray.create(1, 2, 3));
                    test.assertEqual(IntegerArray.create(1, 2, 3), set);
                });

                runner.test("with non-empty with duplicates", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(IntegerArray.create(1, 2, 2, 1));
                    test.assertEqual(IntegerArray.create(1, 2), set);
                });
            });

            runner.testGroup("remove()", () ->
            {
                runner.test("with not-found null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertThrows(() -> set.remove(null).await(),
                        new NotFoundException("Could not find the value null."));
                });

                runner.test("with found null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.add(null);
                    final Integer removeResult = set.remove(null).await();
                    test.assertEqual(null, removeResult);
                    test.assertEqual(Iterable.create(), set);
                });

                runner.test("with not-found non-null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    test.assertThrows(() -> set.remove(20).await(),
                        new NotFoundException("Could not find the value 20."));
                });

                runner.test("with found non-null", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.add(20);
                    final Integer removeResult = set.remove(20).await();
                    test.assertEqual(20, removeResult);
                    test.assertEqual(Iterable.create(), set);
                });
            });

            runner.testGroup("clear()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.clear();
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableSet<Integer> set = creator.run(0);
                    set.addAll(1, 2, 3, 4);
                    test.assertEqual(4, set.getCount());
                    set.clear();
                    test.assertEqual(0, set.getCount());
                });
            });
        });
    }
}
