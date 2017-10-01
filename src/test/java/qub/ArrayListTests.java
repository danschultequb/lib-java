package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayListTests extends IterableTests
{
    @Override
    protected Iterable<Integer> createIterable(int count)
    {
        final ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; ++i)
        {
            result.add(i);
        }
        return result;
    }

    @Test
    public void add()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        assertEquals(0, arrayList.getCount());
        assertFalse(arrayList.any());
        assertEquals(null, arrayList.get(0));

        for (int i = 0; i < 100; ++i)
        {
            arrayList.add(100 - i);
            assertEquals(i + 1, arrayList.getCount());
            assertTrue(arrayList.any());
            assertEquals(100 - i, arrayList.get(i).intValue());
        }
    }

    @Test
    public void addAll()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        assertEquals(0, arrayList.getCount());
        assertFalse(arrayList.any());
        assertEquals(null, arrayList.get(0));

        arrayList.addAll();
        assertEquals(0, arrayList.getCount());
        assertFalse(arrayList.any());
        assertEquals(null, arrayList.get(0));

        arrayList.addAll(0);
        assertEquals(1, arrayList.getCount());
        assertTrue(arrayList.any());
        assertEquals(0, arrayList.get(0).intValue());

        arrayList.addAll(1, 2, 3, 4, 5);
        assertEquals(6, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }

        arrayList.addAll((Iterator<Integer>)null);
        assertEquals(6, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }

        arrayList.addAll(Array.<Integer>fromValues().iterate());
        assertEquals(6, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }

        arrayList.addAll(Array.fromValues(6, 7, 8, 9).iterate());
        assertEquals(10, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }

        arrayList.addAll((Iterable<Integer>)null);
        assertEquals(10, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }

        arrayList.addAll(Array.<Integer>fromValues());
        assertEquals(10, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }

        arrayList.addAll(Array.fromValues(10, 11));
        assertEquals(12, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }
    }

    @Test
    public void set()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = -1; i <= 1; ++i)
        {
            arrayList.set(i, i);
            assertNull(arrayList.get(i));
        }

        for (int i = 0; i < 10; ++i)
        {
            arrayList.add(i);
        }

        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            arrayList.set(i, -arrayList.get(i));
        }

        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(-i, arrayList.get(i).intValue());
        }
    }

    @Test
    public void removeAt()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = -1; i <= 1; ++i)
        {
            assertNull(arrayList.removeAt(i));
        }
        assertEquals(0, arrayList.getCount());

        for (int i = 0; i < 10; ++i)
        {
            arrayList.add(i);
        }
        assertEquals(10, arrayList.getCount());

        assertEquals(0, arrayList.removeAt(0).intValue());
        assertEquals(9, arrayList.getCount());

        assertEquals(9, arrayList.removeAt(8).intValue());
        assertEquals(8, arrayList.getCount());

        assertEquals(5, arrayList.removeAt(4).intValue());
        assertEquals(7, arrayList.getCount());
    }

    @Test
    public void removeFirst()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();

        assertNull(arrayList.removeFirst());

        arrayList.add(10);
        assertEquals(10, arrayList.removeFirst().intValue());
        assertNull(arrayList.removeFirst());

        arrayList.addAll(11, 12, 13);
        assertEquals(11, arrayList.removeFirst().intValue());
        assertEquals(12, arrayList.removeFirst().intValue());
        assertEquals(13, arrayList.removeFirst().intValue());
        assertNull(arrayList.removeFirst());
    }

    @Test
    public void clear()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.clear();
        assertEquals(0, arrayList.getCount());

        for (int i = 0; i < 5; ++i)
        {
            arrayList.add(i);
        }
        assertEquals(5, arrayList.getCount());

        arrayList.clear();
        assertEquals(0, arrayList.getCount());
    }

    @Test
    public void iterateReverseWithEmpty()
    {
        final ArrayList<Integer> array = new ArrayList<>();
        final Iterator<Integer> iterator = array.iterateReverse();
        assertFalse(iterator.hasStarted());
        assertFalse(iterator.hasCurrent());
        assertNull(iterator.getCurrent());

        assertFalse(iterator.next());
        assertTrue(iterator.hasStarted());
        assertFalse(iterator.hasCurrent());
        assertNull(iterator.getCurrent());
    }

    @Test
    public void iterateReverseWithNonEmpty()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
        {
            arrayList.add(i);
        }
        final Iterator<Integer> iterator = arrayList.iterateReverse();
        assertFalse(iterator.hasStarted());
        assertFalse(iterator.hasCurrent());
        assertNull(iterator.getCurrent());

        for (int i = 9; i >= 0; --i)
        {
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
    public void fromValues()
    {
        final ArrayList<Integer> arrayList1 = ArrayList.fromValues();
        assertEquals(0, arrayList1.getCount());

        final ArrayList<Integer> arrayList2 = ArrayList.fromValues((Integer[])null);
        assertEquals(0, arrayList2.getCount());

        final ArrayList<Integer> arrayList3 = ArrayList.fromValues(1, 2, 3);
        assertEquals(3, arrayList3.getCount());
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(i + 1, arrayList3.get(i).intValue());
        }

        final ArrayList<Integer> arrayList4 = ArrayList.fromValues((Iterator<Integer>)null);
        assertEquals(0, arrayList4.getCount());

        final ArrayList<Integer> arrayList5 = ArrayList.fromValues(new Array<Integer>(0).iterate());
        assertEquals(0, arrayList5.getCount());

        final ArrayList<Integer> arrayList6 = ArrayList.fromValues(Array.fromValues(1, 2, 3).iterate());
        assertEquals(3, arrayList6.getCount());
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(i + 1, arrayList6.get(i).intValue());
        }

        final ArrayList<Integer> arrayList7 = ArrayList.fromValues((Iterable<Integer>)null);
        assertEquals(0, arrayList7.getCount());

        final ArrayList<Integer> arrayList8 = ArrayList.fromValues(new Array<Integer>(0));
        assertEquals(0, arrayList8.getCount());

        final ArrayList<Integer> arrayList9 = ArrayList.fromValues(Array.fromValues(1, 2, 3));
        assertEquals(3, arrayList9.getCount());
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(i + 1, arrayList9.get(i).intValue());
        }
    }
}
