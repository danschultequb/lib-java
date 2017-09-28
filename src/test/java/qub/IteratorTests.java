package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class IteratorTests
{
    @Test
    public void anyWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.any());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void anyWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertFalse(iterator.any());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void anyWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, false);
        assertIterator(iterator, false, false, null);
        assertTrue(iterator.any());
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void anyWithNonEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, true);
        assertIterator(iterator, true, true, 0);
        assertTrue(iterator.any());
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void anyWithNonEmptyAtSecondElementIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, true);
        assertIterator(iterator, true, true, 0);
        assertTrue(iterator.next());
        assertIterator(iterator, true, true, 1);
        assertTrue(iterator.any());
        assertIterator(iterator, true, true, 1);
    }

    @Test
    public void anyWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.any(null));
        assertIterator(iterator, false, false, null);
    }

    @Test
    public void anyWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.any(null));
        assertIterator(iterator, false, false, null);
    }

    @Test
    public void anyWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.any(Math.isEven));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void anyWithNonEmptyNonStartedIteratorAndNonMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(1, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.any(Math.isOdd));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void anyWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, false, null);
        assertTrue(iterator.any(Math.isOdd));
        assertIterator(iterator, true, true, 1);
    }

    @Test
    public void getCountWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void getCountWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void getCountWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertEquals(3, iterator.getCount());
        assertIterator(iterator, true, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void getCountWithNonEmptyAtFirstValueIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertEquals(3, iterator.getCount());
        assertIterator(iterator, true, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void getCountWithNonEmptyAtSecondValueIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertTrue(iterator.next());
        assertIterator(iterator, true, true, 1);
        assertEquals(2, iterator.getCount());
        assertIterator(iterator, true, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void firstWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.first());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void firstWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertNull(iterator.first());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void firstWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertEquals(0, iterator.first().intValue());
        assertIterator(iterator, true, true, 0);
        assertEquals(0, iterator.first().intValue());
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void firstWithNonEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertEquals(0, iterator.getCurrent().intValue());
        assertIterator(iterator, true, true, 0);
        assertEquals(0, iterator.first().intValue());
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void firstWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.first(null));
        assertIterator(iterator, false, false, null);
    }

    @Test
    public void firstWithEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertNull(iterator.first(null));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void firstWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.first(null));
        assertIterator(iterator, false, false, null);
    }

    @Test
    public void firstWithNonEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertNull(iterator.first(null));
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void firstWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.first(Math.isOdd));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void firstWithEmptyStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertNull(iterator.first(Math.isOdd));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void firstWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertEquals(1, iterator.first(Math.isOdd).intValue());
        assertIterator(iterator, true, true, 1);
        assertEquals(1, iterator.first(Math.isOdd).intValue());
        assertIterator(iterator, true, true, 1);
    }

    @Test
    public void firstWithNonEmptyStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertEquals(0, iterator.first(Math.isEven).intValue());
        assertIterator(iterator, true, true, 0);
        assertEquals(0, iterator.first(Math.isEven).intValue());
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void lastWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertEquals(2, iterator.last().intValue());
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithNonEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertEquals(2, iterator.last().intValue());
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.last(null));
        assertIterator(iterator, false, false, null);
    }

    @Test
    public void lastWithEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last(null));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.last(null));
        assertIterator(iterator, false, false, null);
    }

    @Test
    public void lastWithNonEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertNull(iterator.last(null));
        assertIterator(iterator, true, true, 0);
    }

    @Test
    public void lastWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertNull(iterator.last(Math.isOdd));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithEmptyStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last(Math.isOdd));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, false, null);
        assertEquals(1, iterator.last(Math.isOdd).intValue());
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last(Math.isOdd));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void lastWithNonEmptyStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, true, 0);
        assertEquals(2, iterator.last(Math.isEven).intValue());
        assertIterator(iterator, true, false, null);
        assertNull(iterator.last(Math.isEven));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void containsWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void containsWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, false, null);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void containsWithNonEmptyNonStartedIteratorAndNotFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(2, false);
        assertIterator(iterator, false, false, null);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void containsWithNonEmptyNonStartedIteratorAndFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, false, null);
        assertTrue(iterator.contains(3));
        assertIterator(iterator, true, true, 3);
        assertTrue(iterator.contains(3));
        assertIterator(iterator, true, true, 3);
    }

    @Test
    public void containsWithNonEmptyStartedIteratorAndNotFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(2, true);
        assertIterator(iterator, true, true, 0);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, false, null);
    }

    @Test
    public void containsWithNonEmptyStartedIteratorAndFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(20, true);
        assertIterator(iterator, true, true, 0);
        assertTrue(iterator.contains(12));
        assertIterator(iterator, true, true, 12);
        assertTrue(iterator.contains(12));
        assertIterator(iterator, true, true, 12);
    }

    @Test
    public void takeWithEmptyNonStartedIteratorAndNegativeToTake()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);

        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertIterator(takeIterator, false, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, false, null);
        assertIterator(takeIterator, true, false, null);
    }

    @Test
    public void takeWithEmptyNonStartedIteratorAndZeroToTake()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);

        final Iterator<Integer> takeIterator = iterator.take(0);
        assertIterator(takeIterator, false, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, false, null);
        assertIterator(takeIterator, true, false, null);
    }

    @Test
    public void takeWithEmptyNonStartedIteratorAndPositiveToTake()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, false, null);

        final Iterator<Integer> takeIterator = iterator.take(2);
        assertIterator(takeIterator, false, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, false, null);
        assertIterator(takeIterator, true, false, null);
    }

    @Test
    public void takeWithNonEmptyNonStartedIteratorAndNegativeToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, false, null);

        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertIterator(takeIterator, false, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, false, null);
        assertIterator(takeIterator, true, false, null);
    }

    @Test
    public void takeWithNonEmptyNonStartedIteratorAndZeroToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, false, null);

        final Iterator<Integer> takeIterator = iterator.take(0);
        assertIterator(takeIterator, false, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, false, null);
        assertIterator(takeIterator, true, false, null);
    }

    @Test
    public void takeWithNonEmptyNonStartedIteratorAndPositiveToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, false, null);

        final Iterator<Integer> takeIterator = iterator.take(2);
        assertIterator(takeIterator, false, false, null);

        for (int i = 0; i < 2; ++i)
        {
            assertTrue(takeIterator.next());
            assertIterator(iterator, true, true, i);
            assertIterator(takeIterator, true, true, i);
        }

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, true, 1);
        assertIterator(takeIterator, true, false, null);
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
        for (final Integer ignored : iterator)
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
        for (final Integer ignored : iterator)
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

    private static Iterator<Integer> createIterator(int count, boolean started)
    {
        final Array<Integer> a = new Array<>(count);
        for (int i = 0; i < count; ++i) {
            a.set(i, i);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(a);
        if (started)
        {
            iterator.next();
        }

        assertEquals(started, iterator.hasStarted());

        return iterator;
    }

    private static void assertIterator(Iterator<Integer> iterator, boolean expectedHasStarted, boolean expectedHasCurrent, Integer expectedCurrent)
    {
        assertEquals(expectedHasStarted, iterator.hasStarted());
        assertEquals(expectedHasCurrent, iterator.hasCurrent());
        assertEquals(expectedCurrent, iterator.getCurrent());
    }
}
