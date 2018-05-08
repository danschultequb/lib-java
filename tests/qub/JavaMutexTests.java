package qub;

public class JavaMutexTests
{
    public static void test(TestRunner runner)
    {
        MutexTests.test(runner, JavaMutex::new);
    }
}
