package qub;

public class ListMapTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ListMap<K,V>", new Action0()
        {
            @Override
            public void run()
            {
                MapTests.test(runner, new Function0<Map<Integer, Boolean>>()
                {
                    @Override
                    public Map<Integer, Boolean> run()
                    {
                        return new ListMap<>();
                    }
                });
            }
        });
    }
}
