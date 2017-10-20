package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileTests
{
    @Test
    public void create()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        final File file = new File(fileSystem, "/A");

        assertTrue(file.create());

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void createWithNullContents()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        final File file = new File(fileSystem, "/A");

        assertTrue(file.create(null));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void createWithEmptyContents()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        final File file = new File(fileSystem, "/A");

        assertTrue(file.create(new byte[0]));

        assertTrue(file.exists());
        assertArrayEquals(new byte[0], file.getContents());
    }

    @Test
    public void createWithNonEmptyContents()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        final File file = new File(fileSystem, "/A");

        assertTrue(file.create(new byte[] { 0, 1, 2, 3, 4 }));

        assertTrue(file.exists());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, file.getContents());
    }

    @Test
    public void exists()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:/");

        final File file = new File(fileSystem, "C:/folder/file.txt");
        assertFalse(file.exists());

        assertTrue(file.create());
        assertTrue(file.exists());
    }

    @Test
    public void equals()
    {
        final InMemoryFileSystem fileSystem1 = new InMemoryFileSystem();
        fileSystem1.createRoot("/");
        fileSystem1.createFile("/hello/there.txt");
        final File file1 = fileSystem1.getFile("/hello/there.txt");

        assertFalse(file1.equals(null));
        assertFalse(file1.equals("/hello/there.txt"));
        assertFalse(file1.equals(fileSystem1.getFile("/hello/there/again.txt")));
        assertTrue(file1.equals(file1));
        assertTrue(file1.equals(fileSystem1.getFile("/hello/there.txt")));
    }

    @Test
    public void getContentsWithNonExistingFile()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        final File file = new File(fileSystem, "/thing.txt");
        assertNull(file.getContents());
    }

    @Test
    public void getContentsWithExistingEmptyFile()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        fileSystem.createFile("/thing.txt");
        final File file = new File(fileSystem, "/thing.txt");
        assertArrayEquals(new byte[0], file.getContents());
    }
}
