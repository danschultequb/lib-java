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
}
