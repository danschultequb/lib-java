package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class StandardOutputWriteStreamTests
{
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
