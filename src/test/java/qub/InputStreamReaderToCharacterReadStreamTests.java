package qub;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class InputStreamReaderToCharacterReadStreamTests
{
    @Test
    public void constructor()
    {
        final InputStreamReaderToCharacterReadStream characterReadStream = new InputStreamReaderToCharacterReadStream(new InMemoryByteReadStream(), CharacterEncoding.UTF_8);
        assertCharacterReadStream(characterReadStream, true, false, null);
    }

    @Test
    public void readCharacterWithException()
    {
        final TestStubInputStream inputStream = new TestStubInputStream();
        inputStream.setThrowOnRead(true);

        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);
        assertNull(characterReadStream.readCharacter());
        assertCharacterReadStream(characterReadStream, true, true, null);
    }

    @Test
    public void readCharactersCharacterArray()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream("abcdefg".getBytes());
        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
        final char[] characters = new char[5];

        int charactersRead = characterReadStream.readCharacters(characters);
        assertEquals(5, charactersRead);
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e' }, characters);
        assertCharacterReadStream(characterReadStream, true, true, 'e');

        charactersRead = characterReadStream.readCharacters(characters);
        assertEquals(2, charactersRead);
        assertArrayEquals(new char[] { 'f', 'g', 'c', 'd', 'e' }, characters);
        assertCharacterReadStream(characterReadStream, true, true, 'g');
    }

    @Test
    public void readCharactersCharacterArrayWithException()
    {
        final TestStubInputStream inputStream = new TestStubInputStream();
        inputStream.setThrowOnRead(true);

        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);

        final char[] characters = new char[5];
        int charactersRead = characterReadStream.readCharacters(characters);
        assertEquals(-1, charactersRead);
        assertArrayEquals(new char[5], characters);
        assertCharacterReadStream(characterReadStream, true, true, null);
    }

    @Test
    public void readCharactersCharacterArrayStartIndexLength()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream("abcdefg".getBytes());
        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
        final char[] characters = new char[5];

        int charactersRead = characterReadStream.readCharacters(characters, 2, 3);
        assertEquals(3, charactersRead);
        assertArrayEquals(new char[] { (char)0, (char)0, 'a', 'b', 'c' }, characters);

        charactersRead = characterReadStream.readCharacters(characters, 1, 4);
        assertEquals(4, charactersRead);
        assertArrayEquals(new char[] { (char)0, 'd', 'e', 'f', 'g' }, characters);
    }

    @Test
    public void readCharactersCharacterArrayStartIndexAndLengthWithException()
    {
        final TestStubInputStream inputStream = new TestStubInputStream();
        inputStream.setThrowOnRead(true);

        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(inputStream);

        final char[] characters = new char[5];
        int charactersRead = characterReadStream.readCharacters(characters, 3, 1);
        assertEquals(-1, charactersRead);
        assertArrayEquals(new char[5], characters);
    }

    @Test
    public void close()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
        assertTrue(characterReadStream.close());
        assertFalse(characterReadStream.isOpen());
        assertFalse(byteReadStream.isOpen());

        assertFalse(characterReadStream.close());
        assertFalse(characterReadStream.isOpen());
        assertFalse(byteReadStream.isOpen());
    }

    @Test
    public void asByteReadStream()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(byteReadStream);
        final ByteReadStream asByteReadStream = characterReadStream.asByteReadStream();
        assertNotNull(asByteReadStream);
        assertSame(byteReadStream, asByteReadStream);
    }

    @Test
    public void next()
    {
        final InputStreamReaderToCharacterReadStream characterReadStream = getCharacterReadStream(new InMemoryByteReadStream("abc".getBytes()));

        for (int i = 0; i < 3; ++i)
        {
            assertTrue(characterReadStream.next());
            assertCharacterReadStream(characterReadStream, true, true, (char)('a' + i));
        }

        assertFalse(characterReadStream.next());
        assertCharacterReadStream(characterReadStream, true, true, null);
    }

    private static InputStreamReaderToCharacterReadStream getCharacterReadStream()
    {
        final ByteReadStream byteReadStream = new InMemoryByteReadStream();
        return getCharacterReadStream(byteReadStream);
    }

    private static InputStreamReaderToCharacterReadStream getCharacterReadStream(InputStream inputStream)
    {
        final ByteReadStream byteReadStream = new InputStreamToByteReadStream(inputStream);
        return getCharacterReadStream(byteReadStream);
    }

    private static InputStreamReaderToCharacterReadStream getCharacterReadStream(ByteReadStream byteReadStream)
    {
        return new InputStreamReaderToCharacterReadStream(byteReadStream, CharacterEncoding.UTF_8);
    }

    private static void assertCharacterReadStream(CharacterReadStream characterReadStream, boolean isOpen, boolean hasStarted, Character current)
    {
        assertNotNull(characterReadStream);
        assertEquals(isOpen, characterReadStream.isOpen());
        assertEquals(hasStarted, characterReadStream.hasStarted());
        assertEquals(current != null, characterReadStream.hasCurrent());
        assertEquals(current, characterReadStream.getCurrent());
    }
}
