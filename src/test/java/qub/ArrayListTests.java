package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayListTests extends ListTests
{
    @Override
    protected List<Integer> createList(int count)
    {
        final ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; ++i)
        {
            result.add(i);
        }
        return result;
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
