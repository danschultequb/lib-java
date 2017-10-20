package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class IteratorToJavaIteratorAdapterTests
{
    @Test
    public void hasNextWithNonStartedEmpty()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertFalse(iterator.hasStarted());

        for (int i = 0; i < 10; ++i)
        {
            assertFalse(javaIterator.hasNext());
        }
    }

    @Test
    public void hasNextWithStartedEmpty()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertFalse(iterator.next());
        assertTrue(iterator.hasStarted());

        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertTrue(iterator.hasStarted());

        for (int i = 0; i < 10; ++i)
        {
            assertFalse(javaIterator.hasNext());
            assertTrue(iterator.hasStarted());
        }
    }

    @Test
    public void hasNextWithNonStartedNonEmpty()
    {
        final Array<Integer> array = new Array<>(5);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertFalse(iterator.hasStarted());

        for (int i = 0; i < 10; ++i)
        {
            assertTrue(javaIterator.hasNext());
            assertTrue(iterator.hasStarted());
        }
    }

    @Test
    public void hasNextWithStartedNonEmpty()
    {
        final Array<Integer> array = new Array<>(5);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());
        assertTrue(iterator.hasStarted());

        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertTrue(iterator.hasStarted());

        for (int i = 0; i < 10; ++i)
        {
            assertTrue(javaIterator.hasNext());
            assertTrue(iterator.hasStarted());
        }
    }

    @Test
    public void nextWithNonStartedEmpty()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertFalse(iterator.hasStarted());

        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertFalse(iterator.hasStarted());

        for (int i = 0; i < 10; ++i)
        {
            assertNull(javaIterator.next());
        }
    }

    @Test
    public void nextWithStartedEmpty()
    {
        final Array<Integer> array = new Array<>(0);
        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertFalse(iterator.next());
        assertTrue(iterator.hasStarted());

        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertTrue(iterator.hasStarted());

        for (int i = 0; i < 10; ++i)
        {
            assertNull(javaIterator.next());
        }
    }

    @Test
    public void nextWithNonStartedNonEmpty()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < array.getCount(); ++i)
        {
            array.set(i, i);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertFalse(iterator.hasStarted());

        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertFalse(iterator.hasStarted());

        for (int i = 0; i < 5; ++i)
        {
            assertEquals(i, javaIterator.next().intValue());
        }

        assertNull(javaIterator.next());
    }

    @Test
    public void nextWithStartedNonEmpty()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < array.getCount(); ++i)
        {
            array.set(i, i);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        assertTrue(iterator.next());
        assertTrue(iterator.hasStarted());

        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        assertTrue(iterator.hasStarted());

        for (int i = 0; i < 5; ++i)
        {
            assertEquals(i, javaIterator.next().intValue());
        }

        assertNull(javaIterator.next());
    }

    @Test
    public void remove()
    {
        final Array<Integer> array = new Array<>(5);
        for (int i = 0; i < array.getCount(); ++i)
        {
            array.set(i, i);
        }

        final Iterator<Integer> iterator = new ArrayIterator<>(array);
        final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
        javaIterator.remove();
    }
}
