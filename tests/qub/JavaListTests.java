package qub;

public class JavaListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final List<Integer> list = JavaList.wrap(new java.util.ArrayList<>());
                for (int i = 0; i < count; ++i)
                {
                    list.add(i);
                }
                return list;
            });
        });
    }
}
