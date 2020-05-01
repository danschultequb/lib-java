package qub;

public interface UTF8CharacterEncodingTests
{
    static void test(TestRunner runner)
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
                        new PreConditionFailure("isDisposed() cannot be true."));
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

            runner.testGroup("decodeNextCharacter(Iterator<Byte>)", () ->
            {
                final Action2<Iterable<Byte>,Throwable> decodeNextCharacterErrorTest = (Iterable<Byte> bytes, Throwable expected) ->
                {
                    runner.test("with " + bytes, (Test test) ->
                    {
                        final Iterator<Byte> iterator = bytes == null ? null : bytes.iterate();
                        test.assertThrows(() -> encoding.decodeNextCharacter(iterator).await(),
                            expected);
                    });
                };

                decodeNextCharacterErrorTest.run(null,
                    new PreConditionFailure("bytes cannot be null."));
                decodeNextCharacterErrorTest.run(Iterable.create(),
                    new EndOfStreamException());
                decodeNextCharacterErrorTest.run(Iterable.create((Byte)null),
                    new IllegalArgumentException("1st byte in decoded character cannot be null."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0x80),
                    new IllegalArgumentException("Expected a leading byte, but found a continuation byte (0x80) instead."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xC0),
                    new IllegalArgumentException("Missing 2nd byte of 2 in decoded character."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xC0, null),
                    new IllegalArgumentException("2nd byte of 2 in decoded character cannot be null."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xC0, (byte)0x11),
                    new IllegalArgumentException("Expected 2nd byte of 2 to be a continuation byte (10xxxxxx), but found 0x11 instead."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xE0),
                    new IllegalArgumentException("Missing 2nd byte of 3 in decoded character."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xE0, null),
                    new IllegalArgumentException("2nd byte of 3 in decoded character cannot be null."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xE0, (byte)0x11),
                    new IllegalArgumentException("Expected 2nd byte of 3 to be a continuation byte (10xxxxxx), but found 0x11 instead."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xE0, (byte)0x80),
                    new IllegalArgumentException("Missing 3rd byte of 3 in decoded character."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xE0, (byte)0x80, null),
                    new IllegalArgumentException("3rd byte of 3 in decoded character cannot be null."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xE0, (byte)0x80, (byte)0x11),
                    new IllegalArgumentException("Expected 3rd byte of 3 to be a continuation byte (10xxxxxx), but found 0x11 instead."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xF0),
                    new IllegalArgumentException("Missing 2nd byte of 4 in decoded character."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80),
                    new IllegalArgumentException("Missing 3rd byte of 4 in decoded character."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x80),
                    new IllegalArgumentException("Missing 4th byte of 4 in decoded character."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x80, null),
                    new IllegalArgumentException("4th byte of 4 in decoded character cannot be null."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x80, (byte)0x11),
                    new IllegalArgumentException("Expected 4th byte of 4 to be a continuation byte (10xxxxxx), but found 0x11 instead."));
                decodeNextCharacterErrorTest.run(Iterable.create((byte)0xF8),
                    new IllegalArgumentException("Found an invalid leading byte (0xF8)."));

                final Action2<byte[],char[]> decodeNextCharacterTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final Iterator<Byte> bytesIterator = Iterator.createFromBytes(bytes);
                        for (int i = 0; i < expectedCharacters.length; ++i)
                        {
                            test.assertEqual(expectedCharacters[i], encoding.decodeNextCharacter(bytesIterator).await());
                        }
                        test.assertThrows(() -> encoding.decodeNextCharacter(bytesIterator).await(),
                            new EndOfStreamException());
                    });
                };

                decodeNextCharacterTest.run(new byte[] { 97 }, new char[]{'a'});
                decodeNextCharacterTest.run(new byte[] { 97, 98, 99, 100 }, new char[]{'a', 'b', 'c', 'd'});
                decodeNextCharacterTest.run(new byte[] { -62, -124 }, new char[] { (char)132 });
            });

            runner.testGroup("decodeNextCharacter(ByteReadStream)", () ->
            {
                final Action2<InMemoryByteStream,Throwable> decodeNextCharacterErrorTest = (InMemoryByteStream byteReadStream, Throwable expected) ->
                {
                    runner.test("with " + (byteReadStream == null ? "null" : ByteArray.create(byteReadStream.getBytes()).map(Bytes::toHexString)), (Test test) ->
                    {
                        test.assertThrows(() -> encoding.decodeNextCharacter(byteReadStream).await(),
                            expected);
                    });
                };

                decodeNextCharacterErrorTest.run(null, new PreConditionFailure("bytes cannot be null."));
                decodeNextCharacterErrorTest.run(InMemoryByteStream.create().endOfStream(), new EndOfStreamException());
                decodeNextCharacterErrorTest.run(InMemoryByteStream.create(new byte[] { (byte)0x80 }).endOfStream(),
                    new IllegalArgumentException("Expected a leading byte, but found a continuation byte (0x80) instead."));
                decodeNextCharacterErrorTest.run(InMemoryByteStream.create(new byte[] { (byte)0xC0 }).endOfStream(),
                    new IllegalArgumentException("Missing 2nd byte of 2 in decoded character."));
                decodeNextCharacterErrorTest.run(InMemoryByteStream.create(new byte[] { (byte)0xE0 }).endOfStream(),
                    new IllegalArgumentException("Missing 2nd byte of 3 in decoded character."));
                decodeNextCharacterErrorTest.run(InMemoryByteStream.create(new byte[] { (byte)0xF0 }).endOfStream(),
                    new IllegalArgumentException("Missing 2nd byte of 4 in decoded character."));
                decodeNextCharacterErrorTest.run(InMemoryByteStream.create(new byte[] { (byte)0xF8 }).endOfStream(),
                    new IllegalArgumentException("Found an invalid leading byte (0xF8)."));

                final Action2<byte[],char[]> decodeNextCharacterTest = (byte[] bytes, char[] expectedCharacters) ->
                {
                    runner.test("with " + Array.toString(bytes), (Test test) ->
                    {
                        final ByteReadStream byteReadStream = ByteReadStream.create(bytes);
                        for (int i = 0; i < expectedCharacters.length; ++i)
                        {
                            test.assertEqual(expectedCharacters[i], encoding.decodeNextCharacter(byteReadStream).await());
                        }
                        test.assertThrows(() -> encoding.decodeNextCharacter(byteReadStream).await(),
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
