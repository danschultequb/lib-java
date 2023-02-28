package qub;

public interface UTF8CharacterEncodingTests
{
    public static void test(TestRunner runner)
    {
        CharacterEncodingTests.test(runner, UTF8CharacterEncoding::new);

        final UTF8CharacterEncoding encoding = new UTF8CharacterEncoding();

        runner.testGroup(UTF8CharacterEncoding.class, () ->
        {
            runner.test("getByteOrderMark()", (Test test) ->
            {
                final byte[] byteOrderMark = encoding.getByteOrderMark();
                test.assertEqual(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }, byteOrderMark);
                test.assertEqual(new byte[] { -17,-69,-65 }, byteOrderMark);
            });

            runner.testGroup("writeByteOrderMark(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> encoding.writeByteOrderMark(null),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    test.assertTrue(byteWriteStream.dispose().await());
                    test.assertThrows(() -> encoding.writeByteOrderMark(byteWriteStream),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("with not disposed", (Test test) ->
                {
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    test.assertEqual(3, encoding.writeByteOrderMark(byteWriteStream).await());
                    test.assertEqual(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }, byteWriteStream.getBytes());
                    test.assertEqual(new byte[] { -17,-69,-65 }, byteWriteStream.getBytes());
                });
            });

            runner.testGroup("encodeCharacter(char)", () ->
            {
                final Action2<Character,byte[]> encodeTest = (Character character, byte[] expectedBytes) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encodeCharacter(character).await());
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

            runner.testGroup("encodeCharacters(String)", () ->
            {
                final Action2<String, byte[]> encodeTest = (String text, byte[] expectedBytes) ->
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
                encodeTest.run("" + (char)132, new byte[] { -62, -124 });
            });

            runner.testGroup("encodeCharacters(char[])", () ->
            {
                final Action2<char[],byte[]> encodeTest = (char[] characters, byte[] expectedBytes) ->
                {
                    runner.test("with " + Array.toString(characters, Characters::escapeAndQuote), (Test test) ->
                    {
                        test.assertEqual(expectedBytes, encoding.encodeCharacters(characters).await());
                    });
                };

                encodeTest.run(new char[0], new byte[0]);
                encodeTest.run(new char[] { 'a' }, new byte[] { 97 });
                encodeTest.run(new char[] { 'b' }, new byte[] { 98 });
                encodeTest.run(new char[] { 'a', 'b' }, new byte[] { 97, 98 });
                encodeTest.run(new char[] { 'y' }, new byte[]{  121 });
                encodeTest.run(new char[] { 'z' }, new byte[] { 122 });
                encodeTest.run(new char[] { '\n' }, new byte[] { 10 });
                encodeTest.run(new char[] { '~' }, new byte[] { 126 });
                encodeTest.run(new char[] { (char)132 }, new byte[] { -62, -124 });
                encodeTest.run(new char[] { (char)0x41 }, new byte[] { 0x41 });
                encodeTest.run(new char[] { (char)0xDF }, new byte[] { (byte)0xC3, (byte)0x9F });
                encodeTest.run(new char[] { (char)0x6771 }, new byte[] { (byte)0xE6, (byte)0x9D, (byte)0xB1 });
                encodeTest.run(new char[] { (char)0xD801, (char)0xDC00 }, new byte[] { (byte)0xF0, (byte)0x90, (byte)0x90, (byte)0x80 });
            });

            runner.testGroup("encodeUnicodeCodePoint(int)", () ->
            {
                final Action2<Integer,Throwable> encodeUnicodeCodePointErrorTest = (Integer unicodeCodePoint, Throwable expected) ->
                {
                    runner.test("with " + Integers.toHexString(unicodeCodePoint, true), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.encodeUnicodeCodePoint(unicodeCodePoint).await(), expected);
                    });
                };

                encodeUnicodeCodePointErrorTest.run(-1, new PreConditionFailure("unicodeCodePoint (-1) must be greater than or equal to 0."));
                encodeUnicodeCodePointErrorTest.run(0x110000, new PostConditionFailure("result (0) must be between 1 and 4."));
            });

            runner.testGroup("decode(byte[])", () ->
            {
                final Action2<byte[],Throwable> decodeFailureTest = (byte[] bytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decodeAsCharacters(bytes).await(), expectedError);
                    });
                };

                decodeFailureTest.run(null,
                    new PreConditionFailure("bytes cannot be null."));

                final Action2<byte[],char[]> decodeTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(CharacterArray.create(expectedCharacters), CharacterArray.create(encoding.decodeAsCharacters(bytes).await()));
                    });
                };

                decodeTest.run(new byte[0], new char[0]);
                decodeTest.run(new byte[] { 97 }, new char[] { 'a' });
                decodeTest.run(new byte[] { 122, 121, 122 }, new char[] { 'z', 'y', 'z' });
                decodeTest.run(new byte[] { -62, -124 }, new char[] { (char)132 });
                decodeTest.run(new byte[] { (byte)0xD8, (byte)0x80, 97 }, new char[] { (char)0x0600, 'a' });

            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
                final Action2<byte[],Throwable> decodeAsStringFailureTest = (byte[] bytes, Throwable expectedError) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decodeAsString(bytes).await(),
                            expectedError);
                    });
                };

                decodeAsStringFailureTest.run(null, new PreConditionFailure("bytes cannot be null."));

                final Action2<byte[], String> decodeAsStringTest = (byte[] bytes, String expectedString) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        test.assertEqual(expectedString, encoding.decodeAsString(bytes).await());
                    });
                };

                decodeAsStringTest.run(new byte[0], "");
                decodeAsStringTest.run(new byte[]{97}, "a");
                decodeAsStringTest.run(new byte[]{98, 97}, "ba");
                decodeAsStringTest.run(new byte[] { -62, -124 }, "" + (char)132);
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

            runner.testGroup("toUnicodeCodePoints(String)", () ->
            {
                final Action2<String,Iterable<Integer>> toUnicodeCodePointsTest = (String characters, Iterable<Integer> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(characters), (Test test) ->
                    {
                        test.assertEqual(expected, encoding.toUnicodeCodePoints(characters));
                    });
                };

                toUnicodeCodePointsTest.run("", List.create());
                toUnicodeCodePointsTest.run("abc", List.create(97, 98, 99));
                toUnicodeCodePointsTest.run("" + (char)0x41, List.create(0x41));
                toUnicodeCodePointsTest.run("" + (char)0xDF, List.create(0xDF));
                toUnicodeCodePointsTest.run("" + (char)0x6771, List.create(0x6771));
                toUnicodeCodePointsTest.run("" + (char)0xD801 + (char)0xDC00, List.create(66560));
            });
        });
    }
}
