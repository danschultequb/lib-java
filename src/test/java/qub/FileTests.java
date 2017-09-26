package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileTests
{
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
}
