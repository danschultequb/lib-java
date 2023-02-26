package qub;

public interface LomutoQuickSortTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(LomutoQuickSort.class, () ->
        {
            SortTests.test(runner, LomutoQuickSort::create);
        });
    }
}
