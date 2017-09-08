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
    public void takeWithNonStartedEmptyIteratorAndNegativeToTake()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertFalse(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithNonStartedEmptyIteratorAndZeroToTake()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> takeIterator = iterator.take(0);
        assertFalse(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithNonStartedEmptyIteratorAndPositiveToTake()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> takeIterator = iterator.take(2);
        assertFalse(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithNonStartedNonEmptyIteratorAndNegativeToTake()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertFalse(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithNonStartedNonEmptyIteratorAndZeroToTake()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> takeIterator = iterator.take(0);
        assertFalse(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithNonStartedNonEmptyIteratorAndPositiveToTake()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> takeIterator = iterator.take(2);
        assertFalse(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        for (int i = 0; i < 2; ++i)
        {
            assertTrue(takeIterator.next());
            assertTrue(takeIterator.hasStarted());
            assertTrue(takeIterator.hasCurrent());
            assertEquals(i, takeIterator.getCurrent().intValue());
        }

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithStartedNonEmptyIteratorAndNegativeToTake()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithStartedNonEmptyIteratorAndZeroToTake()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        iterator.next();

        final Iterator<Integer> takeIterator = iterator.take(0);
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithStartedNonEmptyIteratorAndPositiveToTakeLessThanCount()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> takeIterator = iterator.take(2);
        assertTrue(takeIterator.hasStarted());
        assertTrue(takeIterator.hasCurrent());
        assertEquals(0, takeIterator.getCurrent().intValue());

        assertTrue(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertTrue(takeIterator.hasCurrent());
        assertEquals(1, takeIterator.getCurrent().intValue());

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithStartedNonEmptyIteratorAndPositiveToTakeEqualToCount()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> takeIterator = iterator.take(array.getCount());
        assertTrue(takeIterator.hasStarted());
        assertTrue(takeIterator.hasCurrent());
        assertEquals(0, takeIterator.getCurrent().intValue());

        for (int i = 1; i < array.getCount(); ++i) {
            assertTrue(takeIterator.next());
            assertTrue(takeIterator.hasStarted());
            assertTrue(takeIterator.hasCurrent());
            assertEquals(i, takeIterator.getCurrent().intValue());
        }

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void takeWithStartedNonEmptyIteratorAndPositiveToTakeGreaterThanCount()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> takeIterator = iterator.take(100);
        assertTrue(takeIterator.hasStarted());
        assertTrue(takeIterator.hasCurrent());
        assertEquals(0, takeIterator.getCurrent().intValue());

        for (int i = 1; i < array.getCount(); ++i) {
            assertTrue(takeIterator.next());
            assertTrue(takeIterator.hasStarted());
            assertTrue(takeIterator.hasCurrent());
            assertEquals(i, takeIterator.getCurrent().intValue());
        }

        assertFalse(takeIterator.next());
        assertTrue(takeIterator.hasStarted());
        assertFalse(takeIterator.hasCurrent());
        assertNull(takeIterator.getCurrent());
    }

    @Test
    public void skipWithNonStartedEmptyIteratorAndNegativeToSkip()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> skipIterator = iterator.skip(-3);
        assertFalse(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithNonStartedEmptyIteratorAndZeroToSkip()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> skipIterator = iterator.skip(0);
        assertFalse(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithNonStartedEmptyIteratorAndPositiveToSkip()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> skipIterator = iterator.skip(2);
        assertFalse(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithNonStartedNonEmptyIteratorAndNegativeToSkip()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> skipIterator = iterator.skip(-3);
        assertFalse(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        for (int i = 0; i < array.getCount(); ++i)
        {
            assertTrue(skipIterator.next());
            assertTrue(skipIterator.hasStarted());
            assertTrue(skipIterator.hasCurrent());
            assertEquals(i, skipIterator.getCurrent().intValue());
        }

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithNonStartedNonEmptyIteratorAndZeroToSkip()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> skipIterator = iterator.skip(0);
        assertFalse(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        for (int i = 0; i < array.getCount(); ++i)
        {
            assertTrue(skipIterator.next());
            assertTrue(skipIterator.hasStarted());
            assertTrue(skipIterator.hasCurrent());
            assertEquals(i, skipIterator.getCurrent().intValue());
        }

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithNonStartedNonEmptyIteratorAndPositiveToSkip()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> skipIterator = iterator.skip(2);
        assertFalse(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        for (int i = 2; i < array.getCount(); ++i)
        {
            assertTrue(skipIterator.next());
            assertTrue(skipIterator.hasStarted());
            assertTrue(skipIterator.hasCurrent());
            assertEquals(i, skipIterator.getCurrent().intValue());
        }

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithStartedNonEmptyIteratorAndNegativeToSkip()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> skipIterator = iterator.skip(-3);
        assertTrue(skipIterator.hasStarted());
        assertTrue(skipIterator.hasCurrent());
        assertEquals(0, skipIterator.getCurrent().intValue());

        for (int i = 1; i < array.getCount(); ++i)
        {
            assertTrue(skipIterator.next());
            assertTrue(skipIterator.hasStarted());
            assertTrue(skipIterator.hasCurrent());
            assertEquals(i, skipIterator.getCurrent().intValue());
        }

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithStartedNonEmptyIteratorAndZeroToSkip()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        iterator.next();

        final Iterator<Integer> skipIterator = iterator.skip(0);
        assertTrue(skipIterator.hasStarted());
        assertTrue(skipIterator.hasCurrent());
        assertEquals(0, skipIterator.getCurrent().intValue());

        for (int i = 1; i < array.getCount(); ++i)
        {
            assertTrue(skipIterator.next());
            assertTrue(skipIterator.hasStarted());
            assertTrue(skipIterator.hasCurrent());
            assertEquals(i, skipIterator.getCurrent().intValue());
        }

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithStartedNonEmptyIteratorAndPositiveToSkipLessThanCount()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> skipIterator = iterator.skip(2);
        assertTrue(skipIterator.hasStarted());
        assertTrue(skipIterator.hasCurrent());
        assertEquals(2, skipIterator.getCurrent().intValue());

        for (int i = 3; i < array.getCount(); ++i)
        {
            assertTrue(skipIterator.next());
            assertTrue(skipIterator.hasStarted());
            assertTrue(skipIterator.hasCurrent());
            assertEquals(i, skipIterator.getCurrent().intValue());
        }

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithStartedNonEmptyIteratorAndPositiveToSkipEqualToCount()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> skipIterator = iterator.skip(array.getCount());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void skipWithStartedNonEmptyIteratorAndPositiveToSkipGreaterThanCount()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> skipIterator = iterator.skip(100);
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());

        assertFalse(skipIterator.next());
        assertTrue(skipIterator.hasStarted());
        assertFalse(skipIterator.hasCurrent());
        assertNull(skipIterator.getCurrent());
    }

    @Test
    public void whereWithNonStartedEmptyIteratorAndNullCondition()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> whereIterator = iterator.where(null);
        assertSame(iterator, whereIterator);
    }

    @Test
    public void whereWithNonStartedEmptyIteratorAndCondition()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertFalse(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());

        assertFalse(whereIterator.next());
        assertTrue(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());
    }

    @Test
    public void whereWithNonStartedNonEmptyIteratorAndNullCondition()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> whereIterator = iterator.where(null);
        assertSame(iterator, whereIterator);
    }

    @Test
    public void whereWithNonStartedNonEmptyIteratorAndNonMatchingCondition()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i * 2);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertFalse(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());

        assertFalse(whereIterator.next());
        assertTrue(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());
    }

    @Test
    public void whereWithNonStartedNonEmptyIteratorAndMatchingCondition()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertFalse(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());

        for (int i = 1; i < 5; i += 2)
        {
            assertTrue(whereIterator.next());
            assertTrue(whereIterator.hasStarted());
            assertTrue(whereIterator.hasCurrent());
            assertEquals(i, whereIterator.getCurrent().intValue());
        }

        assertFalse(whereIterator.next());
        assertTrue(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());
    }

    @Test
    public void whereWithStartedNonEmptyIteratorAndNullCondition()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> whereIterator = iterator.where(null);
        assertSame(iterator, whereIterator);
    }

    @Test
    public void whereWithStartedNonEmptyIteratorAndNonMatchingCondition()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i * 2);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertTrue(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());

        assertFalse(whereIterator.next());
        assertTrue(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());
    }

    @Test
    public void whereWithStartedNonEmptyIteratorAndMatchingCondition()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertTrue(whereIterator.hasStarted());
        assertTrue(whereIterator.hasCurrent());
        assertEquals(1, whereIterator.getCurrent().intValue());

        assertTrue(whereIterator.next());
        assertTrue(whereIterator.hasStarted());
        assertTrue(whereIterator.hasCurrent());
        assertEquals(3, whereIterator.getCurrent().intValue());

        assertFalse(whereIterator.next());
        assertTrue(whereIterator.hasStarted());
        assertFalse(whereIterator.hasCurrent());
        assertNull(whereIterator.getCurrent());
    }

    @Test
    public void mapWithNonStartedEmptyIteratorAndNullConversion()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> mapIterator = iterator.map(null);
        assertFalse(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());

        assertFalse(mapIterator.next());
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());
    }

    @Test
    public void mapWithNonStartedEmptyIteratorAndConversion()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
        assertFalse(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());

        assertFalse(mapIterator.next());
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());
    }

    @Test
    public void mapWithNonStartedNonEmptyIteratorAndNoConversion()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i * 2);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Boolean> mapIterator = iterator.map(null);
        assertFalse(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());

        assertFalse(mapIterator.next());
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());
    }

    @Test
    public void mapWithNonStartedNonEmptyIteratorAndConversion()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
        assertFalse(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(mapIterator.next());
            assertTrue(mapIterator.hasStarted());
            assertTrue(mapIterator.hasCurrent());
            assertEquals(i % 2 == 1, mapIterator.getCurrent());
        }

        assertFalse(mapIterator.next());
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());
    }

    @Test
    public void mapWithStartedNonEmptyIteratorAndNoConversion()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i * 2);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Boolean> mapIterator = iterator.map(null);
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());

        assertFalse(mapIterator.next());
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());
    }

    @Test
    public void mapWithStartedNonEmptyIteratorAndConversion()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
        assertTrue(mapIterator.hasStarted());
        assertTrue(mapIterator.hasCurrent());
        assertFalse(mapIterator.getCurrent());

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(mapIterator.next());
            assertTrue(mapIterator.hasStarted());
            assertTrue(mapIterator.hasCurrent());
            assertEquals(i % 2 == 1, mapIterator.getCurrent());
        }

        assertFalse(mapIterator.next());
        assertTrue(mapIterator.hasStarted());
        assertFalse(mapIterator.hasCurrent());
        assertNull(mapIterator.getCurrent());
    }

    @Test
    public void instanceOfWithNonStartedEmptyIteratorAndNullType()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
        assertFalse(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithNonStartedEmptyIteratorAndType()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Boolean> instanceOfIterator = iterator.instanceOf(Boolean.class);
        assertFalse(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithNonStartedNonEmptyIteratorAndNullType()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
        assertFalse(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithNonStartedNonEmptyIteratorAndNoMatches()
    {
        final Array<Number> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i * 2);
        }

        final Iterator<Number> iterator = new ArrayIterator<>(array);
        final Iterator<Double> instanceOfIterator = iterator.instanceOf(Double.class);
        assertFalse(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithNonStartedNonEmptyIteratorAndMatches()
    {
        final Array<Number> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Number> iterator = new ArrayIterator<>(array);
        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
        assertFalse(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(instanceOfIterator.next());
            assertTrue(instanceOfIterator.hasStarted());
            assertTrue(instanceOfIterator.hasCurrent());
            assertEquals(i, instanceOfIterator.getCurrent().intValue());
        }

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithStartedNonEmptyIteratorAndNullType()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithStartedNonEmptyIteratorAndNoMatches()
    {
        final Array<Number> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i * 2);
        }
        final Iterator<Number> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Float> instanceOfIterator = iterator.instanceOf(Float.class);
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
    }

    @Test
    public void instanceOfWithStartedNonEmptyIteratorAndMatches()
    {
        final Array<Number> array = new Array<>(5);
        for (int i = 0; i < 5; ++i)
        {
            array.set(i, i);
        }
        final Iterator<Number> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
        assertTrue(instanceOfIterator.hasStarted());
        assertTrue(instanceOfIterator.hasCurrent());
        assertEquals(0, instanceOfIterator.getCurrent().intValue());

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(instanceOfIterator.next());
            assertTrue(instanceOfIterator.hasStarted());
            assertTrue(instanceOfIterator.hasCurrent());
            assertEquals(i, instanceOfIterator.getCurrent().intValue());
        }

        assertFalse(instanceOfIterator.next());
        assertTrue(instanceOfIterator.hasStarted());
        assertFalse(instanceOfIterator.hasCurrent());
        assertNull(instanceOfIterator.getCurrent());
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
