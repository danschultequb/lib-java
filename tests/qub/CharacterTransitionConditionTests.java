package qub;

public class CharacterTransitionConditionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CharacterTransitionCondition.class, () ->
        {
            runner.test("matches()", (Test test) ->
            {
                final CharacterTransitionCondition condition = new CharacterTransitionCondition('a');
                test.assertTrue(condition.matches('a'));
                test.assertFalse(condition.matches('b'));
            });
        });
    }
}
