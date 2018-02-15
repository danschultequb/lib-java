package qub;

public class JavaStopwatchTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JavaStopwatch", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("stop()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no wait after start()", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final JavaStopwatch stopwatch = new JavaStopwatch();
                                stopwatch.start();
                                final Duration duration = stopwatch.stop();
                                test.assertNotNull(duration);
                                test.assertEqual(DurationUnits.Nanoseconds, duration.getUnits());
                                test.assertEqual(0, duration.getValue(), 1000000);
                            }
                        });

                        runner.test("with no start()", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final JavaStopwatch stopwatch = new JavaStopwatch();
                                test.assertNull(stopwatch.stop());
                            }
                        });
                    }
                });
            }
        });
    }
}
