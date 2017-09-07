package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class StandardOutputWriteStreamTests
{
    @Test
    public void constructor()
    {
        final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
        assertTrue(stdout.isOpen());
    }

    @Test
    public void close()
    {
        final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
        assertFalse(stdout.close());
        assertTrue(stdout.isOpen());
        assertFalse(stdout.close());
    }

    @Test
    public void write()
    {
        final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
        stdout.write((byte)97);
    }

    @Test
    public void writeByteArray()
    {
        final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
        stdout.write(new byte[]{98, 99, 100});
    }

    @Test
    public void writeByteArrayWithStartIndexAndLength()
    {
        final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
        stdout.write(new byte[]{101, 102, 103, 104, 105}, 0, 4);
    }
}
