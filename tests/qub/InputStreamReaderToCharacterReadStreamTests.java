package qub;

import java.io.IOException;

public class InputStreamReaderToCharacterReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InputStreamReaderToCharacterReadStream.class, () ->
        {
            runner.test("constructor(ByteReadStream,CharacterEncoding)", (Test test) ->
            {
                final InputStreamReaderToCharacterReadStream characterReadStream = new InputStreamReaderToCharacterReadStream(new InMemoryByteReadStream(), CharacterEncoding.UTF_8);
                assertCharacterReadStream(test, characterReadStream, false, false, null);
            });
            
            runner.test("readCharacter() with exception", (Test test) ->
            {
                final TestStubInputStream inputStream = new TestStubInputStream();
                inputStream.setThrowOnRead(true);

                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);
                test.assertDone(null, new IOException(), characterReadStream.readCharacter());
                assertCharacterReadStream(test, characterReadStream, false, true, null);
            });
            
            runner.test("readCharacters(char[])", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream("abcdefg".getBytes());
                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                final char[] characters = new char[5];

                int charactersRead = characterReadStream.readCharacters(characters);
                test.assertEqual(5, charactersRead);
                test.assertEqual(new char[] { 'a', 'b', 'c', 'd', 'e' }, characters);
                assertCharacterReadStream(test, characterReadStream, false, true, 'e');

                charactersRead = characterReadStream.readCharacters(characters);
                test.assertEqual(2, charactersRead);
                test.assertEqual(new char[] { 'f', 'g', 'c', 'd', 'e' }, characters);
                assertCharacterReadStream(test, characterReadStream, false, true, 'g');
            });
            
            runner.test("readCharacters(char[]) with exception", (Test test) ->
            {
                final TestStubInputStream inputStream = new TestStubInputStream();
                inputStream.setThrowOnRead(true);

                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);

                final char[] characters = new char[5];
                int charactersRead = characterReadStream.readCharacters(characters);
                test.assertEqual(-1, charactersRead);
                test.assertEqual(new char[5], characters);
                assertCharacterReadStream(test, characterReadStream, false, true, null);
            });
            
            runner.test("readCharacters(char[],int,int)", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream("abcdefg".getBytes());
                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                final char[] characters = new char[5];

                int charactersRead = characterReadStream.readCharacters(characters, 2, 3);
                test.assertEqual(3, charactersRead);
                test.assertEqual(new char[] { (char)0, (char)0, 'a', 'b', 'c' }, characters);

                charactersRead = characterReadStream.readCharacters(characters, 1, 4);
                test.assertEqual(4, charactersRead);
                test.assertEqual(new char[] { (char)0, 'd', 'e', 'f', 'g' }, characters);
            });
            
            runner.test("readCharacters(char[],int,int) with exception", (Test test) ->
            {
                final TestStubInputStream inputStream = new TestStubInputStream();
                inputStream.setThrowOnRead(true);

                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);

                final char[] characters = new char[5];
                int charactersRead = characterReadStream.readCharacters(characters, 3, 1);
                test.assertEqual(-1, charactersRead);
                test.assertEqual(new char[5], characters);
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                characterReadStream.close();
                test.assertTrue(characterReadStream.isDisposed());
                test.assertTrue(byteReadStream.isDisposed());

                characterReadStream.close();
                test.assertTrue(characterReadStream.isDisposed());
                test.assertTrue(byteReadStream.isDisposed());
            });
            
            runner.test("asByteReadStream()", (Test test) ->
            {
                final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                final ByteReadStream asByteReadStream = characterReadStream.asByteReadStream();
                test.assertNotNull(asByteReadStream);
                test.assertSame(byteReadStream, asByteReadStream);
            });
            
            runner.test("next()", (Test test) ->
            {
                final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(new InMemoryByteReadStream("abc".getBytes()));

                for (int i = 0; i < 3; ++i)
                {
                    test.assertTrue(characterReadStream.next());
                    assertCharacterReadStream(test, characterReadStream, false, true, (char)('a' + i));
                }

                test.assertFalse(characterReadStream.next());
                assertCharacterReadStream(test, characterReadStream, false, true, null);
            });
        });
    }

    private static InputStreamReaderToCharacterReadStream getCharacterReadStream(TestStubInputStream inputStream)
    {
        final ByteReadStream byteReadStream = new InputStreamToByteReadStream(inputStream);
        return getCharacterReadStream(byteReadStream);
    }

    private static InputStreamReaderToCharacterReadStream getCharacterReadStream(ByteReadStream byteReadStream)
    {
        return new InputStreamReaderToCharacterReadStream(byteReadStream, CharacterEncoding.UTF_8);
    }

    private static void assertCharacterReadStream(Test test, CharacterReadStream characterReadStream, boolean isDisposed, boolean hasStarted, Character current)
    {
        test.assertNotNull(characterReadStream);
        test.assertEqual(isDisposed, characterReadStream.isDisposed());
        test.assertEqual(hasStarted, characterReadStream.hasStarted());
        test.assertEqual(current != null, characterReadStream.hasCurrent());
        test.assertEqual(current, characterReadStream.getCurrent());
    }
}
