package qub;

import static org.junit.Assert.assertEquals;

public class ArrayIteratorTests extends IteratorTests
{
    protected Iterator<Integer> createIterator(int count, boolean started)
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
}
