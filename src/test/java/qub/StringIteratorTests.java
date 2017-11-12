package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringIteratorTests
{
    @Test
    public void constructorWithNull()
    {
        final StringIterator iterator = new StringIterator(null);
        assertIterator(iterator, false, null);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null);
    }

    @Test
    public void constructorWithEmpty()
    {
        final StringIterator iterator = new StringIterator("");
        assertIterator(iterator, false, null);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null);
    }

    @Test
    public void constructorWithNonEmpty()
    {
        final StringIterator iterator = new StringIterator("ab");
        assertIterator(iterator, false, null);

        assertTrue(iterator.next());
        assertIterator(iterator, true, 'a');

        assertTrue(iterator.next());
        assertIterator(iterator, true, 'b');

        assertFalse(iterator.next());
        assertIterator(iterator, true, null);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null);
    }

    private static void assertIterator(StringIterator iterator, boolean hasStarted, Character current)
    {
        assertEquals(hasStarted, iterator.hasStarted());
        assertEquals(current != null, iterator.hasCurrent());
        assertEquals(current, iterator.getCurrent());
    }
}
