package qub;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class InputStreamToByteReadStreamTests
{
    @Test
    public void constructor()
    {
        final ByteArrayInputStream inputStream = getInputStream(5);
        final InputStreamToByteReadStream readStream = new InputStreamToByteReadStream(inputStream);
        assertByteReadStream(readStream, true, false, null);
    }

    private static void closeTest(InputStream inputStream, boolean expectedResult)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(readStream, expectedResult);
        assertByteReadStream(readStream, false, false, null);
    }

    private static void closeTest(InputStream inputStream, boolean expectedResult, boolean expectedIsOpen)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(readStream, expectedResult, expectedIsOpen);
    }

    private static void closeTest(InputStreamToByteReadStream readStream, boolean expectedResult)
    {
        closeTest(readStream, expectedResult, false);
    }

    private static void closeTest(InputStreamToByteReadStream readStream, boolean expectedResult, boolean expectedIsOpen)
    {
        assertEquals("close()", expectedResult, readStream.close());
        assertEquals("isOpen()", expectedIsOpen, readStream.isOpen());
    }

    @Test
    public void close() throws IOException
    {
        closeTest(getInputStream(0), true);
        closeTest(getInputStream(5), true);

        final ByteArrayInputStream closedInputStream = getInputStream(1);
        closedInputStream.close();
        closeTest(closedInputStream, true);

        final InputStreamToByteReadStream closedReadStream = getByteReadStream(1);
        closedReadStream.close();
        closeTest(closedReadStream, false);

        final TestStubInputStream testStubInputStream = new TestStubInputStream();
        testStubInputStream.setThrowOnClose(true);
        closeTest(testStubInputStream, false, true);
    }

    @Test
    public void read()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(2);

        assertEquals(Byte.valueOf((byte)0), byteReadStream.readByte());
        assertEquals(Byte.valueOf((byte)1), byteReadStream.readByte());
        assertEquals(null, byteReadStream.readByte());
    }

    @Test
    public void readWithException()
    {
        final TestStubInputStream inputStream = new TestStubInputStream();
        inputStream.setThrowOnRead(true);
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(inputStream);

        assertEquals(null, byteReadStream.readByte());
    }

    @Test
    public void readByteArrayWithNoBytes()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(0);

        final byte[] buffer = new byte[10];
        assertEquals(-1, byteReadStream.readBytes(buffer));
    }

    @Test
    public void readByteArrayWithBytes()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(3);

        final byte[] buffer = new byte[10];
        assertEquals(3, byteReadStream.readBytes(buffer));
        assertArrayEquals(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, buffer);

        assertEquals(-1, byteReadStream.readBytes(buffer));
    }

    @Test
    public void asCharacterReadStreamWithNoEncoding()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

        final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();

        assertEquals(Character.valueOf('a'), characterReadStream.readCharacter());
    }

    @Test
    public void asCharacterReadStreamWithEncoding()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

        final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream(CharacterEncoding.US_ASCII);

        assertEquals(Character.valueOf('a'), characterReadStream.readCharacter());
    }

    @Test
    public void asCharacterReadStreamWithNullEncoding()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(10);

        assertNull(byteReadStream.asCharacterReadStream((CharacterEncoding)null));
    }

    @Test
    public void next()
    {
        final InputStreamToByteReadStream byteReadStream = getByteReadStream(5);

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(byteReadStream.next());
            assertByteReadStream(byteReadStream, true, true, (byte)i);
        }

        assertFalse(byteReadStream.next());
        assertByteReadStream(byteReadStream, true, true, null);
    }

    private static ByteArrayInputStream getInputStream(int byteCount)
    {
        final byte[] bytes = new byte[byteCount];
        for (int i = 0; i < byteCount; ++i)
        {
            bytes[i] = (byte)i;
        }
        return new ByteArrayInputStream(bytes);
    }

    private static InputStreamToByteReadStream getByteReadStream(int byteCount)
    {
        return getByteReadStream(getInputStream(byteCount));
    }

    private static InputStreamToByteReadStream getByteReadStream(InputStream inputStream)
    {
        return new InputStreamToByteReadStream(inputStream);
    }

    private static void assertByteReadStream(ByteReadStream byteReadStream, boolean isOpen, boolean hasStarted, Byte current)
    {
        assertNotNull(byteReadStream);
        assertEquals(isOpen, byteReadStream.isOpen());
        assertEquals(hasStarted, byteReadStream.hasStarted());
        assertEquals(current != null, byteReadStream.hasCurrent());
        assertEquals(current, byteReadStream.getCurrent());
    }
}
