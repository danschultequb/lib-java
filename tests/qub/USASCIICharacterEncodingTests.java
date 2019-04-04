package qub;

public class USASCIICharacterEncodingTests
{
    public static void test(TestRunner runner)
    {
        final USASCIICharacterEncoding encoding = new USASCIICharacterEncoding();

        runner.testGroup(USASCIICharacterEncoding.class, () ->
        {
            runner.testGroup("encode(char)", () ->
            {
                final Action2<Character,Byte> encodeTest = (Character character, Byte expectedByte) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertSuccess(new byte[] { expectedByte }, encoding.encode(character));
                    });
                };

                encodeTest.run('a', (byte)97);
                encodeTest.run('b', (byte)98);
                encodeTest.run('y', (byte)121);
                encodeTest.run('z', (byte)122);
                encodeTest.run('a', (byte)97);
                encodeTest.run('\n', (byte)10);
                encodeTest.run('~', (byte)126);
                encodeTest.run((char)132, (byte)-124);
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

                final Action2<String,byte[]> encodeTest = (String text, byte[] expectedBytes) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encode(text).await());
                    });
                };

                encodeTest.run("a", new byte[] { 97 });
                encodeTest.run("b", new byte[] { 98 });
                encodeTest.run("ab", new byte[] { 97, 98 });
                encodeTest.run("y", new byte[] { 121 });
                encodeTest.run("z", new byte[] { 122 });
                encodeTest.run("\n", new byte[] { 10 });
                encodeTest.run("~", new byte[] { 126 });
                encodeTest.run("" + (char)132, new byte[] { -124 });
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

                final Action2<char[],byte[]> encodeTest = (char[] characters, byte[] expectedBytes) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encode(characters).await());
                    });
                };

                encodeTest.run(new char[] { 'a' }, new byte[] { 97 });
                encodeTest.run(new char[] { 'b' }, new byte[] { 98 });
                encodeTest.run(new char[] { 'a', 'b' }, new byte[] { 97, 98 });
                encodeTest.run(new char[] { 'y' }, new byte[] { 121 });
                encodeTest.run(new char[] { 'z' }, new byte[] { 122 });
                encodeTest.run(new char[] { '\n' }, new byte[] { 10 });
                encodeTest.run(new char[] { '~' }, new byte[] { 126 });
                encodeTest.run(new char[] { (char)132 }, new byte[] { -124 });
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

                final Action2<byte[],char[]> decodeTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(expectedCharacters, encoding.decode(bytes).await());
                    });
                };

                decodeTest.run(new byte[] { 97 }, new char[] { 'a' });
                decodeTest.run(new byte[] { 122, 121, 122 }, new char[] { 'z', 'y', 'z' });
                decodeTest.run(new byte[] { -124 }, new char[] { (char)132 });
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

                final Action2<byte[],String> decodeAsStringTest = (byte[] bytes, String expectedString) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(expectedString, encoding.decodeAsString(bytes).await());
                    });
                };

                decodeAsStringTest.run(new byte[] { 97 }, "a");
                decodeAsStringTest.run(new byte[] { 98, 97 }, "ba");
                decodeAsStringTest.run(new byte[] { -124 }, "" + (char)132);
            });

            runner.testGroup("decodeNextCharacter(Iterator<Byte>)", () ->
            {
                final Action2<byte[],Throwable> decodeNextCharacterFailureTest = (byte[] bytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = bytes == null ? null :
                            bytes.length == 0 ? Iterator.empty() :
                            Iterator.createFromBytes(bytes);
                        test.assertThrows(() -> encoding.decodeNextCharacter(bytesIterator).await(), expectedError);
                    });
                };

                decodeNextCharacterFailureTest.run(null, new PreConditionFailure("bytes cannot be null."));
                decodeNextCharacterFailureTest.run(new byte[0], new EndOfStreamException());

                final Action2<byte[],char[]> decodeNextCharacterTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = bytes == null ? null : Iterator.createFromBytes(bytes);
                        if (expectedCharacters != null)
                        {
                            for (int i = 0; i < expectedCharacters.length; ++i)
                            {
                                test.assertEqual(expectedCharacters[i], encoding.decodeNextCharacter(bytesIterator).await());
                            }
                        }
                        test.assertThrows(() -> encoding.decodeNextCharacter(bytesIterator).await(),
                            new EndOfStreamException());
                    });
                };

                decodeNextCharacterTest.run(new byte[] { 97 }, new char[] { 'a' });
                decodeNextCharacterTest.run(new byte[] { 97, 98, 99, 100 }, new char[] { 'a', 'b', 'c', 'd' });
                decodeNextCharacterTest.run(new byte[] { -124 }, new char[] { (char)132 });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(encoding.equals((Object)null));
                });

                runner.test("with non-CharacterEncoding object", (Test test) ->
                {
                    test.assertFalse(encoding.equals("hello"));
                });

                runner.test("with same CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals((Object)encoding));
                });

                runner.test("with equal CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals((Object)new USASCIICharacterEncoding()));
                });

                runner.test("with different CharacterEncoding", (Test test) ->
                {
                    test.assertFalse(encoding.equals((Object)new UTF8CharacterEncoding()));
                });
            });

            runner.testGroup("equals(CharacterEncoding)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(encoding.equals((CharacterEncoding)null));
                });

                runner.test("with same CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals(encoding));
                });

                runner.test("with equal CharacterEncoding", (Test test) ->
                {
                    test.assertTrue(encoding.equals(new USASCIICharacterEncoding()));
                });

                runner.test("with different CharacterEncoding", (Test test) ->
                {
                    test.assertFalse(encoding.equals(new UTF8CharacterEncoding()));
                });
            });
        });
    }
}
