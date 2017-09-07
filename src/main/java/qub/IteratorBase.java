package qub;

public abstract class IteratorBase<T> implements Iterator<T>
{
    @Override
    public boolean any()
    {
        return hasCurrent() || next();
    }

    @Override
    public boolean any(Function1<T,Boolean> condition)
    {
        boolean result = false;

        if (condition != null)
        {
            if (!hasStarted())
            {
                next();
            }

            while (hasCurrent())
            {
                if (condition.run(getCurrent()))
                {
                    result = true;
                    break;
                }
                next();
            }
        }

        return result;
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
    public Iterator<T> where(Function1<T,Boolean> condition)
    {
        return condition == null ? this : new WhereIterator<>(this, condition);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return new JavaIterator<>(this);
    }
}
