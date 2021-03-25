package qub;

public interface JavaMutexTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaMutex.class, () ->
        {
            MutexTests.test(runner, (Clock clock) ->
            {
                return clock == null
                    ? JavaMutex.create()
                    : JavaMutex.create(clock);
            });

            runner.test("create()", (Test test) ->
            {
                final JavaMutex mutex = JavaMutex.create();
                test.assertNotNull(mutex);
                test.assertFalse(mutex.isAcquired());
                test.assertFalse(mutex.isAcquiredByCurrentThread());
            });

            runner.testGroup("create(Clock)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaMutex.create(null),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final JavaMutex mutex = JavaMutex.create(clock);
                    test.assertNotNull(mutex);
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());
                });
            });
        });
    }
}
