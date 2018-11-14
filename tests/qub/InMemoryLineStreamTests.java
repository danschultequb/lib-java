package qub;

public class InMemoryLineStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryLineStream.class, () ->
        {
            LineReadStreamTests.test(runner, (String text, Boolean includeNewLines) -> new InMemoryLineStream(text, includeNewLines).endOfStream());
            AsyncDisposableTests.test(runner, (Test test) -> new InMemoryLineStream("", test.getMainAsyncRunner()));

            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryLineStream lineReadStream = new InMemoryLineStream(null).endOfStream();
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final InMemoryLineStream lineReadStream = new InMemoryLineStream("").endOfStream();
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with \"abcd\"", (Test test) ->
                {
                    final InMemoryLineStream lineReadStream = new InMemoryLineStream("abcd").endOfStream();
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess("abcd", lineReadStream.readLine());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with \"\\n\\n\"", (Test test) ->
                {
                    final InMemoryLineStream lineReadStream = new InMemoryLineStream("\n\n").endOfStream();
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertSuccess("", lineReadStream.readLine());
                    test.assertSuccess("", lineReadStream.readLine());
                    test.assertSuccess(null, lineReadStream.readLine());
                });

                runner.test("with " + Strings.escapeAndQuote("\r\na\rb\r\nc\r\r\n"), (Test test) ->
                {
                    final InMemoryLineStream lineReadStream = new InMemoryLineStream("\r\na\rb\r\nc\r\r\n").endOfStream();
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
                final InMemoryLineStream lineReadStream = new InMemoryLineStream("a\nb\nc");
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
                test.assertThrows(lineReadStream::readLine);
                test.assertNull(lineReadStream.getCurrent());
            });

            runner.test("getText()", (Test test) ->
            {
                final InMemoryLineStream lineStream = new InMemoryLineStream();
                test.assertSuccess("", lineStream.getText());

                lineStream.writeLine("hello");
                test.assertSuccess("hello\n", lineStream.getText());
            });
        });
    }
}
