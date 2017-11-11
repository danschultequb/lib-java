package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryCharacterReadStreamTests
{
    @Test
    public void readCharactersInt()
    {
        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(20));
        assertNull(characterReadStream.readCharacters(1));
    }

    @Test
    public void readCharactersCharacterArray()
    {
        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
        final char[] characters = new char[2];

        assertEquals(2, characterReadStream.readCharacters(characters));
        assertArrayEquals(new char[] { 'a', 'b' }, characters);

        assertEquals(1, characterReadStream.readCharacters(characters));
        assertArrayEquals(new char[] { 'c', 'b' }, characters);

        assertEquals(-1, characterReadStream.readCharacters(characters));
        assertArrayEquals(new char[] { 'c', 'b' }, characters);
    }

    @Test
    public void asLineReadStream()
    {
        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
        final LineReadStream lineReadStream = characterReadStream.asLineReadStream();
        assertNotNull(lineReadStream);
    }
}
