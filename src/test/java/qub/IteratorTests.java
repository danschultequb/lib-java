package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class IteratorTests
{
    @Test
    public void anyWithEmptyNonStartedIterator()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertFalse(iterator.any());
    }

    @Test
    public void anyWithEmptyStartedIterator()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertFalse(iterator.next());
        assertFalse(iterator.any());
    }

    @Test
    public void anyWithNonEmptyNonStartedIterator()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertTrue(iterator.any());
    }

    @Test
    public void anyWithNonEmptyAtFirstElementIterator()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertTrue(iterator.next());
        assertTrue(iterator.any());
    }

    @Test
    public void anyWithNonEmptyAtSecondElementIterator()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertTrue(iterator.next());
        assertTrue(iterator.next());
        assertTrue(iterator.any());
    }

    @Test
    public void anyWithEmptyAndNullCondition()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> i = new ArrayIterator<>(a);
        assertFalse(i.any(null));
    }

    @Test
    public void anyWithNonEmptyAndNullCondition()
    {
        final Array<Integer> a = new Array<>(5);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> i = new ArrayIterator<>(a);
        assertFalse(i.any(null));
    }

    @Test
    public void anyWithEmptyAndCondition()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> i = new ArrayIterator<>(a);
        assertFalse(i.any(Math.isEven));
    }

    @Test
    public void anyWithNonEmptyAndNonMatchingCondition()
    {
        final Array<Integer> a = new Array<>(5);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i * 2);
        }
        final Iterator<Integer> i = new ArrayIterator<>(a);
        assertFalse(i.any(Math.isOdd));
        assertTrue(i.hasStarted());
        assertFalse(i.hasCurrent());
    }

    @Test
    public void anyWithNonEmptyAndMatchingCondition()
    {
        final Array<Integer> a = new Array<>(5);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> i = new ArrayIterator<>(a);
        assertTrue(i.any(Math.isOdd));
        assertTrue(i.hasStarted());
        assertTrue(i.hasCurrent());
        assertEquals(1, i.getCurrent().intValue());
    }

    @Test
    public void getCountWithNonStartedEmptyIterator()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertEquals(0, iterator.getCount());
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void getCountWithStartedEmptyIterator()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertFalse(iterator.next());
        assertEquals(0, iterator.getCount());
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void getCountWithNonStartedNonEmptyIterator()
    {
        final Array<Integer> array = new Array<>(3);
        for (int i = 0; i < array.getCount(); ++i) {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertEquals(array.getCount(), iterator.getCount());
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void getCountWithStartedNonEmptyIterator()
    {
        final Array<Integer> array = new Array<>(3);
        for (int i = 0; i < array.getCount(); ++i) {
            array.set(i, i);
        }

        final Iterator<Integer> iterator1 = new ArrayIterator<>(array);
        assertTrue(iterator1.next());
        assertEquals(array.getCount(), iterator1.getCount());
        assertEquals(0, iterator1.getCount());

        final Iterator<Integer> iterator2 = new ArrayIterator<>(array);
        assertTrue(iterator2.next());
        assertTrue(iterator2.next());
        assertEquals(array.getCount() - 1, iterator2.getCount());
        assertEquals(0, iterator2.getCount());
    }

    @Test
    public void forEachWithEmptyNonStartedIterator()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(a);

        int elementCount = 0;
        for (final Integer element : iterator)
        {
            ++elementCount;
        }

        assertEquals(0, elementCount);
    }

    @Test
    public void forEachWithEmptyStartedIterator()
    {
        final Array<Integer> a = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertFalse(iterator.next());

        int elementCount = 0;
        for (final Integer element : iterator)
        {
            ++elementCount;
        }

        assertEquals(0, elementCount);
    }

    @Test
    public void forEachWithNonEmptyNonStartedIterator()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(a);

        int i = 0;
        for (final Integer element : iterator)
        {
            assertEquals(i, element.intValue());
            ++i;
        }

        assertEquals(a.getCount(), i);
    }

    @Test
    public void forEachWithNonEmptyAtFirstElementIterator()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertTrue(iterator.next());

        int i = 0;
        for (final Integer element : iterator)
        {
            assertEquals(i, element.intValue());
            ++i;
        }

        assertEquals(a.getCount(), i);
    }

    @Test
    public void forEachWithNonEmptyAtSecondElementIterator()
    {
        final Array<Integer> a = new Array<>(10);
        for (int i = 0; i < a.getCount(); ++i) {
            a.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        assertTrue(iterator.next());
        assertTrue(iterator.next());

        int i = 1;
        for (final Integer element : iterator)
        {
            assertEquals(i, element.intValue());
            ++i;
        }

        assertEquals(a.getCount(), i);
    }
}
