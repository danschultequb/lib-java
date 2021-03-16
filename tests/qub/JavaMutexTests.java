package qub;

public interface JavaMutexTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaMutex.class, () ->
        {
            MutexTests.test(runner, JavaMutex::create);

            runner.test("create()", (Test test) ->
            {
                final JavaMutex mutex = JavaMutex.create();
                test.assertFalse(mutex.isAcquired());
            });
        });
    }
}
