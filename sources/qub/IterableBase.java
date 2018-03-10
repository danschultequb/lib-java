package qub;

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
    public final T first()
    {
        return IterableBase.first(this);
    }

    @Override
    public final T first(Function1<T, Boolean> condition)
    {
        return IterableBase.first(this, condition);
    }

    @Override
    public final T last()
    {
        return IterableBase.last(this);
    }

    @Override
    public final T last(Function1<T, Boolean> condition)
    {
        return IterableBase.last(this, condition);
    }

    @Override
    public final boolean contains(T value)
    {
        return IterableBase.contains(this, value);
    }

    @Override
    public final boolean contains(Function1<T, Boolean> condition)
    {
        return IterableBase.contains(this, condition);
    }

    @Override
    public final Iterable<T> take(int toTake)
    {
        return IterableBase.take(this, toTake);
    }

    @Override
    public final Iterable<T> skip(int toSkip)
    {
        return IterableBase.skip(this, toSkip);
    }

    @Override
    public final Iterable<T> skipFirst()
    {
        return IterableBase.skipFirst(this);
    }

    @Override
    public final Iterable<T> skipLast()
    {
        return IterableBase.skipLast(this);
    }

    @Override
    public final Iterable<T> skipLast(int toSkip)
    {
        return IterableBase.skipLast(this, toSkip);
    }

    @Override
    public final Iterable<T> skipUntil(Function1<T, Boolean> condition)
    {
        return IterableBase.skipUntil(this, condition);
    }

    @Override
    public final Iterable<T> where(Function1<T, Boolean> condition)
    {
        return IterableBase.where(this, condition);
    }

    @Override
    public <U> Iterable<U> map(Function1<T, U> conversion)
    {
        return IterableBase.map(this, conversion);
    }

    @Override
    public final <U> Iterable<U> instanceOf(Class<U> type)
    {
        return IterableBase.instanceOf(this, type);
    }

    @Override
    public final boolean equals(Object rhs)
    {
        return IterableBase.equals(this, rhs);
    }

    @Override
    public final boolean equals(Iterable<T> rhs)
    {
        return IterableBase.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return IterableBase.toString(this);
    }

    @Override
    public final java.util.Iterator<T> iterator()
    {
        return IterableBase.iterator(this);
    }

    /**
     * Get whether or not this Iterable contains any values.
     * @return Whether or not this Iterable contains any values.
     */
    public static <T> boolean any(Iterable<T> iterable)
    {
        return iterable.iterate().any();
    }

    /**
     * Get the number of values that are in this Iterable.
     * @return The number of values that are in this Iterable.
     */
    public static <T> int getCount(Iterable<T> iterable)
    {
        return iterable.iterate().getCount();
    }

    /**
     * Get the first value in this Iterable.
     * @return The first value of this Iterable, or null if this Iterable is empty.
     */
    public static <T> T first(Iterable<T> iterable)
    {
        return iterable.iterate().first();
    }

    /**
     * Get the first value in this Iterable that matches the provided condition.
     * @return The first value of this Iterable that matches the provided condition, or null if this
     * Iterable has no values that match the condition.
     */
    public static <T> T first(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return iterable.iterate().first(condition);
    }

    /**
     * Get the last value in this Iterable.
     * @return The last value in this Iterable, or null if this Iterable is empty.
     */
    public static <T> T last(Iterable<T> iterable)
    {
        return iterable.iterate().last();
    }

    /**
     * Get the last value in this Iterable that matches the provided condition.
     * @param condition The condition to run against each of the values in this Iterable.
     * @return The last value of this Iterable that matches the provided condition, or null if this
     * Iterable has no values that match the condition.
     */
    public static <T> T last(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return iterable.iterate().last(condition);
    }

    /**
     * Get whether or not this Iterable contains the provided value using the standard equals()
     * method to compare values.
     * @param value The value to look for in this Iterator.
     * @return Whether or not this Iterator contains the provided value.
     */
    public static <T> boolean contains(Iterable<T> iterable, T value)
    {
        return iterable.iterate().contains(value);
    }

    /**
     * Get whether or not this Iterable contains a value that matches the provided condition.
     * @param condition The condition to check against the values in this Iterable.
     * @return Whether or not this Iterable contains a value that matches the provided condition.
     */
    public static <T> boolean contains(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return iterable.iterate().contains(condition);
    }

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this Iterable to a fixed number of values.
     */
    public static <T> Iterable<T> take(Iterable<T> iterable, int toTake)
    {
        return new TakeIterable<>(iterable, toTake);
    }

    /**
     * Create a new Iterable that will skip over the first toSkip number of elements in this
     * Iterable and then return the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterable that will skip over the first toSkip number of elements in this
     * Iterable and then return the remaining elements.
     */
    public static <T> Iterable<T> skip(Iterable<T> iterable, int toSkip)
    {
        return toSkip <= 0 ? iterable : new SkipIterable<>(iterable, toSkip);
    }

    /**
     * Create a new Iterable will skip over the first element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the first element in this Iterable and return the
     * remaining elements.
     */
    public static <T> Iterable<T> skipFirst(Iterable<T> iterable)
    {
        return iterable.skip(1);
    }

    /**
     * Create a new Iterable will skip over the last element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the last element in this Iterable and return the
     * remaining elements.
     */
    public static <T> Iterable<T> skipLast(Iterable<T> iterable)
    {
        return iterable.skipLast(1);
    }

    /**
     * Create a new Iterable that will skip over the last toSkip elements in this Iterable and
     * return the remaining elements.
     * @param toSkip The number of elements to skip from the end.
     * @return A new Iterable that will skip over the last toSkip elements in this Iterable and
     * return the remaining elements.
     */
    public static <T> Iterable<T> skipLast(Iterable<T> iterable, int toSkip)
    {
        return toSkip <= 0 ? iterable : iterable.take(iterable.getCount() - toSkip);
    }

    /**
     * Create a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true. The returned Iterator will start at the
     * element after the element that made the condition true.
     * @param condition The condition.
     * @return a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true.
     */
    public static <T> Iterable<T> skipUntil(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return new SkipUntilIterable<>(iterable, condition);
    }

    /**
     * Create a new Iterable that only returns the values from this Iterable that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned from the created Iterable.
     * @return An Iterable that only returns the values from this Iterator that satisfy the given
     * condition.
     */
    public static <T> Iterable<T> where(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        return condition == null ? iterable : new WhereIterable<>(iterable, condition);
    }

    /**
     * Convert this Iterable into an Iterable that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Iterable that returns values of type U instead of type T.
     */
    public static <T,U> Iterable<U> map(Iterable<T> iterable, Function1<T,U> conversion)
    {
        return new MapIterable<>(iterable, conversion);
    }

    /**
     * Convert this Iterable into an Iterable that only returns the values in this Iterable that are
     * of type or sub-classes of type U.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     * @return An Iterable that only returns the values in this Iterable that are of type or
     * sub-classes of type U.
     */
    public static <T,U> Iterable<U> instanceOf(Iterable<T> iterable, Class<U> type)
    {
        return new InstanceOfIterable<>(iterable, type);
    }

    /**
     * Create a java.util.Iterator that will iterate over this Iterable.
     * @return A java.util.Iterator that will iterate over this Iterable.
     */
    public static <T> java.util.Iterator<T> iterator(Iterable<T> iterable)
    {
        return iterable.iterate().iterator();
    }

    /**
     * Get whether or not the provided iterator is equal to the provided value.
     * @param iterable The Iterable to compare against the provided value.
     * @param value The vlaue to compare against the provided Iterator.
     * @param <T> The type of values in the Iterator.
     * @return Whether or not the provided iterator is equal to the provided value.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Iterable<T> iterable, Object value)
    {
        return value instanceof Iterable && iterable.equals((Iterable<T>)value);
    }

    /**
     * Get whether or not this Iterable contains equal elements in the same order as the provided
     * Iterable.
     * @param rhs The Iterable to compare against this Iterable.
     * @return Whether or not this Iterable contains equal elements in the same order as the
     * provided Iterable.
     */
    public static <T> boolean equals(Iterable<T> lhs, Iterable<T> rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            result = true;

            final Iterator<T> lhsIterator = lhs.iterate();
            final Iterator<T> rhsIterator = rhs.iterate();
            while (lhsIterator.next() & rhsIterator.next())
            {
                if (!Comparer.equal(lhsIterator.getCurrent(), rhsIterator.getCurrent()))
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

    public static <T> String toString(Iterable<T> iterable)
    {
        final StringBuilder builder = new StringBuilder();
        if (iterable == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append('[');

            boolean addedAValue = false;
            for (final Object value : iterable)
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
        }
        return builder.toString();
    }
}
