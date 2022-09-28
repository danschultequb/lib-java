package qub;

public class ConcatenateIterator<T> implements Iterator<T>
{
    private final Iterator<Iterator<T>> iterators;

    private ConcatenateIterator(Iterator<Iterator<T>> iterators)
    {
        PreCondition.assertNotNull(iterators, "iterators");

        this.iterators = iterators;
    }

    public static <T> ConcatenateIterator<T> create(Iterator<Iterator<T>> iterators)
    {
        return new ConcatenateIterator<>(iterators);
    }

    @Override
    public boolean hasStarted()
    {
        return this.iterators.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasCurrentIterator();
    }

    @Override
    public T getCurrent()
    {
        return this.iterators.getCurrent().getCurrent();
    }

    private boolean nextIterator()
    {
        return this.iterators.next();
    }

    private boolean hasCurrentIterator()
    {
        return this.iterators.hasCurrent();
    }

    private Iterator<T> getCurrentIterator()
    {
        PreCondition.assertTrue(this.hasCurrentIterator(), "this.hasCurrentIterator()");

        return this.iterators.getCurrent();
    }

    @Override
    public boolean next()
    {
        boolean result = false;

        if (!this.hasStarted())
        {
            while (this.nextIterator())
            {
                final Iterator<T> currentIterator = this.getCurrentIterator();
                if (currentIterator.hasCurrent() || currentIterator.next())
                {
                    result = true;
                    break;
                }
            }
        }
        else
        {
            while (this.hasCurrentIterator())
            {
                if (this.getCurrentIterator().next())
                {
                    result = true;
                    break;
                }
                else if (this.nextIterator())
                {
                    final Iterator<T> currentIterator = this.getCurrentIterator();
                    if (currentIterator.hasCurrent() || currentIterator.next())
                    {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
