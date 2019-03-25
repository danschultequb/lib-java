package qub;

public class FixedUIWidthTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FixedUIWidth.class, () ->
        {
            runner.testGroup("constructor(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new FixedUIWidth(null), new PreConditionFailure("width cannot be null."));
                });

                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> new FixedUIWidth(Distance.centimeters(-1)), new PreConditionFailure("width (-1.0 Centimeters) must be greater than or equal to 0.0 Inches."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final FixedUIWidth width = new FixedUIWidth(Distance.zero);
                    test.assertEqual(Distance.zero, width.getWidth(null));
                });

                runner.test("with positive", (Test test) ->
                {
                    final FixedUIWidth width = new FixedUIWidth(Distance.inches(7));
                    test.assertEqual(Distance.inches(7), width.getWidth(null));
                });
            });
        });
    }
}
