package qub;

/**
 * An abstract base implementation of Iterable that contains the common implementation of many of
 * Iterable's methods.
 * @param <T> The type of value that this IterableBase contains.
 */
public abstract class IterableBase<T> implements Iterable<T>
{
    @Override
    public boolean any()
    {
        return iterate().any();
    }

    @Override
    public boolean any(Function1<T,Boolean> condition)
    {
        return iterate().any(condition);
    }

    @Override
    public int getCount()
    {
        return iterate().getCount();
    }

    @Override
    public T first()
    {
        return iterate().first();
    }

    @Override
    public T first(Function1<T,Boolean> condition)
    {
        return iterate().first(condition);
    }

    @Override
    public T last()
    {
        return iterate().last();
    }

    @Override
    public T last(Function1<T,Boolean> condition)
    {
        return iterate().last(condition);
    }

    @Override
    public boolean contains(T value)
    {
        return iterate().contains(value);
    }

    @Override
    public boolean contains(Function1<T,Boolean> condition)
    {
        return iterate().contains(condition);
    }

    @Override
    public Iterable<T> take(int toTake)
    {
        return new TakeIterable<>(this, toTake);
    }

    @Override
    public Iterable<T> skip(int toSkip)
    {
        return toSkip <= 0 ? this : new SkipIterable<>(this, toSkip);
    }

    @Override
    public Iterable<T> skipLast()
    {
        return skipLast(1);
    }

    @Override
    public Iterable<T> skipLast(int toSkip)
    {
        return toSkip <= 0 ? this : take(getCount() - toSkip);
    }

    @Override
    public Iterable<T> where(Function1<T,Boolean> condition)
    {
        return condition == null ? this : new WhereIterable<>(this, condition);
    }

    @Override
    public <U> Iterable<U> map(Function1<T,U> conversion)
    {
        return new MapIterable<>(this, conversion);
    }

    @Override
    public <U> Iterable<U> instanceOf(Class<U> type)
    {
        return new InstanceOfIterable<>(this, type);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return iterate().iterator();
    }
}
