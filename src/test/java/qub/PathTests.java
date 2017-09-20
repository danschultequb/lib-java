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

        final Path normalizedPath = path.normalize();
        assertEquals("", normalizedPath.toString());
        assertSame(normalizedPath, normalizedPath.normalize());
    }

    @Test
    public void parseNonEmpty()
    {
        final Path path = Path.parse("/hello/there.txt");
        assertNotNull(path);
        assertEquals("/hello/there.txt", path.toString());
        assertTrue(path.equals(path));
        assertTrue(path.equals((Object)path));

        final Path normalizedPath = path.normalize();
        assertEquals("/hello/there.txt", normalizedPath.toString());
        assertSame(normalizedPath, normalizedPath.normalize());
    }

    @Test
    public void parseNonEmptyWithMultipleSlashesInARow()
    {
        final Path path = Path.parse("/\\/test1//");
        assertNotNull(path);
        assertEquals("/\\/test1//", path.toString());

        final Path normalizedPath = path.normalize();
        assertEquals("/test1/", normalizedPath.toString());
    }
}
