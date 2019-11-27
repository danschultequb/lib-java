package qub;

public interface ManualClockTests
{
    DateTime currentDateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0));

    static void test(TestRunner runner)
    {
        runner.testGroup(ManualClock.class, () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null mainAsyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> new ManualClock(currentDateTime, null),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with null currentDateTime", (Test test) ->
                {
                    test.assertThrows(() -> new ManualClock(null, test.getMainAsyncRunner()),
                        new PreConditionFailure("currentDateTime cannot be null."));
                });

                runner.test("with non-null currentDateTime", (Test test) ->
                {
                    final ManualClock clock = new ManualClock(currentDateTime, test.getMainAsyncRunner());
                    test.assertEqual(currentDateTime, clock.getCurrentDateTime());
                });
            });

            runner.testGroup("advance()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        test.assertThrows(() -> clock.advance(null), new PreConditionFailure("duration cannot be null."));
                    });
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        clock.advance(Duration.seconds(0));
                        test.assertEqual(currentDateTime, clock.getCurrentDateTime());
                    });
                    
                });

                runner.test("with 5 minutes", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        clock.advance(Duration.minutes(5));
                        test.assertEqual(currentDateTime.plus(Duration.minutes(5)), clock.getCurrentDateTime());
                    });
                });
            });

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();
                        test.assertThrows(() -> clock.scheduleAfter(null, () -> value.set(true)),
                            new PreConditionFailure("duration cannot be null."));
                    });
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();
                        clock.scheduleAfter(Duration.seconds(-5), () -> value.set(true)).await();
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.get());
                    });
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();
                        clock.scheduleAfter(Duration.seconds(0), () -> value.set(true)).await();
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.get());
                    });
                });

                runner.test("with positive Duration", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();

                        final Result<Void> asyncAction = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(true));
                        test.assertFalse(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());

                        clock.advance(Duration.milliseconds(49));
                        test.assertFalse(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), clock.getCurrentDateTime());

                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        clock.advance(Duration.milliseconds(1));
                        test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                        asyncAction.await();
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.get());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(50)), clock.getCurrentDateTime());
                        test.assertTrue(asyncAction.isCompleted());
                    });
                });

                runner.test("with multiple actions at the same positive Duration", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final IntegerValue value = Value.create(0);

                        final Result<Void> asyncTask1 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(1));
                        final Result<Void> asyncTask2 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(2));
                        test.assertEqual(0, value.get());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());
                        test.assertEqual(2, clock.getPausedTaskCount());

                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        clock.advance(Duration.milliseconds(50));
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(50)), clock.getCurrentDateTime());
                        test.assertEqual(2, asyncRunner.getScheduledTaskCount());

                        asyncTask1.await();
                        test.assertEqual(1, value.get());
                        test.assertTrue(asyncTask1.isCompleted());
                        test.assertFalse(asyncTask2.isCompleted());

                        asyncTask2.await();
                        test.assertEqual(2, value.get());
                        test.assertTrue(asyncTask1.isCompleted());
                        test.assertTrue(asyncTask2.isCompleted());
                    });
                });

                runner.test("with action before an existing action", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<DateTime> value = Value.create();

                        final Result<Void> asyncAction1 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(clock.getCurrentDateTime()));
                        final Result<Void> asyncAction2 = clock.scheduleAfter(Duration.milliseconds(25), () -> value.set(clock.getCurrentDateTime()));
                        test.assertFalse(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());
                        test.assertEqual(2, clock.getPausedTaskCount());

                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        clock.advance(Duration.milliseconds(49));
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), clock.getCurrentDateTime());
                        test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                        asyncAction2.await();
                        test.assertTrue(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), value.get());
                        test.assertFalse(asyncAction1.isCompleted());
                        test.assertTrue(asyncAction2.isCompleted());

                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        clock.advance(Duration.milliseconds(5));
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(54)), clock.getCurrentDateTime());
                        test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                        asyncAction1.await();
                        test.assertTrue(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(54)), value.get());
                        test.assertTrue(asyncAction1.isCompleted());
                        test.assertTrue(asyncAction2.isCompleted());
                    });
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        test.assertThrows(() -> clock.scheduleAt(null, () -> {}), new PreConditionFailure("dateTime cannot be null."));
                    });
                });

                runner.test("with DateTime before now", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();
                        final DateTime startTime = clock.getCurrentDateTime();
                        clock.scheduleAt(
                            startTime.minus(Duration.seconds(1)),
                            () -> value.set(true)).await();
                        final DateTime endTime = clock.getCurrentDateTime();
                        final Duration duration = endTime.minus(startTime);
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.get());
                        test.assertLessThanOrEqualTo(duration, Duration.milliseconds(20));
                    });
                });

                runner.test("with DateTime \"at\" now", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();
                        final DateTime startTime = clock.getCurrentDateTime();
                        clock.scheduleAt(
                            startTime,
                            () -> value.set(true)).await();
                        final DateTime endTime = clock.getCurrentDateTime();
                        final Duration duration = endTime.minus(startTime);
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.get());
                        test.assertLessThanOrEqualTo(duration, Duration.milliseconds(20));
                    });
                });

                runner.test("with Datetime after now", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = createClock(asyncRunner);
                        final Value<Boolean> value = BooleanValue.create();

                        final Result<Void> asyncAction = clock.scheduleAt(
                            currentDateTime.plus(Duration.milliseconds(50)),
                            () -> value.set(true));
                        test.assertFalse(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());

                        clock.advance(Duration.milliseconds(49));
                        test.assertFalse(value.hasValue());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), clock.getCurrentDateTime());

                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        clock.advance(Duration.milliseconds(1));
                        test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                        asyncAction.await();
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.get());
                        test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(50)), clock.getCurrentDateTime());
                        test.assertTrue(asyncAction.isCompleted());
                    });
                });
            });
        });
    }

    static ManualClock createClock(ManualAsyncRunner asyncRunner)
    {
        return new ManualClock(currentDateTime, asyncRunner);
    }
}
