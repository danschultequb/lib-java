package qub;

public class JavaStopwatchTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaStopwatch.class, () ->
        {
            runner.testGroup("stop()", () ->
            {
                runner.test("with no wait after start()", (Test test) ->
                {
                    final JavaStopwatch stopwatch = new JavaStopwatch();
                    stopwatch.start();
                    final Duration duration = stopwatch.stop();
                    test.assertNotNull(duration);
                    test.assertEqual(DurationUnit.Nanoseconds, duration.getUnits());
                    test.assertEqual(0, duration.getValue(), 1000000);
                });

                runner.test("with no start()", (Test test) ->
                {
                    final JavaStopwatch stopwatch = new JavaStopwatch();
                    test.assertNull(stopwatch.stop());
                });
            });
        });
    }
}
