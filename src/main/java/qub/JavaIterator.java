package qub;

/**
 * An adapter that converts a qub.Iterator interface to a java.util.Iterator interface.
 * @param <T> The type of element that this java.util.Iterator returns.
 */
class JavaIterator<T> implements java.util.Iterator<T>
{
    private final Iterator<T> iterator;

    JavaIterator(Iterator<T> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext()
    {
        return iterator.hasStarted() ? iterator.hasCurrent() : iterator.next();
    }

    @Override
    public T next()
    {
        if (!iterator.hasStarted()) {
            iterator.next();
        }

        final T result = iterator.getCurrent();
        iterator.next();

        return result;
    }

    @Override
    public void remove()
    {
    }
}
