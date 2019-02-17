package qub;

public class ArrayListTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(ArrayList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final ArrayList<Integer> result = new ArrayList<>();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
