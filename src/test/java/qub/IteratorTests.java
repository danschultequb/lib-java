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
