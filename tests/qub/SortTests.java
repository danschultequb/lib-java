package qub;

public interface SortTests
{
    public static void test(TestRunner runner, Function0<? extends Sort> creator)
    {
        runner.testGroup(Sort.class, () ->
        {
            runner.testGroup("sort(MutableIndexable<T>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Sort sort = creator.run();
                    test.assertThrows(() -> sort.sort(null),
                        new PreConditionFailure("values cannot be null."));
                });

                final Action2<MutableIndexable<Integer>,Iterable<Integer>> sortTest = (MutableIndexable<Integer> values, Iterable<Integer> expected) ->
                {
                    runner.test("with " + values.toString(), (Test test) ->
                    {
                        final Sort sort = creator.run();
                        sort.sort(values);
                        test.assertEqual(expected, values);
                    });
                };

                sortTest.run(
                    MutableIndexable.create(),
                    Iterable.create());
                sortTest.run(
                    MutableIndexable.create(1),
                    Iterable.create(1));
                sortTest.run(
                    MutableIndexable.create(1, 2),
                    Iterable.create(1, 2));
                sortTest.run(
                    MutableIndexable.create(2, 1),
                    Iterable.create(1, 2));
                sortTest.run(
                    MutableIndexable.create(1, 2, 3),
                    Iterable.create(1, 2, 3));
                sortTest.run(
                    MutableIndexable.create(1, 3, 2),
                    Iterable.create(1, 2, 3));
                sortTest.run(
                    MutableIndexable.create(2, 1, 3),
                    Iterable.create(1, 2, 3));
                sortTest.run(
                    MutableIndexable.create(2, 3, 1),
                    Iterable.create(1, 2, 3));
                sortTest.run(
                    MutableIndexable.create(3, 1, 2),
                    Iterable.create(1, 2, 3));
                sortTest.run(
                    MutableIndexable.create(3, 2, 1),
                    Iterable.create(1, 2, 3));
                sortTest.run(
                    MutableIndexable.create(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    Iterable.create(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                sortTest.run(
                    MutableIndexable.create(5, 10, 7, 3, 1, 2, 4, 6, 8, 9),
                    Iterable.create(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                sortTest.run(
                    MutableIndexable.create(10, 9, 8, 7, 6, 5, 4, 3, 2, 1),
                    Iterable.create(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                sortTest.run(
                    MutableIndexable.create(1, 1, 1, 1),
                    Iterable.create(1, 1, 1, 1));
            });
        });
    }
}
