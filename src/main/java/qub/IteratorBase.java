package qub;

public abstract class IteratorBase<T> implements Iterator<T>
{
    @Override
    public T takeCurrent()
    {
        final T current = getCurrent();
        next();
        return current;
    }

    @Override
    public boolean any()
    {
        return hasCurrent() || next();
    }

    @Override
    public int getCount()
    {
        int result = hasCurrent() ? 1 : 0;
        while (next()) {
            ++result;
        }
        return result;
    }

    @Override
    public T first()
    {
        if (!hasStarted())
        {
            next();
        }
        return getCurrent();
    }

    @Override
    public T first(Function1<T,Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            if (hasCurrent() && condition.run(getCurrent()))
            {
                result = getCurrent();
            }
            else
            {
                while (next())
                {
                    if (condition.run(getCurrent()))
                    {
                        result = getCurrent();
                        break;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public T last()
    {
        T result = null;

        if (hasCurrent())
        {
            result = getCurrent();
        }

        while (next())
        {
            result = getCurrent();
        }

        return result;
    }

    @Override
    public T last(Function1<T,Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            if (hasCurrent() && condition.run(getCurrent()))
            {
                result = getCurrent();
            }

            while (next())
            {
                if (condition.run(getCurrent()))
                {
                    result = getCurrent();
                }
            }
        }

        return result;
    }

    @Override
    public boolean contains(final T value)
    {
        return contains(new Function1<T,Boolean>()
        {
            @Override
            public Boolean run(T iteratorValue)
            {
                return Comparer.equal(iteratorValue, value);
            }
        });
    }

    @Override
    public boolean contains(Function1<T,Boolean> condition)
    {
        boolean result = false;

        if (condition != null)
        {
            if (hasCurrent())
            {
                result = condition.run(getCurrent());
            }

            while (!result && next())
            {
                if (condition.run(getCurrent()))
                {
                    result = condition.run(getCurrent());
                }
            }
        }

        return result;
    }

    @Override
    public Iterator<T> take(int toTake)
    {
        return new TakeIterator<>(this, toTake);
    }

    @Override
    public Iterator<T> skip(int toSkip)
    {
        return new SkipIterator<>(this, toSkip);
    }

    @Override
    public Iterator<T> skipUntil(Function1<T,Boolean> condition)
    {
        return new SkipUntilIterator<>(this, condition);
    }

    @Override
    public Iterator<T> where(Function1<T,Boolean> condition)
    {
        return condition == null ? this : new WhereIterator<>(this, condition);
    }

    @Override
    public <U> Iterator<U> map(Function1<T,U> conversion)
    {
        return new MapIterator<>(this, conversion);
    }

    @Override
    public <U> Iterator<U> instanceOf(Class<U> type)
    {
        return new InstanceOfIterator<>(this, type);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return new IteratorToJavaIteratorAdapter<>(this);
    }
}
