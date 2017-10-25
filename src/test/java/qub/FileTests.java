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

        assertTrue(file.create((byte[])null));

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
    public void createWithNullContentsString()
    {
        final File file = getFile();

        assertTrue(file.create((String)null));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void createWithEmptyContentsString()
    {
        final File file = getFile();

        assertTrue(file.create(""));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void createWithNonEmptyContentsString()
    {
        final File file = getFile();

        assertTrue(file.create("hello"));

        assertTrue(file.exists());
        assertEquals("hello", file.getContentsAsString());
    }

    @Test
    public void createWithNullContentsStringAndEncoding()
    {
        final File file = getFile();

        assertTrue(file.create(null, CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void createWithEmptyContentsStringAndEncoding()
    {
        final File file = getFile();

        assertTrue(file.create("", CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void createWithNonEmptyContentsStringAndEncoding()
    {
        final File file = getFile();

        assertTrue(file.create("hello", CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("hello", file.getContentsAsString());
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

    @Test
    public void setContentsWithNonExistingFileAndNullContents()
    {
        final File file = getFile();

        assertTrue(file.setContents((byte[])null));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void setContentsWithExistingFileAndNullContents()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents((byte[])null));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void setContentsWithNonExistingFileAndEmptyContents()
    {
        final File file = getFile();

        assertTrue(file.setContents(new byte[0]));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void setContentsWithExistingFileAndEmptyContents()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents(new byte[0]));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void setContentsWithNonExistingFileAndNonEmptyContents()
    {
        final File file = getFile();

        assertTrue(file.setContents(new byte[] { 0, 1, 2, 3 }));

        assertTrue(file.exists());
        assertArrayEquals(new byte[] { 0, 1, 2, 3 }, file.getContents());
    }

    @Test
    public void setContentsWithExistingFileAndNonEmptyContents()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents(new byte[] { 0, 1, 2, 3 }));

        assertTrue(file.exists());
        assertArrayEquals(new byte[] { 0, 1, 2, 3 }, file.getContents());
    }

    @Test
    public void setContentsWithNonExistingFileAndNullContentsString()
    {
        final File file = getFile();

        assertTrue(file.setContents((String)null));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithExistingFileAndNullContentsString()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents((String)null));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithNonExistingFileAndEmptyContentsString()
    {
        final File file = getFile();

        assertTrue(file.setContents(""));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithExistingFileAndEmptyContentsString()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents(""));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithNonExistingFileAndNonEmptyContentsString()
    {
        final File file = getFile();

        assertTrue(file.setContents("XYZ"));

        assertTrue(file.exists());
        assertEquals("XYZ", file.getContentsAsString());
    }

    @Test
    public void setContentsWithExistingFileAndNonEmptyContentsString()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents("XYZ"));

        assertTrue(file.exists());
        assertEquals("XYZ", file.getContentsAsString());
    }

    @Test
    public void setContentsWithNonExistingFileAndNullContentsStringAndEncoding()
    {
        final File file = getFile();

        assertTrue(file.setContents(null, CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithExistingFileAndNullContentsStringAndEncoding()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents(null, CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithNonExistingFileAndEmptyContentsStringAndEncoding()
    {
        final File file = getFile();

        assertTrue(file.setContents("", CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithExistingFileAndEmptyContentsStringAndEncoding()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents("", CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("", file.getContentsAsString());
    }

    @Test
    public void setContentsWithNonExistingFileAndNonEmptyContentsStringAndEncoding()
    {
        final File file = getFile();

        assertTrue(file.setContents("ABC", CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("ABC", file.getContentsAsString());
    }

    @Test
    public void setContentsWithExistingFileAndNonEmptyContentsStringAndEncoding()
    {
        final File file = getFile();
        file.create();

        assertTrue(file.setContents("ABC", CharacterEncoding.ASCII));

        assertTrue(file.exists());
        assertEquals("ABC", file.getContentsAsString());
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
