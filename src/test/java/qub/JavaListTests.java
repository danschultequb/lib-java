package qub;

public class JavaListTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JavaList<T>", new Action0()
        {
            @Override
            public void run()
            {
                ListTests.test(runner, new Function1<Integer, List<Integer>>()
                {
                    @Override
                    public List<Integer> run(Integer count)
                    {
                        final List<Integer> list = JavaList.wrap(new java.util.ArrayList<Integer>());
                        for (int i = 0; i < count; ++i)
                        {
                            list.add(i);
                        }
                        return list;
                    }
                });
            }
        });
    }
}
