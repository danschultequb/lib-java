package qub;

public class SortTests
{
    public static void test(TestRunner runner, Function0<Sort> creator)
    {
        runner.testGroup(Sort.class, () ->
        {
            runner.testGroup("sort(MutableIndexable<T>, Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null indexable", (Test test) ->
                {
                    final Sort sort = creator.run();
                    sort.sort((List<Integer>)null, Integers::compare);
                });

                runner.test("with null comparer", (Test test) ->
                {
                    final Sort sort = creator.run();
                    final Array<Integer> values = Array.create(new Integer[] { 3, 2, 1 });
                    sort.sort(values, null);
                    test.assertEqual(Array.create(new Integer[] { 3, 2, 1 }), values);
                });

                final Action2<int[],int[]> sortTest = (int[] values, int[] expectedSortedValues) ->
                {
                    final Array<Integer> valuesArray = Array.create(values);
                    runner.test("with " + valuesArray, (Test test) ->
                    {
                        final Sort sort = creator.run();
                        sort.sort(valuesArray, Integers::compare);
                        test.assertEqual(Array.create(expectedSortedValues), valuesArray);
                    });
                };

                sortTest.run(new int[0], new int[0]);
                sortTest.run(new int[] { 15 }, new int[] { 15 });
                sortTest.run(new int[] { 1, 2 }, new int[] { 1, 2 });
                sortTest.run(new int[] { 2, 1 }, new int[] { 1, 2 });
                sortTest.run(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });
                sortTest.run(new int[] { 3, 2, 1 }, new int[] { 1, 2, 3 });
                sortTest.run(new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
            });

            runner.testGroup("sort(List<T extends Comparable<T>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Sort sort = creator.run();
                    sort.sort(null);
                });

                final Action2<DateTime[],DateTime[]> sortTest = (DateTime[] values, DateTime[] expectedSortedValues) ->
                {
                    final Array<DateTime> valuesArray = Array.create(values);
                    runner.test("with " + valuesArray, (Test test) ->
                    {
                        final Sort sort = creator.run();
                        sort.sort(valuesArray);
                        test.assertEqual(Array.create(expectedSortedValues), valuesArray);
                    });
                };

                sortTest.run(new DateTime[0], new DateTime[0]);
                sortTest.run(new DateTime[] { DateTime.utc(15) }, new DateTime[] { DateTime.utc(15) });
                sortTest.run(new DateTime[] { DateTime.utc(1), DateTime.utc(2) }, new DateTime[] { DateTime.utc(1), DateTime.utc(2) });
                sortTest.run(new DateTime[] { DateTime.utc(2), DateTime.utc(1) }, new DateTime[] { DateTime.utc(1), DateTime.utc(2) });
                sortTest.run(new DateTime[] { DateTime.utc(1), DateTime.utc(2), DateTime.utc(3) }, new DateTime[] { DateTime.utc(1), DateTime.utc(2), DateTime.utc(3) });
                sortTest.run(new DateTime[] { DateTime.utc(3), DateTime.utc(2), DateTime.utc(1) }, new DateTime[] { DateTime.utc(1), DateTime.utc(2), DateTime.utc(3) });
            });
        });
    }
}
