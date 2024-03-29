package qub;

public interface PostConditionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(PostCondition.class, () ->
        {
            runner.testGroup("assertCondition(boolean,String)", () ->
            {
                final Action3<Boolean,String,Throwable> assertConditionErrorTest = (Boolean condition, String message, Throwable expected) ->
                {
                    runner.test("with " + English.andList(condition, Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        test.assertThrows(() -> PostCondition.assertCondition(condition, message),
                            expected);
                    });
                };

                assertConditionErrorTest.run(false, null, new PostConditionFailure(null));
                assertConditionErrorTest.run(false, "", new PostConditionFailure(""));
                assertConditionErrorTest.run(false, "hello", new PostConditionFailure("hello"));

                final Action2<Boolean,String> assertConditionTest = (Boolean condition, String message) ->
                {
                    runner.test("with " + English.andList(condition, Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        PostCondition.assertCondition(condition, message);
                    });
                };

                assertConditionTest.run(true, null);
                assertConditionTest.run(true, "");
                assertConditionTest.run(true, "hello");
            });

            runner.testGroup("assertSame(T,T,String)", () ->
            {
                runner.test("with same reference", (Test test) ->
                {
                    final Integer value = 5;
                    PostCondition.assertSame(value, value, "value");
                });

                runner.test("with not same reference", (Test test) ->
                {
                    @SuppressWarnings("removal")
                    final Integer value1 = new Integer(5);
                    @SuppressWarnings("removal")
                    final Integer value2 = new Integer(5);
                    test.assertThrows(() -> PostCondition.assertSame(value1, value2, "value2"),
                        new PostConditionFailure("value2 (5) must be the same object as 5."));
                });

                runner.test("with constant String references", (Test test) ->
                {
                    final String value1 = "hello";
                    final String value2 = "hello";
                    PostCondition.assertSame(value1, value2, "value2");
                });

                runner.test("with constant Integer references", (Test test) ->
                {
                    final Integer value1 = 5;
                    final Integer value2 = 5;
                    PostCondition.assertSame(value1, value2, "value2");
                });
            });

            runner.testGroup("assertNotSame(T,T,String)", () ->
            {
                runner.test("with same reference", (Test test) ->
                {
                    final Integer value = 5;
                    test.assertThrows(() -> PostCondition.assertNotSame(value, value, "value"),
                        new PostConditionFailure("value (5) must not be the same object as 5."));
                });

                runner.test("with not same reference", (Test test) ->
                {
                    @SuppressWarnings("removal")
                    final Integer value1 = new Integer(5);
                    @SuppressWarnings("removal")
                    final Integer value2 = new Integer(5);
                    PostCondition.assertNotSame(value1, value2, "value2");
                });

                runner.test("with constant String references", (Test test) ->
                {
                    final String value1 = "hello";
                    final String value2 = "hello";
                    test.assertThrows(() -> PostCondition.assertNotSame(value1, value2, "value2"),
                        new PostConditionFailure("value2 (hello) must not be the same object as hello."));
                });

                runner.test("with constant Integer references", (Test test) ->
                {
                    final Integer value1 = 5;
                    final Integer value2 = 5;
                    test.assertThrows(() -> PostCondition.assertNotSame(value1, value2, "value2"),
                        new PostConditionFailure("value2 (5) must not be the same object as 5."));
                });
            });
        });
    }
}
