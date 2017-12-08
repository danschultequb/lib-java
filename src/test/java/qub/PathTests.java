package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class PathTests
{
    @Test
    public void concatenateRelativePathWithNullPathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenate((String)null);
        assertSame(path, result);
    }

    @Test
    public void concatenateRelativePathWithEmptyPathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenate("");
        assertSame(path, result);
    }

    @Test
    public void concatenateRelativePathWithRelativePathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenate("segment");
        assertEquals("thing", path.toString());
        assertEquals("thingsegment", result.toString());
    }

    @Test
    public void concatenateRelativeWithForwardSlashRelativePathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenate("a/b/c");
        assertEquals("thing", path.toString());
        assertEquals("thinga/b/c", result.toString());
    }

    @Test
    public void concatenateRelativeWithBackslashRelativePathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenate("a\\b\\c");
        assertEquals("thing", path.toString());
        assertEquals("thinga\\b\\c", result.toString());
    }

    @Test
    public void concatenateRelativePathWithRootedPathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenate("C:/test/");
        assertEquals("thing", path.toString());
        assertNull(result);
    }

    @Test
    public void concatenateForwardSlashRelativePathWithNullPathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenate((String)null);
        assertSame(path, result);
    }

    @Test
    public void concatenateForwardSlashRelativePathWithEmptyPathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenate("");
        assertSame(path, result);
    }

    @Test
    public void concatenateForwardSlashRelativePathWithRelativePathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenate("segment");
        assertEquals("z/y", path.toString());
        assertEquals("z/ysegment", result.toString());
    }

    @Test
    public void concatenateForwardSlashRelativeWithForwardSlashRelativePathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenate("a/b/c");
        assertEquals("z/y", path.toString());
        assertEquals("z/ya/b/c", result.toString());
    }

    @Test
    public void concatenateForwardSlashRelativeWithBackslashRelativePathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenate("a\\b\\c");
        assertEquals("z/y", path.toString());
        assertEquals("z/ya\\b\\c", result.toString());
    }

    @Test
    public void concatenateForwardSlashRelativePathWithRootedPathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenate("C:/test/");
        assertEquals("z/y", path.toString());
        assertNull(result);
    }

    @Test
    public void concatenateBackslashRelativePathWithNullPathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenate((String)null);
        assertSame(path, result);
    }

    @Test
    public void concatenateBackslashRelativePathWithEmptyPathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenate("");
        assertSame(path, result);
    }

    @Test
    public void concatenateBackslashRelativePathWithRelativePathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenate("segment");
        assertEquals("z\\y", path.toString());
        assertEquals("z\\ysegment", result.toString());
    }

    @Test
    public void concatenateBackslashRelativeWithForwardSlashRelativePathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenate("a/b/c");
        assertEquals("z\\y", path.toString());
        assertEquals("z\\ya/b/c", result.toString());
    }

    @Test
    public void concatenateBackslashRelativeWithBackslashRelativePathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenate("a\\b\\c");
        assertEquals("z\\y", path.toString());
        assertEquals("z\\ya\\b\\c", result.toString());
    }

    @Test
    public void concatenateBackslashRelativePathWithRootedPathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenate("C:/test/");
        assertEquals("z\\y", path.toString());
        assertNull(result);
    }

    @Test
    public void concatenateSegmentRelativePathWithNullPathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenateSegment((String)null);
        assertSame(path, result);
    }

    @Test
    public void concatenateSegmentRelativePathWithEmptyPathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenateSegment("");
        assertSame(path, result);
    }

    @Test
    public void concatenateSegmentRelativePathWithRelativePathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenateSegment("segment");
        assertEquals("thing", path.toString());
        assertEquals("thing/segment", result.toString());
    }

    @Test
    public void concatenateSegmentRelativeWithForwardSlashRelativePathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenateSegment("a/b/c");
        assertEquals("thing", path.toString());
        assertEquals("thing/a/b/c", result.toString());
    }

    @Test
    public void concatenateSegmentRelativeWithBackslashRelativePathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenateSegment("a\\b\\c");
        assertEquals("thing", path.toString());
        assertEquals("thing/a\\b\\c", result.toString());
    }

    @Test
    public void concatenateSegmentRelativePathWithRootedPathString()
    {
        final Path path = Path.parse("thing");
        final Path result = path.concatenateSegment("C:/test/");
        assertEquals("thing", path.toString());
        assertNull(result);
    }

    @Test
    public void concatenateSegmentForwardSlashRelativePathWithNullPathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenateSegment((String)null);
        assertSame(path, result);
    }

    @Test
    public void concatenateSegmentForwardSlashRelativePathWithEmptyPathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenateSegment("");
        assertSame(path, result);
    }

    @Test
    public void concatenateSegmentForwardSlashRelativePathWithRelativePathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenateSegment("segment");
        assertEquals("z/y", path.toString());
        assertEquals("z/y/segment", result.toString());
    }

    @Test
    public void concatenateSegmentForwardSlashRelativeWithForwardSlashRelativePathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenateSegment("a/b/c");
        assertEquals("z/y", path.toString());
        assertEquals("z/y/a/b/c", result.toString());
    }

    @Test
    public void concatenateSegmentForwardSlashRelativeWithBackslashRelativePathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenateSegment("a\\b\\c");
        assertEquals("z/y", path.toString());
        assertEquals("z/y/a\\b\\c", result.toString());
    }

    @Test
    public void concatenateSegmentForwardSlashRelativePathWithRootedPathString()
    {
        final Path path = Path.parse("z/y");
        final Path result = path.concatenateSegment("C:/test/");
        assertEquals("z/y", path.toString());
        assertNull(result);
    }

    @Test
    public void concatenateSegmentBackslashRelativePathWithNullPathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenateSegment((String)null);
        assertSame(path, result);
    }

    @Test
    public void concatenateSegmentBackslashRelativePathWithEmptyPathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenateSegment("");
        assertSame(path, result);
    }

    @Test
    public void concatenateSegmentBackslashRelativePathWithRelativePathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenateSegment("segment");
        assertEquals("z\\y", path.toString());
        assertEquals("z\\y/segment", result.toString());
    }

    @Test
    public void concatenateSegmentBackslashRelativeWithForwardSlashRelativePathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenateSegment("a/b/c");
        assertEquals("z\\y", path.toString());
        assertEquals("z\\y/a/b/c", result.toString());
    }

    @Test
    public void concatenateSegmentBackslashRelativeWithBackslashRelativePathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenateSegment("a\\b\\c");
        assertEquals("z\\y", path.toString());
        assertEquals("z\\y/a\\b\\c", result.toString());
    }

    @Test
    public void concatenateSegmentBackslashRelativePathWithRootedPathString()
    {
        final Path path = Path.parse("z\\y");
        final Path result = path.concatenateSegment("C:/test/");
        assertEquals("z\\y", path.toString());
        assertNull(result);
    }

    @Test
    public void endsWithNonEmptyAndNull()
    {
        final Path path = Path.parse("apples");
        assertFalse(path.endsWith(null));
    }

    @Test
    public void endsWithNonEmptyAndEmpty()
    {
        final Path path = Path.parse("apples");
        assertFalse(path.endsWith(""));
    }

    @Test
    public void endsWithNonEmptyAndNonEmptyWithDifferentSuffix()
    {
        final Path path = Path.parse("apples");
        assertFalse(path.endsWith("sel"));
    }

    @Test
    public void endsWithNonEmptyAndNonEmptyWithEqualSuffix()
    {
        final Path path = Path.parse("apples");
        assertTrue(path.endsWith("les"));
    }

    @Test
    public void parseNull()
    {
        assertNull(Path.parse(null));
    }

    @Test
    public void parseEmpty()
    {
        final Path path = Path.parse("");
        assertNull(path);
    }

    @Test
    public void parseNonEmpty()
    {
        final Path path = Path.parse("/hello/there.txt");
        assertNotNull(path);
        assertEquals("/hello/there.txt", path.toString());
        assertTrue(path.isRooted());
        assertEquals("/", path.getRoot());
        assertEquals(Path.parse("/"), path.getRootPath());
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
        assertTrue(path.isRooted());
        assertEquals("/", path.getRoot());
        assertEquals(Path.parse("/"), path.getRootPath());
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
        assertTrue(path.isRooted());
        assertEquals("C:", path.getRoot());
        assertEquals(Path.parse("C:"), path.getRootPath());
        final Indexable<String> pathSegments = path.getSegments();
        assertNotNull(pathSegments);
        assertArrayEquals(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(pathSegments));

        final Path normalizedPath = path.normalize();
        assertEquals("C:/Windows/System32/cmd.exe", normalizedPath.toString());
        final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
        assertNotNull(normalizedPathSegments);
        assertArrayEquals(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(normalizedPathSegments));
    }

    @Test
    public void hasFileExtensionWithPathWithNoPeriod()
    {
        final Path path = Path.parse("/a/b/c/");
        assertFalse(path.hasFileExtension());
    }

    @Test
    public void hasFileExtensionWithPathWithFileExtension()
    {
        final Path path = Path.parse("folder/file.txt");
        assertTrue(path.hasFileExtension());
    }

    @Test
    public void hasFileExtensionWithPeriodBeforeLastSegment()
    {
        final Path path = Path.parse("a.b/c/d");
        assertFalse(path.hasFileExtension());
    }

    @Test
    public void getFileExtensionWithPathWithNoPeriod()
    {
        final Path path = Path.parse("/a/b/c/");
        assertNull(path.getFileExtension());
    }

    @Test
    public void getFileExtensionWithPathWithFileExtension()
    {
        final Path path = Path.parse("folder/file.txt");
        assertEquals(".txt", path.getFileExtension());
    }

    @Test
    public void getFileExtensionWithPeriodBeforeLastSegment()
    {
        final Path path = Path.parse("a.b/c/d");
        assertNull(path.getFileExtension());
    }

    @Test
    public void withoutFileExtensionWithPathWithNoPeriod()
    {
        final Path path = Path.parse("/a/b/c/");
        assertSame(path, path.withoutFileExtension());
    }

    @Test
    public void withoutFileExtensionWithPathWithFileExtension()
    {
        final Path path = Path.parse("folder/file.txt");
        assertEquals(Path.parse("folder/file"), path.withoutFileExtension());
    }

    @Test
    public void withoutFileExtensionWithPeriodBeforeLastSegment()
    {
        final Path path = Path.parse("a.b/c/d");
        assertSame(path, path.withoutFileExtension());
    }
}
