package qub;

public class ShellSortTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ShellSort.class, () ->
        {
            SortTests.test(runner, ShellSort::new);
        });
    }
}
