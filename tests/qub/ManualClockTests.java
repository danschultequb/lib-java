package qub;

public class ManualClockTests
{
    private static final DateTime currentDateTime = DateTime.local(0);

    public static void test(TestRunner runner)
    {
        runner.testGroup(ManualClock.class, () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null mainAsyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> new ManualClock(null, currentDateTime));
                });

                runner.test("with null currentDateTime", (Test test) ->
                {
                    test.assertThrows(() -> new ManualClock(test.getMainAsyncRunner(), null));
                });

                runner.test("with non-null currentDateTime", (Test test) ->
                {
                    final ManualClock clock = new ManualClock(test.getMainAsyncRunner(), currentDateTime);
                    test.assertEqual(currentDateTime, clock.getCurrentDateTime());
                });
            });

            runner.testGroup("advance()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    test.assertThrows(() -> clock.advance(null));
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    clock.advance(Duration.seconds(0));
                    test.assertEqual(currentDateTime, clock.getCurrentDateTime());
                });

                runner.test("with 5 minutes", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    clock.advance(Duration.minutes(5));
                    test.assertEqual(currentDateTime.plus(Duration.minutes(5)), clock.getCurrentDateTime());
                });
            });

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    final Value<Boolean> value = BooleanValue.create();
                    test.assertThrows(() -> clock.scheduleAfter(null, () -> value.set(true)).await());
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    final Value<Boolean> value = BooleanValue.create();
                    clock.scheduleAfter(Duration.seconds(-5), () -> value.set(true)).await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    final Value<Boolean> value = BooleanValue.create();
                    clock.scheduleAfter(Duration.seconds(0), () -> value.set(true)).await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });

                runner.test("with positive Duration", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    final Value<Boolean> value = BooleanValue.create();

                    final AsyncAction asyncAction = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(true));
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.local(0), clock.getCurrentDateTime());

                    clock.advance(Duration.milliseconds(49));
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.local(49), clock.getCurrentDateTime());

                    test.assertEqual(0, test.getMainAsyncRunner().getScheduledTaskCount());
                    clock.advance(Duration.milliseconds(1));
                    test.assertEqual(1, test.getMainAsyncRunner().getScheduledTaskCount());
                    asyncAction.await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertEqual(DateTime.local(50), clock.getCurrentDateTime());
                    test.assertTrue(asyncAction.isCompleted());
                });

                runner.test("with multiple actions at the same positive Duration", (Test test) ->
                {
                    final AsyncRunner mainAsyncRunner = test.getMainAsyncRunner();
                    final ManualClock clock = createClock(mainAsyncRunner);
                    final IntegerValue value = Value.create(0);

                    final AsyncAction asyncAction1 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(1));
                    final AsyncAction asyncAction2 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(2));
                    test.assertEqual(0, value.get());
                    test.assertEqual(DateTime.local(0), clock.getCurrentDateTime());
                    test.assertEqual(2, clock.getPausedTaskCount());

                    test.assertEqual(0, mainAsyncRunner.getScheduledTaskCount());
                    clock.advance(Duration.milliseconds(50));
                    test.assertEqual(DateTime.local(50), clock.getCurrentDateTime());
                    test.assertEqual(2, mainAsyncRunner.getScheduledTaskCount());

                    asyncAction1.await();
                    test.assertEqual(1, value.get());
                    test.assertTrue(asyncAction1.isCompleted());
                    test.assertFalse(asyncAction2.isCompleted());

                    asyncAction2.await();
                    test.assertEqual(2, value.get());
                    test.assertTrue(asyncAction1.isCompleted());
                    test.assertTrue(asyncAction2.isCompleted());
                });

                runner.test("with action before an existing action", (Test test) ->
                {
                    final AsyncRunner mainAsyncRunner = test.getMainAsyncRunner();
                    final ManualClock clock = createClock(mainAsyncRunner);
                    final Value<DateTime> value = Value.create();

                    final AsyncAction asyncAction1 = clock.scheduleAfter(Duration.milliseconds(50), () -> value.set(clock.getCurrentDateTime()));
                    final AsyncAction asyncAction2 = clock.scheduleAfter(Duration.milliseconds(25), () -> value.set(clock.getCurrentDateTime()));
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.local(0), clock.getCurrentDateTime());
                    test.assertEqual(2, clock.getPausedTaskCount());

                    test.assertEqual(0, mainAsyncRunner.getScheduledTaskCount());
                    clock.advance(Duration.milliseconds(49));
                    test.assertEqual(DateTime.local(49), clock.getCurrentDateTime());
                    test.assertEqual(1, mainAsyncRunner.getScheduledTaskCount());

                    asyncAction2.await();
                    test.assertTrue(value.hasValue());
                    test.assertEqual(DateTime.local(49), value.get());
                    test.assertFalse(asyncAction1.isCompleted());
                    test.assertTrue(asyncAction2.isCompleted());

                    test.assertEqual(0, mainAsyncRunner.getScheduledTaskCount());
                    clock.advance(Duration.milliseconds(5));
                    test.assertEqual(DateTime.local(54), clock.getCurrentDateTime());
                    test.assertEqual(1, mainAsyncRunner.getScheduledTaskCount());

                    asyncAction1.await();
                    test.assertTrue(value.hasValue());
                    test.assertEqual(DateTime.local(54), value.get());
                    test.assertTrue(asyncAction1.isCompleted());
                    test.assertTrue(asyncAction2.isCompleted());
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    test.assertThrows(() -> clock.scheduleAt(null, () -> {}));
                });

                runner.test("with DateTime before now", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
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

                runner.test("with DateTime \"at\" now", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
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

                runner.test("with Datetime after now", (Test test) ->
                {
                    final ManualClock clock = createClock(test);
                    final Value<Boolean> value = BooleanValue.create();

                    final AsyncAction asyncAction = clock.scheduleAt(
                        currentDateTime.plus(Duration.milliseconds(50)),
                        () ->
                        {
                            value.set(true);
                        });
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.local(0), clock.getCurrentDateTime());

                    clock.advance(Duration.milliseconds(49));
                    test.assertFalse(value.hasValue());
                    test.assertEqual(DateTime.local(49), clock.getCurrentDateTime());

                    test.assertEqual(0, test.getMainAsyncRunner().getScheduledTaskCount());
                    clock.advance(Duration.milliseconds(1));
                    test.assertEqual(1, test.getMainAsyncRunner().getScheduledTaskCount());
                    asyncAction.await();
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertEqual(DateTime.local(50), clock.getCurrentDateTime());
                    test.assertTrue(asyncAction.isCompleted());
                });
            });
        });
    }

    private static ManualClock createClock(Test test)
    {
        return createClock(test.getMainAsyncRunner());
    }

    private static ManualClock createClock(AsyncRunner mainAsyncRunner)
    {
        return new ManualClock(mainAsyncRunner, currentDateTime);
    }
}
