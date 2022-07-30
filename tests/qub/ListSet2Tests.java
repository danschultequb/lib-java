package qub;

public interface ListSet2Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ListSet2.class, () ->
        {
            MutableSetTests.test(runner, (Integer valueCount) ->
            {
                final ListSet2<Integer> result = ListSet2.create();
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
