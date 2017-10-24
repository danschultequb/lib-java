package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileTests
{
    @Test
    public void create()
    {
        final File file = getFile();

        assertTrue(file.create());

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void createWithNullContents()
    {
        final File file = getFile();

        assertTrue(file.create(null));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void createWithEmptyContents()
    {
        final File file = getFile();

        assertTrue(file.create(new byte[0]));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void createWithNonEmptyContents()
    {
        final File file = getFile();

        assertTrue(file.create(new byte[] { 0, 1, 2, 3, 4 }));

        assertTrue(file.exists());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, file.getContents());
    }

    @Test
    public void existsWhenFileNotCreated()
    {
        final File file = getFile();
        assertFalse(file.exists());
    }

    @Test
    public void existsAfterFileIsCreated()
    {
        final File file = getFile();
        file.create();
        assertTrue(file.exists());
    }

    @Test
    public void equalsNull()
    {
        final File file = getFile();
        assertFalse(file.equals(null));
    }

    @Test
    public void equalsPathString()
    {
        final File file = getFile();
        assertFalse(file.equals(file.getPath().toString()));
    }

    @Test
    public void equalsPath()
    {
        final File file = getFile();
        assertFalse(file.equals(file.getPath()));
    }

    @Test
    public void equalsDifferentFileWithSameFileSystem()
    {
        final FileSystem fileSystem = getFileSystem();
        final File lhs = getFile(fileSystem, "/a/path.txt");
        final File rhs = getFile(fileSystem, "/not/the/same/path.txt");
        assertFalse(lhs.equals(rhs));
    }

    @Test
    public void equalsSelf()
    {
        final File file = getFile();
        assertTrue(file.equals(file));
    }

    @Test
    public void equalsFileWithEqualPathAndSameFileSystem()
    {
        final FileSystem fileSystem = getFileSystem();
        final File lhs = getFile(fileSystem);
        final File rhs = getFile(fileSystem);
        assertTrue(lhs.equals(rhs));
    }

    @Test
    public void getContentsWithNonExistingFile()
    {
        final File file = getFile();
        assertNull(file.getContents());
    }

    @Test
    public void getContentsWithExistingEmptyFile()
    {
        final File file = getFile();
        file.create();
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void getContentsAsStringWithNonExistingFile()
    {
        final File file = getFile();
        assertNull(file.getContentsAsString());
    }

    @Test
    public void getContentsAsStringWithExistingFileWithNoContents()
    {
        final File file = getFile();
        file.create();
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void getContentsAsStringWithExistingFileWithContents()
    {
        final File file = getFile();
        file.create("Hello".getBytes());
        assertEquals("Hello", file.getContentsAsString());
    }

    @Test
    public void getContentsAsStringWithNullEncodingAndNonExistingFile()
    {
        final File file = getFile();
        assertNull(file.getContentsAsString());
    }

    @Test
    public void getContentsAsStringWithNullEncodingAndExistingFileWithNoContents()
    {
        final File file = getFile();
        file.create();
        assertNull(file.getContentsAsString(null));
    }

    @Test
    public void getContentsAsStringWithNullEncodingAndExistingFileWithContents()
    {
        final File file = getFile();
        file.create("Hello".getBytes());
        assertNull(file.getContentsAsString(null));
    }

    @Test
    public void getContentsAsStringWithNonNullEncodingAndNonExistingFile()
    {
        final File file = getFile();
        assertNull(file.getContentsAsString(CharacterEncoding.ASCII));
    }

    @Test
    public void getContentsAsStringWithNonNullEncodingAndExistingFileWithNoContents()
    {
        final File file = getFile();
        file.create();
        assertEquals("", file.getContentsAsString(CharacterEncoding.ASCII));
    }

    @Test
    public void getContentsAsStringWithNonNullEncodingAndExistingFileWithContents()
    {
        final File file = getFile();
        file.create("Hello".getBytes());
        assertEquals("Hello", file.getContentsAsString(CharacterEncoding.ASCII));
    }

    private static FileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private static File getFile()
    {
        final FileSystem fileSystem = getFileSystem();
        return getFile(fileSystem);
    }

    private static File getFile(FileSystem fileSystem)
    {
        return getFile(fileSystem, "/A");
    }

    private static File getFile(FileSystem fileSystem, String filePath)
    {
        return new File(fileSystem, filePath);
    }
}
