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
}
