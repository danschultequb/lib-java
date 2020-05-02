package qub;

public interface USASCIICharacterEncodingTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(USASCIICharacterEncoding.class, () ->
        {
            CharacterEncodingTests.test(runner, USASCIICharacterEncoding::new);

            runner.testGroup("encodeCharacter(char)", () ->
            {
                final Action2<Character,Throwable> encodeErrorTest = (Character character, Throwable expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        final USASCIICharacterEncoding encoding = new USASCIICharacterEncoding();
                        test.assertThrows(() -> encoding.encodeCharacter(character).await(), expected);
                    });
                };

                // encodeErrorTest.run((char)-1, new PreConditionFailure("character (-1) must be between 0 and 255."));
                encodeErrorTest.run((char)256, new PreConditionFailure("character (256) must be between 0 and 255."));

                final Action2<Character,Byte> encodeTest = (Character character, Byte expectedByte) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        final USASCIICharacterEncoding encoding = new USASCIICharacterEncoding();
                        test.assertEqual(new byte[] { expectedByte }, encoding.encodeCharacter(character).await());
                    });
                };

                encodeTest.run('\0', (byte)0);
                encodeTest.run('a', (byte)97);
                encodeTest.run('b', (byte)98);
                encodeTest.run('y', (byte)121);
                encodeTest.run('z', (byte)122);
                encodeTest.run('a', (byte)97);
                encodeTest.run('\n', (byte)10);
                encodeTest.run('~', (byte)126);
                encodeTest.run((char)132, (byte)-124);
                encodeTest.run((char)255, (byte)-1);
            });

            final USASCIICharacterEncoding encoding = new USASCIICharacterEncoding();

            runner.testGroup("encodeCharacters(String)", () ->
            {
                final Action2<String,Throwable> encodeFailureTest = (String text, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.encodeCharacters(text).await(), expectedError);
                    });
                };

                encodeFailureTest.run("" + (char)256, new PreConditionFailure("character (256) must be between 0 and 255."));

                final Action2<String,byte[]> encodeTest = (String text, byte[] expectedBytes) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encodeCharacters(text).await());
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

            runner.testGroup("encodeCharacters(char[])", () ->
            {
                final Action2<char[],Throwable> encodeFailureTest = (char[] characters, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.encodeCharacters(characters).await(), expectedError);
                    });
                };

                encodeFailureTest.run(new char[] { (char)256 }, new PreConditionFailure("character (256) must be between 0 and 255."));

                final Action2<char[],byte[]> encodeTest = (char[] characters, byte[] expectedBytes) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encodeCharacters(characters).await());
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
                final Action2<byte[],char[]> decodeTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(expectedCharacters, encoding.decodeAsCharacters(bytes).await());
                    });
                };

                decodeTest.run(new byte[] { 97 }, new char[] { 'a' });
                decodeTest.run(new byte[] { 122, 121, 122 }, new char[] { 'z', 'y', 'z' });
                decodeTest.run(new byte[] { -124 }, new char[] { (char)132 });
            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
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

            runner.testGroup("decodeAsCharacters(Iterator<Byte>)", () ->
            {
                final Action2<Byte[],Throwable> decodeAsCharactersErrorTest = (Byte[] bytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> iterator = Iterator.create(bytes);
                        test.assertThrows(() -> encoding.decodeAsCharacters(iterator).await(), expectedError);
                    });
                };

                decodeAsCharactersErrorTest.run(new Byte[] { null }, new IllegalArgumentException("Cannot decode a null byte."));
                decodeAsCharactersErrorTest.run(new Byte[] { 97, null }, new IllegalArgumentException("Cannot decode a null byte."));

                final Action2<byte[],char[]> decodeAsCharactersTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = Iterator.createFromBytes(bytes);
                        test.assertEqual(expectedCharacters, encoding.decodeAsCharacters(bytesIterator).await());
                        test.assertEqual(new char[0], encoding.decodeAsCharacters(bytesIterator).await());
                    });
                };

                decodeAsCharactersTest.run(new byte[] { 97 }, new char[] { 'a' });
                decodeAsCharactersTest.run(new byte[] { 97, 98, 99, 100 }, new char[] { 'a', 'b', 'c', 'd' });
                decodeAsCharactersTest.run(new byte[] { -124 }, new char[] { (char)132 });
            });

            runner.testGroup("iterateDecodedCharacters(Iterator<Byte>)", () ->
            {
                final Action2<byte[],char[]> iterateDecodedCharactersTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(
                            CharacterArray.create(expectedCharacters),
                            encoding.iterateDecodedCharacters(ByteArray.create(bytes).iterate()).toList());
                    });
                };

                iterateDecodedCharactersTest.run(new byte[] { 97 }, new char[] { 'a' });
                iterateDecodedCharactersTest.run(new byte[] { 97, 98, 99, 100 }, new char[] { 'a', 'b', 'c', 'd' });
                iterateDecodedCharactersTest.run(new byte[] { -124 }, new char[] { (char)132 });
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
