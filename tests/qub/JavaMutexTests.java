package qub;

public interface JavaMutexTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaMutex.class, () ->
        {
            MutexTests.test(runner, JavaMutex::new);

            runner.test("constructor()", (Test test) ->
            {
                final JavaMutex mutex = new JavaMutex();
                test.assertFalse(mutex.isAcquired());
            });
        });
    }
}
