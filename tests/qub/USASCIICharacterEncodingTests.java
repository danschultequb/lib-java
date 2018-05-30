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
            });

            runner.testGroup("encode(String)", () ->
            {
                final Action3<String,byte[],Throwable> encodeTest = (String text, byte[] expectedBytes, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertDone(expectedBytes, expectedError, encoding.encode(text));
                    });
                };

                encodeTest.run(null, null, new IllegalArgumentException("text cannot be null."));
                encodeTest.run("", null, new IllegalArgumentException("text cannot be empty."));
                encodeTest.run("a", new byte[] { 97 }, null);
                encodeTest.run("b", new byte[] { 98 }, null);
                encodeTest.run("ab", new byte[] { 97, 98 }, null);
                encodeTest.run("y", new byte[] { 121 }, null);
                encodeTest.run("z", new byte[] { 122 }, null);
                encodeTest.run("\n", new byte[] { 10 }, null);
                encodeTest.run("~", new byte[] { 126 }, null);
            });

            runner.testGroup("encode(char[])", () ->
            {
                final Action3<char[],byte[],Throwable> encodeTest = (char[] characters, byte[] expectedBytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertDone(expectedBytes, expectedError, encoding.encode(characters));
                    });
                };

                encodeTest.run(null, null, new IllegalArgumentException("characters cannot be null."));
                encodeTest.run(new char[0], null, new IllegalArgumentException("characters cannot be empty."));
                encodeTest.run(new char[] { 'a' }, new byte[] { 97 }, null);
                encodeTest.run(new char[] { 'b' }, new byte[] { 98 }, null);
                encodeTest.run(new char[] { 'a', 'b' }, new byte[] { 97, 98 }, null);
                encodeTest.run(new char[] { 'y' }, new byte[] { 121 }, null);
                encodeTest.run(new char[] { 'z' }, new byte[] { 122 }, null);
                encodeTest.run(new char[] { '\n' }, new byte[] { 10 }, null);
                encodeTest.run(new char[] { '~' }, new byte[] { 126 }, null);
            });

            runner.testGroup("decode(byte[])", () ->
            {
                final Action3<byte[],char[],Throwable> decodeTest = (byte[] bytes, char[] expectedCharacters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertDone(expectedCharacters, expectedError, encoding.decode(bytes));
                    });
                };

                decodeTest.run(null, null, new IllegalArgumentException("bytes cannot be null."));
                decodeTest.run(new byte[0], null, new IllegalArgumentException("bytes cannot be empty."));
                decodeTest.run(new byte[] { 97 }, new char[] { 'a' }, null);
                decodeTest.run(new byte[] { 122, 121, 122 }, new char[] { 'z', 'y', 'z' }, null);
            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
                final Action3<byte[],String,Throwable> decodeAsStringTest = (byte[] bytes, String expectedString, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertDone(expectedString, expectedError, encoding.decodeAsString(bytes));
                    });
                };

                decodeAsStringTest.run(null, null, new IllegalArgumentException("bytes cannot be null."));
                decodeAsStringTest.run(new byte[0], null, new IllegalArgumentException("bytes cannot be empty."));
                decodeAsStringTest.run(new byte[] { 97 }, "a", null);
                decodeAsStringTest.run(new byte[] { 98, 97 }, "ba", null);
            });

            runner.testGroup("decodeNextCharacter(Iterator<Byte>)", () ->
            {
                final Action3<byte[],char[],Throwable> decodeNextCharacterTest = (byte[] bytes, char[] expectedCharacters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = bytes == null ? null : Array.fromValues(bytes).iterate();
                        if (expectedCharacters != null)
                        {
                            for (int i = 0; i < expectedCharacters.length; ++i)
                            {
                                test.assertSuccess(expectedCharacters[i], encoding.decodeNextCharacter(bytesIterator));
                            }
                        }
                        test.assertDone(null, expectedError, encoding.decodeNextCharacter(bytesIterator));
                    });
                };

                decodeNextCharacterTest.run(null, null, new IllegalArgumentException("bytes cannot be null."));
                decodeNextCharacterTest.run(new byte[0], null, null);
                decodeNextCharacterTest.run(new byte[] { 97 }, new char[] { 'a' }, null);
                decodeNextCharacterTest.run(new byte[] { 97, 98, 99, 100 }, new char[] { 'a', 'b', 'c', 'd' }, null);
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
            });
        });
    }
}
