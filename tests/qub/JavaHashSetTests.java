package qub;

public interface JavaHashSetTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaHashSet.class, () ->
        {
            MutableSetTests.test(runner, (Integer valueCount) ->
            {
                final JavaHashSet<Integer> result = JavaHashSet.create();
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
