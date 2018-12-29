package qub;

public class AlgorithmsTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Algorithms.class, () ->
        {
            runner.testGroup("traverse()", () ->
            {
                final Function1<Integer,Iterable<Integer>> getNextValues = (Integer value) ->
                {
                    final List<Integer> nextValues = List.create();
                    if (value + 2 <= 20)
                    {
                        nextValues.add(value + 2);
                    }
                    if (value * 2 <= 20)
                    {
                        nextValues.add(value * 2);
                    }
                    return nextValues;
                };

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Algorithms.traverse(null, getNextValues), new PreConditionFailure("startValues cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(Iterable.empty(), Algorithms.traverse(Iterable.empty(), getNextValues));
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    test.assertEqual(Array.create(2, 4, 8, 16, 18, 20, 10, 12, 14, 6), Algorithms.traverse(Array.create(2), getNextValues));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Algorithms.traverse(Array.create(2), null), new PreConditionFailure("getNextValues cannot be null."));
                });
            });
        });
    }
}
