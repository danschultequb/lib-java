package qub;

public class InMemoryLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryLineReadStream.class, () ->
        {
            LineReadStreamTests.test(runner, InMemoryLineReadStream::new);

            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream(null);
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual(null, lineReadStream.readLine());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual(null, lineReadStream.readLine());
                });

                runner.test("with \"abcd\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("abcd");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual("abcd", lineReadStream.readLine());
                    test.assertEqual(null, lineReadStream.readLine());
                });

                runner.test("with \"\\n\\n\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("\n\n");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual("", lineReadStream.readLine());
                    test.assertEqual("", lineReadStream.readLine());
                    test.assertEqual(null, lineReadStream.readLine());
                });

                runner.test("with " + Strings.escapeAndQuote("\r\na\rb\r\nc\r\r\n"), (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("\r\na\rb\r\nc\r\r\n");
                    test.assertFalse(lineReadStream.isDisposed());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual("", lineReadStream.readLine());
                    test.assertEqual("a\rb", lineReadStream.readLine());
                    test.assertEqual("c\r", lineReadStream.readLine());
                    test.assertEqual(null, lineReadStream.readLine());
                });
            });

            runner.test("close()", (Test test) ->
            {
                final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("a\nb\nc");
                test.assertFalse(lineReadStream.isDisposed());
                test.assertEqual("a", lineReadStream.readLine());
                test.assertEqual("a", lineReadStream.getCurrent());

                lineReadStream.close();
                test.assertTrue(lineReadStream.isDisposed());
                test.assertNull(lineReadStream.getCurrent());
                test.assertNull(lineReadStream.readLine());
                test.assertNull(lineReadStream.getCurrent());
            });
        });
    }
}
