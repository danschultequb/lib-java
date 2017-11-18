package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class OutputStreamWriterToCharacterWriteStreamTests
{
    @Test
    public void constructor()
    {
        final ByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = new OutputStreamWriterToCharacterWriteStream(byteWriteStream, CharacterEncoding.US_ASCII);
        assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
        assertSame(CharacterEncoding.US_ASCII, characterWriteStream.getCharacterEncoding());
    }

    @Test
    public void writeByte()
    {
        InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertTrue(characterWriteStream.write((byte)60));
        assertArrayEquals(new byte[] { 60 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeByteWithException()
    {
        TestStubOutputStream outputStream = new TestStubOutputStream();
        OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertFalse(characterWriteStream.write((byte)60));
    }

    @Test
    public void writeByteArray()
    {
        InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertTrue(characterWriteStream.write(new byte[] { 0, 1, 2 }));
        assertArrayEquals(new byte[] { 0, 1, 2 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeByteArrayWithException()
    {
        TestStubOutputStream outputStream = new TestStubOutputStream();
        OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertFalse(characterWriteStream.write(new byte[] { 3, 4, 5, 6 }));
    }

    @Test
    public void writeByteArrayStartIndexAndLength()
    {
        InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertTrue(characterWriteStream.write(new byte[] { 0, 1, 2 }, 1, 2));
        assertArrayEquals(new byte[] { 1, 2 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeByteArrayStartIndexAndLengthWithException()
    {
        TestStubOutputStream outputStream = new TestStubOutputStream();
        OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertFalse(characterWriteStream.write(new byte[] { 3, 4, 5, 6 }, 2, 1));
    }

    @Test
    public void writeCharacter()
    {
        InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertTrue(characterWriteStream.write('a'));
        assertArrayEquals(new byte[] { 97 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeCharacterWithException()
    {
        TestStubOutputStream outputStream = new TestStubOutputStream();
        OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertFalse(characterWriteStream.write('a'));
    }

    @Test
    public void writeString()
    {
        InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertTrue(characterWriteStream.write("test"));
        assertArrayEquals(new byte[] { 116, 101, 115, 116 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeStringWithException()
    {
        TestStubOutputStream outputStream = new TestStubOutputStream();
        OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertFalse(characterWriteStream.write("test again"));
    }

    @Test
    public void close()
    {
        InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        characterWriteStream.close();
        assertFalse(characterWriteStream.isOpen());
        assertFalse(byteWriteStream.isOpen());

        characterWriteStream.close();
        assertFalse(characterWriteStream.isOpen());
        assertFalse(byteWriteStream.isOpen());
    }

    @Test
    public void closeWithException()
    {
        TestStubOutputStream outputStream = new TestStubOutputStream();
        OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        characterWriteStream.close();
        assertTrue(characterWriteStream.isOpen());
        assertTrue(byteWriteStream.isOpen());
    }

    @Test
    public void asByteWriteStream()
    {
        final ByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
        assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
    }

    @Test
    public void asLineWriteStream()
    {
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream();
        final LineWriteStream lineWriteStream = characterWriteStream.asLineWriteStream();
        assertNotNull(lineWriteStream);
        assertSame(characterWriteStream, lineWriteStream.asCharacterWriteStream());
    }

    @Test
    public void asLineWriteStreamWithLineSeparator()
    {
        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream();
        final LineWriteStream lineWriteStream = characterWriteStream.asLineWriteStream("\n");
        assertNotNull(lineWriteStream);
        assertEquals("\n", lineWriteStream.getLineSeparator());
        assertSame(characterWriteStream, lineWriteStream.asCharacterWriteStream());
    }

    private static OutputStreamWriterToCharacterWriteStream getCharacterWriteStream()
    {
        return getCharacterWriteStream(new InMemoryByteWriteStream());
    }

    private static OutputStreamWriterToCharacterWriteStream getCharacterWriteStream(ByteWriteStream byteWriteStream)
    {
        return new OutputStreamWriterToCharacterWriteStream(byteWriteStream, CharacterEncoding.UTF_8);
    }
}
