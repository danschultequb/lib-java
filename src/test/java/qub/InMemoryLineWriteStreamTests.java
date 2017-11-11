package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryLineWriteStreamTests
{
    @Test
    public void getText()
    {
        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream();
        assertEquals("", writeStream.getText());

        writeStream.writeLine("hello");
        assertEquals("hello\n", writeStream.getText());
    }
}
