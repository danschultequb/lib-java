package qub;

public interface EmptyExceptionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(EmptyException.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final EmptyException e = new EmptyException();
                test.assertNotNull(e);
                test.assertNull(e.getMessage());
                test.assertNull(e.getCause());
            });

            runner.testGroup("constructor(String)", () ->
            {
                final Action1<String> constructorTest = (String message) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(message), (Test test) ->
                    {
                        final EmptyException e = new EmptyException(message);
                        test.assertNotNull(e);
                        test.assertEqual(message, e.getMessage());
                        test.assertNull(e.getCause());
                    });
                };

                constructorTest.run(null);
                constructorTest.run("");
                constructorTest.run("hello");
            });

            runner.testGroup("constructor(Throwable)", () ->
            {
                final Action1<Throwable> constructorTest = (Throwable cause) ->
                {
                    runner.test("with " + Objects.toString(cause), (Test test) ->
                    {
                        final EmptyException e = new EmptyException(cause);
                        test.assertNotNull(e);
                        test.assertEqual(cause == null ? null : cause.toString(), e.getMessage());
                        test.assertSame(cause, e.getCause());
                    });
                };

                constructorTest.run(null);
                constructorTest.run(new NotFoundException("oops"));
            });

            runner.testGroup("constructor(String,Throwable)", () ->
            {
                final Action2<String,Throwable> constructorTest = (String message, Throwable cause) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(message), cause), (Test test) ->
                    {
                        final EmptyException e = new EmptyException(message, cause);
                        test.assertNotNull(e);
                        test.assertEqual(message, e.getMessage());
                        test.assertSame(cause, e.getCause());
                    });
                };

                constructorTest.run(null, null);
                constructorTest.run("", null);
                constructorTest.run("hello", null);
                constructorTest.run(null, new NotFoundException("oops"));
                constructorTest.run("", new NotFoundException("oops"));
                constructorTest.run("hello", new NotFoundException("oops"));
            });
        });
    }
}
