package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class IterableTests
{
    /**
     * Create the Iterable that will be used for a single test. If the Iterable has elements, each
     * element should be the value of the index it appears in the Iterable.
     * @param count The requested number of elements that should appear in the Iterable.
     * @return The Iterable that will be used for a single test.
     */
    protected abstract Iterable<Integer> createIterable(int count);
    
    @Test
    public void iterateWithEmpty()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterator<Integer> iterator = iterable.iterate();
            assertNotNull(iterator);
            assertFalse(iterator.hasStarted());
            assertFalse(iterator.hasCurrent());
            assertNull(iterator.getCurrent());

            assertFalse(iterator.next());
            assertTrue(iterator.hasStarted());
            assertFalse(iterator.hasCurrent());
            assertNull(iterator.getCurrent());
        }
    }

    @Test
    public void iterateWithNonEmpty()
    {
        final Iterable<Integer> iterable = createIterable(5);
        final Iterator<Integer> iterator = iterable.iterate();
        assertNotNull(iterator);
        assertFalse(iterator.hasStarted());
        assertFalse(iterator.hasCurrent());
        assertNull(iterator.getCurrent());

        for (int i = 0; i < iterable.getCount(); ++i) {
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
    public void forEachWithEmptyIterable()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            int elementCount = 0;
            for (final Integer element : iterable)
            {
                assertEquals(elementCount, element.intValue());
                ++elementCount;
            }

            assertEquals(0, elementCount);
        }
    }

    @Test
    public void forEachWithNonEmptyIterable()
    {
        final Iterable<Integer> iterable = createIterable(10);

        int i = 0;
        for (final Integer element : iterable)
        {
            assertEquals(i, element.intValue());
            ++i;
        }

        assertEquals(iterable.getCount(), i);
    }

    @Test
    public void anyWithEmptyIterable()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertFalse(iterable.any());
        }
    }

    @Test
    public void anyWithNonEmptyIterable()
    {
        final Iterable<Integer> iterable = createIterable(110);
        assertTrue(iterable.any());
    }

    @Test
    public void anyWithEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertFalse(iterable.any(null));
        }
    }

    @Test
    public void anyWithNonEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(5);
        assertFalse(iterable.any(null));
    }

    @Test
    public void anyWithEmptyAndCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertFalse(iterable.any(Math.isEven));
        }
    }

    @Test
    public void anyWithNonEmptyAndNonMatchingCondition()
    {
        final Iterable<Integer> iterable = createIterable(5);
        assertFalse(iterable.any(new Function1<Integer,Boolean>() {
            @Override
            public Boolean run(Integer element) {
                return element > 10;
            }
        }));
    }

    @Test
    public void anyWithNonEmptyAndMatchingCondition()
    {
        final Iterable<Integer> iterable = createIterable(5);
        assertTrue(iterable.any(Math.isOdd));
    }

    @Test
    public void firstWithEmpty()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertNull(iterable.first());
        }
    }

    @Test
    public void firstWithNonEmpty()
    {
        final Iterable<Integer> iterable = createIterable(3);
        assertEquals(0, iterable.first().intValue());
    }

    @Test
    public void firstWithEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertNull(iterable.first(null));
        }
    }

    @Test
    public void firstWithNonEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(4);
        assertNull(iterable.first(null));
    }

    @Test
    public void firstWithEmptyAndCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertNull(iterable.first(Math.isOdd));
        }
    }

    @Test
    public void firstWithNonEmptyAndConditionWithNoMatches()
    {
        final Iterable<Integer> iterable = createIterable(1);
        assertNull(iterable.first(Math.isOdd));
    }

    @Test
    public void firstWithNonEmptyAndConditionWithMatches()
    {
        final Iterable<Integer> iterable = createIterable(1);
        assertEquals(0, iterable.first(Math.isEven).intValue());
    }

    @Test
    public void lastWithEmpty()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertNull(iterable.first());
        }
    }

    @Test
    public void lastWithNonEmpty()
    {
        final Iterable<Integer> iterable = createIterable(3);
        assertEquals(2, iterable.last().intValue());
    }

    @Test
    public void lastWithEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertNull(iterable.last(null));
        }
    }

    @Test
    public void lastWithNonEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(4);
        assertNull(iterable.last(null));
    }

    @Test
    public void lastWithEmptyAndCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertNull(iterable.last(Math.isOdd));
        }
    }

    @Test
    public void lastWithNonEmptyAndConditionWithNoMatches()
    {
        final Iterable<Integer> iterable = createIterable(1);
        assertNull(iterable.last(Math.isOdd));
    }

    @Test
    public void lastWithNonEmptyAndConditionWithMatches()
    {
        final Iterable<Integer> iterable = createIterable(5);
        assertEquals(3, iterable.last(Math.isOdd).intValue());
    }

    @Test
    public void takeWithEmptyAndNegativeToTake()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> takeIterable = iterable.take(-1);
            assertFalse(takeIterable.any());
            assertEquals(0, takeIterable.getCount());

            final Iterator<Integer> takeIterator = takeIterable.iterate();
            assertFalse(takeIterator.any());
            assertEquals(0, takeIterator.getCount());
        }
    }

    @Test
    public void containsWithEmptyIterable()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertArrayEquals(new int[0], Array.toIntArray(iterable));

            assertFalse(iterable.contains(3));
        }
    }

    @Test
    public void containsWithNonEmptyIterableAndNotFoundValue()
    {
        final Iterable<Integer> iterable = createIterable(2);
        assertArrayEquals(new int[] { 0, 1 }, Array.toIntArray(iterable));

        assertFalse(iterable.contains(3));
    }

    @Test
    public void containsWithNonEmptyIteratorAndFoundValue()
    {
        final Iterable<Integer> iterable = createIterable(5);
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, Array.toIntArray(iterable));

        assertTrue(iterable.contains(3));
    }

    @Test
    public void containsWithEmptyIterableAndCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            assertArrayEquals(new int[0], Array.toIntArray(iterable));

            assertFalse(iterable.contains(Math.isOdd));
        }
    }

    @Test
    public void containsWithNonEmptyIterableAndNotFoundCondition()
    {
        final Iterable<Integer> iterable = createIterable(1);
        assertArrayEquals(new int[] { 0 }, Array.toIntArray(iterable));

        assertFalse(iterable.contains(Math.isOdd));
    }

    @Test
    public void containsWithNonEmptyIteratorAndFoundCondition()
    {
        final Iterable<Integer> iterable = createIterable(5);
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, Array.toIntArray(iterable));

        assertTrue(iterable.contains(Math.isOdd));
    }

    @Test
    public void takeWithEmptyAndZeroToTake()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> takeIterable = iterable.take(0);
            assertFalse(takeIterable.any());
            assertEquals(0, takeIterable.getCount());

            final Iterator<Integer> takeIterator = takeIterable.iterate();
            assertFalse(takeIterator.any());
            assertEquals(0, takeIterator.getCount());
        }
    }

    @Test
    public void takeWithEmptyAndPositiveToTake()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> takeIterable = iterable.take(3);
            assertFalse(takeIterable.any());
            assertEquals(0, takeIterable.getCount());

            final Iterator<Integer> takeIterator = takeIterable.iterate();
            assertFalse(takeIterator.any());
            assertEquals(0, takeIterator.getCount());
        }
    }

    @Test
    public void takeWithNonEmptyAndNegativeToTake()
    {
        final Iterable<Integer> iterable = createIterable(4);
        final Iterable<Integer> takeIterable = iterable.take(-1);
        assertFalse(takeIterable.any());
        assertEquals(0, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertFalse(takeIterator.any());
        assertEquals(0, takeIterator.getCount());
    }

    @Test
    public void takeWithNonEmptyAndZeroToTake()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> takeIterable = iterable.take(0);
        assertFalse(takeIterable.any());
        assertEquals(0, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertFalse(takeIterator.any());
        assertEquals(0, takeIterator.getCount());
    }

    @Test
    public void takeWithNonEmptyAndPositiveToTakeLessThanCount()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> takeIterable = iterable.take(3);
        assertTrue(takeIterable.any());
        assertEquals(3, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertTrue(takeIterator.any());
        assertEquals(3, takeIterator.getCount());
    }

    @Test
    public void takeWithNonEmptyAndPositiveToTakeEqualToCount()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> takeIterable = iterable.take(iterable.getCount());
        assertTrue(takeIterable.any());
        assertEquals(4, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertTrue(takeIterator.any());
        assertEquals(4, takeIterator.getCount());
    }

    @Test
    public void takeWithNonEmptyAndPositiveToTakeGreaterThanCount()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> takeIterable = iterable.take(14);
        assertTrue(takeIterable.any());
        assertEquals(4, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertTrue(takeIterator.any());
        assertEquals(4, takeIterator.getCount());
    }

    @Test
    public void skipWithEmptyAndNegativeToSkip()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> skipIterable = iterable.skip(-1);
            assertSame(iterable, skipIterable);
        }
    }

    @Test
    public void skipWithEmptyAndZeroToSkip()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> skipIterable = iterable.skip(0);
            assertSame(iterable, skipIterable);
        }
    }

    @Test
    public void skipWithEmptyAndPositiveToSkip()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> skipIterable = iterable.skip(3);
            assertFalse(skipIterable.any());
            assertEquals(0, skipIterable.getCount());

            final Iterator<Integer> skipIterator = skipIterable.iterate();
            assertFalse(skipIterator.any());
            assertEquals(0, skipIterator.getCount());
        }
    }

    @Test
    public void skipWithNonEmptyAndNegativeToSkip()
    {
        final Iterable<Integer> iterable = createIterable(4);
        final Iterable<Integer> skipIterable = iterable.skip(-1);
        assertSame(iterable, skipIterable);
    }

    @Test
    public void skipWithNonEmptyAndZeroToSkip()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> skipIterable = iterable.skip(0);
        assertSame(iterable, skipIterable);
    }

    @Test
    public void skipWithNonEmptyAndPositiveToSkipLessThanCount()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> skipIterable = iterable.skip(3);
        assertTrue(skipIterable.any());
        assertEquals(1, skipIterable.getCount());

        final Iterator<Integer> skipIterator = skipIterable.iterate();
        assertTrue(skipIterator.any());
        assertEquals(1, skipIterator.getCount());
    }

    @Test
    public void skipWithNonEmptyAndPositiveToSkipEqualToCount()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> skipIterable = iterable.skip(iterable.getCount());
        assertFalse(skipIterable.any());
        assertEquals(0, skipIterable.getCount());

        final Iterator<Integer> skipIterator = skipIterable.iterate();
        assertFalse(skipIterator.any());
        assertEquals(0, skipIterator.getCount());
    }

    @Test
    public void skipWithNonEmptyAndPositiveToSkipGreaterThanCount()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> skipIterable = iterable.skip(14);
        assertFalse(skipIterable.any());
        assertEquals(0, skipIterable.getCount());

        final Iterator<Integer> skipIterator = skipIterable.iterate();
        assertFalse(skipIterator.any());
        assertEquals(0, skipIterator.getCount());
    }

    @Test
    public void whereWithEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> takeIterable = iterable.where(null);
            assertSame(iterable, takeIterable);
        }
    }

    @Test
    public void whereWithEmptyAndCondition()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Integer> whereIterable = iterable.where(Math.isOdd);
            assertFalse(whereIterable.any());
            assertEquals(0, whereIterable.getCount());

            final Iterator<Integer> whereIterator = whereIterable.iterate();
            assertFalse(whereIterator.any());
            assertEquals(0, whereIterator.getCount());
        }
    }

    @Test
    public void whereWithNonEmptyAndNullCondition()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> whereIterable = iterable.where(null);
        assertSame(iterable, whereIterable);
    }

    @Test
    public void whereWithNonEmptyAndNonMatchingCondition()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> takeIterable = iterable.where(new Function1<Integer,Boolean>()
        {
            @Override
            public Boolean run(Integer value)
            {
                return value != null && value > 10;
            }
        });
        assertFalse(takeIterable.any());
        assertEquals(0, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertFalse(takeIterator.any());
        assertEquals(0, takeIterator.getCount());
    }

    @Test
    public void whereWithNonEmptyAndMatchingCondition()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> whereIterable = iterable.where(Math.isOdd);
        assertTrue(whereIterable.any());
        assertEquals(2, whereIterable.getCount());

        final Iterator<Integer> whereIterator = whereIterable.iterate();
        assertTrue(whereIterator.any());
        assertEquals(2, whereIterator.getCount());
    }

    @Test
    public void mapWithEmptyAndNoConversion()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Boolean> mapIterable = iterable.map(null);
            assertFalse(mapIterable.any());
            assertEquals(0, mapIterable.getCount());

            final Iterator<Boolean> mapIterator = mapIterable.iterate();
            assertFalse(mapIterator.any());
            assertEquals(0, mapIterator.getCount());
        }
    }

    @Test
    public void mapWithEmptyAndConversion()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Boolean> mapIterable = iterable.map(Math.isOdd);
            assertFalse(mapIterable.any());
            assertEquals(0, mapIterable.getCount());

            final Iterator<Boolean> mapIterator = mapIterable.iterate();
            assertFalse(mapIterator.any());
            assertEquals(0, mapIterator.getCount());
        }
    }

    @Test
    public void mapWithNonEmptyAndNoConversion()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Boolean> mapIterable = iterable.map(null);
        assertFalse(mapIterable.any());
        assertEquals(0, mapIterable.getCount());

        final Iterator<Boolean> mapIterator = mapIterable.iterate();
        assertFalse(mapIterator.any());
        assertEquals(0, mapIterator.getCount());
    }

    @Test
    public void mapWithNonEmptyAndConversion()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Boolean> mapIterable = iterable.map(Math.isOdd);
        assertTrue(mapIterable.any());
        assertEquals(4, mapIterable.getCount());

        final Iterator<Boolean> mapIterator = mapIterable.iterate();
        assertTrue(mapIterator.any());
        assertEquals(4, mapIterator.getCount());
    }

    @Test
    public void mapWithEmptyAndWrongType()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Boolean> instanceOfIterable = iterable.instanceOf(Boolean.class);
            assertFalse(instanceOfIterable.any());
            assertEquals(0, instanceOfIterable.getCount());

            final Iterator<Boolean> instanceOfIterator = instanceOfIterable.iterate();
            assertFalse(instanceOfIterator.any());
            assertEquals(0, instanceOfIterator.getCount());
        }
    }

    @Test
    public void mapWithEmptyAndCorrectType()
    {
        final Iterable<Integer> iterable = createIterable(0);
        // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
        // an Iterable with 0 elements is requested.
        if (iterable != null)
        {
            final Iterable<Number> instanceOfIterable = iterable.instanceOf(Number.class);
            assertFalse(instanceOfIterable.any());
            assertEquals(0, instanceOfIterable.getCount());

            final Iterator<Number> instanceOfIterator = instanceOfIterable.iterate();
            assertFalse(instanceOfIterator.any());
            assertEquals(0, instanceOfIterator.getCount());
        }
    }

    @Test
    public void instanceOfWithNonEmptyAndNoMatches()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Boolean> instanceOfIterable = iterable.instanceOf(Boolean.class);
        assertFalse(instanceOfIterable.any());
        assertEquals(0, instanceOfIterable.getCount());

        final Iterator<Boolean> instanceOfIterator = instanceOfIterable.iterate();
        assertFalse(instanceOfIterator.any());
        assertEquals(0, instanceOfIterator.getCount());
    }

    @Test
    public void instanceOfWithNonEmptyAndMatches()
    {
        final Iterable<Integer> iterable = createIterable(4);

        final Iterable<Integer> instanceOfIterable = iterable.instanceOf(Integer.class);
        assertTrue(instanceOfIterable.any());
        assertEquals(4, instanceOfIterable.getCount());

        final Iterator<Integer> instanceOfIterator = instanceOfIterable.iterate();
        assertTrue(instanceOfIterator.any());
        assertEquals(4, instanceOfIterator.getCount());
    }
}
