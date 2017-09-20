package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JavaFileSystemTests
{
    @Test
    public void getRoot()
    {
        final JavaFileSystem fileSystem = new JavaFileSystem();
        final Root root1 = fileSystem.getRoot("/daffy/");
        assertFalse(root1.exists());
        assertEquals("/daffy/", root1.toString());
    }

    @Test
    public void getRoots()
    {
        final JavaFileSystem fileSystem = new JavaFileSystem();
        final Iterable<Root> roots = fileSystem.getRoots();
        assertNotNull(roots);
        assertTrue(1 <= roots.getCount());
    }
}
