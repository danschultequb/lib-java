package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayTests extends IterableTests
{
    @Override
    public Iterable<Integer> createIterable(int count)
    {
        final Array<Integer> result = new Array<>(count);
        for (int i = 0; i < count; ++i)
        {
            result.set(i, i);
        }
        return result;
    }

    @Test
    public void fromValuesWithNoArguments()
    {
        final Array<Integer> array = Array.fromValues();
        assertEquals(0, array.getCount());
    }

    @Test
    public void fromValuesWithOneArgument()
    {
        final Array<Integer> array = Array.fromValues(101);
        assertEquals(1, array.getCount());
        assertEquals(101, array.get(0).intValue());
    }

    @Test
    public void fromValuesWithTwoArguments()
    {
        final Array<Integer> array = Array.fromValues(101, 102);
        assertEquals(2, array.getCount());
        assertEquals(101, array.get(0).intValue());
        assertEquals(102, array.get(1).intValue());
    }

    @Test
    public void fromValuesWithNullIterator()
    {
        final Array<Integer> array = Array.fromValues((Iterator<Integer>)null);
        assertEquals(0, array.getCount());
    }

    @Test
    public void fromValuesWithEmptyIterator()
    {
        final Array<Integer> array = Array.fromValues(new Array<Integer>(0).iterate());
        assertEquals(0, array.getCount());
    }

    @Test
    public void fromValuesWithNonEmptyIterator()
    {
        final Array<Integer> array = Array.fromValues(Array.fromValues(1, 2, 3).iterate());
        assertEquals(3, array.getCount());
        assertEquals(1, array.get(0).intValue());
        assertEquals(2, array.get(1).intValue());
        assertEquals(3, array.get(2).intValue());
    }

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
    public void setAll()
    {
        final Array<Integer> a = new Array<>(0);
        a.setAll(50);

        final Array<Integer> a2 = new Array<>(200);
        a2.setAll(3);
        for (int i = 0; i < a2.getCount(); ++i)
        {
            assertEquals(3, a2.get(i).intValue());
        }
    }

    @Test
    public void iterateReverseWithEmpty()
    {
        final Array<Integer> array = new Array<>(0);
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
        final Array<Integer> array = new Array<>(10);
        for (int i = 0; i < array.getCount(); ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = array.iterateReverse();
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
    public void toByteArrayIteratorNull()
    {
        assertNull(Array.toByteArray((Iterator<Byte>)null));
    }

    @Test
    public void toByteArrayIteratorEmpty()
    {
        assertArrayEquals(new byte[0], Array.toByteArray(new Array<Byte>(0).iterate()));
    }

    @Test
    public void toByteArrayIteratorNonEmpty()
    {
        assertArrayEquals(new byte[] { 0, 1, 2 }, Array.toByteArray(Array.fromValues((byte)0, (byte)1, (byte)2).iterate()));
    }

    @Test
    public void toByteArrayIterableNull()
    {
        assertNull(Array.toByteArray((Iterable<Byte>)null));
    }

    @Test
    public void toByteArrayIterableEmpty()
    {
        assertArrayEquals(new byte[0], Array.toByteArray(new Array<Byte>(0)));
    }

    @Test
    public void toIntArrayIteratorNull()
    {
        assertNull(Array.toIntArray((Iterator<Integer>)null));
    }

    @Test
    public void toIntArrayIteratorEmpty()
    {
        assertArrayEquals(new int[0], Array.toIntArray(new Array<Integer>(0).iterate()));
    }

    @Test
    public void toIntArrayIteratorNonEmpty()
    {
        assertArrayEquals(new int[] { 0, 1, 2 }, Array.toIntArray(Array.fromValues(0, 1, 2).iterate()));
    }

    @Test
    public void toIntArrayIterableNull()
    {
        assertNull(Array.toIntArray((Iterable<Integer>)null));
    }

    @Test
    public void toIntArrayIterableEmpty()
    {
        assertArrayEquals(new int[0], Array.toIntArray(new Array<Integer>(0)));
    }

    @Test
    public void toStringArrayIteratorNull()
    {
        assertNull(Array.toStringArray((Iterator<String>)null));
    }

    @Test
    public void toStringArrayIteratorEmpty()
    {
        assertArrayEquals(new String[0], Array.toStringArray(new Array<String>(0).iterate()));
    }

    @Test
    public void toStringArrayIteratorNonEmpty()
    {
        assertArrayEquals(new String[] { "0", "1", "2" }, Array.toStringArray(Array.fromValues("0", "1", "2").iterate()));
    }

    @Test
    public void toStringArrayIterableNull()
    {
        assertNull(Array.toStringArray((Iterable<String>)null));
    }

    @Test
    public void toStringArrayIterableEmpty()
    {
        assertArrayEquals(new String[0], Array.toStringArray(new Array<String>(0)));
    }
}
