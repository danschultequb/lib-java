package qub;

public class CharacterTransitionConditionTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CharacterTransitionCondition", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("matches()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final CharacterTransitionCondition condition = new CharacterTransitionCondition('a');
                        test.assertTrue(condition.matches('a'));
                        test.assertFalse(condition.matches('b'));
                    }
                });
            }
        });
    }
}
