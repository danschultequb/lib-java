package qub;

public interface JavaArrayListTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(JavaArrayList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final JavaArrayList<Integer> result = JavaArrayList.create();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
