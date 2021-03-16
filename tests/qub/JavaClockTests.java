package qub;

public interface JavaClockTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaClock.class, () ->
        {
            runner.test("getCurrentDateTime()",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner parallelAsyncRunner) ->
            {
                final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                final DateTime currentDateTime = clock.getCurrentDateTime();
                test.assertNotNull(currentDateTime);
            });

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                runner.test("with null Duration",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    test.assertThrows(() -> clock.scheduleAfter((Duration)null, () -> {}),
                        new PreConditionFailure("duration cannot be null."));
                });

                runner.test("with negative Duration",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final Value<Boolean> value = Value.create();
                    clock.scheduleAfter(Duration.seconds(-5), () -> value.set(true)).await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });

                runner.test("with zero Duration",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final Value<Boolean> value = Value.create();
                    clock.scheduleAfter(Duration.seconds(0), () -> value.set(true)).await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });

                runner.test("with positive Duration",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    final Duration delay = Duration.milliseconds(50);
                    clock.scheduleAfter(delay, () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertGreaterThanOrEqualTo(duration, delay, Duration.microseconds(1));
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                runner.test("with null DateTime",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    test.assertThrows(() -> clock.scheduleAt(null, () -> {}),
                        new PreConditionFailure("dateTime cannot be null."));
                });

                runner.test("with null action",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    test.assertThrows(() -> clock.scheduleAt(clock.getCurrentDateTime(), null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with DateTime before now",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    clock.scheduleAt(
                        startTime.minus(Duration.seconds(1)),
                        () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertLessThanOrEqualTo(duration, Duration.milliseconds(50));
                });

                runner.test("with DateTime \"at\" now",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    clock.scheduleAt(
                        startTime,
                        () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertLessThanOrEqualTo(duration, Duration.milliseconds(30));
                });

                runner.test("with Datetime after now",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final JavaClock clock = JavaClock.create(parallelAsyncRunner);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    final Duration delay = Duration.milliseconds(50);
                    clock.scheduleAt(startTime.plus(delay), () -> value.set(true)) .await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertGreaterThanOrEqualTo(duration, delay, Duration.microseconds(1));
                });
            });
        });
    }
}
