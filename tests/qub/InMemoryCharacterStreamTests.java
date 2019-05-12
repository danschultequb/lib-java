package qub;

public class InMemoryCharacterStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryCharacterStream.class, () ->
        {
            runner.testGroup("readCharacter()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(characterReadStream::readCharacter,
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertNull(characterReadStream.readCharacter().await());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("d");
                    test.assertEqual('d', characterReadStream.readCharacter().await());
                    test.assertNull(characterReadStream.readCharacter().await());
                });

                runner.test("with two characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("ef");
                    test.assertEqual('e', characterReadStream.readCharacter().await());
                    test.assertEqual('f', characterReadStream.readCharacter().await());
                    test.assertNull(characterReadStream.readCharacter().await());
                });
            });

            runner.testGroup("readCharacters(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(() -> characterReadStream.readCharacters(5),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with negative charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(() -> characterReadStream.readCharacters(-1),
                        new PreConditionFailure("charactersToRead (-1) must be greater than 0."));
                });

                runner.test("with zero charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(() -> characterReadStream.readCharacters(0),
                        new PreConditionFailure("charactersToRead (0) must be greater than 0."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertNull(characterReadStream.readCharacters(5).await());
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc");
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(5).await());
                    test.assertNull(characterReadStream.readCharacters(1).await());
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcd");
                    test.assertEqual(new char[] { 'a', 'b', 'c', 'd' }, characterReadStream.readCharacters(4).await());
                    test.assertNull(characterReadStream.readCharacters(1).await());
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcdefghi");
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(3).await());
                    test.assertEqual(new char[] { 'd' }, characterReadStream.readCharacters(1).await());
                    test.assertEqual(new char[] { 'e', 'f', 'g', 'h', 'i' }, characterReadStream.readCharacters(1000).await());
                });
            });

            runner.testGroup("readCharacters(char[])", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertTrue(characterReadStream.dispose().await());
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = null;
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters),
                        new PreConditionFailure("outputCharacters cannot be null."));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[0];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters),
                        new PreConditionFailure("outputCharacters cannot be empty."));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[5];
                    test.assertNull(characterReadStream.readCharacters(outputCharacters).await());
                });

                runner.test("with fewer characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc");
                    final char[] outputCharacters = new char[5];
                    test.assertEqual(3, characterReadStream.readCharacters(outputCharacters).await());
                    test.assertEqual(new char[] { 'a', 'b', 'c', '\0', '\0' }, outputCharacters);
                    test.assertNull(characterReadStream.readCharacter().await());
                });

                runner.test("with equal characters to read to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defgh");
                    final char[] outputCharacters = new char[5];
                    test.assertEqual(5, characterReadStream.readCharacters(outputCharacters).await());
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertNull(characterReadStream.readCharacter().await());
                });

                runner.test("with more characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defghij");
                    final char[] outputCharacters = new char[5];
                    test.assertEqual(5, characterReadStream.readCharacters(outputCharacters).await());
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertEqual('i', characterReadStream.readCharacter().await());
                });
            });

            runner.testGroup("readCharacters(char[],int,int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertTrue(characterReadStream.dispose().await());
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 3),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = null;
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 3),
                        new PreConditionFailure("outputCharacters cannot be null."));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[0];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 3),
                        new PreConditionFailure("outputCharacters cannot be empty."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, -10, 3),
                        new PreConditionFailure("startIndex (-10) must be between 0 and 5."));
                });

                runner.test("with startIndex equal to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, outputCharacters.length, 3),
                        new PreConditionFailure("startIndex (6) must be between 0 and 5."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, -50),
                        new PreConditionFailure("length (-50) must be between 1 and 4."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 0),
                        new PreConditionFailure("length (0) must be between 1 and 4."));
                });

                runner.test("with length greater than outputCharacters.length - startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 5),
                        new PreConditionFailure("length (5) must be between 1 and 4."));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertNull(characterReadStream.readCharacters(outputCharacters, 2, 3).await());
                });

                runner.test("with fewer characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("a");
                    final char[] outputCharacters = new char[6];
                    test.assertEqual(1, characterReadStream.readCharacters(outputCharacters, 2, 3).await());
                    test.assertEqual(new char[] { '\0', '\0', 'a', '\0', '\0', '\0' }, outputCharacters);
                    test.assertNull(characterReadStream.readCharacter().await());
                });

                runner.test("with equal characters to read to length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("def");
                    final char[] outputCharacters = new char[6];
                    test.assertEqual(3, characterReadStream.readCharacters(outputCharacters, 2, 3).await());
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', '\0' }, outputCharacters);
                    test.assertNull(characterReadStream.readCharacter().await());
                });

                runner.test("with more characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defghij");
                    final char[] outputCharacters = new char[6];
                    test.assertEqual(4, characterReadStream.readCharacters(outputCharacters, 2, 4).await());
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', 'g' }, outputCharacters);
                    test.assertEqual('h', characterReadStream.readCharacter().await());
                });
            });

            runner.testGroup("readAllCharacters()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertTrue(characterReadStream.dispose().await());
                    test.assertThrows(characterReadStream::readAllCharacters,
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("");
                    test.assertEqual(new char[0], characterReadStream.readAllCharacters().await());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("f");
                    test.assertEqual(new char[] { 'f' }, characterReadStream.readAllCharacters().await());
                });

                runner.test("with multiple characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("fedcb");
                    test.assertEqual(new char[] { 'f', 'e', 'd', 'c', 'b' }, characterReadStream.readAllCharacters().await());
                });
            });

            runner.testGroup("readString(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(() -> characterReadStream.readString(5),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertNull(characterReadStream.readString(5).await());
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc");
                    test.assertEqual("abc", characterReadStream.readString(5).await());
                    test.assertNull(characterReadStream.readString(1).await());
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcd");
                    test.assertEqual("abcd", characterReadStream.readString(4).await());
                    test.assertNull(characterReadStream.readString(1).await());
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcdefghi");
                    test.assertEqual("abc", characterReadStream.readString(3).await());
                    test.assertEqual("d", characterReadStream.readString(1).await());
                    test.assertEqual("efghi", characterReadStream.readString(1000).await());
                });
            });

            runner.testGroup("readLine()", () ->
            {
                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertTrue(characterReadStream.dispose().await());
                    test.assertThrows(characterReadStream::readLine,
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                final Action2<String,Iterable<String>> readLineTest = (String text, Iterable<String> expectedLines) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final InMemoryCharacterStream stream = createStream(text);
                        int lineNumber = 0;
                        for (final String expectedLine : expectedLines)
                        {
                            ++lineNumber;
                            test.assertEqual(expectedLine, stream.readLine().await(), "Wrong line " + lineNumber);
                        }
                        test.assertThrows(() -> stream.readLine().await(), new EndOfStreamException());
                    });
                };

                readLineTest.run("", Iterable.create());
                readLineTest.run("hello", Iterable.create("hello"));
                readLineTest.run("hello\n", Iterable.create("hello"));
                readLineTest.run("a\r", Iterable.create("a\r"));
                readLineTest.run("a\r\n", Iterable.create("a"));
                readLineTest.run("a\r\r\n", Iterable.create("a\r"));
                readLineTest.run("\n\n\n", Iterable.create("", "", ""));
                readLineTest.run("a\nb", Iterable.create("a", "b"));
                readLineTest.run("a\rb", Iterable.create("a\rb"));
            });

            runner.testGroup("readLine(boolean)", () ->
            {
                runner.testGroup("with false includeNewLine", () ->
                {
                    runner.test("with disposed", (Test test) ->
                    {
                        final InMemoryCharacterStream characterReadStream = createStream();
                        test.assertTrue(characterReadStream.dispose().await());
                        test.assertThrows(characterReadStream::readLine,
                            new PreConditionFailure("isDisposed() cannot be true."));
                    });

                    final Action2<String,Iterable<String>> readLineTest = (String text, Iterable<String> expectedLines) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                        {
                            final InMemoryCharacterStream stream = createStream(text);
                            int lineNumber = 0;
                            for (final String expectedLine : expectedLines)
                            {
                                ++lineNumber;
                                test.assertEqual(expectedLine, stream.readLine(false).await(), "Wrong line " + lineNumber);
                            }
                            test.assertThrows(() -> stream.readLine(false).await(), new EndOfStreamException());
                        });
                    };

                    readLineTest.run("", Iterable.create());
                    readLineTest.run("hello", Iterable.create("hello"));
                    readLineTest.run("hello\n", Iterable.create("hello"));
                    readLineTest.run("a\r", Iterable.create("a\r"));
                    readLineTest.run("a\r\n", Iterable.create("a"));
                    readLineTest.run("a\r\r\n", Iterable.create("a\r"));
                    readLineTest.run("\n\n\n", Iterable.create("", "", ""));
                    readLineTest.run("a\nb", Iterable.create("a", "b"));
                    readLineTest.run("a\rb", Iterable.create("a\rb"));
                });

                runner.testGroup("with true includeNewLine", () ->
                {
                    runner.test("with disposed", (Test test) ->
                    {
                        final InMemoryCharacterStream characterReadStream = createStream();
                        test.assertTrue(characterReadStream.dispose().await());
                        test.assertThrows(characterReadStream::readLine,
                            new PreConditionFailure("isDisposed() cannot be true."));
                    });

                    final Action2<String,Iterable<String>> readLineTest = (String text, Iterable<String> expectedLines) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                        {
                            final InMemoryCharacterStream stream = createStream(text);
                            int lineNumber = 0;
                            for (final String expectedLine : expectedLines)
                            {
                                ++lineNumber;
                                test.assertEqual(expectedLine, stream.readLine(true).await(), "Wrong line " + lineNumber);
                            }
                            test.assertThrows(() -> stream.readLine(true).await(), new EndOfStreamException());
                        });
                    };

                    readLineTest.run("", Iterable.create());
                    readLineTest.run("hello", Iterable.create("hello"));
                    readLineTest.run("hello\n", Iterable.create("hello\n"));
                    readLineTest.run("a\r", Iterable.create("a\r"));
                    readLineTest.run("a\r\n", Iterable.create("a\r\n"));
                    readLineTest.run("a\r\r\n", Iterable.create("a\r\r\n"));
                    readLineTest.run("\n\n\n", Iterable.create("\n", "\n", "\n"));
                    readLineTest.run("a\nb", Iterable.create("a\n", "b"));
                    readLineTest.run("a\rb", Iterable.create("a\rb"));
                });
            });
        });
    }

    private static InMemoryCharacterStream createStream()
    {
        return createStream(null, true);
    }

    private static InMemoryCharacterStream createStream(String text)
    {
        return createStream(text, true);
    }

    private static InMemoryCharacterStream createStream(String text, boolean endOfStream)
    {
        InMemoryCharacterStream result = new InMemoryCharacterStream();
        if (!Strings.isNullOrEmpty(text))
        {
            result.write(text);
        }
        if (endOfStream)
        {
            result = result.endOfStream();
        }
        return result;
    }
}
