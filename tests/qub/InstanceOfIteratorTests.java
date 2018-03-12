package qub;

public class InstanceOfIteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InstanceOfIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                return ArrayIteratorTests.createIterator(count, started)
                    .instanceOf(Integer.class);
            });
        });
    }
}
