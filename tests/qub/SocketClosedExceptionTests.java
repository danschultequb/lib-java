package qub;

public interface SocketClosedExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SocketClosedException.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final SocketClosedException e = new SocketClosedException();
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
                        final SocketClosedException e = new SocketClosedException(message);
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
                final Action1<Throwable> constructorTest = (Throwable innerException) ->
                {
                    runner.test("with " + innerException, (Test test) ->
                    {
                        final SocketClosedException e = new SocketClosedException(innerException);
                        test.assertNotNull(e);
                        test.assertEqual(innerException == null ? null : innerException.toString(), e.getMessage());
                        test.assertSame(innerException, e.getCause());
                    });
                };

                constructorTest.run(null);
                constructorTest.run(new NotFoundException("hello"));
            });

            runner.testGroup("constructor(String,Throwable)", () ->
            {
                final Action2<String,Throwable> constructorTest = (String message, Throwable innerException) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(message), innerException), (Test test) ->
                    {
                        final SocketClosedException e = new SocketClosedException(message, innerException);
                        test.assertNotNull(e);
                        test.assertEqual(message, e.getMessage());
                        test.assertSame(innerException, e.getCause());
                    });
                };

                constructorTest.run(null, null);
                constructorTest.run("", null);
                constructorTest.run("hello", null);

                constructorTest.run(null, new NotFoundException("hello"));
                constructorTest.run("", new NotFoundException("hello"));
                constructorTest.run("hello", new NotFoundException("hello"));
            });
        });
    }
}
