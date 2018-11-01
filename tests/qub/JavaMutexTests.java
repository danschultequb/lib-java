package qub;

public class JavaMutexTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaMutex.class, () ->
        {
            MutexTests.test(runner, JavaMutex::new);

            runner.test("constructor()", (Test test) ->
            {
                final JavaMutex mutex = new JavaMutex();
                test.assertNull(mutex.getClock());
            });
        });
    }
}
