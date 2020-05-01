package qub;

public interface CharacterEncodingTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterEncoding.class, () ->
        {
            runner.test("US_ASCII", (Test test) ->
            {
                test.assertNotNull(CharacterEncoding.US_ASCII);
                test.assertInstanceOf(CharacterEncoding.US_ASCII, USASCIICharacterEncoding.class);
            });

            runner.test("UTF_8", (Test test) ->
            {
                test.assertNotNull(CharacterEncoding.UTF_8);
                test.assertInstanceOf(CharacterEncoding.UTF_8, UTF8CharacterEncoding.class);
            });
        });
    }

    static void test(TestRunner runner, Function0<? extends CharacterEncoding> creator)
    {
        runner.testGroup(CharacterEncoding.class, () ->
        {
            runner.testGroup("encodeCharacter(char)", () ->
            {
                final Action1<Character> encodeCharacterTest = (Character character) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        final byte[] bytes = encoding.encodeCharacter(character).await();
                        test.assertNotNull(bytes);
                        test.assertGreaterThanOrEqualTo(bytes.length, 1);
                    });
                };

                encodeCharacterTest.run('a');
                encodeCharacterTest.run('b');
            });

            runner.testGroup("encodeCharacter(char,ByteWriteStream)", () ->
            {
                runner.test("with null ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.encodeCharacter('a', null),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    byteWriteStream.dispose().await();

                    test.assertThrows(() -> encoding.encodeCharacter('a', byteWriteStream),
                        new PreConditionFailure("byteWriteStream.isDisposed() cannot be true."));
                });
            });

            runner.testGroup("encodeCharacters(String)", () ->
            {
                final Action2<String,Throwable> encodeCharactersErrorTest = (String text, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertThrows(() -> encoding.encodeCharacters(text).await(), expectedError);
                    });
                };

                encodeCharactersErrorTest.run(null, new PreConditionFailure("text cannot be null."));

                final Action2<String,byte[]> encodeCharactersTest = (String text, byte[] expectedBytes) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertEqual(expectedBytes, encoding.encodeCharacters(text).await());
                    });
                };

                encodeCharactersTest.run("", new byte[0]);
            });

            runner.testGroup("encodeCharacters(String,ByteWriteStream)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final String text = null;
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    test.assertThrows(() -> encoding.encodeCharacters(text, byteWriteStream),
                        new PreConditionFailure("text cannot be null."));
                });

                runner.test("with null ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final String text = "a";
                    final InMemoryByteStream byteWriteStream = null;
                    test.assertThrows(() -> encoding.encodeCharacters(text, byteWriteStream),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final String text = "a";
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    byteWriteStream.dispose().await();
                    test.assertThrows(() -> encoding.encodeCharacters(text, byteWriteStream),
                        new PreConditionFailure("byteWriteStream.isDisposed() cannot be true."));
                });

                runner.test("with " + Strings.escapeAndQuote(""), (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new byte[0], encoding.encodeCharacters("").await());
                });
            });

            runner.testGroup("encodeCharacters(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.encodeCharacters((char[])null),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new byte[0], encoding.encodeCharacters(new char[0]).await());
                });
            });

            runner.testGroup("encodeCharacters(char[],ByteWriteStream)", () ->
            {
                runner.test("with null char[]", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final char[] characters = null;
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteWriteStream),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with null ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final char[] characters = new char[] { 'a' };
                    final InMemoryByteStream byteWriteStream = null;
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteWriteStream),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final char[] characters = new char[] { 'a' };
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    byteWriteStream.dispose().await();
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteWriteStream),
                        new PreConditionFailure("byteWriteStream.isDisposed() cannot be true."));
                });

                runner.test("with " + Strings.escapeAndQuote(""), (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final char[] characters = new char[] { 'a' };
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();

                    final int bytesWritten = encoding.encodeCharacters(characters, byteWriteStream).await();
                    test.assertGreaterThanOrEqualTo(1, bytesWritten);

                    final byte[] encodedBytes = byteWriteStream.getBytes();
                    test.assertNotNull(encodedBytes);
                    test.assertGreaterThanOrEqualTo(encodedBytes.length, 1);
                });
            });

            runner.testGroup("encodeCharacters(char[],int,int)", () ->
            {
                final Action4<char[],Integer,Integer,Throwable> encodeCharactersErrorTest = (char[] characters, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(characters), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertThrows(() -> encoding.encodeCharacters(characters, startIndex, length).await(), expected);
                    });
                };

                encodeCharactersErrorTest.run(null, 0, 0, new PreConditionFailure("characters cannot be null."));
                encodeCharactersErrorTest.run(new char[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 1."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, 0, 2, new PreConditionFailure("length (2) must be between 0 and 1."));

                final Action3<char[],Integer,Integer> encodeCharactersTest = (char[] characters, Integer startIndex, Integer length) ->
                {
                    runner.test("with " + English.andList(Array.toString(characters), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertEqual(new byte[0], encoding.encodeCharacters(characters, startIndex, length).await());
                    });
                };

                encodeCharactersTest.run(new char[0], 0, 0);
                encodeCharactersTest.run(new char[] { 'a' }, 0, 0);
                encodeCharactersTest.run(new char[] { 'a', 'b' }, 0, 0);
                encodeCharactersTest.run(new char[] { 'a', 'b' }, 1, 0);
            });

            runner.testGroup("encodeCharacters(char[],int,int,ByteWriteStream)", () ->
            {
                final Action4<char[],Integer,Integer,Throwable> encodeCharactersErrorTest = (char[] characters, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(characters), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                        test.assertThrows(() -> encoding.encodeCharacters(characters, startIndex, length, byteWriteStream).await(), expected);
                    });
                };

                encodeCharactersErrorTest.run(null, 0, 0, new PreConditionFailure("characters cannot be null."));
                encodeCharactersErrorTest.run(new char[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 1."));
                encodeCharactersErrorTest.run(new char[] { 'a' }, 0, 2, new PreConditionFailure("length (2) must be between 0 and 1."));

                runner.test("with null ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final InMemoryByteStream byteWriteStream = null;
                    test.assertThrows(() -> encoding.encodeCharacters(new char[0], 0, 0, byteWriteStream).await(),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                    byteWriteStream.dispose().await();
                    test.assertThrows(() -> encoding.encodeCharacters(new char[0], 0, 0, byteWriteStream).await(),
                        new PreConditionFailure("byteWriteStream.isDisposed() cannot be true."));
                });

                final Action3<char[],Integer,Integer> encodeCharactersTest = (char[] characters, Integer startIndex, Integer length) ->
                {
                    runner.test("with " + English.andList(Array.toString(characters), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                        test.assertEqual(0, encoding.encodeCharacters(characters, startIndex, length, byteWriteStream).await());
                        test.assertEqual(new byte[0], byteWriteStream.getBytes());
                    });
                };

                encodeCharactersTest.run(new char[0], 0, 0);
                encodeCharactersTest.run(new char[] { 'a' }, 0, 0);
                encodeCharactersTest.run(new char[] { 'a', 'b' }, 0, 0);
                encodeCharactersTest.run(new char[] { 'a', 'b' }, 1, 0);

                runner.test("with non-empty bytes written", (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        final InMemoryByteStream byteWriteStream = InMemoryByteStream.create();
                        final int bytesWritten = encoding.encodeCharacters(new char[] { 'a', 'b' }, 0, 2, byteWriteStream).await();
                        test.assertGreaterThanOrEqualTo(bytesWritten, 1);

                        final byte[] bytes = byteWriteStream.getBytes();
                        test.assertNotNull(bytes);
                        test.assertGreaterThanOrEqualTo(bytes.length, 1);
                    });
            });

            runner.testGroup("encodeCharacters(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.encodeCharacters((Iterable<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new byte[0], encoding.encodeCharacters(Iterable.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final byte[] result = encoding.encodeCharacters(Iterable.create('a')).await();
                    test.assertNotNull(result);
                    test.assertGreaterThanOrEqualTo(result.length, 1);
                });

                runner.test("with null character", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    test.assertThrows(() -> encoding.encodeCharacters(Iterable.create((Character)null)).await(),
                        new IllegalArgumentException("Can't encode a null character."));
                });
            });

            runner.testGroup("encodeCharacters(Iterable<Character>,ByteWriteStream)", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterable<Character> characters = null;
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with null ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterable<Character> characters = Iterable.create();
                    final InMemoryByteStream byteStream = null;
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterable<Character> characters = Iterable.create();
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    byteStream.dispose().await();
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream),
                        new PreConditionFailure("byteWriteStream.isDisposed() cannot be true."));
                });

                runner.test("with empty characters", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterable<Character> characters = Iterable.create();
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    test.assertEqual(0, encoding.encodeCharacters(characters, byteStream).await());
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with non-empty characters", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterable<Character> characters = Iterable.create('a');
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();

                    final int bytesWritten = encoding.encodeCharacters(characters, byteStream).await();
                    test.assertGreaterThanOrEqualTo(bytesWritten, 1);

                    final byte[] bytes = byteStream.getBytes();
                    test.assertNotNull(bytes);
                    test.assertGreaterThanOrEqualTo(bytes.length, 1);
                });

                runner.test("with null first character", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterable<Character> characters = Iterable.create((Character)null);
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();

                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream).await(),
                        new IllegalArgumentException("Can't encode a null character."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with null second character", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterable<Character> characters = Iterable.create('a', null);
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();

                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream).await(),
                        new IllegalArgumentException("Can't encode a null character."));

                    final byte[] bytes = byteStream.getBytes();
                    test.assertNotNull(bytes);
                    test.assertGreaterThanOrEqualTo(bytes.length, 1);
                });
            });

            runner.testGroup("encodeCharacters(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.encodeCharacters((Iterator<Character>)null),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new byte[0], encoding.encodeCharacters(Iterator.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final byte[] result = encoding.encodeCharacters(Iterator.create('a')).await();
                    test.assertNotNull(result);
                    test.assertGreaterThanOrEqualTo(result.length, 1);
                });

                runner.test("with null character", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    test.assertThrows(() -> encoding.encodeCharacters(Iterator.create((Character)null)).await(),
                        new IllegalArgumentException("Can't encode a null character."));
                });
            });

            runner.testGroup("encodeCharacters(Iterator<Character>,ByteWriteStream)", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterator<Character> characters = null;
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with null ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterator<Character> characters = Iterator.create();
                    final InMemoryByteStream byteStream = null;
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream),
                        new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterator<Character> characters = Iterator.create();
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    byteStream.dispose().await();
                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream),
                        new PreConditionFailure("byteWriteStream.isDisposed() cannot be true."));
                });

                runner.test("with empty characters", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final Iterator<Character> characters = Iterator.create();
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    test.assertEqual(0, encoding.encodeCharacters(characters, byteStream).await());
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with non-empty characters", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterator<Character> characters = Iterator.create('a');
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();

                    final int bytesWritten = encoding.encodeCharacters(characters, byteStream).await();
                    test.assertGreaterThanOrEqualTo(bytesWritten, 1);

                    final byte[] bytes = byteStream.getBytes();
                    test.assertNotNull(bytes);
                    test.assertGreaterThanOrEqualTo(bytes.length, 1);
                });

                runner.test("with null first character", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterator<Character> characters = Iterator.create((Character)null);
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();

                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream).await(),
                        new IllegalArgumentException("Can't encode a null character."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with null second character", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterator<Character> characters = Iterator.create('a', null);
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();

                    test.assertThrows(() -> encoding.encodeCharacters(characters, byteStream).await(),
                        new IllegalArgumentException("Can't encode a null character."));

                    final byte[] bytes = byteStream.getBytes();
                    test.assertNotNull(bytes);
                    test.assertGreaterThanOrEqualTo(bytes.length, 1);
                });
            });

            runner.testGroup("decodeAsCharacters(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeAsCharacters((byte[])null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new char[0], encoding.decodeAsCharacters(new byte[0]).await());
                });
            });

            runner.testGroup("decodeAsCharacters(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,Throwable> decodeAsCharactersErrorTest = (byte[] bytes, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(bytes), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertThrows(() -> encoding.decodeAsCharacters(bytes, startIndex, length).await(),
                            expected);
                    });
                };

                decodeAsCharactersErrorTest.run(null, 0, 0, new PreConditionFailure("bytes cannot be null."));
                decodeAsCharactersErrorTest.run(new byte[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                decodeAsCharactersErrorTest.run(new byte[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                decodeAsCharactersErrorTest.run(new byte[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                decodeAsCharactersErrorTest.run(new byte[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));
                decodeAsCharactersErrorTest.run(new byte[] { 9 }, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                decodeAsCharactersErrorTest.run(new byte[] { 9 }, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                decodeAsCharactersErrorTest.run(new byte[] { 9 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 1."));
                decodeAsCharactersErrorTest.run(new byte[] { 9 }, 0, 2, new PreConditionFailure("length (2) must be between 0 and 1."));
                decodeAsCharactersErrorTest.run(new byte[] { 8, 9 }, -1, 0, new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                decodeAsCharactersErrorTest.run(new byte[] { 8, 9 }, 2, 0, new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                decodeAsCharactersErrorTest.run(new byte[] { 8, 9 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 2."));
                decodeAsCharactersErrorTest.run(new byte[] { 8, 9 }, 0, 3, new PreConditionFailure("length (3) must be between 0 and 2."));
                decodeAsCharactersErrorTest.run(new byte[] { 8, 9 }, 1, 2, new PreConditionFailure("length (2) must be between 0 and 1."));

                final Action3<byte[],Integer,Integer> decodeAsCharactersTest = (byte[] bytes, Integer startIndex, Integer length) ->
                {
                    runner.test("with " + English.andList(Array.toString(bytes), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertEqual(new char[0], encoding.decodeAsCharacters(bytes, startIndex, length).await());
                    });
                };

                decodeAsCharactersTest.run(new byte[0], 0, 0);
                decodeAsCharactersTest.run(new byte[] { 1, 2, 3 }, 1, 0);
            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeAsString((byte[])null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual("", encoding.decodeAsString(new byte[0]).await());
                });
            });

            runner.testGroup("decodeAsString(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,Throwable> decodeAsStringErrorTest = (byte[] bytes, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(bytes), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertThrows(() -> encoding.decodeAsString(bytes, startIndex, length).await(),
                            expected);
                    });
                };

                decodeAsStringErrorTest.run(null, 0, 0, new PreConditionFailure("bytes cannot be null."));
                decodeAsStringErrorTest.run(new byte[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                decodeAsStringErrorTest.run(new byte[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                decodeAsStringErrorTest.run(new byte[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                decodeAsStringErrorTest.run(new byte[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));
                decodeAsStringErrorTest.run(new byte[] { 9 }, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                decodeAsStringErrorTest.run(new byte[] { 9 }, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                decodeAsStringErrorTest.run(new byte[] { 9 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 1."));
                decodeAsStringErrorTest.run(new byte[] { 9 }, 0, 2, new PreConditionFailure("length (2) must be between 0 and 1."));
                decodeAsStringErrorTest.run(new byte[] { 8, 9 }, -1, 0, new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                decodeAsStringErrorTest.run(new byte[] { 8, 9 }, 2, 0, new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                decodeAsStringErrorTest.run(new byte[] { 8, 9 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 2."));
                decodeAsStringErrorTest.run(new byte[] { 8, 9 }, 0, 3, new PreConditionFailure("length (3) must be between 0 and 2."));
                decodeAsStringErrorTest.run(new byte[] { 8, 9 }, 1, 2, new PreConditionFailure("length (2) must be between 0 and 1."));

                final Action3<byte[],Integer,Integer> decodeAsStringTest = (byte[] bytes, Integer startIndex, Integer length) ->
                {
                    runner.test("with " + English.andList(Array.toString(bytes), startIndex, length), (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertEqual("", encoding.decodeAsString(bytes, startIndex, length).await());
                    });
                };

                decodeAsStringTest.run(new byte[0], 0, 0);
                decodeAsStringTest.run(new byte[] { 1, 2, 3 }, 1, 0);
            });

            runner.testGroup("decodeAsCharacters(Iterable<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeAsCharacters((Iterable<Byte>)null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new char[0], encoding.decodeAsCharacters(Iterable.create()).await());
                });
            });

            runner.testGroup("decodeAsCharacters(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeAsCharacters((Iterator<Byte>)null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(new char[0], encoding.decodeAsCharacters(Iterator.create()).await());
                });
            });

            runner.testGroup("decodeAsString(Iterable<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeAsString((Iterable<Byte>)null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual("", encoding.decodeAsString(Iterable.create()).await());
                });
            });

            runner.testGroup("decodeAsString(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeAsString((Iterator<Byte>)null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual("", encoding.decodeAsString(Iterator.create()).await());
                });
            });

            runner.testGroup("decodeNextCharacter(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final InMemoryByteStream byteStream = null;
                    test.assertThrows(() -> encoding.decodeNextCharacter(byteStream),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final InMemoryByteStream byteStream = InMemoryByteStream.create().endOfStream();
                    byteStream.dispose().await();
                    test.assertThrows(() -> encoding.decodeNextCharacter(byteStream),
                        new PreConditionFailure("bytes.isDisposed() cannot be true."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    final InMemoryByteStream byteStream = InMemoryByteStream.create().endOfStream();
                    test.assertThrows(() -> encoding.decodeNextCharacter(byteStream).await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("decodeNextCharacter(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeNextCharacter((Iterator<Byte>)null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.decodeNextCharacter(Iterator.create()).await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("iterateDecodedCharacters(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertThrows(() -> encoding.iterateDecodedCharacters(null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertEqual(Iterable.create(), encoding.iterateDecodedCharacters(Iterator.create()).toList());
                });

                runner.test("with null Byte", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();

                    final Iterator<Character> characters = encoding.iterateDecodedCharacters(Iterator.create((Byte)null));
                    test.assertNotNull(characters);
                    test.assertFalse(characters.hasStarted());

                    test.assertThrows(IllegalArgumentException.class, () ->
                    {
                        characters.next();
                        characters.getCurrent();
                    });
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action1<Object> equalsTest = (Object rhs) ->
                {
                    runner.test("with " + rhs, (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertFalse(encoding.equals(rhs));
                    });
                };

                equalsTest.run(null);
                equalsTest.run("hello");

                runner.test("with equal object", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertTrue(encoding.equals((Object)creator.run()));
                });

                runner.test("with same object", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertTrue(encoding.equals((Object)encoding));
                });
            });

            runner.testGroup("equals(CharacterEncoding)", () ->
            {
                final Action1<CharacterEncoding> equalsTest = (CharacterEncoding rhs) ->
                {
                    runner.test("with " + rhs, (Test test) ->
                    {
                        final CharacterEncoding encoding = creator.run();
                        test.assertFalse(encoding.equals(rhs));
                    });
                };

                equalsTest.run(null);

                runner.test("with equal object", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertTrue(encoding.equals(creator.run()));
                });

                runner.test("with same object", (Test test) ->
                {
                    final CharacterEncoding encoding = creator.run();
                    test.assertTrue(encoding.equals(encoding));
                });
            });
        });
    }
}
