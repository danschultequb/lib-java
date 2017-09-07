package qub;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class StandardInputStreamStreamTests
{
    @Test
    public void constructor()
    {
        final StandardInputReadStream stdin = new StandardInputReadStream();
        assertTrue(stdin.isOpen());
    }

    @Test
    public void close()
    {
        final StandardInputReadStream stdin = new StandardInputReadStream();
        assertFalse(stdin.close());
        assertTrue(stdin.isOpen());
    }

    @Test
    public void readBytes() throws IOException
    {
        final StandardInputReadStream stdin = new StandardInputReadStream();

        System.setIn(new ByteArrayInputStream(new byte[0]));
        assertNull(stdin.readBytes(10));

        System.setIn(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertArrayEquals(new byte[] { 1, 2, 3 }, stdin.readBytes(10));
    }
}
