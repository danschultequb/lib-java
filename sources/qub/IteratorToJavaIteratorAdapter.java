package qub;

/**
 * An adapter that converts a qub.Iterator interface to a java.util.Iterator interface.
 * @param <T> The type of element that this java.util.Iterator returns.
 */
public class IteratorToJavaIteratorAdapter<T> implements java.util.Iterator<T>
{
    private final Iterator<T> iterator;
    private Boolean hasNext;

    private IteratorToJavaIteratorAdapter(Iterator<T> iterator)
    {
        PreCondition.assertNotNull(iterator, "iterator");

        this.iterator = iterator;
        if (this.iterator.hasCurrent())
        {
            this.hasNext = true;
        }
    }

    public static <T> IteratorToJavaIteratorAdapter<T> create(Iterator<T> iterator)
    {
        return new IteratorToJavaIteratorAdapter<>(iterator);
    }

    @Override
    public boolean hasNext()
    {
        if (this.hasNext == null)
        {
            if (!this.iterator.hasStarted())
            {
                this.hasNext = this.iterator.next();
            }
            else if (this.iterator.hasCurrent())
            {
                this.hasNext = this.iterator.next();
            }
            else
            {
                this.hasNext = false;
            }
        }
        return this.hasNext;
    }

    @Override
    public T next()
    {
        PreCondition.assertTrue(this.hasNext(), "this.hasNext()");

        // Advance forward even if assertions have been removed from the build.
        this.hasNext();

        final T result = this.iterator.getCurrent();
        this.hasNext = null;

        return result;
    }

    @Override
    public void remove()
    {
    }
}
