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

        int elementCount = 0;
        for (final Integer element : iterable)
        {
            assertEquals(elementCount, element.intValue());
            ++elementCount;
        }

        assertEquals(0, elementCount);
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
        assertFalse(iterable.any());
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
        assertFalse(iterable.any(null));
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
        assertFalse(iterable.any(Math.isEven));
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
    public void takeWithEmptyAndNegativeToTake()
    {
        final Iterable<Integer> iterable = createIterable(0);
        final Iterable<Integer> takeIterable = iterable.take(-1);
        assertFalse(takeIterable.any());
        assertEquals(0, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertFalse(takeIterator.any());
        assertEquals(0, takeIterator.getCount());
    }

    @Test
    public void takeWithEmptyAndZeroToTake()
    {
        final Iterable<Integer> iterable = createIterable(0);
        final Iterable<Integer> takeIterable = iterable.take(0);
        assertFalse(takeIterable.any());
        assertEquals(0, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertFalse(takeIterator.any());
        assertEquals(0, takeIterator.getCount());
    }

    @Test
    public void takeWithEmptyAndPositiveToTake()
    {
        final Iterable<Integer> iterable = createIterable(0);
        final Iterable<Integer> takeIterable = iterable.take(3);
        assertFalse(takeIterable.any());
        assertEquals(0, takeIterable.getCount());

        final Iterator<Integer> takeIterator = takeIterable.iterate();
        assertFalse(takeIterator.any());
        assertEquals(0, takeIterator.getCount());
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
}
