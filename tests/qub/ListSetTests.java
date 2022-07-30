package qub;

public interface ListSetTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ListSet.class, () ->
        {
            MutableSetTests.test(runner, (Integer valueCount) ->
            {
                final ListSet<Integer> result = ListSet.create();
                if (valueCount != null && valueCount > 0)
                {
                    for (int i = 0; i < valueCount; i++)
                    {
                        result.add(i);
                    }
                }
                return result;
            });
        });
    }
}
