package qub;

public interface Set2Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Set2.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableSet<Boolean> set = Set2.create();
                test.assertNotNull(set);
                test.assertEqual(Iterable.create(), set);
            });

            runner.test("create(T...)", (Test test) ->
            {
                final MutableSet<String> set = Set2.create("a", "b", "c");
                test.assertNotNull(set);
                test.assertEqual(Iterable.create("a", "b", "c"), set);
            });
        });
    }

    public static void test(TestRunner runner, Function1<Integer,? extends Set2<Integer>> creator)
    {
        runner.testGroup(Set2.class, () ->
        {
            IterableTests.test(runner, creator);

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Set2<Integer>,Object,Boolean> equalsTest = (Set2<Integer> lhs, Object rhs, Boolean expected) ->
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
                equalsTest.run(creator.run(3), Set2.create(), false);
                equalsTest.run(creator.run(3), Set2.create(0, 1), false);
                equalsTest.run(creator.run(3), Set2.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Set2.create(2, 0, 1), true);
                equalsTest.run(creator.run(3), Set2.create(0, 1, 2, 3), false);
            });

            runner.testGroup("equals(Iterable<T>)", () ->
            {
                final Action3<Set2<Integer>,Iterable<Integer>,Boolean> equalsTest = (Set2<Integer> lhs, Iterable<Integer> rhs, Boolean expected) ->
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
                equalsTest.run(creator.run(3), Set2.create(), false);
                equalsTest.run(creator.run(3), Set2.create(0, 1), false);
                equalsTest.run(creator.run(3), Set2.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Set2.create(2, 0, 1), true);
                equalsTest.run(creator.run(3), Set2.create(0, 1, 2, 3), false);
            });

            runner.testGroup("equals(Set<T>)", () ->
            {
                final Action3<Set2<Integer>,Set2<Integer>,Boolean> equalsTest = (Set2<Integer> lhs, Set2<Integer> rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(3), null, false);
                equalsTest.run(creator.run(3), Set2.create(), false);
                equalsTest.run(creator.run(3), Set2.create(0, 1), false);
                equalsTest.run(creator.run(3), Set2.create(0, 1, 2), true);
                equalsTest.run(creator.run(3), Set2.create(2, 0, 1), true);
                equalsTest.run(creator.run(3), Set2.create(0, 1, 2, 3), false);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Set2<Integer>,String> toStringTest = (Set2<Integer> set, String expected) ->
                {
                    runner.test("with " + set, (Test test) ->
                    {
                        test.assertEqual(expected, set.toString());
                    });
                };

                toStringTest.run(Set2.create(), "{}");
                toStringTest.run(Set2.create(1), "{1}");
                toStringTest.run(Set2.create(1,3), "{1,3}");
                toStringTest.run(Set2.create(1,3,2), "{1,3,2}");
            });
        });
    }
}
