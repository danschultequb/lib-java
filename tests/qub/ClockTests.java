package qub;

public interface ClockTests
{
    static void test(TestRunner runner, Function0<? extends Clock> creator)
    {
        runner.testGroup(Clock.class, () ->
        {
            runner.test("getCurrentOffset()", (Test test) ->
            {
                final Clock clock = creator.run();
                final Duration offset = clock.getCurrentOffset();
                test.assertNotNull(offset);
                test.assertBetween(Duration.hours(-18), offset, Duration.hours(18));
            });

            runner.test("getCurrentDateTime()", (Test test) ->
            {
                final Clock clock = creator.run();
                final DateTime currentDateTime = clock.getCurrentDateTime();
                test.assertNotNull(currentDateTime);
            });

            runner.testGroup("scheduleAfter(Duration,Action0)", () ->
            {
                final Action3<Duration,Action0,Throwable> scheduleAfterErrorTest = (Duration duration, Action0 action, Throwable expected) ->
                {
                    runner.test("with " + English.andList(duration, (action == null ? "null" : "non-null") + " action"), (Test test) ->
                    {
                        final Clock clock = creator.run();
                        test.assertThrows(() -> clock.scheduleAfter(duration, action),
                            expected);
                    });
                };

                scheduleAfterErrorTest.run(null, Action0.empty, new PreConditionFailure("duration cannot be null."));
                scheduleAfterErrorTest.run(Duration.seconds(1), null, new PreConditionFailure("action cannot be null."));

                runner.test("with negative Duration", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final IntegerValue counter = IntegerValue.create(0);
                    clock.scheduleAfter(Duration.seconds(-5), counter::increment).await();
                    test.assertEqual(1, counter.get());
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final IntegerValue counter = IntegerValue.create(0);
                    clock.scheduleAfter(Duration.zero, counter::increment).await();
                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("scheduleAt(DateTime,Action0)", () ->
            {
                final Action3<DateTime,Action0,Throwable> scheduleAtErrorTest = (DateTime dateTime, Action0 action, Throwable expected) ->
                {
                    runner.test("with " + English.andList(dateTime, (action == null ? "null" : "non-null") + " action"), (Test test) ->
                    {
                        final Clock clock = creator.run();
                        test.assertThrows(() -> clock.scheduleAt(dateTime, action),
                            expected);
                    });
                };

                scheduleAtErrorTest.run(null, Action0.empty, new PreConditionFailure("dateTime cannot be null."));
                scheduleAtErrorTest.run(DateTime.epoch, null, new PreConditionFailure("action cannot be null."));

                runner.test("with DateTime before now", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final IntegerValue counter = IntegerValue.create(0);
                    final DateTime startTime = clock.getCurrentDateTime();

                    clock.scheduleAt(startTime.minus(Duration.seconds(1)), counter::increment).await();

                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertEqual(1, counter.get());
                    test.assertLessThanOrEqualTo(duration, Duration.milliseconds(50));
                });

                runner.test("with DateTime at now", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final IntegerValue counter = IntegerValue.create(0);
                    final DateTime startTime = clock.getCurrentDateTime();

                    clock.scheduleAt(startTime, counter::increment).await();

                    final DateTime endTime = clock.getCurrentDateTime();
                    final Duration duration = endTime.minus(startTime);
                    test.assertEqual(1, counter.get());
                    test.assertLessThanOrEqualTo(duration, Duration.milliseconds(50));
                });
            });

            runner.testGroup("delay(Duration)", () ->
            {
                runner.test("with null duration", (Test test) ->
                {
                    final Clock clock = creator.run();
                    test.assertThrows(() -> clock.delay(null),
                        new PreConditionFailure("duration cannot be null."));
                });

                runner.test("with negative duration", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final DateTime beforeDelay = clock.getCurrentDateTime();

                    final Void delayResult = clock.delay(Duration.milliseconds(-5)).await();
                    test.assertNull(delayResult);

                    final DateTime afterDelay = clock.getCurrentDateTime();
                    test.assertTrue(beforeDelay.equals(afterDelay, Duration.milliseconds(10)));
                });

                runner.test("with zero duration", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final DateTime beforeDelay = clock.getCurrentDateTime();

                    final Void delayResult = clock.delay(Duration.zero).await();
                    test.assertNull(delayResult);

                    final DateTime afterDelay = clock.getCurrentDateTime();
                    test.assertTrue(beforeDelay.equals(afterDelay, Duration.milliseconds(10)));
                });
            });

            runner.testGroup("delayUntil(DateTime)", () ->
            {
                runner.test("with null dateTime", (Test test) ->
                {
                    final Clock clock = creator.run();
                    test.assertThrows(() -> clock.delayUntil(null),
                        new PreConditionFailure("dateTime cannot be null."));
                });

                runner.test("with dateTime before now", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final DateTime beforeDelay = clock.getCurrentDateTime();

                    final Void delayResult = clock.delayUntil(beforeDelay.minus(Duration.milliseconds(5))).await();
                    test.assertNull(delayResult);

                    final DateTime afterDelay = clock.getCurrentDateTime();
                    test.assertTrue(beforeDelay.equals(afterDelay, Duration.milliseconds(10)));
                });

                runner.test("with dateTime at now", (Test test) ->
                {
                    final Clock clock = creator.run();
                    final DateTime beforeDelay = clock.getCurrentDateTime();

                    final Void delayResult = clock.delayUntil(beforeDelay).await();
                    test.assertNull(delayResult);

                    final DateTime afterDelay = clock.getCurrentDateTime();
                    test.assertTrue(beforeDelay.equals(afterDelay, Duration.milliseconds(10)));
                });
            });
        });
    }
}
