package qub;

public class LineReadStreamTests
{
    public static void test(TestRunner runner, Function2<String,Boolean,LineReadStream> creatorWithIncludeNewLines)
    {
        final Function1<String,LineReadStream> creator = (String text) -> creatorWithIncludeNewLines.run(text, false);

        runner.testGroup(LineReadStream.class, () ->
        {
            runner.testGroup("constructor(String,boolean)", () ->
            {
                runner.test("with null and false", (Test test) ->
                {
                    final LineReadStream lineReadStream = creatorWithIncludeNewLines.run(null, false);
                    assertLineReadStream(test, lineReadStream, false, false, null);
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                });

                runner.test("with null and true", (Test test) ->
                {
                    final LineReadStream lineReadStream = creatorWithIncludeNewLines.run(null, true);
                    assertLineReadStream(test, lineReadStream, false, false, null);
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertTrue(lineReadStream.getIncludeNewLines());
                });
            });

            runner.test("close()", (Test test) ->
            {
                final LineReadStream lineReadStream = creator.run(null);
                lineReadStream.close();
                assertLineReadStream(test, lineReadStream, true, false, null);

                lineReadStream.close();
                assertLineReadStream(test, lineReadStream, true, false, null);
            });

            runner.testGroup("readLine()", () ->
            {
                runner.test("when closed", (Test test) ->
                {
                    final LineReadStream lineReadStream = creator.run("test");
                    lineReadStream.close();
                    test.assertError(new IllegalArgumentException("lineReadStream must not be disposed."), lineReadStream.readLine());
                });

                runner.testGroup("check CharacterReadStream.getCurrent()", () ->
                {
                    runner.test("with null and false include new lines", (Test test) ->
                    {
                        try (final LineReadStream lineReadStream = creatorWithIncludeNewLines.run(null, false))
                        {
                            final CharacterReadStream characterReadStream = lineReadStream.asCharacterReadStream();
                            lineReadStream.readLine();
                            test.assertNull(characterReadStream.getCurrent());
                        }
                    });

                    runner.test("with " + Strings.escapeAndQuote("") + " and false include new lines", (Test test) ->
                    {
                        try (final LineReadStream lineReadStream = creatorWithIncludeNewLines.run("", false))
                        {
                            final CharacterReadStream characterReadStream = lineReadStream.asCharacterReadStream();
                            lineReadStream.readLine();
                            test.assertNull(characterReadStream.getCurrent());
                        }
                    });

                    runner.test("with " + Strings.escapeAndQuote("abc") + " and false include new lines", (Test test) ->
                    {
                        try (final LineReadStream lineReadStream = creatorWithIncludeNewLines.run("abc", false))
                        {
                            final CharacterReadStream characterReadStream = lineReadStream.asCharacterReadStream();
                            lineReadStream.readLine();
                            test.assertNull(characterReadStream.getCurrent());
                        }
                    });

                    runner.test("with " + Strings.escapeAndQuote("a\n") + " and false include new lines", (Test test) ->
                    {
                        try (final LineReadStream lineReadStream = creatorWithIncludeNewLines.run("a\n", false))
                        {
                            final CharacterReadStream characterReadStream = lineReadStream.asCharacterReadStream();
                            lineReadStream.readLine();
                            test.assertEqual(Strings.escape('\n'), Strings.escape(characterReadStream.getCurrent()));

                            lineReadStream.readLine();
                            test.assertNull(characterReadStream.getCurrent());
                        }
                    });
                });

                final Action3<String,Boolean,String[]> readLineTest = (String text, Boolean includeNewLines, String[] expectedLines) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + includeNewLines + " include new lines", (Test test) ->
                    {
                        try (final LineReadStream lineReadStream = creatorWithIncludeNewLines.run(text, includeNewLines))
                        {
                            final int expectedLineCount = expectedLines == null ? 0 : expectedLines.length;
                            for (int i = 0; i < expectedLineCount; ++i)
                            {
                                final Result<String> readLineResult = lineReadStream.readLine();
                                test.assertSuccess(readLineResult);
                                test.assertEqual(Strings.escape(expectedLines[i]), Strings.escape(readLineResult.getValue()));
                                assertLineReadStream(test, lineReadStream, false, true, expectedLines[i]);
                            }

                            test.assertSuccess(lineReadStream.readLine());
                            assertLineReadStream(test, lineReadStream, false, true, null);
                        }
                    });
                };

                readLineTest.run(null, false, new String[0]);
                readLineTest.run("hello", false, new String[] { "hello" });
                readLineTest.run("\n", false, new String[] { "" });
                readLineTest.run("\r\n", false, new String[] { "" });
                readLineTest.run("a\n", false, new String[] { "a" });
                readLineTest.run("a\r", false, new String[] { "a\r" });
                readLineTest.run("a\r\n", false, new String[] { "a" });
                readLineTest.run("\nb", false, new String[] { "", "b" });
                readLineTest.run("\r\nb", false, new String[] { "", "b" });
                readLineTest.run("a\nb", false, new String[] { "a", "b" });
                readLineTest.run("a\r\nb", false, new String[] { "a", "b" });
                readLineTest.run("a\r\r\r\r\rb", false, new String[] { "a\r\r\r\r\rb"});
                readLineTest.run("a\r\r\r\r\r\n", false, new String[] { "a\r\r\r\r"});
                readLineTest.run("a\nb\r\nc", false, new String[] { "a", "b", "c" });

                readLineTest.run(null, true, new String[0]);
                readLineTest.run("hello", true, new String[] { "hello" });
                readLineTest.run("\n", true, new String[] { "\n" });
                readLineTest.run("\r\n", true, new String[] { "\r\n" });
                readLineTest.run("a\n", true, new String[] { "a\n" });
                readLineTest.run("a\r", true, new String[] { "a\r" });
                readLineTest.run("a\r\n", true, new String[] { "a\r\n" });
                readLineTest.run("\nb", true, new String[] { "\n", "b" });
                readLineTest.run("\r\nb", true, new String[] { "\r\n", "b" });
                readLineTest.run("a\nb", true, new String[] { "a\n", "b" });
                readLineTest.run("a\r\nb", true, new String[] { "a\r\n", "b" });
                readLineTest.run("a\r\r\r\r\rb", true, new String[] { "a\r\r\r\r\rb"});
                readLineTest.run("a\r\r\r\r\r\n", true, new String[] { "a\r\r\r\r\r\n"});
                readLineTest.run("a\nb\r\nc", true, new String[] { "a\n", "b\r\n", "c" });
            });

            runner.test("next() with true includeNewLines", (Test test) ->
            {
                final LineReadStream lineReadStream = creatorWithIncludeNewLines.run("a\nb\r\nc", true);

                test.assertTrue(lineReadStream.next());
                assertLineReadStream(test, lineReadStream, false, true, "a\n");

                test.assertTrue(lineReadStream.next());
                assertLineReadStream(test, lineReadStream, false, true, "b\r\n");

                test.assertTrue(lineReadStream.next());
                assertLineReadStream(test, lineReadStream, false, true, "c");

                test.assertFalse(lineReadStream.next());
                assertLineReadStream(test, lineReadStream, false, true, null);
            });

            runner.test("asCharacterReadStream()", (Test test) ->
            {
                final LineReadStream lineReadStream = creator.run("test");
                test.assertNotSame(lineReadStream, lineReadStream.asCharacterReadStream().asLineReadStream());
            });

            runner.test("asByteReadStream()", (Test test) ->
            {
                final LineReadStream lineReadStream = creator.run("test");
                test.assertNotSame(lineReadStream, lineReadStream.asByteReadStream().asLineReadStream());
            });
        });
    }

    private static void assertLineReadStream(Test test, LineReadStream lineReadStream, boolean isDisposed, boolean hasStarted, String current)
    {
        test.assertNotNull(lineReadStream);
        test.assertEqual(isDisposed, lineReadStream.isDisposed());
        test.assertEqual(hasStarted, lineReadStream.hasStarted());
        test.assertEqual(current != null, lineReadStream.hasCurrent());
        test.assertEqual(Strings.escape(current), Strings.escape(lineReadStream.getCurrent()));
    }
}
