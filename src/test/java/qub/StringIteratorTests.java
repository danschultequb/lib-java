package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringIteratorTests
{
    @Test
    public void constructorWithNull()
    {
        final StringIterator iterator = new StringIterator(null);
        assertIterator(iterator, false, null, 0);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null, 0);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null, 0);
    }

    @Test
    public void constructorWithEmpty()
    {
        final StringIterator iterator = new StringIterator("");
        assertIterator(iterator, false, null, 0);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null, 0);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null, 0);
    }

    @Test
    public void constructorWithNonEmpty()
    {
        final StringIterator iterator = new StringIterator("ab");
        assertIterator(iterator, false, null, 0);

        assertTrue(iterator.next());
        assertIterator(iterator, true, 'a', 0);

        assertTrue(iterator.next());
        assertIterator(iterator, true, 'b', 1);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null, 2);

        assertFalse(iterator.next());
        assertIterator(iterator, true, null, 2);
    }

    @Test
    public void setCurrentIndexWithNegativeIndex()
    {
        final StringIterator iterator = new StringIterator("hello");

        iterator.setCurrentIndex(-10);
        assertIterator(iterator, true, 'h', 0);
    }

    @Test
    public void setCurrentIndexWithZeroIndex()
    {
        final StringIterator iterator = new StringIterator("hello");

        iterator.setCurrentIndex(0);
        assertIterator(iterator, true, 'h', 0);
    }

    @Test
    public void setCurrentIndexWithPositiveIndexLessThanElementCount()
    {
        final StringIterator iterator = new StringIterator("hello");

        iterator.setCurrentIndex(1);
        assertIterator(iterator, true, 'e', 1);
    }

    @Test
    public void setCurrentIndexWithPositiveIndexEqualToOneLessThanElementCount()
    {
        final StringIterator iterator = new StringIterator("hello");

        iterator.setCurrentIndex(4);
        assertIterator(iterator, true, 'o', 4);
    }

    @Test
    public void setCurrentIndexWithPositiveIndexEqualToElementCount()
    {
        final StringIterator iterator = new StringIterator("hello");

        iterator.setCurrentIndex(5);
        assertIterator(iterator, true, null, 5);
    }

    @Test
    public void setCurrentIndexWithPositiveIndexGreaterThanElementCount()
    {
        final StringIterator iterator = new StringIterator("hello");

        iterator.setCurrentIndex(6);
        assertIterator(iterator, true, null, 5);
    }

    private static void assertIterator(StringIterator iterator, boolean hasStarted, Character current, int currentIndex)
    {
        assertEquals(hasStarted, iterator.hasStarted());
        assertEquals(current != null, iterator.hasCurrent());
        assertEquals(current, iterator.getCurrent());
        assertEquals(currentIndex, iterator.getCurrentIndex());
    }
}
