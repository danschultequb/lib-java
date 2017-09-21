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
        final Indexable<String> pathSegments = path.getSegments();
        assertNotNull(pathSegments);
        assertEquals(0, pathSegments.getCount());

        final Path normalizedPath = path.normalize();
        assertEquals("", normalizedPath.toString());
        assertSame(normalizedPath, normalizedPath.normalize());
        final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
        assertNotNull(normalizedPathSegments);
        assertEquals(0, normalizedPathSegments.getCount());
    }

    @Test
    public void parseNonEmpty()
    {
        final Path path = Path.parse("/hello/there.txt");
        assertNotNull(path);
        assertEquals("/hello/there.txt", path.toString());
        assertTrue(path.equals(path));
        assertTrue(path.equals((Object)path));
        final Indexable<String> pathSegments = path.getSegments();
        assertNotNull(pathSegments);
        assertArrayEquals(new String[] { "/", "hello", "there.txt" }, Array.toStringArray(pathSegments));

        final Path normalizedPath = path.normalize();
        assertEquals("/hello/there.txt", normalizedPath.toString());
        assertSame(normalizedPath, normalizedPath.normalize());
        final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
        assertNotNull(normalizedPathSegments);
        assertArrayEquals(new String[] { "/", "hello", "there.txt" }, Array.toStringArray(normalizedPathSegments));
    }

    @Test
    public void parseNonEmptyWithMultipleSlashesInARow()
    {
        final Path path = Path.parse("/\\/test1//");
        assertNotNull(path);
        assertEquals("/\\/test1//", path.toString());
        final Indexable<String> pathSegments = path.getSegments();
        assertNotNull(pathSegments);
        assertArrayEquals(new String[] { "/", "test1" }, Array.toStringArray(pathSegments));

        final Path normalizedPath = path.normalize();
        assertEquals("/test1/", normalizedPath.toString());
        final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
        assertNotNull(normalizedPathSegments);
        assertArrayEquals(new String[] { "/", "test1" }, Array.toStringArray(normalizedPathSegments));
    }

    @Test
    public void parseWindowsRootedPath()
    {
        final Path path = Path.parse("C:\\Windows\\System32\\cmd.exe");
        assertNotNull(path);
        assertEquals("C:\\Windows\\System32\\cmd.exe", path.toString());
        final Indexable<String> pathSegments = path.getSegments();
        assertNotNull(pathSegments);
        assertArrayEquals(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(pathSegments));

        final Path normalizedPath = path.normalize();
        assertEquals("C:/Windows/System32/cmd.exe", normalizedPath.toString());
        final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
        assertNotNull(normalizedPathSegments);
        assertArrayEquals(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(normalizedPathSegments));
    }
}
