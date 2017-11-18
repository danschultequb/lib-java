package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryCharacterWriteStreamTests
{
    @Test
    public void constructorWithEncoding()
    {
        final InMemoryCharacterWriteStream writeStream = new InMemoryCharacterWriteStream(CharacterEncoding.US_ASCII);
        assertEquals(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
    }
}
