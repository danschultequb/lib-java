package qub;

public interface SetTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Set.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableSet<Boolean> set = Set.create();
                test.assertNotNull(set);
                test.assertEqual(Iterable.create(), set);
            });

            runner.test("create(T...)", (Test test) ->
            {
                final MutableSet<String> set = Set.create("a", "b", "c");
                test.assertNotNull(set);
                test.assertEqual(Iterable.create("a", "b", "c"), set);
            });
        });
    }

    public static void test(TestRunner runner, Function1<Integer,? extends Set<Integer>> creator)
    {
        runner.testGroup(Set.class, () ->
        {
            IterableTests.test(runner, creator);

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Set<Integer>,Object,Boolean> equalsTest = (Set<Integer> lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(3), null, false);
                equalsTest.run(creator.run(3), "foo", false);
                equalsTest.run(creator.run(3), Iterable.create(), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Iterable.create(2, 0, 1), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1, 2, 2), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1, 2, 3), false);
                equalsTest.run(creator.run(3), Set.create(), false);
                equalsTest.run(creator.run(3), Set.create(0, 1), false);
                equalsTest.run(creator.run(3), Set.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Set.create(2, 0, 1), true);
                equalsTest.run(creator.run(3), Set.create(0, 1, 2, 3), false);
            });

            runner.testGroup("equals(Iterable<T>)", () ->
            {
                final Action3<Set<Integer>,Iterable<Integer>,Boolean> equalsTest = (Set<Integer> lhs, Iterable<Integer> rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(3), null, false);
                equalsTest.run(creator.run(3), Iterable.create(), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Iterable.create(2, 0, 1), false);
                equalsTest.run(creator.run(3), Iterable.create(0, 1, 2, 2), false);
                equalsTest.run(creator.run(3), Iterable.create(1, 2, 3, 4), false);
                equalsTest.run(creator.run(3), Set.create(), false);
                equalsTest.run(creator.run(3), Set.create(0, 1), false);
                equalsTest.run(creator.run(3), Set.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Set.create(2, 0, 1), true);
                equalsTest.run(creator.run(3), Set.create(0, 1, 2, 3), false);
            });

            runner.testGroup("equals(Set<T>)", () ->
            {
                final Action3<Set<Integer>, Set<Integer>,Boolean> equalsTest = (Set<Integer> lhs, Set<Integer> rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(3), null, false);
                equalsTest.run(creator.run(3), Set.create(), false);
                equalsTest.run(creator.run(3), Set.create(0, 1), false);
                equalsTest.run(creator.run(3), Set.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Set.create(2, 0, 1), true);
                equalsTest.run(creator.run(3), Set.create(0, 1, 2, 3), false);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Set<Integer>,String> toStringTest = (Set<Integer> set, String expected) ->
                {
                    runner.test("with " + set, (Test test) ->
                    {
                        test.assertEqual(expected, set.toString());
                    });
                };

                toStringTest.run(Set.create(), "{}");
                toStringTest.run(Set.create(1), "{1}");
                toStringTest.run(Set.create(1,3), "{1,3}");
                toStringTest.run(Set.create(1,3,2), "{1,3,2}");
            });
        });
    }
}
