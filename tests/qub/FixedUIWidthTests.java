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
                    test.assertThrows(() -> new FixedUIWidth(null));
                });

                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> new FixedUIWidth(Distance.centimeters(-1)));
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
