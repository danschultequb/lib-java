package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class IteratorTests
{
    protected abstract Iterator<Integer> createIterator(int count, boolean started);

    @Test
    public void takeBeforeStarting()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertNull(iterator.takeCurrent());
        assertIterator(iterator, true, null);
    }

    @Test
    public void anyWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertFalse(iterator.any());
        assertIterator(iterator, true, null);
    }

    @Test
    public void anyWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertFalse(iterator.any());
        assertIterator(iterator, true, null);
    }

    @Test
    public void anyWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, false);
        assertIterator(iterator, false, null);
        assertTrue(iterator.any());
        assertIterator(iterator, true, 0);
    }

    @Test
    public void anyWithNonEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, true);
        assertIterator(iterator, true, 0);
        assertTrue(iterator.any());
        assertIterator(iterator, true, 0);
    }

    @Test
    public void anyWithNonEmptyAtSecondElementIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, true);
        assertIterator(iterator, true, 0);
        assertTrue(iterator.next());
        assertIterator(iterator, true, 1);
        assertTrue(iterator.any());
        assertIterator(iterator, true, 1);
    }

    @Test
    public void getCountWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
    }

    @Test
    public void getCountWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
    }

    @Test
    public void getCountWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertEquals(3, iterator.getCount());
        assertIterator(iterator, true, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
    }

    @Test
    public void getCountWithNonEmptyAtFirstValueIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertEquals(3, iterator.getCount());
        assertIterator(iterator, true, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
    }

    @Test
    public void getCountWithNonEmptyAtSecondValueIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertTrue(iterator.next());
        assertIterator(iterator, true, 1);
        assertEquals(2, iterator.getCount());
        assertIterator(iterator, true, null);
        assertEquals(0, iterator.getCount());
        assertIterator(iterator, true, null);
    }

    @Test
    public void firstWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.first());
        assertIterator(iterator, true, null);
    }

    @Test
    public void firstWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertNull(iterator.first());
        assertIterator(iterator, true, null);
    }

    @Test
    public void firstWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertEquals(0, iterator.first().intValue());
        assertIterator(iterator, true, 0);
        assertEquals(0, iterator.first().intValue());
        assertIterator(iterator, true, 0);
    }

    @Test
    public void firstWithNonEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertEquals(0, iterator.getCurrent().intValue());
        assertIterator(iterator, true, 0);
        assertEquals(0, iterator.first().intValue());
        assertIterator(iterator, true, 0);
    }

    @Test
    public void firstWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.first(null));
        assertIterator(iterator, false, null);
    }

    @Test
    public void firstWithEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertNull(iterator.first(null));
        assertIterator(iterator, true, null);
    }

    @Test
    public void firstWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.first(null));
        assertIterator(iterator, false, null);
    }

    @Test
    public void firstWithNonEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertNull(iterator.first(null));
        assertIterator(iterator, true, 0);
    }

    @Test
    public void firstWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.first(Math.isOdd));
        assertIterator(iterator, true, null);
    }

    @Test
    public void firstWithEmptyStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertNull(iterator.first(Math.isOdd));
        assertIterator(iterator, true, null);
    }

    @Test
    public void firstWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertEquals(1, iterator.first(Math.isOdd).intValue());
        assertIterator(iterator, true, 1);
        assertEquals(1, iterator.first(Math.isOdd).intValue());
        assertIterator(iterator, true, 1);
    }

    @Test
    public void firstWithNonEmptyStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertEquals(0, iterator.first(Math.isEven).intValue());
        assertIterator(iterator, true, 0);
        assertEquals(0, iterator.first(Math.isEven).intValue());
        assertIterator(iterator, true, 0);
    }

    @Test
    public void lastWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertEquals(2, iterator.last().intValue());
        assertIterator(iterator, true, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithNonEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertEquals(2, iterator.last().intValue());
        assertIterator(iterator, true, null);
        assertNull(iterator.last());
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.last(null));
        assertIterator(iterator, false, null);
    }

    @Test
    public void lastWithEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertNull(iterator.last(null));
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.last(null));
        assertIterator(iterator, false, null);
    }

    @Test
    public void lastWithNonEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertNull(iterator.last(null));
        assertIterator(iterator, true, 0);
    }

    @Test
    public void lastWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertNull(iterator.last(Math.isOdd));
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithEmptyStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertNull(iterator.last(Math.isOdd));
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, false);
        assertIterator(iterator, false, null);
        assertEquals(1, iterator.last(Math.isOdd).intValue());
        assertIterator(iterator, true, null);
        assertNull(iterator.last(Math.isOdd));
        assertIterator(iterator, true, null);
    }

    @Test
    public void lastWithNonEmptyStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);
        assertEquals(2, iterator.last(Math.isEven).intValue());
        assertIterator(iterator, true, null);
        assertNull(iterator.last(Math.isEven));
        assertIterator(iterator, true, null);
    }

    @Test
    public void containsWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, null);
    }

    @Test
    public void containsWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, null);
    }

    @Test
    public void containsWithNonEmptyNonStartedIteratorAndNotFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(2, false);
        assertIterator(iterator, false, null);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, null);
    }

    @Test
    public void containsWithNonEmptyNonStartedIteratorAndFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);
        assertTrue(iterator.contains(3));
        assertIterator(iterator, true, 3);
        assertTrue(iterator.contains(3));
        assertIterator(iterator, true, 3);
    }

    @Test
    public void containsWithNonEmptyStartedIteratorAndNotFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(2, true);
        assertIterator(iterator, true, 0);
        assertFalse(iterator.contains(3));
        assertIterator(iterator, true, null);
    }

    @Test
    public void containsWithNonEmptyStartedIteratorAndFoundValue()
    {
        final Iterator<Integer> iterator = createIterator(20, true);
        assertIterator(iterator, true, 0);
        assertTrue(iterator.contains(12));
        assertIterator(iterator, true, 12);
        assertTrue(iterator.contains(12));
        assertIterator(iterator, true, 12);
    }

    @Test
    public void takeWithEmptyNonStartedIteratorAndNegativeToTake()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertIterator(takeIterator, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, null);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithEmptyNonStartedIteratorAndZeroToTake()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> takeIterator = iterator.take(0);
        assertIterator(takeIterator, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, null);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithEmptyNonStartedIteratorAndPositiveToTake()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> takeIterator = iterator.take(2);
        assertIterator(takeIterator, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyNonStartedIteratorAndNegativeToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertIterator(takeIterator, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, null);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyNonStartedIteratorAndZeroToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> takeIterator = iterator.take(0);
        assertIterator(takeIterator, false, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, false, null);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyNonStartedIteratorAndPositiveToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> takeIterator = iterator.take(2);
        assertIterator(takeIterator, false, null);

        for (int i = 0; i < 2; ++i)
        {
            assertTrue(takeIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(takeIterator, true, i);
        }

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, 1);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyStartedIteratorAndNegativeToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> takeIterator = iterator.take(-3);
        assertIterator(takeIterator, true, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, 0);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyStartedIteratorAndZeroToTake()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> takeIterator = iterator.take(0);
        assertIterator(takeIterator, true, null);

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, 0);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyStartedIteratorAndPositiveToTakeLessThanCount()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> takeIterator = iterator.take(2);
        assertIterator(iterator, true, 0);
        assertIterator(takeIterator, true, 0);

        assertTrue(takeIterator.next());
        assertIterator(iterator, true, 1);
        assertIterator(takeIterator, true, 1);

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, 1);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyStartedIteratorAndPositiveToTakeEqualToCount()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> takeIterator = iterator.take(5);
        assertIterator(iterator, true, 0);
        assertIterator(takeIterator, true, 0);

        for (int i = 1; i < 5; ++i) {
            assertTrue(takeIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(takeIterator, true, i);
        }

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, 4);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void takeWithNonEmptyStartedIteratorAndPositiveToTakeGreaterThanCount()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> takeIterator = iterator.take(100);
        assertIterator(iterator, true, 0);
        assertIterator(takeIterator, true, 0);

        for (int i = 1; i < 5; ++i) {
            assertTrue(takeIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(takeIterator, true, i);
        }

        assertFalse(takeIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(takeIterator, true, null);
    }

    @Test
    public void skipWithEmptyNonStartedIteratorAndNegativeToSkip()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipIterator = iterator.skip(-3);
        assertIterator(iterator, false, null);
        assertIterator(skipIterator, false, null);

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithEmptyNonStartedIteratorAndZeroToSkip()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipIterator = iterator.skip(0);
        assertIterator(iterator, false, null);
        assertIterator(skipIterator, false, null);

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithEmptyNonStartedIteratorAndPositiveToSkip()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipIterator = iterator.skip(2);
        assertIterator(iterator, false, null);
        assertIterator(skipIterator, false, null);

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyNonStartedIteratorAndNegativeToSkip()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipIterator = iterator.skip(-3);
        assertIterator(iterator, false, null);
        assertIterator(skipIterator, false, null);

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(skipIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipIterator, true, i);
        }

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyNonStartedIteratorAndZeroToSkip()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipIterator = iterator.skip(0);
        assertIterator(iterator, false, null);
        assertIterator(skipIterator, false, null);

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(skipIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipIterator, true, i);
        }

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyNonStartedIteratorAndPositiveToSkip()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipIterator = iterator.skip(2);
        assertIterator(iterator, false, null);
        assertIterator(skipIterator, false, null);

        for (int i = 2; i < 5; ++i)
        {
            assertTrue(skipIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipIterator, true, i);
        }

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyStartedIteratorAndNegativeToSkip()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipIterator = iterator.skip(-3);
        assertIterator(iterator, true, 0);
        assertIterator(skipIterator, true, 0);

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(skipIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipIterator, true, i);
        }

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyStartedIteratorAndZeroToSkip()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipIterator = iterator.skip(0);
        assertIterator(iterator, true, 0);
        assertIterator(skipIterator, true, 0);

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(skipIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipIterator, true, i);
        }

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyStartedIteratorAndPositiveToSkipLessThanCount()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipIterator = iterator.skip(2);
        assertIterator(iterator, true, 0);
        assertIterator(skipIterator, true, 2);
        assertIterator(iterator, true, 2);

        for (int i = 3; i < 5; ++i)
        {
            assertTrue(skipIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipIterator, true, i);
        }

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyStartedIteratorAndPositiveToSkipEqualToCount()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipIterator = iterator.skip(5);
        assertIterator(iterator, true, 0);
        assertIterator(skipIterator, true, null);
        assertIterator(iterator, true, null);

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipWithNonEmptyStartedIteratorAndPositiveToSkipGreaterThanCount()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipIterator = iterator.skip(100);
        assertIterator(iterator, true, 0);
        assertIterator(skipIterator, true, null);
        assertIterator(iterator, true, null);

        assertFalse(skipIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipIterator, true, null);
    }

    @Test
    public void skipUntilWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(null);
        assertIterator(iterator, false, null);
        assertIterator(skipUntilIterator, false, null);

        assertFalse(skipUntilIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipUntilIterator, true, null);
    }

    @Test
    public void skipUntilWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
        assertIterator(iterator, false, null);
        assertIterator(skipUntilIterator, false, null);

        assertFalse(skipUntilIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipUntilIterator, true, null);
    }

    @Test
    public void skipUntilWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(null);
        assertIterator(iterator, false, null);
    }

    @Test
    public void skipUntilWithNonEmptyNonStartedIteratorAndNonMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(6, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Comparer.equal(20));
        assertIterator(iterator, false, null);
        assertIterator(skipUntilIterator, false, null);

        assertFalse(skipUntilIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipUntilIterator, true, null);
    }

    @Test
    public void skipUntilWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
        assertIterator(iterator, false, null);
        assertIterator(skipUntilIterator, false, null);

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(skipUntilIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipUntilIterator, true, i);
        }

        assertFalse(skipUntilIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipUntilIterator, true, null);
    }

    @Test
    public void skipUntilWithNonEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(null);
        assertIterator(iterator, true, 0);
        assertNull(skipUntilIterator.getCurrent());
        assertIterator(skipUntilIterator, true, null);
        assertIterator(iterator, true, 0);
    }

    @Test
    public void skipUntilWithNonEmptyStartedIteratorAndNonMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
        assertIterator(iterator, true, 0);
        assertIterator(skipUntilIterator, true, 1);
        assertIterator(iterator, true, 1);

        assertTrue(skipUntilIterator.next());
        assertIterator(iterator, true, 2);
        assertIterator(skipUntilIterator, true, 2);

        assertFalse(skipUntilIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipUntilIterator, true, null);
    }

    @Test
    public void skipUntilWithStartedNonEmptyIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math.isOdd);
        assertIterator(iterator, true, 0);
        assertIterator(skipUntilIterator, true, 1);
        assertIterator(iterator, true, 1);

        for (int i = 2; i < 5; ++i)
        {
            assertTrue(skipUntilIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(skipUntilIterator, true, i);
        }

        assertFalse(skipUntilIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(skipUntilIterator, true, null);
    }

    @Test
    public void whereWithEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> whereIterator = iterator.where(null);
        assertIterator(iterator, false, null);
        assertSame(iterator, whereIterator);
    }

    @Test
    public void whereWithEmptyNonStartedIteratorAndCondition()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertIterator(iterator, false, null);
        assertIterator(whereIterator, false, null);

        assertFalse(whereIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(whereIterator, true, null);
    }

    @Test
    public void whereWithNonEmptyNonStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> whereIterator = iterator.where(null);
        assertIterator(iterator, false, null);
        assertSame(iterator, whereIterator);
    }

    @Test
    public void whereWithNonEmptyNonStartedIteratorAndNonMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(6, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> whereIterator = iterator.where(Comparer.equal(20));
        assertIterator(iterator, false, null);
        assertIterator(whereIterator, false, null);

        assertFalse(whereIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(whereIterator, true, null);
    }

    @Test
    public void whereWithNonEmptyNonStartedIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertIterator(iterator, false, null);
        assertIterator(whereIterator, false, null);

        for (int i = 1; i < 5; i += 2)
        {
            assertTrue(whereIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(whereIterator, true, i);
        }

        assertFalse(whereIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(whereIterator, true, null);
    }

    @Test
    public void whereWithNonEmptyStartedIteratorAndNullCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> whereIterator = iterator.where(null);
        assertIterator(iterator, true, 0);
        assertSame(iterator, whereIterator);
    }

    @Test
    public void whereWithNonEmptyStartedIteratorAndNonMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(3, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertIterator(iterator, true, 0);
        assertIterator(whereIterator, true, 1);
        assertIterator(iterator, true, 1);

        assertFalse(whereIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(whereIterator, true, null);
    }

    @Test
    public void whereWithStartedNonEmptyIteratorAndMatchingCondition()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> whereIterator = iterator.where(Math.isOdd);
        assertIterator(iterator, true, 0);
        assertIterator(whereIterator, true, 1);
        assertIterator(iterator, true, 1);

        assertTrue(whereIterator.next());
        assertIterator(iterator, true, 3);
        assertIterator(whereIterator, true, 3);

        assertFalse(whereIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(whereIterator, true, null);
    }

    @Test
    public void mapWithEmptyNonStartedIteratorAndNullConversion()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> mapIterator = iterator.map(null);
        assertIterator(iterator, false, null);
        assertIterator(mapIterator, false, null);

        assertFalse(mapIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(mapIterator, true, null);
    }

    @Test
    public void mapWithEmptyNonStartedIteratorAndConversion()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
        assertIterator(iterator, false, null);
        assertIterator(mapIterator, false, null);

        assertFalse(mapIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(mapIterator, true, null);
    }

    @Test
    public void mapWithNonEmptyNonStartedIteratorAndNoConversion()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Boolean> mapIterator = iterator.map(null);
        assertIterator(iterator, false, null);
        assertIterator(mapIterator, false, null);

        assertFalse(mapIterator.next());
        assertIterator(iterator, true, 0);
        assertIterator(mapIterator, true, null);
    }

    @Test
    public void mapWithNonEmptyNonStartedIteratorAndConversion()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
        assertIterator(iterator, false, null);
        assertIterator(mapIterator, false, null);

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(mapIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(mapIterator, true, i % 2 == 1);
        }

        assertFalse(mapIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(mapIterator, true, null);
    }

    @Test
    public void mapWithNonEmptyStartedIteratorAndNoConversion()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Boolean> mapIterator = iterator.map(null);
        assertIterator(iterator, true, 0);
        assertIterator(mapIterator, true, null);

        assertFalse(mapIterator.next());
        assertIterator(iterator, true, 0);
        assertIterator(mapIterator, true, null);
    }

    @Test
    public void mapWithNonEmptyStartedIteratorAndConversion()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Boolean> mapIterator = iterator.map(Math.isOdd);
        assertIterator(iterator, true, 0);
        assertIterator(mapIterator, true, false);

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(mapIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(mapIterator, true, i % 2 == 1);
        }

        assertFalse(mapIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(mapIterator, true, null);
    }

    @Test
    public void instanceOfWithEmptyNonStartedIteratorAndNullType()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
        assertIterator(iterator, false, null);
        assertIterator(instanceOfIterator, false, null);

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithEmptyNonStartedIteratorAndType()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        final Iterator<Boolean> instanceOfIterator = iterator.instanceOf(Boolean.class);
        assertIterator(iterator, false, null);
        assertIterator(instanceOfIterator, false, null);

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithNonEmptyNonStartedIteratorAndNullType()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
        assertIterator(iterator, false, null);
        assertIterator(instanceOfIterator, false, null);

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, 0);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithNonEmptyNonStartedIteratorAndNoMatches()
    {
        final Iterator<Integer> iterator = createIterator(5, false);
        assertIterator(iterator, false, null);

        final Iterator<Double> instanceOfIterator = iterator.instanceOf(Double.class);
        assertIterator(iterator, false, null);
        assertIterator(instanceOfIterator, false, null);

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithNonEmptyNonStartedIteratorAndMatches()
    {
        final Iterator<Number> iterator = createIterator(5, false).map(new Function1<Integer, Number>()
        {
            @Override
            public Number run(Integer value)
            {
                return value;
            }
        });
        assertIterator(iterator, false, null);

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
        assertIterator(iterator, false, null);
        assertIterator(instanceOfIterator, false, null);

        for (int i = 0; i < 5; ++i)
        {
            assertTrue(instanceOfIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(instanceOfIterator, true, i);
        }

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithNonEmptyStartedIteratorAndNullType()
    {
        final Iterator<Integer> iterator = createIterator(5, true);
        assertIterator(iterator, true, 0);

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(null);
        assertIterator(iterator, true, 0);
        assertIterator(instanceOfIterator, true, null);
        assertIterator(iterator, true, 0);

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, 0);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithNonEmptyStartedIteratorAndNoMatches()
    {
        final Iterator<Number> iterator = createIterator(5, true).map(new Function1<Integer, Number>()
        {
            @Override
            public Number run(Integer value)
            {
                return value;
            }
        });
        assertIterator(iterator, true, 0);

        final Iterator<Float> instanceOfIterator = iterator.instanceOf(Float.class);
        assertIterator(iterator, true, 0);
        assertIterator(instanceOfIterator, true, null);
        assertIterator(iterator, true, null);

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void instanceOfWithNonEmptyStartedIteratorAndMatches()
    {
        final Iterator<Number> iterator = createIterator(5, true).map(new Function1<Integer, Number>()
        {
            @Override
            public Number run(Integer value)
            {
                return value;
            }
        });
        assertIterator(iterator, true, 0);

        final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
        assertIterator(iterator, true, 0);
        assertIterator(instanceOfIterator, true, 0);
        assertIterator(iterator, true, 0);

        for (int i = 1; i < 5; ++i)
        {
            assertTrue(instanceOfIterator.next());
            assertIterator(iterator, true, i);
            assertIterator(instanceOfIterator, true, i);
        }

        assertFalse(instanceOfIterator.next());
        assertIterator(iterator, true, null);
        assertIterator(instanceOfIterator, true, null);
    }

    @Test
    public void forEachWithEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, false);
        assertIterator(iterator, false, null);

        int elementCount = 0;
        for (final Integer ignored : iterator)
        {
            ++elementCount;
        }
        assertIterator(iterator, true, null);

        assertEquals(0, elementCount);
    }

    @Test
    public void forEachWithEmptyStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(0, true);
        assertIterator(iterator, true, null);

        int elementCount = 0;
        for (final Integer ignored : iterator)
        {
            ++elementCount;
        }
        assertIterator(iterator, true, null);

        assertEquals(0, elementCount);
    }

    @Test
    public void forEachWithNonEmptyNonStartedIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, false);
        assertIterator(iterator, false, null);

        int i = 0;
        for (final Integer element : iterator)
        {
            assertEquals(i, element.intValue());
            ++i;
        }
        assertIterator(iterator, true, null);

        assertEquals(10, i);
    }

    @Test
    public void forEachWithNonEmptyAtFirstElementIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, true);
        assertIterator(iterator, true, 0);

        int i = 0;
        for (final Integer element : iterator)
        {
            assertEquals(i, element.intValue());
            ++i;
        }
        assertIterator(iterator, true, null);

        assertEquals(10, i);
    }

    @Test
    public void forEachWithNonEmptyAtSecondElementIterator()
    {
        final Iterator<Integer> iterator = createIterator(10, true);
        assertIterator(iterator, true, 0);
        assertTrue(iterator.next());
        assertIterator(iterator, true, 1);

        int i = 0;
        for (final Integer element : iterator)
        {
            assertEquals(i + 1, element.intValue());
            ++i;
        }
        assertIterator(iterator, true, null);

        assertEquals(9, i);
    }

    private static <T> void assertIterator(Iterator<T> iterator, boolean expectedHasStarted, T expectedCurrent)
    {
        assertEquals("Wrong hasStarted()", expectedHasStarted, iterator.hasStarted());
        assertEquals("Wrong hasCurrent()", expectedCurrent != null, iterator.hasCurrent());
        assertEquals("Wrong getCurrent()", expectedCurrent, iterator.getCurrent());
    }
}
