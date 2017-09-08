package qub;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class StandardErrorTextWriteStreamTests
{
    @Test
    public void constructor()
    {
        final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream();
        assertTrue(stderr.isOpen());
    }

    @Test
    public void close()
    {
        final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream();
        assertFalse(stderr.close());
        assertTrue(stderr.isOpen());
        assertFalse(stderr.close());
    }

    @Test
    public void write()
    {
        withTempStderr(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStderr)
            {
                final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream();
                stderr.write((byte) 97);

                assertArrayEquals(new byte[] { 97 }, mockStderr.toByteArray());
            }
        });
    }

    @Test
    public void writeByteArray()
    {
        withTempStderr(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStderr)
            {
                final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream();
                stderr.write(new byte[]{98, 99, 100});

                assertArrayEquals(new byte[] { 98, 99, 100 }, mockStderr.toByteArray());
            }
        });
    }

    @Test
    public void writeByteArrayWithStartIndexAndLength()
    {
        withTempStderr(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStderr)
            {
                final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream();
                stderr.write(new byte[]{101, 102, 103, 104, 105}, 0, 4);

                assertArrayEquals(new byte[] { 101, 102, 103, 104 }, mockStderr.toByteArray());
            }
        });
    }

    @Test
    public void writeString()
    {
        withTempStderr(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStderr)
            {
                final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream();
                stderr.write("abc");

                assertArrayEquals(new byte[] { 97, 98, 99 }, mockStderr.toByteArray());
            }
        });
    }

    @Test
    public void writeLine()
    {
        withTempStderr(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStderr)
            {
                final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream("\n");
                stderr.writeLine();

                assertArrayEquals(new byte[] { 10 }, mockStderr.toByteArray());
            }
        });
    }

    @Test
    public void writeLineWithString()
    {
        withTempStderr(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStderr)
            {
                final StandardErrorTextWriteStream stderr = new StandardErrorTextWriteStream("\r\n");
                stderr.writeLine("abcd");

                assertArrayEquals(new byte[] { 97, 98, 99, 100, 13, 10 }, mockStderr.toByteArray());
            }
        });
    }

    private static void withTempStderr(Action1<ByteArrayOutputStream> test)
    {
        final PrintStream stderrBackup = System.out;
        try
        {
            final ByteArrayOutputStream error = new ByteArrayOutputStream();
            System.setErr(new PrintStream(error));

            test.run(error);
        }
        finally
        {
            System.setErr(stderrBackup);
        }
    }
}
