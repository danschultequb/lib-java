package qub;

public class DisplayTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Display.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Display display = new Display(1, 2, 3, 4, 5, 6);
                test.assertEqual(1, display.getWidthInPixels());
                test.assertEqual(2, display.getHeightInPixels());
                test.assertEqual(3, display.getHorizontalDpi());
                test.assertEqual(4, display.getVerticalDpi());
                test.assertEqual(5, display.getHorizontalScale());
                test.assertEqual(6, display.getVerticalScale());
            });

            runner.test("getWidth()", (Test test) ->
            {
                final Display display = new Display(100, 200, 20, 25, 1, 1);
                test.assertEqual(Distance.inches(5), display.getWidth());
            });

            runner.test("getWidth()", (Test test) ->
            {
                final Display display = new Display(100, 200, 20, 25, 1, 1);
                test.assertEqual(Distance.inches(8), display.getHeight());
            });
        });
    }
}
