package qub;

public interface InsertionSortTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InsertionSort.class, () ->
        {
            SortTests.test(runner, InsertionSort::create);
        });
    }
}
