package qub;

public class JavaIteratorToIteratorAdapter<T> extends IteratorBase<T>
{
    private final java.util.Iterator<T> javaIterator;
    private boolean hasStarted;
    private boolean hasCurrent;
    private T current;

    private JavaIteratorToIteratorAdapter(java.util.Iterator<T> javaIterator)
    {
        this.javaIterator = javaIterator;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasCurrent;
    }

    @Override
    public T getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        hasStarted = true;

        hasCurrent = javaIterator.hasNext();
        current = !hasCurrent ? null : javaIterator.next();

        return hasCurrent;
    }

    public static <T> Iterator<T> wrap(java.util.Iterator<T> javaIterator)
    {
        return javaIterator == null ? null : new JavaIteratorToIteratorAdapter<>(javaIterator);
    }
}
