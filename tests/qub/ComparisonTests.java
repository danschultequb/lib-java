package qub;

public interface ComparisonTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup("from(double)", () ->
        {
            final Action2<Double,Comparison> fromTest = (Double differenceValue, Comparison expected) ->
            {
                runner.test("with " + differenceValue, (Test test) ->
                {
                    test.assertEqual(expected, Comparison.from(differenceValue));
                });
            };

            fromTest.run(-1.0, Comparison.LessThan);
            fromTest.run(-0.001, Comparison.LessThan);
            fromTest.run(0.0, Comparison.Equal);
            fromTest.run(0.000001, Comparison.GreaterThan);
            fromTest.run(10.0, Comparison.GreaterThan);
        });

        runner.testGroup("from(double,double)", () ->
        {
            runner.test("with negative margin of error", (Test test) ->
            {
                test.assertThrows(() -> Comparison.from(5, -0.001),
                    new PreConditionFailure("marginOfError (-0.001) must be greater than or equal to 0.0."));
            });

            final Action3<Double,Double,Comparison> fromTest = (Double differenceValue, Double marginOfError, Comparison expected) ->
            {
                runner.test("with " + differenceValue + " and " + marginOfError + " margin of error", (Test test) ->
                {
                    test.assertEqual(expected, Comparison.from(differenceValue, marginOfError));
                });
            };

            fromTest.run(-1.0, 0.0, Comparison.LessThan);
            fromTest.run(-0.001, 0.0, Comparison.LessThan);
            fromTest.run(0.0, 0.0, Comparison.Equal);
            fromTest.run(0.000001, 0.0, Comparison.GreaterThan);
            fromTest.run(10.0, 0.0, Comparison.GreaterThan);

            fromTest.run(-1.0, 0.01, Comparison.LessThan);
            fromTest.run(-0.001, 0.01, Comparison.Equal);
            fromTest.run(0.0, 0.01, Comparison.Equal);
            fromTest.run(0.000001, 0.01, Comparison.Equal);
            fromTest.run(10.0, 0.01, Comparison.GreaterThan);
        });
    }
}
