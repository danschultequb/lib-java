package qub;

public class FixedUIHeightTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FixedUIHeight.class, () ->
        {
            runner.testGroup("constructor(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new FixedUIHeight(null));
                });

                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> new FixedUIHeight(Distance.centimeters(-1)));
                });

                runner.test("with zero", (Test test) ->
                {
                    final FixedUIHeight height = new FixedUIHeight(Distance.zero);
                    test.assertEqual(Distance.zero, height.getHeight(null));
                });

                runner.test("with positive", (Test test) ->
                {
                    final FixedUIHeight height = new FixedUIHeight(Distance.inches(7));
                    test.assertEqual(Distance.inches(7), height.getHeight(null));
                });
            });
        });
    }
}
