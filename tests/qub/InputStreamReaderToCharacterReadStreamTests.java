package qub;

public class InputStreamReaderToCharacterReadStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InputStreamReaderToCharacterReadStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor(ByteReadStream,CharacterEncoding)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InputStreamReaderToCharacterReadStream characterReadStream = new InputStreamReaderToCharacterReadStream(new InMemoryByteReadStream(), CharacterEncoding.UTF_8);
                        assertCharacterReadStream(test, characterReadStream, true, false, null);
                    }
                });
                
                runner.test("readCharacter() with exception", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final TestStubInputStream inputStream = new TestStubInputStream();
                        inputStream.setThrowOnRead(true);

                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);
                        test.assertNull(characterReadStream.readCharacter());
                        assertCharacterReadStream(test, characterReadStream, true, true, null);
                    }
                });
                
                runner.test("readCharacters(char[])", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream("abcdefg".getBytes());
                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                        final char[] characters = new char[5];

                        int charactersRead = characterReadStream.readCharacters(characters);
                        test.assertEqual(5, charactersRead);
                        test.assertEqual(new char[] { 'a', 'b', 'c', 'd', 'e' }, characters);
                        assertCharacterReadStream(test, characterReadStream, true, true, 'e');

                        charactersRead = characterReadStream.readCharacters(characters);
                        test.assertEqual(2, charactersRead);
                        test.assertEqual(new char[] { 'f', 'g', 'c', 'd', 'e' }, characters);
                        assertCharacterReadStream(test, characterReadStream, true, true, 'g');
                    }
                });
                
                runner.test("readCharacters(char[]) with exception", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final TestStubInputStream inputStream = new TestStubInputStream();
                        inputStream.setThrowOnRead(true);

                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);

                        final char[] characters = new char[5];
                        int charactersRead = characterReadStream.readCharacters(characters);
                        test.assertEqual(-1, charactersRead);
                        test.assertEqual(new char[5], characters);
                        assertCharacterReadStream(test, characterReadStream, true, true, null);
                    }
                });
                
                runner.test("readCharacters(char[],int,int)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
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
                    }
                });
                
                runner.test("readCharacters(char[],int,int) with exception", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final TestStubInputStream inputStream = new TestStubInputStream();
                        inputStream.setThrowOnRead(true);

                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);

                        final char[] characters = new char[5];
                        int charactersRead = characterReadStream.readCharacters(characters, 3, 1);
                        test.assertEqual(-1, charactersRead);
                        test.assertEqual(new char[5], characters);
                    }
                });
                
                runner.test("close()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                        characterReadStream.close();
                        test.assertFalse(characterReadStream.isOpen());
                        test.assertFalse(byteReadStream.isOpen());

                        characterReadStream.close();
                        test.assertFalse(characterReadStream.isOpen());
                        test.assertFalse(byteReadStream.isOpen());
                    }
                });
                
                runner.test("asByteReadStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
                        final ByteReadStream asByteReadStream = characterReadStream.asByteReadStream();
                        test.assertNotNull(asByteReadStream);
                        test.assertSame(byteReadStream, asByteReadStream);
                    }
                });
                
                runner.test("next()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(new InMemoryByteReadStream("abc".getBytes()));

                        for (int i = 0; i < 3; ++i)
                        {
                            test.assertTrue(characterReadStream.next());
                            assertCharacterReadStream(test, characterReadStream, true, true, (char)('a' + i));
                        }

                        test.assertFalse(characterReadStream.next());
                        assertCharacterReadStream(test, characterReadStream, true, true, null);
                    }
                });
            }
        });
    }

    private static InputStreamReaderToCharacterReadStream getCharacterReadStream()
    {
        final ByteReadStream byteReadStream = new InMemoryByteReadStream();
        return getCharacterReadStream(byteReadStream);
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

    private static void assertCharacterReadStream(Test test, CharacterReadStream characterReadStream, boolean isOpen, boolean hasStarted, Character current)
    {
        test.assertNotNull(characterReadStream);
        test.assertEqual(isOpen, characterReadStream.isOpen());
        test.assertEqual(hasStarted, characterReadStream.hasStarted());
        test.assertEqual(current != null, characterReadStream.hasCurrent());
        test.assertEqual(current, characterReadStream.getCurrent());
    }
}
