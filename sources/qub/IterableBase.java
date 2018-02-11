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
        return IterableBase.any(this);
    }

    @Override
    public int getCount()
    {
        return IterableBase.getCount(this);
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
        return IterableBase.take(this, toTake);
    }

    @Override
    public Iterable<T> skip(int toSkip)
    {
        return IterableBase.skip(this, toSkip);
    }

    @Override
    public Iterable<T> skipLast()
    {
        return IterableBase.skipLast(this);
    }

    @Override
    public Iterable<T> skipLast(int toSkip)
    {
        return IterableBase.skipLast(this, toSkip);
    }

    @Override
    public Iterable<T> skipUntil(Function1<T,Boolean> condition)
    {
        return IterableBase.skipUntil(this, condition);
    }

    @Override
    public Iterable<T> where(Function1<T,Boolean> condition)
    {
        return IterableBase.where(this, condition);
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
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return rhs instanceof Iterable && equals((Iterable<T>)rhs);
    }

    @Override
    public boolean equals(Iterable<T> rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            result = true;

            final Iterator<T> lhsIterator = iterate();
            final Iterator<T> rhsIterator = rhs.iterate();
            while (lhsIterator.next() & rhsIterator.next())
            {
                final T lhsCurrent = lhsIterator.getCurrent();
                final T rhsCurrent = rhsIterator.getCurrent();
                if (lhsCurrent != rhsCurrent &&
                    (lhsCurrent == null || rhsCurrent == null || !lhsCurrent.equals(rhsCurrent)))
                {
                    result = false;
                    break;
                }
            }

            if (result)
            {
                result = !lhsIterator.hasCurrent() && !rhsIterator.hasCurrent();
            }
        }

        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');

        boolean addedAValue = false;
        for (final T value : this)
        {
            if (addedAValue)
            {
                builder.append(',');
            }
            else
            {
                addedAValue = true;
            }
            builder.append(value.toString());
        }

        builder.append(']');
        return builder.toString();
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return iterate().iterator();
    }

    public static <T> boolean any(Iterable<T> iterable)
    {
        return iterable != null && iterable.iterate().any();
    }

    public static <T> int getCount(Iterable<T> iterable)
    {
        return iterable == null ? 0 : iterable.iterate().getCount();
    }

    public static <T> Iterable<T> take(Iterable<T> iterable, int toTake)
    {
        return new TakeIterable<>(iterable, toTake);
    }

    public static <T> Iterable<T> skip(Iterable<T> iterable, int toSkip)
    {
        return iterable == null || toSkip <= 0 ? iterable : new SkipIterable<>(iterable, toSkip);
    }

    public static <T> Iterable<T> skipLast(Iterable<T> iterable)
    {
        return iterable == null ? iterable : iterable.skipLast(1);
    }

    public static <T> Iterable<T> skipLast(Iterable<T> iterable, int toSkip)
    {
        return iterable == null || toSkip <= 0 ? iterable : iterable.take(iterable.getCount() - toSkip);
    }

    public static <T> Iterable<T> skipUntil(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return iterable == null ? iterable : new SkipUntilIterable<>(iterable, condition);
    }

    public static <T> Iterable<T> where(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return iterable == null || condition == null ? iterable : new WhereIterable<>(iterable, condition);
    }
}
