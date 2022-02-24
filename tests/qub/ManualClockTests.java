package qub;

public interface ManualClockTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ManualClock.class,
            (TestResources resources) -> Tuple.create(resources.getMainAsyncRunner()),
            (AsyncScheduler mainAsyncRunner) ->
        {
            ClockTests.test(runner, () -> ManualClock.create(mainAsyncRunner));

            runner.test("create()", (Test test) ->
            {
                final ManualClock clock = ManualClock.create();
                test.assertEqual(DateTime.epoch, clock.getCurrentDateTime());
                test.assertThrows(() -> clock.scheduleAfter(Duration.zero, Action0.empty),
                    new PreConditionFailure("this.asyncRunner cannot be null."));
            });

            runner.testGroup("create(DateTime)", () ->
            {
                runner.test("with null currentDateTime", (Test test) ->
                {
                    test.assertThrows(() -> ManualClock.create((DateTime)null),
                        new PreConditionFailure("currentDateTime cannot be null."));
                });

                runner.test("with non-null currentDateTime", (Test test) ->
                {
                    DateTime currentDateTime = DateTime.create(1, 2, 3);
                    final ManualClock clock = ManualClock.create(currentDateTime);
                    test.assertNotNull(clock);
                    test.assertEqual(currentDateTime.getOffset(), clock.getCurrentOffset());
                    test.assertEqual(currentDateTime, clock.getCurrentDateTime());
                    test.assertThrows(() -> clock.scheduleAfter(Duration.zero, Action0.empty),
                        new PreConditionFailure("this.asyncRunner cannot be null."));
                });
            });

            runner.testGroup("create(AsyncScheduler)", () ->
            {
                runner.test("with null asyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> ManualClock.create((AsyncScheduler)null),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with non-null asyncRunner", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(mainAsyncRunner);
                    test.assertNotNull(clock);
                    test.assertEqual(Duration.zero, clock.getCurrentOffset());
                    test.assertEqual(DateTime.epoch, clock.getCurrentDateTime());

                    final IntegerValue counter = IntegerValue.create(0);
                    clock.scheduleAfter(Duration.zero, counter::increment).await();
                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("create(DateTime,AsyncScheduler)", () ->
            {
                final Action3<DateTime,AsyncScheduler,Throwable> createErrorTest = (DateTime currentDateTime, AsyncScheduler asyncRunner, Throwable expected) ->
                {
                    runner.test("with " + English.andList(currentDateTime, (asyncRunner != null ? "non-" : "") + "null asyncRunner"), (Test test) ->
                    {
                        test.assertThrows(() -> ManualClock.create(currentDateTime, asyncRunner),
                            expected);
                    });
                };

                createErrorTest.run(null, mainAsyncRunner, new PreConditionFailure("currentDateTime cannot be null."));
                createErrorTest.run(DateTime.epoch, null, new PreConditionFailure("asyncRunner cannot be null."));

                runner.test("with valid arguments", (Test test) ->
                {
                    final DateTime currentDateTime = DateTime.create(1, 2, 3);
                    final ManualClock clock = ManualClock.create(currentDateTime, mainAsyncRunner);
                    test.assertNotNull(clock);
                    test.assertEqual(currentDateTime, clock.getCurrentDateTime());
                    test.assertEqual(currentDateTime.getOffset(), clock.getCurrentOffset());

                    final IntegerValue counter = IntegerValue.create(0);
                    clock.scheduleAfter(Duration.zero, counter::increment).await();
                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("advance(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(mainAsyncRunner);
                    test.assertThrows(() -> clock.advance((Duration)null),
                        new PreConditionFailure("duration cannot be null."));
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = ManualClock.create(mainAsyncRunner);
                        clock.advance(Duration.seconds(0)).await();
                        test.assertEqual(DateTime.epoch, clock.getCurrentDateTime());
                    });

                });

                runner.test("with 5 minutes", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final ManualClock clock = ManualClock.create(mainAsyncRunner);
                        clock.advance(Duration.minutes(5)).await();
                        test.assertEqual(DateTime.epoch.plus(Duration.minutes(5)), clock.getCurrentDateTime());
                    });
                });
            });

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                runner.test("with positive Duration", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(mainAsyncRunner);
                    final IntegerValue counter = IntegerValue.create(0);

                    final Result<Void> asyncAction = clock.scheduleAfter(Duration.milliseconds(50), counter::increment);
                    test.assertEqual(0, counter.get());
                    test.assertEqual(DateTime.epoch, clock.getCurrentDateTime());

                    clock.advance(Duration.milliseconds(49)).await();
                    test.assertEqual(0, counter.get());
                    test.assertEqual(DateTime.epoch.plus(Duration.milliseconds(49)), clock.getCurrentDateTime());

                    clock.advance(Duration.milliseconds(1)).await();
                    test.assertEqual(0, counter.get());
                    test.assertEqual(DateTime.epoch.plus(Duration.milliseconds(50)), clock.getCurrentDateTime());

                    asyncAction.await();
                    test.assertEqual(1, counter.get());
                    test.assertEqual(DateTime.epoch.plus(Duration.milliseconds(50)), clock.getCurrentDateTime());
                    test.assertTrue(asyncAction.isCompleted());
                });

                runner.test("with multiple actions at the same positive Duration", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(mainAsyncRunner);
                    final IntegerValue value = Value.create(0);

                    final Result<Void> asyncTask1 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(1));
                    final Result<Void> asyncTask2 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(2));
                    test.assertEqual(0, value.get());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());
                    test.assertEqual(2, clock.getPausedTaskCount());

                    clock.advance(Duration.milliseconds(50)).await();
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(50)), clock.getCurrentDateTime());

                    asyncTask1.await();
                    test.assertEqual(1, value.get());
                    test.assertTrue(asyncTask1.isCompleted());
                    test.assertFalse(asyncTask2.isCompleted());

                    asyncTask2.await();
                    test.assertEqual(2, value.get());
                    test.assertTrue(asyncTask1.isCompleted());
                    test.assertTrue(asyncTask2.isCompleted());
                });

                runner.test("with action before an existing action", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(mainAsyncRunner);
                    final Value<DateTime> value = Value.create();

                    final Result<Void> asyncAction1 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(clock.getCurrentDateTime()));
                    final Result<Void> asyncAction2 = clock.scheduleAfter(Duration.milliseconds(25), () -> value.set(clock.getCurrentDateTime()));
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());
                    test.assertEqual(2, clock.getPausedTaskCount());

                    clock.advance(Duration.milliseconds(49)).await();
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), clock.getCurrentDateTime());

                    asyncAction2.await();
                    test.assertTrue(value.hasValue());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), value.get());
                    test.assertFalse(asyncAction1.isCompleted());
                    test.assertTrue(asyncAction2.isCompleted());

                    clock.advance(Duration.milliseconds(5)).await();
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(54)), clock.getCurrentDateTime());

                    asyncAction1.await();
                    test.assertTrue(value.hasValue());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(54)), value.get());
                    test.assertTrue(asyncAction1.isCompleted());
                    test.assertTrue(asyncAction2.isCompleted());
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                runner.test("with Datetime after now", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(mainAsyncRunner);
                    final Value<Boolean> value = BooleanValue.create();

                    final Result<Void> asyncAction = clock.scheduleAt(
                        DateTime.epoch.plus(Duration.milliseconds(50)),
                        () -> value.set(true));
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(0)), clock.getCurrentDateTime());

                    clock.advance(Duration.milliseconds(49)).await();
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(49)), clock.getCurrentDateTime());

                    clock.advance(Duration.milliseconds(1)).await();
                    asyncAction.await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(50)), clock.getCurrentDateTime());
                    test.assertTrue(asyncAction.isCompleted());
                });
            });
        });
    }
}
