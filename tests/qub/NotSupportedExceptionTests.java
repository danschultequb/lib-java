package qub;

public interface NotSupportedExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(NotSupportedException.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final NotSupportedException e = new NotSupportedException();
                test.assertNotNull(e);
                test.assertEqual("This feature is not yet supported.", e.getMessage());
                test.assertNull(e.getCause());
            });

            runner.testGroup("constructor(String)", () ->
            {
                final Action2<String,Throwable> constructorErrorTest = (String message, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(message), (Test test) ->
                    {
                        test.assertThrows(() -> new NotSupportedException(message),
                            expected);
                    });
                };

                constructorErrorTest.run(null, new PreConditionFailure("message cannot be null."));
                constructorErrorTest.run("", new PreConditionFailure("message cannot be empty."));

                final Action1<String> constructorTest = (String message) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(message), (Test test) ->
                    {
                        NotSupportedException e = new NotSupportedException(message);
                        test.assertNotNull(e);
                        test.assertEqual(message, e.getMessage());
                        test.assertNull(e.getCause());
                    });
                };

                constructorTest.run("hello");
            });
        });
    }
}
