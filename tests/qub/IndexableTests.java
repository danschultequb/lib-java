package qub;

public class IndexableTests
{
    public static void test(TestRunner runner, Function1<Integer,Indexable<Integer>> createIndexable)
    {
        runner.testGroup(Indexable.class, () ->
        {
            IterableTests.test(runner, createIndexable::run);

            runner.testGroup("get()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(0);
                    test.assertThrows(() -> indexable.get(-5), new PreConditionFailure("Cannot access values in an empty Indexable."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(2);
                    test.assertEqual(0, indexable.get(0));
                });

                runner.test("with index less than Indexable count", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(10);
                    test.assertEqual(7, indexable.get(7));
                });

                runner.test("with index equal to Indexable count", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(3);
                    test.assertThrows(() -> indexable.get(3));
                });

                runner.test("with index greater than Indexable count", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(3);
                    test.assertThrows(() -> indexable.get(4));
                });
            });

            runner.testGroup("indexOf()", () ->
            {
                runner.test("with empty Indexable and null condition", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(0);
                    test.assertThrows(() -> indexable.indexOf((Function1<Integer,Boolean>)null));
                });

                runner.test("with empty Indexable and non-null condition", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(0);
                    test.assertEqual(-1, indexable.indexOf(Math.isOdd));
                });

                runner.test("with non-empty Indexable and null condition", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(1);
                    test.assertThrows(() -> indexable.indexOf((Function1<Integer,Boolean>)null));
                });

                runner.test("with non-empty Indexable and non-matching condition", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(1);
                    test.assertEqual(-1, indexable.indexOf(Math.isOdd));
                });

                runner.test("with non-empty Indexable and matching condition", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(7);
                    test.assertEqual(1, indexable.indexOf(Math.isOdd));
                });

                runner.test("with non-empty Indexable and null value", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(2);
                    test.assertEqual(-1, indexable.indexOf((Integer)null));
                });

                runner.test("with non-empty Indexable and not found value", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(2);
                    test.assertEqual(-1, indexable.indexOf(20));
                });

                runner.test("with non-empty Indexable and found value", (Test test) ->
                {
                    final Indexable<Integer> indexable = createIndexable.run(10);
                    test.assertEqual(4, indexable.indexOf(4));
                });
            });
        });
    }
}
