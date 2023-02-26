package qub;

public interface QuickSortTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(QuickSort.class, () ->
        {
            SortTests.test(runner, QuickSort::create);
        });
    }
}
