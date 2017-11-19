package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class IndexableTests extends IterableTests
{
    @Override
    protected Iterable<Integer> createIterable(int count)
    {
        return createIndexable(count);
    }

    protected abstract Indexable<Integer> createIndexable(int count);

    @Test
    public void createIndexableWithNegative()
    {
        final Indexable<Integer> indexable = createIndexable(-2);
        assertNotNull(indexable);
        assertFalse(indexable.any());
    }

    @Test
    public void createIndexableWithZero()
    {
        final Indexable<Integer> indexable = createIndexable(0);
        assertNotNull(indexable);
        assertFalse(indexable.any());
    }

    @Test
    public void createIndexableWithPositive()
    {
        final Indexable<Integer> indexable = createIndexable(10);
        assertNotNull(indexable);
        assertTrue(indexable.any());
        assertEquals(10, indexable.getCount());
        for (int i = 0; i < 10; ++i)
        {
            assertEquals(i, indexable.get(i).intValue());
        }
    }

    @Test
    public void getWithNegativeIndex()
    {
        final Indexable<Integer> indexable = createIndexable(0);
        assertNull(indexable.get(-5));
    }

    @Test
    public void getWithIndexEqualToIndexablesCount()
    {
        final Indexable<Integer> indexable = createIndexable(3);
        assertNull(indexable.get(3));
    }

    @Test
    public void indexOfWithEmptyAndNullCondition()
    {
        final Indexable<Integer> indexable = createIndexable(0);
        assertEquals(-1, indexable.indexOf((Function1<Integer,Boolean>)null));
    }

    @Test
    public void indexOfWithEmptyAndNonNullCondition()
    {
        final Indexable<Integer> indexable = createIndexable(0);
        assertEquals(-1, indexable.indexOf(Math.isOdd));
    }

    @Test
    public void indexOfWithNonEmptyAndNullCondition()
    {
        final Indexable<Integer> indexable = createIndexable(1);
        assertEquals(-1, indexable.indexOf((Function1<Integer,Boolean>)null));
    }

    @Test
    public void indexOfWithNonEmptyAndNonMatchingCondition()
    {
        final Indexable<Integer> indexable = createIndexable(1);
        assertEquals(-1, indexable.indexOf(Math.isOdd));
    }

    @Test
    public void indexOfWithNonEmptyAndMatchingCondition()
    {
        final Indexable<Integer> indexable = createIndexable(7);
        assertEquals(1, indexable.indexOf(Math.isOdd));
    }

    @Test
    public void indexOfWithNullValue()
    {
        final Indexable<Integer> indexable = createIndexable(2);
        assertEquals(-1, indexable.indexOf((Integer)null));
    }

    @Test
    public void indexOfWithNotFoundValue()
    {
        final Indexable<Integer> indexable = createIndexable(2);
        assertEquals(-1, indexable.indexOf(20));
    }

    @Test
    public void indexOfWithFoundValue()
    {
        final Indexable<Integer> indexable = createIndexable(10);
        assertEquals(4, indexable.indexOf(4));
    }
}
