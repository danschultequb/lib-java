package qub;

public class PathSeparatorTransitionConditionTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("PathSeparatorTransitionCondition", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("matches()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final PathSeparatorTransitionCondition condition = new PathSeparatorTransitionCondition();
                        test.assertTrue(condition.matches('/'));
                        test.assertTrue(condition.matches('\\'));
                        test.assertFalse(condition.matches('a'));
                    }
                });
            }
        });
    }
}
