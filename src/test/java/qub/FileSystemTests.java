package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class FileSystemTests
{
    protected abstract FileSystem getFileSystem();

    @Test
    public void getRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final Root root1 = fileSystem.getRoot("/daffy/");
        assertFalse(root1.exists());
        assertEquals("/daffy/", root1.toString());
    }

    @Test
    public void getRoots()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<Root> roots = fileSystem.getRoots();
        assertNotNull(roots);
        assertTrue(1 <= roots.getCount());
    }

    @Test
    public void getEntriesForExistingPath()
    {
        boolean foundEntries = false;

        final FileSystem fileSystem = getFileSystem();
        for (final Root root : fileSystem.getRoots())
        {
            final Iterable<FileSystemEntry> entries = fileSystem.getEntries(root.getPath());
            if (entries.any())
            {
                foundEntries = true;
                break;
            }
        }

        assertTrue(foundEntries);
    }

    @Test
    public void getEntriesForNonExistingPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getEntries("/i/dont/exist/");
        assertNotNull(entries);
        assertFalse(entries.any());
    }

    @Test
    public void getEntriesForNullPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getEntries((Path)null);
        assertNotNull(entries);
        assertFalse(entries.any());
    }
}
