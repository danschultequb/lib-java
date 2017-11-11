package qub;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class OutputStreamToByteWriteStreamTests
{
    @Test
    public void constructor()
    {
        final OutputStreamToByteWriteStream writeStream = new OutputStreamToByteWriteStream(getOutputStream());
        assertTrue(writeStream.isOpen());
    }

    private static void closeTest(OutputStream outputStream, boolean expectedCloseResult, boolean expectedIsOpen)
    {
        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
        assertEquals(expectedCloseResult, writeStream.close());
        assertEquals(expectedIsOpen, writeStream.isOpen());
    }

    @Test
    public void close()
    {
        closeTest(new ByteArrayOutputStream(), true, false);
        closeTest(new TestStubOutputStream(), false, true);
    }

    private static void writeByteTests(byte toWrite, boolean expectedWriteResult)
    {
        writeByteTests(new ByteArrayOutputStream(), toWrite, expectedWriteResult);
    }

    private static void writeByteTests(ByteArrayOutputStream outputStream, byte toWrite, boolean expectedWriteResult)
    {
        writeByteTests((OutputStream)outputStream, toWrite, expectedWriteResult);
        assertArrayEquals(new byte[] { toWrite }, outputStream.toByteArray());
    }

    private static void writeByteTests(OutputStream outputStream, byte toWrite, boolean expectedWriteResult)
    {
        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
        assertEquals(expectedWriteResult, writeStream.write(toWrite));
    }

    private static void writeByteArrayTests(byte[] toWrite, boolean expectedWriteResult)
    {
        writeByteArrayTests(new ByteArrayOutputStream(), toWrite, expectedWriteResult);
    }

    private static void writeByteArrayTests(ByteArrayOutputStream outputStream, byte[] toWrite, boolean expectedWriteResult)
    {
        writeByteArrayTests((OutputStream)outputStream, toWrite, expectedWriteResult);
        assertArrayEquals(toWrite == null ? new byte[0] : toWrite, outputStream.toByteArray());
    }

    private static void writeByteArrayTests(OutputStream outputStream, byte[] toWrite, boolean expectedWriteResult)
    {
        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
        assertEquals(expectedWriteResult, writeStream.write(toWrite));
    }

    private static void writeByteArrayStartIndexAndLengthTests(byte[] toWrite, int startIndex, int length, boolean expectedWriteResult)
    {
        writeByteArrayStartIndexAndLengthTests(new ByteArrayOutputStream(), toWrite, startIndex, length, expectedWriteResult);
    }

    private static void writeByteArrayStartIndexAndLengthTests(ByteArrayOutputStream outputStream, byte[] toWrite, int startIndex, int length, boolean expectedWriteResult)
    {
        writeByteArrayStartIndexAndLengthTests((OutputStream)outputStream, toWrite, startIndex, length, expectedWriteResult);

        final byte[] expectedWrittenBytes = Array.clone(toWrite, startIndex, length);
        assertArrayEquals(expectedWrittenBytes == null ? new byte[0] : expectedWrittenBytes, outputStream.toByteArray());
    }

    private static void writeByteArrayStartIndexAndLengthTests(OutputStream outputStream, byte[] toWrite, int startIndex, int length, boolean expectedWriteResult)
    {
        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
        assertEquals(expectedWriteResult, writeStream.write(toWrite, startIndex, length));
    }

    @Test
    public void write()
    {
        writeByteTests((byte)10, true);
        writeByteTests(new TestStubOutputStream(), (byte)20, false);

        writeByteArrayTests(null, false);
        writeByteArrayTests(new TestStubOutputStream(), null, false);
        writeByteArrayTests(new byte[0], false);
        writeByteArrayTests(new TestStubOutputStream(), new byte[0], false);
        writeByteArrayTests(new byte[] { 0, 1, 2, 3 }, true);
        writeByteArrayTests(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, false);

        writeByteArrayStartIndexAndLengthTests(null, 0, 0, false);
        writeByteArrayStartIndexAndLengthTests(new TestStubOutputStream(), null, 0, 0, false);
        writeByteArrayStartIndexAndLengthTests(new byte[0], 0, 0, false);
        writeByteArrayStartIndexAndLengthTests(new TestStubOutputStream(), new byte[0], 0, 0, false);
        writeByteArrayStartIndexAndLengthTests(new byte[] { 0, 1, 2 }, 0, 0, false);
        writeByteArrayStartIndexAndLengthTests(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false);
        writeByteArrayStartIndexAndLengthTests(new byte[] { 0, 1, 2 }, 1, 1, true);
        writeByteArrayStartIndexAndLengthTests(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, false);
    }

    @Test
    public void asCharacterWriteStreamWithNoEncoding()
    {
        final ByteArrayOutputStream outputStream = getOutputStream();
        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

        final CharacterWriteStream characterWriteStream = writeStream.asCharacterWriteStream();
        characterWriteStream.write("abc");

        assertArrayEquals(new byte[] { 97, 98, 99 }, outputStream.toByteArray());
    }

    @Test
    public void asCharacterWriteStreamWithNullEncoding()
    {
        final ByteArrayOutputStream outputStream = getOutputStream();
        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

        assertNull(writeStream.asCharacterWriteStream((CharacterEncoding)null));
    }

    private static ByteArrayOutputStream getOutputStream()
    {
        return new ByteArrayOutputStream();
    }

    private static OutputStreamToByteWriteStream getWriteStream(OutputStream outputStream)
    {
        return new OutputStreamToByteWriteStream(outputStream);
    }
}
