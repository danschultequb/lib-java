package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryLineWriteStreamTests
{
    @Test
    public void constructorWithEncoding()
    {
        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream(CharacterEncoding.US_ASCII);
        assertEquals(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
    }

    @Test
    public void constructorWithEncodingAndLineSeparator()
    {
        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
        assertEquals(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
        assertEquals("\r\n", writeStream.getLineSeparator());
    }

    @Test
    public void getText()
    {
        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream();
        assertEquals("", writeStream.getText());

        writeStream.writeLine("hello");
        assertEquals("hello\n", writeStream.getText());
    }
}
