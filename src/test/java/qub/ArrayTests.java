package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayTests
{
    @Test
    public void constructorWith0Length()
    {
        final Array<Integer> a = new Array<>(0);
        assertEquals(0, a.getCount());
    }

    @Test
    public void constructorWith1Length()
    {
        final Array<Integer> a = new Array<>(1);
        assertEquals(1, a.getCount());
        assertEquals(null, a.get(0));
    }

    @Test
    public void getWithNegativeIndex()
    {
        final Array<Integer> a = new Array<>(10);
        assertEquals(null, a.get(-1));
    }

    @Test
    public void getWithTooLargeIndex()
    {
        final Array<Integer> a = new Array<>(10);
        assertEquals(null, a.get(10));
    }

    @Test
    public void setWithNegativeIndex()
    {
        final Array<Integer> a = new Array<>(10);
        a.set(-1, 49);
        assertEquals(null, a.get(-1));
    }

    @Test
    public void setWithTooLargeIndex()
    {
        final Array<Integer> a = new Array<>(10);
        a.set(10, 48);
        assertEquals(null, a.get(10));
    }

    @Test
    public void set()
    {
        final Array<Integer> a = new Array<>(11);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
            assertEquals(i, a.get(i).intValue());
        }
    }

    @Test
    public void iterateWithEmpty()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> i = a.iterate();
        assertNotNull(i);
        assertFalse(i.hasStarted());
        assertFalse(i.hasCurrent());
        assertNull(i.getCurrent());

        assertFalse(i.next());
        assertTrue(i.hasStarted());
        assertFalse(i.hasCurrent());
        assertNull(i.getCurrent());
    }

    public void iterateWithNonEmpty()
    {
        final Array<Integer> a = new Array<>(5);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }

        final Iterator<Integer> iterator = a.iterate();
        assertNotNull(iterator);
        assertFalse(iterator.hasStarted());
        assertFalse(iterator.hasCurrent());
        assertNull(iterator.getCurrent());

        for (int i = 0; i < a.getCount(); ++i) {
            assertTrue(iterator.next());
            assertTrue(iterator.hasStarted());
            assertTrue(iterator.hasCurrent());
            assertEquals(i, iterator.getCurrent().intValue());
        }

        assertFalse(iterator.next());
        assertTrue(iterator.hasStarted());
        assertFalse(iterator.hasCurrent());
        assertNull(iterator.getCurrent());
    }

    @Test
    public void forEachWithEmptyArray()
    {
        final Array<Integer> a = new Array<>(0);

        int elementCount = 0;
        for (final Integer i : a)
        {
            ++elementCount;
        }

        assertEquals(0, elementCount);
    }

    @Test
    public void forEachWithNonEmptyArray()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }

        int i = 0;
        for (final Integer element : a)
        {
            assertEquals(i, element.intValue());
            ++i;
        }

        assertEquals(a.getCount(), i);
    }

    @Test
    public void anyWithEmptyArray()
    {
        final Array<Integer> array = new Array<>(0);
        assertFalse(array.any());
    }

    @Test
    public void anyWithNonEmptyArray()
    {
        final Array<Integer> array = new Array<>(110);
        assertTrue(array.any());
    }
}
