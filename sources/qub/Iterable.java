package qub;

/**
 * An interface of a collection that can have its contents iterated through multiple times.
 * @param <T> The type of value that this Iterable contains.
 */
public interface Iterable<T> extends java.lang.Iterable<T>
{
    /**
     * Get an empty Iterable.
     * @param <U> The type of values that would be stored in the Iterable.
     * @return An empty Iterable.
     */
    static <U> Iterable<U> empty()
    {
        return new Array<>(0);
    }

    /**
     * Get an Iterator that will iterate over the contents of this Iterable.
     * @return An Iterator that will iterate over the contents of this Iterable.
     */
    Iterator<T> iterate();

    /**
     * Get whether or not this Iterable contains any values.
     * @return Whether or not this Iterable contains any values.
     */
    default boolean any()
    {
        return iterate().any();
    }

    /**
     * Get the number of values that are in this Iterable.
     * @return The number of values that are in this Iterable.
     */
    default int getCount()
    {
        return iterate().getCount();
    }

    /**
     * Get the first value in this Iterable.
     * @return The first value of this Iterable, or null if this Iterable is empty.
     */
    default T first()
    {
        return iterate().first();
    }

    /**
     * Get the first value in this Iterable that isMatch the provided condition.
     * @return The first value of this Iterable that isMatch the provided condition, or null if this
     * Iterable has no values that match the condition.
     */
    default T first(Function1<T,Boolean> condition)
    {
        return iterate().first(condition);
    }

    /**
     * Get the last value in this Iterable.
     * @return The last value in this Iterable, or null if this Iterable is empty.
     */
    default T last()
    {
        return iterate().last();
    }

    /**
     * Get the last value in this Iterable that isMatch the provided condition.
     * @param condition The condition to run against each of the values in this Iterable.
     * @return The last value of this Iterable that isMatch the provided condition, or null if this
     * Iterable has no values that match the condition.
     */
    default T last(Function1<T,Boolean> condition)
    {
        return iterate().last(condition);
    }

    /**
     * Get whether or not this Iterable contains the provided value using the standard equals()
     * method to compare values.
     * @param value The value to look for in this Iterable.
     * @return Whether or not this Iterable contains the provided value.
     */
    default boolean contains(T value)
    {
        return contains(value, Comparer::equal);
    }

    /**
     * Get whether or not this Iterable contains the provided value using the provided comparison
     * method to compare values.
     * @param value The value to look for in this Iterable.
     * @param comparison The comparison function to use to compare values.
     * @return Whether or not this Iterable contains the provided value.
     */
    default boolean contains(T value, Function2<T,T,Boolean> comparison)
    {
        return contains((T existingValue) -> comparison.run(value, existingValue));
    }

    /**
     * Get whether or not this Iterable contains a value that matches the provided condition.
     * @param condition The condition to check against the values in this Iterable.
     * @return Whether or not this Iterable contains a value that matches the provided condition.
     */
    default boolean contains(Function1<T,Boolean> condition)
    {
        return iterate().contains(condition);
    }

    /**
     * Get whether or not this Iterable does not contain the provided value.
     * @param value The value to look for.
     * @return Whether or not this Iterable does not contain the provided value.
     */
    default boolean doesNotContain(T value)
    {
        return !contains(value);
    }

    /**
     * Get whether or not this Iterable does not contain the provided value using the provided
     * comparison function.
     * @param value The value to look for.
     * @param comparison The function to use to compare the provided value against the values within
     *                   this Iterable.
     * @return Whether or not this Iterable does not contain the provided value using the provided
     * comparison function.
     */
    default boolean doesNotContain(T value, Function2<T,T,Boolean> comparison)
    {
        return !contains(value, comparison);
    }

    /**
     * Get whether or not this Iterable does not contain a value that matches the provided
     * condition.
     * @param condition The function to look for a match for.
     * @return Whether or not this Iterable does not contain a value that matches the provided
     * condition.
     */
    default boolean doesNotContain(Function1<T,Boolean> condition)
    {
        return !contains(condition);
    }

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this Iterable to a fixed number of values.
     */
    default Iterable<T> take(int toTake)
    {
        return new TakeIterable<>(this, toTake);
    }

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values from the end.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this iterable to a fixed number of values from the end.
     */
    default Iterable<T> takeLast(int toTake)
    {
        return skip(getCount() - toTake).take(toTake);
    }

    /**
     * Create a new Iterable that will skip over the first toSkip number of elements in this
     * Iterable and then return the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterable that will skip over the first toSkip number of elements in this
     * Iterable and then return the remaining elements.
     */
    default Iterable<T> skip(int toSkip)
    {
        return toSkip <= 0 ? this : new SkipIterable<>(this, toSkip);
    }

    /**
     * Create a new Iterable will skip over the first element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the first element in this Iterable and return the
     * remaining elements.
     */
    default Iterable<T> skipFirst()
    {
        return skip(1);
    }

    /**
     * Create a new Iterable will skip over the last element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the last element in this Iterable and return the
     * remaining elements.
     */
    default Iterable<T> skipLast()
    {
        return skipLast(1);
    }

    /**
     * Create a new Iterable that will skip over the last toSkip elements in this Iterable and
     * return the remaining elements.
     * @param toSkip The number of elements to skip from the end.
     * @return A new Iterable that will skip over the last toSkip elements in this Iterable and
     * return the remaining elements.
     */
    default Iterable<T> skipLast(int toSkip)
    {
        return toSkip <= 0 ? this : take(getCount() - toSkip);
    }

    /**
     * Create a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true. The returned Iterator will start at the
     * element after the element that made the condition true.
     * @param condition The condition.
     * @return a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true.
     */
    default Iterable<T> skipUntil(Function1<T,Boolean> condition)
    {
        return new SkipUntilIterable<>(this, condition);
    }

    /**
     * Create a new Iterable that only returns the values from this Iterable that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned from the created Iterable.
     * @return An Iterable that only returns the values from this Iterator that satisfy the given
     * condition.
     */
    default Iterable<T> where(Function1<T,Boolean> condition)
    {
        return condition == null ? this : new WhereIterable<>(this, condition);
    }

    /**
     * Convert this Iterable into an Iterable that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Iterable that returns values of type U instead of type T.
     */
    default <U> Iterable<U> map(Function1<T,U> conversion)
    {
        return new MapIterable<>(this, conversion);
    }

    /**
     * Convert this Iterable into an Iterable that only returns the values in this Iterable that are
     * of type or sub-classes of type U.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     * @return An Iterable that only returns the values in this Iterable that are of type or
     * sub-classes of type U.
     */
    default <U> Iterable<U> instanceOf(Class<U> type)
    {
        return new InstanceOfIterable<>(this, type);
    }

    /**
     * Get the value in this Iterable that is the maximum based on the provided comparer function.
     * @param comparer The function to use to compare the values in this Iterable.
     * @return The maximum value in this Iterable based on the provided comparer function.
     */
    default T minimum(Function2<T,T,Comparison> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        T result = null;

        final Iterator<T> iterator = iterate();
        if (iterator.next())
        {
            result = iterator.takeCurrent();
            while (iterator.hasCurrent())
            {
                final T current = iterator.takeCurrent();
                if (comparer.run(current, result) == Comparison.LessThan)
                {
                    result = current;
                }
            }
        }

        return result;
    }

    /**
     * Get the value in this Iterable that is the maximum based on the provided comparer function.
     * @param comparer The function to use to compare the values in this Iterable.
     * @return The maximum value in this Iterable based on the provided comparer function.
     */
    default T maximum(Function2<T,T,Comparison> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        T result = null;

        final Iterator<T> iterator = iterate();
        if (iterator.next())
        {
            result = iterator.takeCurrent();
            while (iterator.hasCurrent())
            {
                final T current = iterator.takeCurrent();
                if (comparer.run(current, result) == Comparison.GreaterThan)
                {
                    result = current;
                }
            }
        }

        return result;
    }

    /**
     * Get whether or not this Iterable contains equal elements in the same order as the provided
     * Iterable.
     * @param rhs The Iterable to compare against this Iterable.
     * @return Whether or not this Iterable contains equal elements in the same order as the
     * provided Iterable.
     */
    default boolean equals(Iterable<T> rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            result = true;

            final Iterator<T> lhsIterator = this.iterate();
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

    /**
     * Create a java.util.Iterator that will iterate over this Iterable.
     * @return A java.util.Iterator that will iterate over this Iterable.
     */
    default java.util.Iterator<T> iterator()
    {
        return iterate().iterator();
    }

    /**
     * Get whether or not the lhs Iterable contains equal elements in the same order as the provided
     * rhs Iterable.
     * @param rhs The Iterable to compare against this Iterable.
     * @return Whether or not this Iterable contains equal elements in the same order as the
     * provided Iterable.
     */
    @SuppressWarnings("unchecked")
    static <T> boolean equals(Iterable<T> lhs, Object rhs)
    {
        return rhs instanceof Iterable && lhs.equals((Iterable<T>)rhs);
    }

    /**
     * Get whether or not the provided Iterable is null or empty.
     * @param value The Iterable to check.
     * @param <T> The type of values contained by the Iterable.
     * @return Whether or not the Iterable is null or empty.
     */
    static <T> boolean isNullOrEmpty(Iterable<T> value)
    {
        return value == null || !value.any();
    }

    /**
     * Get the String representation of the provided Iterable.
     * @return The String representation of the provided Iterable.
     */
    static String toString(Iterable<?> iterable)
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
                builder.append(Objects.toString(value));
            }

            builder.append(']');
        }
        return builder.toString();
    }

    /**
     * Create a new Iterable from the provided values.
     * @param values The values to convert to an Iterable.
     * @param <T> The type of values in the created Iterable.
     * @return The created Iterable.
     */
    @SafeVarargs
    static <T> Iterable<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(values);
    }
}
