package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReadStreamToLineReadStreamTests
{
    @Test
    public void constructor()
    {
        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
        final CharacterReadStreamToLineReadStream lineReadStream = new CharacterReadStreamToLineReadStream(characterReadStream);
        assertLineReadStream(lineReadStream, true, false, null);
        assertEquals(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
        assertFalse(lineReadStream.getIncludeNewLines());
    }

    @Test
    public void close()
    {
        final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream();
        assertTrue(lineReadStream.close());
        assertLineReadStream(lineReadStream, false, false, null);

        assertFalse(lineReadStream.close());
        assertLineReadStream(lineReadStream, false, false, null);
    }

    @Test
    public void readLineWithFalseIncludeNewLines()
    {
        final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream("a\nb\r\nc");

        assertEquals("a", lineReadStream.readLine(false));
        assertLineReadStream(lineReadStream, true, true, "a");

        assertEquals("b", lineReadStream.readLine(false));
        assertLineReadStream(lineReadStream, true, true, "b");

        assertEquals("c", lineReadStream.readLine(false));
        assertLineReadStream(lineReadStream, true, true, "c");

        assertEquals(null, lineReadStream.readLine(false));
        assertLineReadStream(lineReadStream, true, true, null);
    }

    @Test
    public void readLineWithTrueIncludeNewLines()
    {
        final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream("a\nb\r\nc");

        assertEquals("a\n", lineReadStream.readLine(true));
        assertLineReadStream(lineReadStream, true, true, "a\n");

        assertEquals("b\r\n", lineReadStream.readLine(true));
        assertLineReadStream(lineReadStream, true, true, "b\r\n");

        assertEquals("c", lineReadStream.readLine(true));
        assertLineReadStream(lineReadStream, true, true, "c");

        assertEquals(null, lineReadStream.readLine(true));
        assertLineReadStream(lineReadStream, true, true, null);
    }

    @Test
    public void nextWithTrueIncludeNewLines()
    {
        final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream("a\nb\r\nc", true);

        assertTrue(lineReadStream.next());
        assertLineReadStream(lineReadStream, true, true, "a\n");

        assertTrue(lineReadStream.next());
        assertLineReadStream(lineReadStream, true, true, "b\r\n");

        assertTrue(lineReadStream.next());
        assertLineReadStream(lineReadStream, true, true, "c");

        assertFalse(lineReadStream.next());
        assertLineReadStream(lineReadStream, true, true, null);
    }

    private static CharacterReadStreamToLineReadStream getLineReadStream()
    {
        return getLineReadStream("");
    }

    private static CharacterReadStreamToLineReadStream getLineReadStream(String text)
    {
        return getLineReadStream(text, false);
    }

    private static CharacterReadStreamToLineReadStream getLineReadStream(String text, boolean includeNewLines)
    {
        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(text);
        return new CharacterReadStreamToLineReadStream(characterReadStream, includeNewLines);
    }

    private static void assertLineReadStream(LineReadStream lineReadStream, boolean isOpen, boolean hasStarted, String current)
    {
        assertNotNull(lineReadStream);
        assertEquals(isOpen, lineReadStream.isOpen());
        assertEquals(hasStarted, lineReadStream.hasStarted());
        assertEquals(current != null, lineReadStream.hasCurrent());
        assertEquals(current, lineReadStream.getCurrent());
    }
}
