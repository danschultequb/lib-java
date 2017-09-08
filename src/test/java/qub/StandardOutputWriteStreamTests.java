package qub;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        withTempStdout(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStdout)
            {
                final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
                stdout.write((byte) 97);

                assertArrayEquals(new byte[] { 97 }, mockStdout.toByteArray());
            }
        });
    }

    @Test
    public void writeByteArray()
    {
        withTempStdout(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStdout)
            {
                final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
                stdout.write(new byte[]{98, 99, 100});

                assertArrayEquals(new byte[] { 98, 99, 100 }, mockStdout.toByteArray());
            }
        });
    }

    @Test
    public void writeByteArrayWithStartIndexAndLength()
    {
        withTempStdout(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStdout)
            {
                final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
                stdout.write(new byte[]{101, 102, 103, 104, 105}, 0, 4);

                assertArrayEquals(new byte[] { 101, 102, 103, 104 }, mockStdout.toByteArray());
            }
        });
    }

    @Test
    public void writeString()
    {
        withTempStdout(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStdout)
            {
                final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
                stdout.write("abc");

                assertArrayEquals(new byte[] { 97, 98, 99 }, mockStdout.toByteArray());
            }
        });
    }

    @Test
    public void writeLine()
    {
        withTempStdout(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStdout)
            {
                final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
                stdout.writeLine();

                assertArrayEquals(new byte[] { 13, 10 }, mockStdout.toByteArray());
            }
        });
    }

    @Test
    public void writeLineWithString()
    {
        withTempStdout(new Action1<ByteArrayOutputStream>()
        {
            @Override
            public void run(ByteArrayOutputStream mockStdout)
            {
                final StandardOutputWriteStream stdout = new StandardOutputWriteStream();
                stdout.writeLine("abcd");

                assertArrayEquals(new byte[] { 97, 98, 99, 100, 13, 10 }, mockStdout.toByteArray());
            }
        });
    }

    private static void withTempStdout(Action1<ByteArrayOutputStream> test)
    {
        final PrintStream stdoutBackup = System.out;
        try
        {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            test.run(output);
        }
        finally
        {
            System.setOut(stdoutBackup);
        }
    }
}
