package qub;

public class InMemoryLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryLineReadStream.class, () ->
        {
            LineReadStreamTests.test(runner, InMemoryLineReadStream::new);
            AsyncDisposableTests.test(runner, (AsyncRunner asyncRunner) -> new InMemoryLineReadStream("", asyncRunner));

            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream(null);
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with \"abcd\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("abcd");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess("abcd", lineReadStream.readLine());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with \"\\n\\n\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("\n\n");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess("", lineReadStream.readLine());
                    test.assertSuccess("", lineReadStream.readLine());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with " + Strings.escapeAndQuote("\r\na\rb\r\nc\r\r\n"), (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("\r\na\rb\r\nc\r\r\n");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess("", lineReadStream.readLine());
                    test.assertSuccess("a\rb", lineReadStream.readLine());
                    test.assertSuccess("c\r", lineReadStream.readLine());
                    test.assertSuccess(null, lineReadStream.readLine());
                });
            });

            runner.test("close()", (Test test) ->
            {
                final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("a\nb\nc");
                test.assertFalse(lineReadStream.isDisposed());
                test.assertSuccess("a", lineReadStream.readLine());
                test.assertEqual("a", lineReadStream.getCurrent());

                try
                {
                    lineReadStream.close();
                }
                catch (Exception e)
                {
                    test.fail(e);
                }
                test.assertTrue(lineReadStream.isDisposed());
                test.assertNull(lineReadStream.getCurrent());
                test.assertError(new IllegalArgumentException("lineReadStream must not be disposed."), lineReadStream.readLine());
                test.assertNull(lineReadStream.getCurrent());
            });
        });
    }
}
