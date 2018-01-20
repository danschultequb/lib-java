package qub;

public class JavaRandomTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JavaRandom", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("getRandomInteger()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final JavaRandom random = new JavaRandom();
                        test.assertNotEqual(random.getRandomInteger(), random.getRandomInteger());

                        for (int i = 0; i < 100; ++i)
                        {
                            test.assertTrue(0 <= random.getRandomInteger());
                        }
                    }
                });
            }
        });
    }
}
