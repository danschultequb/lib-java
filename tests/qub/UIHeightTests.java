package qub;

public class UIHeightTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIHeight.class, () ->
        {
            runner.testGroup("fixed(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> UIHeight.fixed(null), new PreConditionFailure("height cannot be null."));
                });

                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> UIHeight.fixed(Distance.centimeters(-1)), new PreConditionFailure("height (-1.0 Centimeters) must be greater than or equal to 0.0 Inches."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final UIHeight height = UIHeight.fixed(Distance.zero);
                    test.assertEqual(Distance.zero, height.getHeight(null));
                });

                runner.test("with positive", (Test test) ->
                {
                    final UIHeight height = UIHeight.fixed(Distance.inches(7));
                    test.assertEqual(Distance.inches(7), height.getHeight(null));
                });
            });
        });
    }
}
