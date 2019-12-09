package qub;

public class MutableIndexableTests
{
    public static void test(TestRunner runner, Function1<Integer,MutableIndexable<Integer>> creator)
    {
        runner.testGroup(MutableIndexable.class, () ->
        {
            IndexableTests.test(runner, creator::run);

            runner.testGroup("set(int,T)", () ->
            {
                runner.test("with 0 index on an empty MutableIndexable", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(0);
                    test.assertThrows(() -> indexable.set(0, 2), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with negative index", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(5);
                    test.assertThrows(() -> indexable.set(-1, 300), new PreConditionFailure("index (-1) must be between 0 and 4."));
                });

                runner.test("with first index", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(5);
                    indexable.set(0, 300);
                    test.assertEqual(Array.create(300, 1, 2, 3, 4), indexable);
                });

                runner.test("with middle index", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(5);
                    indexable.set(2, 300);
                    test.assertEqual(Array.create(0, 1, 300, 3, 4), indexable);
                });

                runner.test("with last index", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(5);
                    indexable.set(4, 300);
                    test.assertEqual(Array.create(0, 1, 2, 3, 300), indexable);
                });

                runner.test("with after last index", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(5);
                    test.assertThrows(() -> indexable.set(5, 300), new PreConditionFailure("index (5) must be between 0 and 4."));
                });
            });

            runner.testGroup("setFirst(T)", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(0);
                    test.assertThrows(() -> indexable.setFirst(20), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(2);
                    indexable.setFirst(20);
                    test.assertEqual(Array.create(20, 1), indexable);
                });
            });

            runner.testGroup("setLast(T)", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(0);
                    test.assertThrows(() -> indexable.setLast(20), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(4);
                    indexable.setLast(20);
                    test.assertEqual(Array.create(0, 1, 2, 20), indexable);
                });
            });

            runner.testGroup("sort(MutableIndexable<T>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> MutableIndexable.sort((MutableIndexable<Distance>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create();
                    test.assertSame(values, MutableIndexable.sort(values));
                });

                runner.test("with one value", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(1));
                    test.assertSame(values, MutableIndexable.sort(values));
                    test.assertEqual(Iterable.create(Distance.feet(1)), values);
                });

                runner.test("with two values in sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(1), Distance.inches(13));
                    test.assertSame(values, MutableIndexable.sort(values));
                    test.assertEqual(Iterable.create(Distance.feet(1), Distance.inches(13)), values);
                });

                runner.test("with two values in reverse-sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(10), Distance.inches(1));
                    test.assertSame(values, MutableIndexable.sort(values));
                    test.assertEqual(Iterable.create(Distance.inches(1), Distance.feet(10)), values);
                });

                runner.test("with three values in sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(1), Distance.inches(13), Distance.miles(0.1));
                    test.assertSame(values, MutableIndexable.sort(values));
                    test.assertEqual(Iterable.create(Distance.feet(1), Distance.inches(13), Distance.miles(0.1)), values);
                });

                runner.test("with three values in reverse-sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.miles(0.5), Distance.feet(10), Distance.inches(1));
                    test.assertSame(values, MutableIndexable.sort(values));
                    test.assertEqual(Iterable.create(Distance.inches(1), Distance.feet(10), Distance.miles(0.5)), values);
                });

                runner.test("with three values in mixed-sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(10), Distance.miles(2), Distance.inches(1));
                    test.assertSame(values, MutableIndexable.sort(values));
                    test.assertEqual(Iterable.create(Distance.inches(1), Distance.feet(10), Distance.miles(2)), values);
                });
            });

            runner.testGroup("sort(Function2<T,T,Boolean>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    final MutableIndexable<Integer> values = Array.create();
                    test.assertThrows(() -> values.sort((Function2<Integer,Integer,Boolean>)null), new PreConditionFailure("lessThan cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create();
                    test.assertSame(values, values.sort(Comparer::lessThan));
                });

                runner.test("with one value", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(1));
                    test.assertSame(values, values.sort(Comparer::lessThan));
                    test.assertEqual(Iterable.create(Distance.feet(1)), values);
                });

                runner.test("with two values in sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(1), Distance.inches(13));
                    test.assertSame(values, values.sort(Comparer::lessThan));
                    test.assertEqual(Iterable.create(Distance.feet(1), Distance.inches(13)), values);
                });

                runner.test("with two values in reverse-sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(10), Distance.inches(1));
                    test.assertSame(values, values.sort(Comparer::lessThan));
                    test.assertEqual(Iterable.create(Distance.inches(1), Distance.feet(10)), values);
                });

                runner.test("with three values in sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(1), Distance.inches(13), Distance.miles(0.1));
                    test.assertSame(values, values.sort(Comparer::lessThan));
                    test.assertEqual(Iterable.create(Distance.feet(1), Distance.inches(13), Distance.miles(0.1)), values);
                });

                runner.test("with three values in reverse-sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.miles(0.5), Distance.feet(10), Distance.inches(1));
                    test.assertSame(values, values.sort(Comparer::lessThan));
                    test.assertEqual(Iterable.create(Distance.inches(1), Distance.feet(10), Distance.miles(0.5)), values);
                });

                runner.test("with three values in mixed-sorted order", (Test test) ->
                {
                    final MutableIndexable<Distance> values = Array.create(Distance.feet(10), Distance.miles(2), Distance.inches(1));
                    test.assertSame(values, values.sort(Comparer::lessThan));
                    test.assertEqual(Iterable.create(Distance.inches(1), Distance.feet(10), Distance.miles(2)), values);
                });
            });
        });
    }
}
