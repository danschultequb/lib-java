package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FolderFileSystemTests
{
    @Test(expected = NullPointerException.class)
    public void constructorWithNullPathString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        new FolderFileSystem(fileSystem, (String)null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithEmptyPathString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        new FolderFileSystem(fileSystem, "");
    }

    @Test
    public void constructorWithRelativePathString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final FolderFileSystem folderFileSystem = new FolderFileSystem(fileSystem, "basefolder");
        assertEquals(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
    }

    @Test
    public void constructorWithRelativePathStringThatEndsWithBackslash()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final FolderFileSystem folderFileSystem = new FolderFileSystem(fileSystem, "basefolder\\");
        assertEquals(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
    }

    @Test
    public void constructorWithRelativePathStringThatEndsWithForwardSlash()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final FolderFileSystem folderFileSystem = new FolderFileSystem(fileSystem, "basefolder/");
        assertEquals(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
    }

    @Test
    public void constructorWithRootedPathString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final FolderFileSystem folderFileSystem = new FolderFileSystem(fileSystem, "\\basefolder");
        assertEquals(Path.parse("\\basefolder"), folderFileSystem.getBaseFolderPath());
    }

    @Test
    public void constructorWithRootedPathStringThatEndsWithBackslash()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final FolderFileSystem folderFileSystem = new FolderFileSystem(fileSystem, "/basefolder\\");
        assertEquals(Path.parse("/basefolder"), folderFileSystem.getBaseFolderPath());
    }

    @Test
    public void constructorWithRootedPathStringThatEndsWithForwardSlash()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final FolderFileSystem folderFileSystem = new FolderFileSystem(fileSystem, "/basefolder/");
        assertEquals(Path.parse("/basefolder"), folderFileSystem.getBaseFolderPath());
    }
}
