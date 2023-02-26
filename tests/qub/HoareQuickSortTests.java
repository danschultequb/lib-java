package qub;

public interface HoareQuickSortTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(HoareQuickSort.class, () ->
        {
            SortTests.test(runner, HoareQuickSort::create);
        });
    }
}
