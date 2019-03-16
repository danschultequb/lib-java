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

                final Action2<String,byte[]> encodeTest = (String text, byte[] expectedBytes) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encode(text).await());
                    });
                };

                encodeTest.run("", new byte[0]);
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

                final Action2<char[],byte[]> encodeTest = (char[] characters, byte[] expectedBytes) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encode(characters).await());
                    });
                };

                encodeTest.run(new char[0], new byte[0]);
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
                final Action1<byte[]> decodeFailureTest = (byte[] bytes) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decode(bytes));
                    });
                };

                decodeFailureTest.run(null);

                final Action3<byte[],char[],Throwable> decodeTest = (byte[] bytes, char[] expectedCharacters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertDone(expectedCharacters, expectedError, encoding.decode(bytes));
                    });
                };

                decodeTest.run(new byte[0], new char[0], null);
                decodeTest.run(new byte[] { 97 }, new char[] { 'a' }, null);
                decodeTest.run(new byte[] { 122, 121, 122 }, new char[] { 'z', 'y', 'z' }, null);
                decodeTest.run(new byte[] { -124 }, new char[] { (char)132 }, null);
            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
                final Action1<byte[]> decodeAsStringFailureTest = (byte[] bytes) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decodeAsString(bytes));
                    });
                };

                decodeAsStringFailureTest.run(null);

                final Action3<byte[],String,Throwable> decodeAsStringTest = (byte[] bytes, String expectedString, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertDone(expectedString, expectedError, encoding.decodeAsString(bytes));
                    });
                };

                decodeAsStringTest.run(new byte[0], "", null);
                decodeAsStringTest.run(new byte[] { 97 }, "a", null);
                decodeAsStringTest.run(new byte[] { 98, 97 }, "ba", null);
                decodeAsStringTest.run(new byte[] { -124 }, "" + (char)132, null);
            });

            runner.testGroup("decodeNextCharacter(Iterator<Byte>)", () ->
            {
                final Action1<byte[]> decodeNextCharacterFailureTest = (byte[] bytes) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = bytes == null ? null : Iterator.createFromBytes(bytes);
                        test.assertThrows(() -> encoding.decodeNextCharacter(bytesIterator));
                    });
                };

                decodeNextCharacterFailureTest.run(null);

                final Action3<byte[],char[],Throwable> decodeNextCharacterTest = (byte[] bytes, char[] expectedCharacters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = bytes == null ? null : Iterator.createFromBytes(bytes);
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

                decodeNextCharacterTest.run(new byte[0], null, null);
                decodeNextCharacterTest.run(new byte[] { 97 }, new char[] { 'a' }, null);
                decodeNextCharacterTest.run(new byte[] { 97, 98, 99, 100 }, new char[] { 'a', 'b', 'c', 'd' }, null);
                decodeNextCharacterTest.run(new byte[] { -124 }, new char[] { (char)132 }, null);
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
