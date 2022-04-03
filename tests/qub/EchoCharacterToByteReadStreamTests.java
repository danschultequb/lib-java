package qub;

public interface EchoCharacterToByteReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(EchoCharacterToByteReadStream.class, () ->
        {
            runner.testGroup("create(CharacterReadStream,CharacterWriteStream)", () ->
            {
                runner.test("with null readStream", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = null;
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();

                    test.assertThrows(() -> EchoCharacterToByteReadStream.create(readStream, writeStream),
                        new PreConditionFailure("readStream cannot be null."));
                });

                runner.test("with null writeStream", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                    final InMemoryCharacterToByteStream writeStream = null;

                    test.assertThrows(() -> EchoCharacterToByteReadStream.create(readStream, writeStream),
                        new PreConditionFailure("writeStream cannot be null."));
                });

                runner.test("with non-null streams", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();

                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);
                    test.assertNotNull(echoReadStream);
                    test.assertFalse(echoReadStream.isDisposed());
                    test.assertSame(readStream.getCharacterEncoding(), echoReadStream.getCharacterEncoding());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("when readStream is at the end of its stream", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                    test.assertThrows(() -> echoReadStream.readByte().await(),
                        new EndOfStreamException());
                    test.assertEqual("", writeStream.getText().await());
                });

                runner.test("when readStream has characters to read", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create("abc").endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                    test.assertEqual((byte)97, echoReadStream.readByte().await());
                    test.assertEqual(new byte[] { 97 }, writeStream.getBytes());

                    test.assertEqual((byte)98, echoReadStream.readByte().await());
                    test.assertEqual(new byte[] { 97, 98 }, writeStream.getBytes());

                    test.assertEqual((byte)99, echoReadStream.readByte().await());
                    test.assertEqual(new byte[] { 97, 98, 99 }, writeStream.getBytes());

                    test.assertThrows(() -> echoReadStream.readByte().await(),
                        new EndOfStreamException());
                    test.assertEqual(new byte[] { 97, 98, 99 }, writeStream.getBytes());
                });
            });

            runner.testGroup("readBytes(byte[],int,int)", () ->
            {
                runner.test("when readStream is at the end of its stream", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                    final byte[] bytes = new byte[6];
                    test.assertThrows(() -> echoReadStream.readBytes(bytes, 2, 3).await(),
                        new EndOfStreamException());
                    test.assertEqual("", writeStream.getText().await());
                });

                runner.test("when readStream has characters to read", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create("abcdef").endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);
                    final byte[] bytes = new byte[6];

                    test.assertEqual(3, echoReadStream.readBytes(bytes, 2, 3).await());
                    test.assertEqual(new byte[] { 0, 0, 97, 98, 99, 0 }, bytes);
                    test.assertEqual(new byte[] { 97, 98, 99 }, writeStream.getBytes());

                    test.assertEqual(1, echoReadStream.readBytes(bytes, 0, 1).await());
                    test.assertEqual(new byte[] { 100, 0, 97, 98, 99, 0 }, bytes);
                    test.assertEqual(new byte[] { 97, 98, 99, 100 }, writeStream.getBytes());

                    test.assertEqual(2, echoReadStream.readBytes(bytes, 0, 6).await());
                    test.assertEqual(new byte[] { 101, 102, 97, 98, 99, 0 }, bytes);
                    test.assertEqual(new byte[] { 97, 98, 99, 100, 101, 102 }, writeStream.getBytes());

                    test.assertThrows(() -> echoReadStream.readBytes(bytes, 0, 6).await(),
                        new EndOfStreamException());
                        test.assertEqual(new byte[] { 101, 102, 97, 98, 99, 0 }, bytes);
                        test.assertEqual(new byte[] { 97, 98, 99, 100, 101, 102 }, writeStream.getBytes());
                });
            });

            runner.testGroup("readCharacter()", () ->
            {
                runner.test("when readStream is at the end of its stream", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                    test.assertThrows(() -> echoReadStream.readCharacter().await(),
                        new EndOfStreamException());
                    test.assertEqual("", writeStream.getText().await());
                });

                runner.test("when readStream has characters to read", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create("abc").endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                    test.assertEqual('a', echoReadStream.readCharacter().await());
                    test.assertEqual("a", writeStream.getText().await());

                    test.assertEqual('b', echoReadStream.readCharacter().await());
                    test.assertEqual("ab", writeStream.getText().await());

                    test.assertEqual('c', echoReadStream.readCharacter().await());
                    test.assertEqual("abc", writeStream.getText().await());

                    test.assertThrows(() -> echoReadStream.readCharacter().await(),
                        new EndOfStreamException());
                    test.assertEqual("abc", writeStream.getText().await());
                });
            });

            runner.testGroup("readCharacters(char[],int,int)", () ->
            {
                runner.test("when readStream is at the end of its stream", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                    final char[] characters = new char[6];
                    test.assertThrows(() -> echoReadStream.readCharacters(characters, 2, 3).await(),
                        new EndOfStreamException());
                    test.assertEqual("", writeStream.getText().await());
                });

                runner.test("when readStream has characters to read", (Test test) ->
                {
                    final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create("abcdef").endOfStream();
                    final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                    final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);
                    final char[] characters = new char[6];

                    test.assertEqual(3, echoReadStream.readCharacters(characters, 2, 3).await());
                    test.assertEqual(new char[] { '\0', '\0', 'a', 'b', 'c', '\0' }, characters);
                    test.assertEqual("abc", writeStream.getText().await());

                    test.assertEqual(1, echoReadStream.readCharacters(characters, 0, 1).await());
                    test.assertEqual(new char[] { 'd', '\0', 'a', 'b', 'c', '\0' }, characters);
                    test.assertEqual("abcd", writeStream.getText().await());

                    test.assertEqual(2, echoReadStream.readCharacters(characters, 0, 6).await());
                    test.assertEqual(new char[] { 'e', 'f', 'a', 'b', 'c', '\0' }, characters);
                    test.assertEqual("abcdef", writeStream.getText().await());

                    test.assertThrows(() -> echoReadStream.readCharacters(characters, 0, 6).await(),
                        new EndOfStreamException());
                    test.assertEqual(new char[] { 'e', 'f', 'a', 'b', 'c', '\0' }, characters);
                    test.assertEqual("abcdef", writeStream.getText().await());
                });
            });

            runner.test("dispose()", (Test test) ->
            {
                final InMemoryCharacterToByteStream readStream = InMemoryCharacterToByteStream.create().endOfStream();
                final InMemoryCharacterToByteStream writeStream = InMemoryCharacterToByteStream.create();
                final EchoCharacterToByteReadStream echoReadStream = EchoCharacterToByteReadStream.create(readStream, writeStream);

                test.assertTrue(echoReadStream.dispose().await());
                test.assertTrue(echoReadStream.isDisposed());
                test.assertTrue(readStream.isDisposed());
                test.assertFalse(writeStream.isDisposed());

                test.assertFalse(echoReadStream.dispose().await());
                test.assertTrue(echoReadStream.isDisposed());
                test.assertTrue(readStream.isDisposed());
                test.assertFalse(writeStream.isDisposed());
            });
        });
    }
}
