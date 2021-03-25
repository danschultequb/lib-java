package qub;

public class SpinMutexTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SpinMutex.class, () ->
        {
            MutexTests.test(runner, (Clock clock) ->
            {
                return clock == null
                    ? SpinMutex.create()
                    : SpinMutex.create(clock);
            });

            runner.test("create()", (Test test) ->
            {
                final SpinMutex mutex = SpinMutex.create();
                test.assertNotNull(mutex);
                test.assertFalse(mutex.isAcquired());
                test.assertFalse(mutex.isAcquiredByCurrentThread());
            });

            runner.testGroup("create(Clock)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SpinMutex.create(null),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final SpinMutex mutex = SpinMutex.create(clock);
                    test.assertNotNull(mutex);
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());
                });
            });
        });
    }
}
