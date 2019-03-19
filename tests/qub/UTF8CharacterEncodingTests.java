package qub;

public class UTF8CharacterEncodingTests
{
    public static void test(TestRunner runner)
    {
        final UTF8CharacterEncoding encoding = new UTF8CharacterEncoding();

        runner.testGroup(UTF8CharacterEncoding.class, () ->
        {
            runner.testGroup("encode(char)", () ->
            {
                final Action2<Character,byte[]> encodeTest = (Character character, byte[] expectedBytes) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertSuccess(expectedBytes, encoding.encode(character));
                    });
                };

                encodeTest.run('a', new byte[] { 97 });
                encodeTest.run('b', new byte[] { 98 });
                encodeTest.run('y', new byte[] { 121 });
                encodeTest.run('z', new byte[] { 122 });
                encodeTest.run('a', new byte[] { 97 });
                encodeTest.run('\n', new byte[] { 10 });
                encodeTest.run('~', new byte[] { 126 });
                encodeTest.run((char)132, new byte[] { -62, -124 });
            });

            runner.testGroup("encode(String)", () ->
            {
                final Action2<String,Throwable> encodeFailureTest = (String text, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.encode(text), expectedError);
                    });
                };

                encodeFailureTest.run(null, new PreConditionFailure("text cannot be null."));
                encodeFailureTest.run("", new PreConditionFailure("text cannot be empty."));

                final Action3<String, byte[], Throwable> encodeTest = (String text, byte[] expectedBytes, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertDone(expectedBytes, expectedError, encoding.encode(text));
                    });
                };

                encodeTest.run("a", new byte[]{97}, null);
                encodeTest.run("b", new byte[]{98}, null);
                encodeTest.run("ab", new byte[]{97, 98}, null);
                encodeTest.run("y", new byte[]{121}, null);
                encodeTest.run("z", new byte[]{122}, null);
                encodeTest.run("\n", new byte[]{10}, null);
                encodeTest.run("~", new byte[]{126}, null);
                encodeTest.run("" + (char)132, new byte[] { -62, -124 }, null);
            });

            runner.testGroup("encode(char[])", () ->
            {
                final Action2<char[],Throwable> encodeFailureTest = (char[] characters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.encode(characters), expectedError);
                    });
                };

                encodeFailureTest.run(null, new PreConditionFailure("characters cannot be null."));
                encodeFailureTest.run(new char[0], new PreConditionFailure("characters cannot be empty."));

                final Action3<char[], byte[], Throwable> encodeTest = (char[] characters, byte[] expectedBytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertDone(expectedBytes, expectedError, encoding.encode(characters));
                    });
                };

                encodeTest.run(new char[]{'a'}, new byte[]{97}, null);
                encodeTest.run(new char[]{'b'}, new byte[]{98}, null);
                encodeTest.run(new char[]{'a', 'b'}, new byte[]{97, 98}, null);
                encodeTest.run(new char[]{'y'}, new byte[]{121}, null);
                encodeTest.run(new char[]{'z'}, new byte[]{122}, null);
                encodeTest.run(new char[]{'\n'}, new byte[]{10}, null);
                encodeTest.run(new char[]{'~'}, new byte[]{126}, null);
                encodeTest.run(new char[] { (char)132 }, new byte[] { -62, -124 }, null);
            });

            runner.testGroup("decode(byte[])", () ->
            {
                final Action2<byte[],Throwable> decodeFailureTest = (byte[] bytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decode(bytes), expectedError);
                    });
                };

                decodeFailureTest.run(null, new PreConditionFailure("bytes cannot be null."));
                decodeFailureTest.run(new byte[0], new PreConditionFailure("bytes cannot be empty."));

                final Action3<byte[], char[], Throwable> decodeTest = (byte[] bytes, char[] expectedCharacters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Result<char[]> result = encoding.decode(bytes);
                        if (expectedError != null)
                        {
                            test.assertError(expectedError, result);
                        }
                        else
                        {
                            test.assertSuccess(expectedCharacters, result);
                        }
                    });
                };

                decodeTest.run(new byte[]{97}, new char[]{'a'}, null);
                decodeTest.run(new byte[]{122, 121, 122}, new char[]{'z', 'y', 'z'}, null);
                decodeTest.run(new byte[] { -62, -124 }, new char[] { (char)132 }, null);
                decodeTest.run(new byte[] { (byte)0xD8, 0x00, 97 }, null, new IllegalArgumentException("Byte 0xD8 is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                decodeTest.run(new byte[] { (byte)0xD8, 0x01, 98 }, null, new IllegalArgumentException("Byte 0xD8 is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                decodeTest.run(new byte[] { (byte)0xDA, 0x01, 99 }, null, new IllegalArgumentException("Byte 0xDA is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                decodeTest.run(new byte[] { (byte)0xDF, (byte)0xFE, 100 }, null, new IllegalArgumentException("Byte 0xDF is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                decodeTest.run(new byte[] { (byte)0xDF, (byte)0xFF, 101 }, null, new IllegalArgumentException("Byte 0xDF is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                decodeTest.run(new byte[] { (byte)0xE0, (byte)0x80, (byte)0x81, 102 }, null, new NotSupportedException("Decoding UTF-8 encoded byte streams with characters composed of 3 or more bytes are not supported."));
                decodeTest.run(new byte[] { (byte)0xF0, (byte)0x80, (byte)0x81, (byte)0x82, 103 }, null, new NotSupportedException("Decoding UTF-8 encoded byte streams with characters composed of 3 or more bytes are not supported."));
            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
                final Action2<byte[],Throwable> decodeAsStringFailureTest = (byte[] bytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decodeAsString(bytes),
                            expectedError);
                    });
                };

                decodeAsStringFailureTest.run(null, new PreConditionFailure("bytes cannot be null."));
                decodeAsStringFailureTest.run(new byte[0], new PreConditionFailure("bytes cannot be empty."));

                final Action2<byte[], String> decodeAsStringTest = (byte[] bytes, String expectedString) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(expectedString, encoding.decodeAsString(bytes).await());
                    });
                };

                decodeAsStringTest.run(new byte[]{97}, "a");
                decodeAsStringTest.run(new byte[]{98, 97}, "ba");
                decodeAsStringTest.run(new byte[] { -62, -124 }, "" + (char)132);
            });

            runner.testGroup("decodeNextCharacter(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> encoding.decodeNextCharacter(null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertThrows(() -> encoding.decodeNextCharacter(Iterator.empty()).awaitError(),
                        new EndOfStreamException());
                });

                final Action2<byte[],char[]> decodeNextCharacterTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = Iterator.createFromBytes(bytes);
                        for (int i = 0; i < expectedCharacters.length; ++i)
                        {
                            test.assertEqual(expectedCharacters[i], encoding.decodeNextCharacter(bytesIterator).await());
                        }
                        test.assertThrows(() -> encoding.decodeNextCharacter(bytesIterator).awaitError(),
                            new EndOfStreamException());
                    });
                };

                decodeNextCharacterTest.run(new byte[] { 97 }, new char[]{'a'});
                decodeNextCharacterTest.run(new byte[] { 97, 98, 99, 100 }, new char[]{'a', 'b', 'c', 'd'});
                decodeNextCharacterTest.run(new byte[] { -62, -124 }, new char[] { (char)132 });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(encoding.equals((Object) null));
                });

                runner.test("with non-CharacterEncoding object", (Test test) ->
                {
                    test.assertFalse(encoding.equals("hello"));
                });

                runner.test("with same CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals((Object) encoding));
                });

                runner.test("with equal CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals((Object) new UTF8CharacterEncoding()));
                });

                runner.test("with different CharacterEncoding", (Test test) ->
                {
                    test.assertFalse(encoding.equals((Object) new USASCIICharacterEncoding()));
                });
            });

            runner.testGroup("equals(CharacterEncoding)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(encoding.equals((CharacterEncoding) null));
                });

                runner.test("with same CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals(encoding));
                });

                runner.test("with equal CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals(new UTF8CharacterEncoding()));
                });

                runner.test("with different CharacterEncoding", (Test test) ->
                {
                    test.assertFalse(encoding.equals(new USASCIICharacterEncoding()));
                });
            });
        });
    }
}
