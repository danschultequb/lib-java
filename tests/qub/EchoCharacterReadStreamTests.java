package qub;

public interface EchoCharacterReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(EchoCharacterReadStream.class, () ->
        {
            runner.testGroup("create(CharacterReadStream,CharacterWriteStream)", () ->
            {
                runner.test("with null readStream", (Test test) ->
                {
                    final InMemoryCharacterStream readStream = null;
                    final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();

                    test.assertThrows(() -> EchoCharacterReadStream.create(readStream, writeStream),
                        new PreConditionFailure("readStream cannot be null."));
                });

                runner.test("with null writeStream", (Test test) ->
                {
                    final InMemoryCharacterStream readStream = InMemoryCharacterStream.create().endOfStream();
                    final InMemoryCharacterStream writeStream = null;

                    test.assertThrows(() -> EchoCharacterReadStream.create(readStream, writeStream),
                        new PreConditionFailure("writeStream cannot be null."));
                });

                runner.test("with non-null streams", (Test test) ->
                {
                    final InMemoryCharacterStream readStream = InMemoryCharacterStream.create().endOfStream();
                    final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();

                    final EchoCharacterReadStream echoReadStream = EchoCharacterReadStream.create(readStream, writeStream);
                    test.assertNotNull(echoReadStream);
                    test.assertFalse(echoReadStream.isDisposed());
                    test.assertSame(readStream.getCharacterEncoding(), echoReadStream.getCharacterEncoding());
                });
            });

            runner.testGroup("readCharacter()", () ->
            {
                runner.test("when readStream is at the end of its stream", (Test test) ->
                {
                    final InMemoryCharacterStream readStream = InMemoryCharacterStream.create().endOfStream();
                    final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();
                    final EchoCharacterReadStream echoReadStream = EchoCharacterReadStream.create(readStream, writeStream);

                    test.assertThrows(() -> echoReadStream.readCharacter().await(),
                        new EndOfStreamException());
                    test.assertEqual("", writeStream.getText().await());
                });

                runner.test("when readStream has characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream readStream = InMemoryCharacterStream.create("abc").endOfStream();
                    final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();
                    final EchoCharacterReadStream echoReadStream = EchoCharacterReadStream.create(readStream, writeStream);

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
                    final InMemoryCharacterStream readStream = InMemoryCharacterStream.create().endOfStream();
                    final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();
                    final EchoCharacterReadStream echoReadStream = EchoCharacterReadStream.create(readStream, writeStream);

                    final char[] characters = new char[6];
                    test.assertThrows(() -> echoReadStream.readCharacters(characters, 2, 3).await(),
                        new EndOfStreamException());
                    test.assertEqual("", writeStream.getText().await());
                });

                runner.test("when readStream has characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream readStream = InMemoryCharacterStream.create("abcdef").endOfStream();
                    final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();
                    final EchoCharacterReadStream echoReadStream = EchoCharacterReadStream.create(readStream, writeStream);
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
                final InMemoryCharacterStream readStream = InMemoryCharacterStream.create().endOfStream();
                final InMemoryCharacterStream writeStream = InMemoryCharacterStream.create();
                final EchoCharacterReadStream echoReadStream = EchoCharacterReadStream.create(readStream, writeStream);

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
