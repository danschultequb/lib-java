package qub;

public interface JavaClockTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaClock.class, () ->
        {
            runner.test("getCurrentDateTime()", (Test test) ->
            {
                final JavaClock clock = createClock(test);
                final DateTime currentDateTime = clock.getCurrentDateTime();
                test.assertNotNull(currentDateTime);
            });

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    test.assertThrows(() -> clock.scheduleAfter(null, () -> {}), new PreConditionFailure("duration cannot be null."));
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    final Value<Boolean> value = Value.create();
                    clock.scheduleAfter(Duration2.seconds(-5), () -> value.set(true)).await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });

                // Lingering async action failures: 2
                runner.test("with zero Duration", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    final Value<Boolean> value = Value.create();
                    clock.scheduleAfter(Duration2.seconds(0), () -> value.set(true)).await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });

                runner.test("with positive Duration", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    final Duration2 delay = Duration2.milliseconds(50);
                    clock.scheduleAfter(delay, () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration2 duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertGreaterThanOrEqualTo(duration, delay, Duration2.microseconds(1));
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    test.assertThrows(() -> clock.scheduleAt(null, () -> {}),
                        new PreConditionFailure("dateTime cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    test.assertThrows(() -> clock.scheduleAt(clock.getCurrentDateTime(), null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with DateTime before now", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    clock.scheduleAt(
                        startTime.minus(Duration2.seconds(1)),
                        () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration2 duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertLessThanOrEqualTo(duration, Duration2.milliseconds(50));
                });

                runner.test("with DateTime \"at\" now", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    clock.scheduleAt(
                        startTime,
                        () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration2 duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertLessThanOrEqualTo(duration, Duration2.milliseconds(30));
                });

                runner.test("with Datetime after now", (Test test) ->
                {
                    final JavaClock clock = createClock(test);
                    final Value<Boolean> value = Value.create();
                    final DateTime startTime = clock.getCurrentDateTime();
                    final Duration2 delay = Duration2.milliseconds(50);
                    clock.scheduleAt(
                        startTime.plus(delay),
                        () -> value.set(true)).await();
                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration2 duration = endTime.minus(startTime);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertGreaterThanOrEqualTo(duration, delay, Duration2.microseconds(1));
                });
            });
        });
    }

    static JavaClock createClock(Test test)
    {
        return new JavaClock(test.getParallelAsyncRunner());
    }
}
