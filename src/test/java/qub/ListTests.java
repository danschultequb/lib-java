package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class ListTests extends IndexableTests
{
    @Override
    protected Indexable<Integer> createIndexable(int count)
    {
        return createList(count);
    }

    protected abstract List<Integer> createList(int count);

    @Test
    public void add()
    {
        final List<Integer> list = createList(0);
        assertEquals(0, list.getCount());
        assertFalse(list.any());
        assertEquals(null, list.get(0));

        for (int i = 0; i < 100; ++i)
        {
            list.add(100 - i);
            assertEquals(i + 1, list.getCount());
            assertTrue(list.any());
            assertEquals(100 - i, list.get(i).intValue());
        }
    }

    @Test
    public void addAll()
    {
        final List<Integer> list = createList(0);
        assertEquals(0, list.getCount());
        assertFalse(list.any());
        assertEquals(null, list.get(0));

        list.addAll();
        assertEquals(0, list.getCount());
        assertFalse(list.any());
        assertEquals(null, list.get(0));

        list.addAll(0);
        assertEquals(1, list.getCount());
        assertTrue(list.any());
        assertEquals(0, list.get(0).intValue());

        list.addAll(1, 2, 3, 4, 5);
        assertEquals(6, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }

        list.addAll((Iterator<Integer>)null);
        assertEquals(6, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }

        list.addAll(Array.<Integer>fromValues().iterate());
        assertEquals(6, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }

        list.addAll(Array.fromValues(6, 7, 8, 9).iterate());
        assertEquals(10, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }

        list.addAll((Iterable<Integer>)null);
        assertEquals(10, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }

        list.addAll(Array.<Integer>fromValues());
        assertEquals(10, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }

        list.addAll(Array.fromValues(10, 11));
        assertEquals(12, list.getCount());
        assertTrue(list.any());
        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(i, list.get(i).intValue());
        }
    }

    @Test
    public void set()
    {
        final List<Integer> list = createList(0);
        for (int i = -1; i <= 1; ++i)
        {
            list.set(i, i);
            assertNull(list.get(i));
        }

        for (int i = 0; i < 10; ++i)
        {
            list.add(i);
        }

        for (int i = 0; i < list.getCount(); ++i)
        {
            list.set(i, -list.get(i));
        }

        for (int i = 0; i < list.getCount(); ++i)
        {
            assertEquals(-i, list.get(i).intValue());
        }
    }

    @Test
    public void removeAt()
    {
        final List<Integer> list = createList(0);
        for (int i = -1; i <= 1; ++i)
        {
            assertNull(list.removeAt(i));
        }
        assertEquals(0, list.getCount());

        for (int i = 0; i < 10; ++i)
        {
            list.add(i);
        }
        assertEquals(10, list.getCount());

        assertEquals(0, list.removeAt(0).intValue());
        assertEquals(9, list.getCount());

        assertEquals(9, list.removeAt(8).intValue());
        assertEquals(8, list.getCount());

        assertEquals(5, list.removeAt(4).intValue());
        assertEquals(7, list.getCount());
    }

    @Test
    public void removeFirst()
    {
        final List<Integer> list = createList(0);

        assertNull(list.removeFirst());

        list.add(10);
        assertEquals(10, list.removeFirst().intValue());
        assertNull(list.removeFirst());

        list.addAll(11, 12, 13);
        assertEquals(11, list.removeFirst().intValue());
        assertEquals(12, list.removeFirst().intValue());
        assertEquals(13, list.removeFirst().intValue());
        assertNull(list.removeFirst());
    }

    @Test
    public void removeFirstWithNullConditionAndEmptyArrayList()
    {
        final List<Integer> list = createList(0);
        assertNull(list.removeFirst(null));
    }

    @Test
    public void removeFirstWithNullConditionAndNonEmptyList()
    {
        final List<Integer> list = createList(4);
        assertNull(list.removeFirst(null));
    }

    @Test
    public void removeFirstWithNonMatchingConditionAndEmptyList()
    {
        final List<Integer> list = createList(0);
        assertNull(list.removeFirst(Math.isOdd));
    }

    @Test
    public void removeFirstWithNonMatchingConditionAndNonEmptyList()
    {
        final List<Integer> list = createList(1);
        assertNull(list.removeFirst(Math.isOdd));
    }

    @Test
    public void removeFirstWithMatchingConditionAndNonEmptyList()
    {
        final List<Integer> list = createList(4);
        assertEquals(1, list.removeFirst(Math.isOdd).intValue());
        assertArrayEquals(new int[] { 0, 2, 3 }, Array.toIntArray(list));
    }

    @Test
    public void removeFirstWhenListHasOnlyOneValueAndItMatches()
    {
        final List<Integer> list = createList(1);
        assertEquals(0, list.removeFirst(Math.isEven).intValue());
        assertFalse(list.any());
    }

    @Test
    public void addAfterRemovingOnlyValueInList()
    {
        final List<Integer> list = createList(1);
        list.removeFirst(Math.isEven);

        list.add(70);
        assertArrayEquals(new int[] { 70 }, Array.toIntArray(list));
    }

    @Test
    public void clear()
    {
        final List<Integer> list = createList(0);
        list.clear();
        assertEquals(0, list.getCount());

        for (int i = 0; i < 5; ++i)
        {
            list.add(i);
        }
        assertEquals(5, list.getCount());

        list.clear();
        assertEquals(0, list.getCount());
    }
}
