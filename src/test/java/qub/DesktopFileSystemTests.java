package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class DesktopFileSystemTests
{
    @Test
    public void getRoots()
    {
        final DesktopFileSystem fileSystem = new DesktopFileSystem();
        final Iterable<Root> roots = fileSystem.getRoots();
        assertNotNull(roots);
        assertTrue(1 <= roots.getCount());
    }
}
