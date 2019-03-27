package qub;

public class UIWidthTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIWidth.class, () ->
        {
            runner.testGroup("fixed(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> UIWidth.fixed(null), new PreConditionFailure("width cannot be null."));
                });

                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> UIWidth.fixed(Distance.centimeters(-1)), new PreConditionFailure("width (-1.0 Centimeters) must be greater than or equal to 0.0 Inches."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final UIWidth width = UIWidth.fixed(Distance.zero);
                    test.assertEqual(Distance.zero, width.getWidth(null));
                });

                runner.test("with positive", (Test test) ->
                {
                    final UIWidth width = UIWidth.fixed(Distance.inches(7));
                    test.assertEqual(Distance.inches(7), width.getWidth(null));
                });
            });
        });
    }
}
