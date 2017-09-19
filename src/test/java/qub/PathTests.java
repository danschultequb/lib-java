package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class PathTests
{
    @Test
    public void parseNull()
    {
        assertNull(Path.parse(null));
    }

    @Test
    public void parseEmpty()
    {
        final Path path = Path.parse("");
        assertNotNull(path);
        assertEquals("", path.toString());
        assertTrue(path.equals(path));
        assertTrue(path.equals((Object)path));
    }
}
