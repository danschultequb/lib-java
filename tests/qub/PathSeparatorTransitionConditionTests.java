package qub;

public class PathSeparatorTransitionConditionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(PathSeparatorTransitionCondition.class, () ->
        {
            runner.test("matches()", (Test test) ->
            {
                final PathSeparatorTransitionCondition condition = new PathSeparatorTransitionCondition();
                test.assertTrue(condition.matches('/'));
                test.assertTrue(condition.matches('\\'));
                test.assertFalse(condition.matches('a'));
            });
        });
    }
}
