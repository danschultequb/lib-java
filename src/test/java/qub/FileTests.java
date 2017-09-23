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
}
