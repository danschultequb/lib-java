package qub;

public interface StopwatchTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Stopwatch.class, () ->
        {
            runner.testGroup("create(Clock)", () ->
            {
                runner.test("with null clock", (Test test) ->
                {
                    test.assertThrows(() -> Stopwatch.create(null),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with non-null clock", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Stopwatch stopwatch = Stopwatch.create(clock);
                    test.assertNotNull(stopwatch);
                    test.assertFalse(stopwatch.hasStarted());
                });
            });

            runner.test("start()", (Test test) ->
            {
                final ManualClock clock = ManualClock.create();
                final Stopwatch stopwatch = Stopwatch.create(clock);

                final Stopwatch startResult = stopwatch.start();
                test.assertSame(stopwatch, startResult);
                test.assertTrue(stopwatch.hasStarted());

                test.assertThrows(() -> stopwatch.start(),
                    new PreConditionFailure("this.hasStarted() cannot be true."));
                test.assertTrue(stopwatch.hasStarted());
            });

            runner.testGroup("stop()", () ->
            {
                runner.test("with no start()", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(DateTime.epoch);
                    final Stopwatch stopwatch = Stopwatch.create(clock);

                    test.assertThrows(() -> stopwatch.stop(),
                        new PreConditionFailure("this.hasStarted() cannot be false."));
                    test.assertFalse(stopwatch.hasStarted());
                });

                runner.test("with no time elapsed after start()", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(DateTime.epoch);
                    final Stopwatch stopwatch = Stopwatch.create(clock);

                    final Stopwatch startResult = stopwatch.start();
                    test.assertSame(stopwatch, startResult);

                    test.assertEqual(Duration.zero, stopwatch.stop());
                    test.assertFalse(stopwatch.hasStarted());
                });

                runner.test("with time elapsed after start()", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create(DateTime.epoch);
                    final Stopwatch stopwatch = Stopwatch.create(clock);

                    stopwatch.start();

                    clock.advance(Duration.seconds(5)).await();

                    test.assertEqual(Duration.seconds(5), stopwatch.stop());
                    test.assertFalse(stopwatch.hasStarted());
                });
            });
        });
    }
}
