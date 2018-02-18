package qub;

public class JavaRandomTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JavaRandom", () ->
        {
            runner.test("getRandomInteger()", (Test test) ->
            {
                final JavaRandom random = new JavaRandom();
                test.assertNotEqual(random.getRandomInteger(), random.getRandomInteger());

                for (int i = 0; i < 100; ++i)
                {
                    test.assertTrue(0 <= random.getRandomInteger());
                }
            });
        });
    }
}
