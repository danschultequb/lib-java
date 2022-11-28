package qub;

public interface JavaClockTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaClock.class,
            (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
            (AsyncRunner parallelAsyncRunner) ->
        {
            ClockTests.test(runner, () -> JavaClock.create(parallelAsyncRunner));

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                runner.test("with positive Duration", (Test test) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final IntegerValue counter = IntegerValue.create(0);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final Duration delay = Duration.milliseconds(50);

                    clock.scheduleAfter(delay, counter::increment).await();

                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertEqual(1, counter.get());
                    test.assertGreaterThanOrEqualTo(duration, delay, Duration.microseconds(1));
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                runner.test("with Datetime after now", (Test test) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final IntegerValue counter = IntegerValue.create(0);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final Duration delay = Duration.milliseconds(50);

                    clock.scheduleAt(startTime.plus(delay), counter::increment).await();

                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertEqual(1, counter.get());
                    test.assertGreaterThanOrEqualTo(duration, delay, Duration.microseconds(1));
                });
            });
        });
    }
}
