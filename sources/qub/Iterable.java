package qub;

/**
 * An interface of a collection that can have its contents iterated through multiple times.
 * @param <T> The type of value that this {@link Iterable} contains.
 */
public interface Iterable<T> extends java.lang.Iterable<T>
{
    /**
     * Create a new {@link Iterable} from the provided values.
     * @param values The values to convert to an {@link Iterable}.
     * @param <T> The type of values in the created {@link Iterable}.
     */
    @SafeVarargs
    public static <T> Iterable<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(values);
    }

    /**
     * Create a new {@link Array} from the values in this {@link Iterable}.
     */
    default Array<T> toArray()
    {
        return Array.create(this);
    }

    /**
     * Create a new {@link List} from the values in this {@link Iterable}.
     */
    public default List<T> toList()
    {
        return List.create(this);
    }

    /**
     * Create a new {@link Set2} from the values in this {@link Iterable}.
     */
    public default Set2<T> toSet()
    {
        return Set2.create(this);
    }

    /**
     * Create a new {@link MutableMap} from the values in this {@link Iterable}. The entry values in
     * the new {@link MutableMap} will be the values in this {@link Iterable}.
     * @param getKey The {@link Function1} that will select an entry's key from a value from this
     *               {@link Iterable}.
     * @param <K> The type of value that will serve as the keys of the {@link MutableMap}.
     */
    public default <K> MutableMap<K,T> toMap(Function1<T,K> getKey)
    {
        return this.toMap(getKey, (T value) -> value);
    }

    /**
     * Create a new {@link MutableMap} from the values in this {@link Iterable}.
     * @param getKey The {@link Function1} that will select an entry's key from a value from this
     *               {@link Iterable}.
     * @param getValue The {@link Function1} that will select an entry's value from a value from
     *                 this {@link Iterable}.
     * @param <K> The type of value that will serve as the keys of the {@link MutableMap}.
     * @param <V> The type of value that will serve as the values of the {@link MutableMap}.
     */
    public default <K,V> MutableMap<K,V> toMap(Function1<T,K> getKey, Function1<T,V> getValue)
    {
        return Map.create(this, getKey, getValue);
    }

    /**
     * Get an {@link Iterator} that will iterate over the contents of this {@link Iterable}.
     */
    public Iterator<T> iterate();

    /**
     * Get whether this {@link Iterable} contains any values.
     */
    public default boolean any()
    {
        return this.iterate().any();
    }

    /**
     * Get the number of values that are in this {@link Iterable}.
     */
    public default int getCount()
    {
        return this.iterate().getCount();
    }

    /**
     * Get the first value in this {@link Iterable}.
     * @exception EmptyException if this {@link Iterable} is empty.
     */
    public default Result<T> first()
    {
        return this.iterate().first();
    }

    /**
     * Get the first value in this {@link Iterable} that matches the provided condition.
     * @param condition The condition to run against each of the values in this {@link Iterable}.
     * @exception NotFoundException if no matching value is found.
     */
    public default Result<T> first(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return this.iterate().first(condition)
            .convertError(NotFoundException.class, () -> new NotFoundException("Could not find a value in this " + Types.getTypeName(Iterable.class) + " that matches the provided condition."));
    }

    /**
     * Get the last value in this {@link Iterable}.
     * @exception EmptyException if this {@link Iterable} is empty.
     */
    public default Result<T> last()
    {
        return this.iterate().last();
    }

    /**
     * Get the last value in this {@link Iterable} that matches the provided condition.
     * @param condition The condition to run against each of the values in this {@link Iterable}.
     * @exception NotFoundException if no value is found.
     */
    public default Result<T> last(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return this.iterate().last(condition)
            .convertError(NotFoundException.class, () -> new NotFoundException("Could not find a value in this " + Types.getTypeName(Iterable.class) + " that matches the provided condition."));
    }

    /**
     * Get whether this {@link Iterable} contains the provided value using {@link Comparer}'s
     * equal(T,T) function to compare values.
     * @param value The value to look for in this {@link Iterable}.
     */
    public default boolean contains(T value)
    {
        return this.contains(value, Comparer::equal);
    }

    /**
     * Get whether or not this Iterable contains the provided value using the provided comparison
     * method to compare values.
     * @param value The value to look for in this Iterable.
     * @param equalFunction The comparison function to use to compare values.
     * @return Whether or not this Iterable contains the provided value.
     */
    public default boolean contains(T value, EqualFunction<T> equalFunction)
    {
        return this.contains((T existingValue) -> equalFunction.run(value, existingValue));
    }

    /**
     * Get whether or not this Iterable contains a value that matches the provided condition.
     * @param condition The condition to check against the values in this Iterable.
     * @return Whether or not this Iterable contains a value that matches the provided condition.
     */
    default boolean contains(Function1<T,Boolean> condition)
    {
        return iterate().contains(condition).await();
    }

    /**
     * Get whether this {@link Iterable} does not contain the provided value.
     * @param value The value to look for.
     */
    public default boolean doesNotContain(T value)
    {
        return !this.contains(value);
    }

    /**
     * Get whether this {@link Iterable} does not contain the provided value using the provided
     * comparison function.
     * @param value The value to look for.
     * @param equalFunction The {@link Function2} to use to compare the provided value against the
     *                   values within this {@link Iterable}.
     */
    public default boolean doesNotContain(T value, EqualFunction<T> equalFunction)
    {
        return !this.contains(value, equalFunction);
    }

    /**
     * Get whether this {@link Iterable} does not contain a value that matches the provided
     * condition.
     * @param condition The {@link Function1} to look for a match for.
     */
    public default boolean doesNotContain(Function1<T,Boolean> condition)
    {
        return !this.contains(condition);
    }

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this Iterable to a fixed number of values.
     */
    default Iterable<T> take(int toTake)
    {
        PreCondition.assertGreaterThanOrEqualTo(toTake, 0, "toTake");

        return new TakeIterable<>(this, toTake);
    }

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values create the end.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this iterable to a fixed number of values create the end.
     */
    default Iterable<T> takeLast(int toTake)
    {
        return skip(Math.maximum(0, getCount() - toTake)).take(toTake);
    }

    /**
     * Create a new {@link Iterable} that will skip over the first toSkip number of elements in this
     * {@link Iterable} and then return the remaining elements.
     * @param toSkip The number of elements to skip.
     */
    public default Iterable<T> skip(int toSkip)
    {
        return SkipIterable.create(this, toSkip);
    }

    /**
     * Create a new Iterable will skip over the first element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the first element in this Iterable and return the
     * remaining elements.
     */
    public default Iterable<T> skipFirst()
    {
        return this.skip(1);
    }

    /**
     * Create a new Iterable will skip over the last element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the last element in this Iterable and return the
     * remaining elements.
     */
    public default Iterable<T> skipLast()
    {
        return this.skipLast(1);
    }

    /**
     * Create a new {@link Iterable} that will skip over the last toSkip elements in this
     * {@link Iterable} and return the remaining elements.
     * @param toSkip The number of elements to skip from the end.
     */
    public default Iterable<T> skipLast(int toSkip)
    {
        PreCondition.assertGreaterThanOrEqualTo(toSkip, 0, "toSkip");

        return this.take(Math.maximum(0, this.getCount() - toSkip));
    }

    /**
     * Create a new {@link Iterable} that will skip over the elements in this {@link Iterable} until
     * it finds an element that makes the provided condition true. The returned {@link Iterable}
     * will start at the element after the element that made the condition true.
     * @param condition The condition.
     */
    public default Iterable<T> skipUntil(Function1<T,Boolean> condition)
    {
        return SkipUntilIterable.create(this, condition);
    }

    /**
     * Create a new {@link Iterable} that only returns the values from this {@link Iterable} that
     * satisfy the given condition.
     * @param condition The condition values must satisfy to be returned from the created
     * {@link Iterable}.
     */
    public default Iterable<T> where(Function1<T,Boolean> condition)
    {
        return WhereIterable.create(this, condition);
    }

    /**
     * Convert this {@link Iterable} into an {@link Iterable} that returns values of type {@link U}
     * instead of type {@link T}.
     * @param conversion The {@link Function1} to use to convert values of type {@link T} to type
     * {@link U}.
     * @param <U> The type to convert values of type {@link T} to.
     */
    public default <U> Iterable<U> map(Function1<T,U> conversion)
    {
        return MapIterable.create(this, conversion);
    }

    /**
     * Convert this {@link Iterable} into an {@link Iterable} that only returns values that are of
     * type or sub-classes of type {@link U}.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     */
    public default <U> Iterable<U> instanceOf(Class<U> type)
    {
        return InstanceOfIterable.create(this, type);
    }

    /**
     * Get whether or this {@link Iterable} contains equal elements in the same order as the
     * provided {@link Iterable}.
     * @param rhs The {@link Iterable} to compare against this {@link Iterable}.
     */
    public default boolean equals(Iterable<T> rhs)
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
     * Create a {@link java.util.Iterator} that will iterate over this {@link Iterable}.
     */
    public default java.util.Iterator<T> iterator()
    {
        return this.iterate().iterator();
    }

    /**
     * Get whether the lhs {@link Iterable} contains equal elements in the same order as the
     * provided rhs {@link Iterable}.
     * @param lhs The left-hand-side {@link Iterable} in the comparison.
     * @param rhs The right-hand-side {@link Iterable} in the comparison.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Iterable<T> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Iterable && lhs.equals((Iterable<T>)rhs);
    }

    /**
     * Get whether the provided {@link Iterable} is null or empty.
     * @param value The {@link Iterable} to check.
     * @param <T> The type of values contained by the {@link Iterable}.
     */
    public static <T> boolean isNullOrEmpty(Iterable<T> value)
    {
        return value == null || !value.any();
    }

    /**
     * Get the String representation of the provided Iterable.
     * @return The String representation of the provided Iterable.
     */
    static String toString(Iterable<?> iterable)
    {
        return Iterable.toString(iterable, '[', ']');
    }

    /**
     * Get the String representation of the provided Iterable.
     * @return The String representation of the provided Iterable.
     */
    static String toString(Iterable<?> iterable, char startCharacter, char endCharacter)
    {
        final StringBuilder builder = new StringBuilder();
        if (iterable == null)
        {
            builder.append("null");
        }
        else
        {
            builder.append(startCharacter);

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

            builder.append(endCharacter);
        }
        return builder.toString();
    }

    /**
     * Order the values in the provided Iterable. This will create a copy of the provided Iterable
     * and will leave the original Iterable unchanged.
     * @param values The values to order.
     * @param <T> The type of values to order.
     * @return The ordered Iterable.
     */
    static <T extends Comparable<T>, U extends T> Iterable<U> order(Iterable<U> values)
    {
        PreCondition.assertNotNull(values, "values");

        return values.order(Comparer::lessThan);
    }

    /**
     * Order the values in this Iterable. This will create a copy of this Iterable and will leave
     * the original Iterable unchanged.
     * @param lessThan The function to use to compare two values.
     * @return The ordered Iterable.
     */
    public default Iterable<T> order(Function2<T,T,Boolean> lessThan)
    {
        PreCondition.assertNotNull(lessThan, "lessThan");

        return this.toList().sort(lessThan);
    }

    /**
     * Traverse a provided space starting with the provided value and using the getNextValues
     * function to find all reachable values.
     * @param startValue The start value.
     * @param getNextValues The function to use to get the next values create a given value.
     * @param <T> The type of values used in this function.
     * @return All of the values that are reachable.
     */
    static <T> Iterable<T> traverse(T startValue, Function1<T,Iterable<T>> getNextValues)
    {
        return Iterable.traverse(Iterable.create(startValue), getNextValues);
    }

    /**
     * Traverse a provided space starting with the provided value and using the getNextValues
     * function to find all reachable values.
     * @param startValues The start values.
     * @param getNextValues The function to use to get the next values create a given value.
     * @param <T> The type of values used in this function.
     * @return All of the values that are reachable.
     */
    static <T> Iterable<T> traverse(Iterable<T> startValues, Function1<T,Iterable<T>> getNextValues)
    {
        PreCondition.assertNotNull(startValues, "startValues");
        PreCondition.assertNotNull(getNextValues, "getNextValues");

        final List<T> result = List.create();
        final Stack<T> toVisit = Stack.create();
        toVisit.pushAll(startValues);
        while (toVisit.any())
        {
            final T value = toVisit.pop().await();

            result.add(value);

            final Iterable<T> nextValues = getNextValues.run(value);
            for (final T nextValue : nextValues)
            {
                if (!result.contains(nextValue) && !toVisit.contains(nextValue))
                {
                    toVisit.push(nextValue);
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
