package qub;

public class ListSetTests
{
    public static void test(TestRunner runner)
    {
        SetTests.test(runner, ListSet::create);
    }
}
