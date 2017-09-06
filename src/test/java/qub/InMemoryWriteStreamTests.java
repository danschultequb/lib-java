package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryWriteStreamTests
{
    @Test
    public void constructor()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        assertArrayEquals(new byte[0], writeStream.getBytes());
    }

    @Test
    public void writeByte()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        writeStream.write((byte)17);
        assertArrayEquals(new byte[] { 17 }, writeStream.getBytes());
    }

    @Test
    public void writeByteArray()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        writeStream.write(new byte[0]);
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write(new byte[] { 1, 2, 3, 4 });
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, writeStream.getBytes());
    }

    @Test
    public void writeByteArrayWithOffsetAndLength()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        writeStream.write(new byte[0], 0, 0);
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 0);
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 2);
        assertArrayEquals(new byte[] { 2, 3 }, writeStream.getBytes());
    }

    @Test
    public void writeString()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        writeStream.write("");
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write("abc");
        assertArrayEquals(new byte[] { 97, 98, 99 }, writeStream.getBytes());
    }

    @Test
    public void writeLine()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        writeStream.writeLine();
        assertArrayEquals(new byte[] { 10 }, writeStream.getBytes());

        writeStream.writeLine();
        assertArrayEquals(new byte[] { 10, 10 }, writeStream.getBytes());
    }

    @Test
    public void writeLineString()
    {
        final InMemoryWriteStream writeStream = new InMemoryWriteStream();
        writeStream.writeLine("");
        assertArrayEquals(new byte[] { 10 }, writeStream.getBytes());

        writeStream.writeLine("defg");
        assertArrayEquals(new byte[] { 10, 100, 101, 102, 103, 10 }, writeStream.getBytes());
    }
}
