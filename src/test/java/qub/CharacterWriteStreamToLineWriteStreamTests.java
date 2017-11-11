package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterWriteStreamToLineWriteStreamTests
{
    @Test
    public void writeByte()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(byteWriteStream);
        assertTrue(lineWriteStream.write((byte)50));
        assertArrayEquals(new byte[] { 50 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeByteArray()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(byteWriteStream);
        assertTrue(lineWriteStream.write(new byte[] { 1, 2, 3, 4 }));
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeByteArrayStartIndexAndLength()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(byteWriteStream);
        assertTrue(lineWriteStream.write(new byte[] { 1, 2, 3, 4 }, 3, 1));
        assertArrayEquals(new byte[] { 4 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeCharacter()
    {
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
        assertTrue(lineWriteStream.write('z'));
        assertArrayEquals(new byte[] { 122 }, characterWriteStream.getBytes());
        assertEquals("z", characterWriteStream.getText());
    }

    @Test
    public void writeString()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
        assertTrue(lineWriteStream.write("tuv"));
        assertArrayEquals(new byte[] { 116, 117, 118 }, byteWriteStream.getBytes());
        assertEquals("tuv", characterWriteStream.getText());
    }

    @Test
    public void writeLine()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
        assertTrue(lineWriteStream.writeLine());
        assertArrayEquals(new byte[] { 10 }, byteWriteStream.getBytes());
        assertEquals("\n", characterWriteStream.getText());
    }

    @Test
    public void writeLineString()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
        assertTrue(lineWriteStream.writeLine("hello"));
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());
        assertEquals("hello\n", characterWriteStream.getText());
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(ByteWriteStream byteWriteStream)
    {
        return getLineWriteStream(byteWriteStream.asCharacterWriteStream());
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return new CharacterWriteStreamToLineWriteStream(characterWriteStream);
    }
}
