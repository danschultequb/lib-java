package qub;

public interface LinePrefixCharacterToByteWriteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LinePrefixCharacterToByteWriteStream.class, () ->
        {
            runner.testGroup("create(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LinePrefixCharacterToByteWriteStream.create((ByteWriteStream)null),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    test.assertTrue(innerStream.dispose().await());
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create((ByteWriteStream)innerStream);
                    test.assertNotNull(stream);
                    test.assertTrue(stream.isDisposed());
                    test.assertEqual("", stream.getLinePrefix());
                    test.assertEqual(innerStream.getNewLine(), stream.getNewLine());
                });

                runner.test("with not disposed", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create((ByteWriteStream)innerStream)
                        .setNewLine("\r\n");
                    test.assertNotNull(stream);
                    test.assertFalse(stream.isDisposed());
                    test.assertEqual("", stream.getLinePrefix());
                    test.assertEqual(7, stream.writeLine("hello").await());
                    test.assertEqual("hello\r\n", innerStream.getText().await());
                    test.assertEqual("\r\n", stream.getNewLine());
                });
            });

            runner.testGroup("create(CharacterToByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LinePrefixCharacterToByteWriteStream.create((CharacterToByteWriteStream)null),
                        new PreConditionFailure("characterToByteWriteStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    test.assertTrue(innerStream.dispose().await());
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);
                    test.assertNotNull(stream);
                    test.assertTrue(stream.isDisposed());
                    test.assertEqual("", stream.getLinePrefix());
                    test.assertEqual(innerStream.getNewLine(), stream.getNewLine());
                });

                runner.test("with not disposed", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create()
                        .setNewLine("\r\n");
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);
                    test.assertNotNull(stream);
                    test.assertFalse(stream.isDisposed());
                    test.assertEqual("", stream.getLinePrefix());
                    test.assertEqual(7, stream.writeLine("hello").await());
                    test.assertEqual("hello\r\n", innerStream.getText().await());
                    test.assertEqual(innerStream.getNewLine(), stream.getNewLine());
                });
            });

            runner.testGroup("setLinePrefix(Function0<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    test.assertThrows(() -> stream.setLinePrefix((Function0<String>)null),
                        new PreConditionFailure("linePrefixFunction cannot be null."));
                });

                runner.test("with function that returns null", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix(() -> (String)null);
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertNull(stream.getLinePrefix());
                });

                runner.test("with function that returns empty", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix(() -> "");
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertEqual("", stream.getLinePrefix());
                });

                runner.test("with function that returns non-empty", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix(() -> "hello");
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertEqual("hello", stream.getLinePrefix());
                });

                runner.test("with function that returns a different value each time it is called", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final IntegerValue value = IntegerValue.create(0);
                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix(() -> value.increment().toString());
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertEqual("1", stream.getLinePrefix());
                    test.assertEqual("2", stream.getLinePrefix());
                    test.assertEqual("3", stream.getLinePrefix());
                });
            });

            runner.testGroup("setLinePrefix(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix((String)null);
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertNull(stream.getLinePrefix());
                });

                runner.test("with empty", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix("");
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertEqual("", stream.getLinePrefix());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    final LinePrefixCharacterToByteWriteStream setLinePrefixResult = stream.setLinePrefix("hello");
                    test.assertSame(stream, setLinePrefixResult);

                    test.assertEqual("hello", stream.getLinePrefix());
                });
            });

            runner.testGroup("setNewLine(char)", () ->
            {
                final Action1<Character> setNewLineTest = (Character newLine) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(newLine), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                        final LinePrefixCharacterToByteWriteStream setNewLineResult = stream.setNewLine(newLine);
                        test.assertSame(stream, setNewLineResult);
                        test.assertEqual(newLine.toString(), stream.getNewLine());
                        test.assertEqual(newLine.toString(), innerStream.getNewLine());
                    });
                };

                setNewLineTest.run('\n');
                setNewLineTest.run('\r');
                setNewLineTest.run(' ');
                setNewLineTest.run('a');
            });

            runner.testGroup("setNewLine(char[])", () ->
            {
                final Action1<char[]> setNewLineTest = (char[] newLine) ->
                {
                    runner.test("with " + (newLine == null ? "null" : CharacterList.create(newLine).map(Characters::escapeAndQuote)), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                        final LinePrefixCharacterToByteWriteStream setNewLineResult = stream.setNewLine(newLine);
                        test.assertSame(stream, setNewLineResult);
                        test.assertEqual(newLine == null ? null : CharacterList.create(newLine).toString(true), stream.getNewLine());
                        test.assertEqual(newLine == null ? null : CharacterList.create(newLine).toString(true), innerStream.getNewLine());
                    });
                };

                setNewLineTest.run(null);
                setNewLineTest.run(new char[] { '\n' });
                setNewLineTest.run(new char[] { '\r' });
                setNewLineTest.run(new char[] { '\r', '\n' });
                setNewLineTest.run(new char[] { ' ' });
                setNewLineTest.run(new char[] { 'a' });
            });

            runner.testGroup("setNewLine(String)", () ->
            {
                final Action1<String> setNewLineTest = (String newLine) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(newLine), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                        final LinePrefixCharacterToByteWriteStream setNewLineResult = stream.setNewLine(newLine);
                        test.assertSame(stream, setNewLineResult);
                        test.assertEqual(newLine, stream.getNewLine());
                        test.assertEqual(newLine, innerStream.getNewLine());
                    });
                };

                setNewLineTest.run(null);
                setNewLineTest.run("");
                setNewLineTest.run("\n");
                setNewLineTest.run("\r");
                setNewLineTest.run("\r\n");
                setNewLineTest.run(" ");
                setNewLineTest.run("a");
            });

            runner.testGroup("write(char)", () ->
            {
                final Action5<String,String,String,Character,String> writeTest = (String previousWritten, String newLine, String prefix, Character toWrite, String expected) ->
                {
                    runner.test("with " + English.andList(
                        Strings.escapeAndQuote(previousWritten) + " previously written",
                        Strings.escapeAndQuote(newLine) + " newline",
                        Strings.escapeAndQuote(prefix) + " prefix",
                        Characters.escapeAndQuote(toWrite) + " to write"),
                        (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream)
                            .setNewLine(newLine)
                            .setLinePrefix(prefix);

                        stream.write(previousWritten).await();

                        stream.write(toWrite).await();

                        test.assertEqual(expected, innerStream.getText().await());
                    });
                };

                writeTest.run("", "\n", "PREFIX", 'a', "PREFIXa");
                writeTest.run("a", "\n", "PREFIX", 'b', "PREFIXab");
                writeTest.run("a\n", "\n", "PREFIX", 'b', "PREFIXa\nPREFIXb");
                writeTest.run("a\nb", "\n", "PREFIX", 'c', "PREFIXa\nPREFIXbc");
                writeTest.run("a", "\n", "PREFIX", '\n', "PREFIXa\n");
                writeTest.run("a\n", "\n", "PREFIX", '\n', "PREFIXa\n\n");

                writeTest.run("", "\n", "", 'a', "a");
                writeTest.run("a", "\n", "", 'b', "ab");
                writeTest.run("a\n", "\n", "", 'b', "a\nb");
                writeTest.run("a\nb", "\n", "", 'c', "a\nbc");
                writeTest.run("a", "\n", "", '\n', "a\n");
                writeTest.run("a\n", "\n", "", '\n', "a\n\n");

                writeTest.run("", "\n", null, 'a', "a");
                writeTest.run("a", "\n", null, 'b', "ab");
                writeTest.run("a\n", "\n", null, 'b', "a\nb");
                writeTest.run("a\nb", "\n", null, 'c', "a\nbc");
                writeTest.run("a", "\n", null, '\n', "a\n");
                writeTest.run("a\n", "\n", null, '\n', "a\n\n");
            });

            runner.testGroup("write(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    test.assertThrows(() -> stream.write((char[])null),
                        new PreConditionFailure("toWrite cannot be null."));

                    test.assertEqual("", innerStream.getText().await());
                });

                final Action5<String,String,String,char[],String> writeTest = (String previousWritten, String newLine, String prefix, char[] toWrite, String expected) ->
                {
                    runner.test("with " + English.andList(
                        Strings.escapeAndQuote(previousWritten) + " previously written",
                        Strings.escapeAndQuote(newLine) + " newline",
                        Strings.escapeAndQuote(prefix) + " prefix",
                        CharacterList.create(toWrite).map(Characters::escapeAndQuote) + " to write"),
                        (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream)
                            .setNewLine(newLine)
                            .setLinePrefix(prefix);

                        stream.write(previousWritten).await();

                        stream.write(toWrite).await();

                        test.assertEqual(expected, innerStream.getText().await());
                    });
                };

                writeTest.run("", "\n", "PREFIX", new char[0], "");
                writeTest.run("a", "\n", "PREFIX", new char[] { 'b' }, "PREFIXab");
                writeTest.run("a\n", "\n", "PREFIX", new char[] { 'b' }, "PREFIXa\nPREFIXb");
                writeTest.run("a\nb", "\n", "PREFIX", new char[] { 'c' }, "PREFIXa\nPREFIXbc");
                writeTest.run("a", "\n", "PREFIX", new char[] { '\n' }, "PREFIXa\n");
                writeTest.run("a\n", "\n", "PREFIX", new char[] { '\n' }, "PREFIXa\n\n");
                writeTest.run("a", "\n", "PREFIX", new char[] { '\n', 'b' }, "PREFIXa\nPREFIXb");
                writeTest.run("a", "\n", "PREFIX", new char[] { '\n', 'b', '\r', '\n' }, "PREFIXa\nPREFIXb\r\n");
                writeTest.run("a", "\n", "PREFIX", new char[] { '\n', 'b', '\r', '\n', 'c' }, "PREFIXa\nPREFIXb\r\nPREFIXc");

                writeTest.run("", "\n", "", new char[0], "");
                writeTest.run("a", "\n", "", new char[] { 'b' }, "ab");
                writeTest.run("a\n", "\n", "", new char[] { 'b' }, "a\nb");
                writeTest.run("a\nb", "\n", "", new char[] { 'c' }, "a\nbc");
                writeTest.run("a", "\n", "", new char[] { '\n' }, "a\n");
                writeTest.run("a\n", "\n", "", new char[] { '\n' }, "a\n\n");
                writeTest.run("a", "\n", "", new char[] { '\n', 'b' }, "a\nb");
                writeTest.run("a", "\n", "", new char[] { '\n', 'b', '\r', '\n' }, "a\nb\r\n");
                writeTest.run("a", "\n", "", new char[] { '\n', 'b', '\r', '\n', 'c' }, "a\nb\r\nc");

                writeTest.run("", "\n", null, new char[0], "");
                writeTest.run("a", "\n", null, new char[] { 'b' }, "ab");
                writeTest.run("a\n", "\n", null, new char[] { 'b' }, "a\nb");
                writeTest.run("a\nb", "\n", null, new char[] { 'c' }, "a\nbc");
                writeTest.run("a", "\n", null, new char[] { '\n' }, "a\n");
                writeTest.run("a\n", "\n", null, new char[] { '\n' }, "a\n\n");
                writeTest.run("a", "\n", null, new char[] { '\n', 'b' }, "a\nb");
                writeTest.run("a", "\n", null, new char[] { '\n', 'b', '\r', '\n' }, "a\nb\r\n");
                writeTest.run("a", "\n", null, new char[] { '\n', 'b', '\r', '\n', 'c' }, "a\nb\r\nc");
            });

            runner.testGroup("write(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                    final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream);

                    test.assertThrows(() -> stream.write((String)null),
                        new PreConditionFailure("toWrite cannot be null."));

                    test.assertEqual("", innerStream.getText().await());
                });

                final Action5<String,String,String,String,String> writeTest = (String previousWritten, String newLine, String prefix, String toWrite, String expected) ->
                {
                    runner.test("with " + English.andList(
                        Strings.escapeAndQuote(previousWritten) + " previously written",
                        Strings.escapeAndQuote(newLine) + " newline",
                        Strings.escapeAndQuote(prefix) + " prefix",
                        Strings.escapeAndQuote(toWrite) + " to write"),
                        (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream)
                            .setNewLine(newLine)
                            .setLinePrefix(prefix);

                        stream.write(previousWritten).await();

                        stream.write(toWrite).await();

                        test.assertEqual(expected, innerStream.getText().await());
                    });
                };

                writeTest.run("", "\n", "PREFIX", "", "");
                writeTest.run("a", "\n", "PREFIX", "b", "PREFIXab");
                writeTest.run("a\n", "\n", "PREFIX", "b", "PREFIXa\nPREFIXb");
                writeTest.run("a\nb", "\n", "PREFIX", "c", "PREFIXa\nPREFIXbc");
                writeTest.run("a", "\n", "PREFIX", "\n", "PREFIXa\n");
                writeTest.run("a\n", "\n", "PREFIX", "\n", "PREFIXa\n\n");
                writeTest.run("a", "\n", "PREFIX", "\nb", "PREFIXa\nPREFIXb");
                writeTest.run("a", "\n", "PREFIX", "\nb\r\n", "PREFIXa\nPREFIXb\r\n");
                writeTest.run("a", "\n", "PREFIX", "\nb\r\nc", "PREFIXa\nPREFIXb\r\nPREFIXc");

                writeTest.run("", "\n", "", "", "");
                writeTest.run("a", "\n", "", "b", "ab");
                writeTest.run("a\n", "\n", "", "b", "a\nb");
                writeTest.run("a\nb", "\n", "", "c", "a\nbc");
                writeTest.run("a", "\n", "", "\n", "a\n");
                writeTest.run("a\n", "\n", "", "\n", "a\n\n");
                writeTest.run("a", "\n", "", "\nb", "a\nb");
                writeTest.run("a", "\n", "", "\nb\r\n", "a\nb\r\n");
                writeTest.run("a", "\n", "", "\nb\r\nc", "a\nb\r\nc");

                writeTest.run("", "\n", null, "", "");
                writeTest.run("a", "\n", null, "b", "ab");
                writeTest.run("a\n", "\n", null, "b", "a\nb");
                writeTest.run("a\nb", "\n", null, "c", "a\nbc");
                writeTest.run("a", "\n", null, "\n", "a\n");
                writeTest.run("a\n", "\n", null, "\n", "a\n\n");
                writeTest.run("a", "\n", null, "\nb", "a\nb");
                writeTest.run("a", "\n", null, "\nb\r\n", "a\nb\r\n");
                writeTest.run("a", "\n", null, "\nb\r\nc", "a\nb\r\nc");
            });

            runner.testGroup("writeLine()", () ->
            {
                final Action4<String,String,String,String> writeTest = (String previousWritten, String newLine, String prefix, String expected) ->
                {
                    runner.test("with " + English.andList(
                        Strings.escapeAndQuote(previousWritten) + " previously written",
                        Strings.escapeAndQuote(newLine) + " newline",
                        Strings.escapeAndQuote(prefix) + " prefix"),
                        (Test test) ->
                    {
                        final InMemoryCharacterToByteStream innerStream = InMemoryCharacterToByteStream.create();
                        final LinePrefixCharacterToByteWriteStream stream = LinePrefixCharacterToByteWriteStream.create(innerStream)
                            .setNewLine(newLine)
                            .setLinePrefix(prefix);

                        stream.write(previousWritten).await();

                        stream.writeLine().await();

                        test.assertEqual(expected, innerStream.getText().await());
                    });
                };

                writeTest.run("", "\n", "PREFIX", "\n");
                writeTest.run("a", "\n", "PREFIX", "PREFIXa\n");
                writeTest.run("a\n", "\n", "PREFIX", "PREFIXa\n\n");
                writeTest.run("a\nb", "\n", "PREFIX", "PREFIXa\nPREFIXb\n");

                writeTest.run("", "\n", "", "\n");
                writeTest.run("a", "\n", "", "a\n");
                writeTest.run("a\n", "\n", "", "a\n\n");
                writeTest.run("a\nb", "\n", "", "a\nb\n");

                writeTest.run("", "\n", null, "\n");
                writeTest.run("a", "\n", null, "a\n");
                writeTest.run("a\n", "\n", null, "a\n\n");
                writeTest.run("a\nb", "\n", null, "a\nb\n");
            });
        });
    }
}
