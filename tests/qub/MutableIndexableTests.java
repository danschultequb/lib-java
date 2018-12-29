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
                    test.assertThrows(() -> indexable.set(0, 2), new PreConditionFailure("Cannot access values in an empty Indexable."));
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
                    test.assertThrows(() -> indexable.setFirst(20), new PreConditionFailure("Cannot access values in an empty Indexable."));
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
                    test.assertThrows(() -> indexable.setLast(20), new PreConditionFailure("Cannot access values in an empty Indexable."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableIndexable<Integer> indexable = creator.run(4);
                    indexable.setLast(20);
                    test.assertEqual(Array.create(0, 1, 2, 20), indexable);
                });
            });
        });
    }
}
