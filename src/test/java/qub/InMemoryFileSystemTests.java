package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryFileSystemTests
{
    @Test
    public void getRoots()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        final Iterable<Root> roots1 = fileSystem.getRoots();
        assertNotNull(roots1);
        assertFalse(roots1.any());

        fileSystem.addRoot("/");
        assertFalse(roots1.any());

        final Iterable<Root> roots2 = fileSystem.getRoots();
        assertNotNull(roots2);
        assertTrue(roots2.any());
        assertEquals(1, roots2.getCount());
        assertEquals(Path.parse("/"), roots2.first().getPath());
    }
}
