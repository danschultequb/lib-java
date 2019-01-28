package qub;

public class SetTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Set.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final Set<Integer> set = Set.create();
                test.assertNotNull(set);
                test.assertEqual(0, set.getCount());
            });

            runner.test("create(T...)", (Test test) ->
            {
                final Set<Integer> set = Set.create(1, 2, 3);
                test.assertEqual(Iterable.create(1, 2, 3), set);
            });
        });
    }
    public static void test(TestRunner runner, Function0<Set<Integer>> creator)
    {
        runner.testGroup(Set.class, () ->
        {
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
        });
    }
}
