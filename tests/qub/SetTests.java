package qub;

public class SetTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Set.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                test.assertEqual(Iterable.empty(), Set.create());
            });

            runner.test("create(T...)", (Test test) ->
            {
                test.assertEqual(Iterable.create(1, 2, 3), Set.create(1, 2, 3));
            });
        });
    }

    public static void test(TestRunner runner, Function0<Set<Integer>> creator)
    {
        runner.testGroup(Set.class, () ->
        {
            IterableTests.test(runner, (Integer count) ->
            {
                final Set<Integer> set = creator.run();
                for (int i = 0; i < count; ++i)
                {
                    set.add(i);
                }
                return set;
            });

            runner.testGroup("add()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();

                    set.add(null);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains((Integer)null));

                    set.add(null);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains((Integer)null));
                });

                runner.test("with single non-null value", (Test test) ->
                {
                    final Set<Integer> set = creator.run();

                    set.add(5);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains(5));

                    set.add(5);
                    test.assertEqual(1, set.getCount());
                    test.assertTrue(set.contains(5));
                });

                runner.test("with multiple non-null values", (Test test) ->
                {
                    final Set<Integer> set = creator.run();

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
                    final Set<Integer> set = creator.run();
                    set.addAll();
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with null array", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll((Integer[])null);
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(new Integer[0]);
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(1, 2, 3);
                    test.assertEqual(Iterable.create(1, 2, 3), set);
                });

                runner.test("with non-empty array with duplicates", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(1, 2, 2, 1);
                    test.assertEqual(Iterable.create(1, 2), set);
                });
            });

            runner.testGroup("addAll(Iterator<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll((Iterator<Integer>)null);
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(Iterator.empty());
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(Iterator.create(1, 2, 3));
                    test.assertEqual(Iterable.create(1, 2, 3), set);
                });

                runner.test("with non-empty with duplicates", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(Iterator.create(1, 2, 2, 1));
                    test.assertEqual(Iterable.create(1, 2), set);
                });
            });

            runner.testGroup("addAll(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll((Iterable<Integer>)null);
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(Iterable.empty());
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(IntegerArray.create(1, 2, 3));
                    test.assertEqual(IntegerArray.create(1, 2, 3), set);
                });

                runner.test("with non-empty with duplicates", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(IntegerArray.create(1, 2, 2, 1));
                    test.assertEqual(IntegerArray.create(1, 2), set);
                });
            });

            runner.testGroup("remove()", () ->
            {
                runner.test("with not-found null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    test.assertError(new NotFoundException("Could not find the value null."), set.remove(null));
                });

                runner.test("with found null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.add(null);
                    test.assertSuccess(null, set.remove(null));
                    test.assertEqual(Iterable.empty(), set);
                });

                runner.test("with not-found non-null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    test.assertError(new NotFoundException("Could not find the value 20."), set.remove(20));
                });

                runner.test("with found non-null", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.add(20);
                    test.assertSuccess(null, set.remove(20));
                    test.assertEqual(Iterable.empty(), set);
                });
            });

            runner.testGroup("clear()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.clear();
                    test.assertEqual(0, set.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Set<Integer> set = creator.run();
                    set.addAll(1, 2, 3, 4);
                    test.assertEqual(4, set.getCount());
                    set.clear();
                    test.assertEqual(0, set.getCount());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)null));
                });

                runner.test("with non-Set or Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)"foo"));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)Iterable.empty()));
                });

                runner.test("with subset Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)IntegerArray.create(1, 2)));
                });

                runner.test("with equal Iterable", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals((Object)IntegerArray.create(1, 2, 3)));
                });

                runner.test("with equal Iterable in different order", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)IntegerArray.create(3, 1, 2)));
                });

                runner.test("with equal Iterable with duplicates", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)IntegerArray.create(1, 2, 3, 3)));
                });

                runner.test("with superset Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)IntegerArray.create(1, 2, 3, 4)));
                });

                runner.test("with empty Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)Set.empty()));
                });

                runner.test("with subset Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)Set.create(1, 2)));
                });

                runner.test("with equal Set", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals((Object)Set.create(1, 2, 3)));
                });

                runner.test("with equal Set in different order", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals((Object)Set.create(3, 1, 2)));
                });

                runner.test("with superset Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Object)Set.create(1, 2, 3, 4)));
                });
            });

            runner.testGroup("equals(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)null));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)Iterable.<Integer>empty()));
                });

                runner.test("with subset Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)IntegerArray.create(1, 2)));
                });

                runner.test("with equal Iterable", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)IntegerArray.create(1, 2, 3)));
                });

                runner.test("with equal Iterable in different order", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)IntegerArray.create(3, 1, 2)));
                });

                runner.test("with equal Iterable with duplicates", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)IntegerArray.create(1, 2, 3, 3)));
                });

                runner.test("with superset Iterable", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)IntegerArray.create(1, 2, 3, 4)));
                });

                runner.test("with empty Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)Set.<Integer>empty()));
                });

                runner.test("with subset Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)Set.create(1, 2)));
                });

                runner.test("with equal Set", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)Set.create(1, 2, 3)));
                });

                runner.test("with equal Set in different order", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)Set.create(3, 1, 2)));
                });

                runner.test("with superset Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Iterable<Integer>)Set.create(1, 2, 3, 4)));
                });
            });

            runner.testGroup("equals(Set<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals((Set<Integer>)null));
                });

                runner.test("with empty Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals(Set.empty()));
                });

                runner.test("with subset Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals(Set.create(1, 2)));
                });

                runner.test("with equal Set", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals(Set.create(1, 2, 3)));
                });

                runner.test("with equal Set in different order", (Test test) ->
                {
                    test.assertTrue(creator.run().addAll(1, 2, 3).equals(Set.create(3, 1, 2)));
                });

                runner.test("with superset Set", (Test test) ->
                {
                    test.assertFalse(creator.run().addAll(1, 2, 3).equals(Set.create(1, 2, 3, 4)));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("{}", Set.empty().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("{1}", creator.run().addAll(1).toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("{1,2}", creator.run().addAll(1, 2).toString());
                });

                runner.test("with three values", (Test test) ->
                {
                    test.assertEqual("{1,2,3}", creator.run().addAll(1, 2, 3).toString());
                });
            });
        });
    }
}
