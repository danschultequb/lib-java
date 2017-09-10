package qub;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class StandardInputTextReadStreamTests
{
    @Test
    public void constructor()
    {
        final StandardInputTextReadStream stdin = new StandardInputTextReadStream();
        assertTrue(stdin.isOpen());
    }

    @Test
    public void close()
    {
        final StandardInputTextReadStream stdin = new StandardInputTextReadStream();
        assertFalse(stdin.close());
        assertTrue(stdin.isOpen());
    }

    @Test
    public void readBytes() throws IOException
    {
        final StandardInputTextReadStream stdin = new StandardInputTextReadStream(CharacterEncoding.ASCII);
        final InputStream stdinBackup = System.in;

        try
        {
            System.setIn(new ByteArrayInputStream(new byte[0]));
            assertNull(stdin.readBytes(10));

            System.setIn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
            assertArrayEquals(new byte[]{1, 2, 3}, stdin.readBytes(10));
        }
        finally
        {
            System.setIn(stdinBackup);
        }
    }
}
