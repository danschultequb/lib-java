package qub;

/**
 * The Iterator interface defines a type that can synchronously iterate over a collection of values.
 * @param <T> The type of value that the Iterator returns.
 */
public interface Iterator<T> extends java.lang.Iterable<T>
{
    /**
     * Advance to this Iterator's first value if the Iterator hasn't started yet. If it has already started, then do
     * nothing.
     */
    default Iterator<T> start()
    {
        if (!this.hasStarted())
        {
            this.next();
        }
        return this;
    }

    /**
     * Whether or not this Iterator has begun iterating over its values.
     * @return Whether or not this Iterator has begun iterating over its values.
     */
    boolean hasStarted();

    /**
     * Whether or not this Iterator has a current value.
     * @return Whether or not this Iterator has a current value.
     */
    boolean hasCurrent();

    /**
     * Get the current value that this Iterator is pointing at.
     * @return The current value that this Iterator is pointing at.
     */
    T getCurrent();

    /**
     * Get the next value for this Iterator. Returns whether or not a new value was found.
     * @return Whether or not a new value was found.
     */
    boolean next();

    /**
     * Return the current value for this Iterator and advance this Iterator to the next value.
     * @return The current value for this Iterator.
     */
    default T takeCurrent()
    {
        final T current = getCurrent();
        next();
        return current;
    }

    /**
     * Get whether or not this Iterator contains any values. This function may move this Iterator
     * forward one position, but it can be called multiple times without consuming any of the
     * values in this Iterator.
     * @return Whether or not this Iterator contains any values.
     */
    default boolean any()
    {
        return this.start().hasCurrent();
    }

    /**
     * Get the number of values that are in this Iterator. This will iterate through all of the
     * values in this Iterator. Use this method only if you care how many values are in the
     * Iterator, not what the values actually are.
     * @return The number of values that are in this Iterator.
     */
    default int getCount()
    {
        int result = this.hasCurrent() ? 1 : 0;
        while (this.next()) {
            ++result;
        }
        return result;
    }

    /**
     * Get the first value in this Iterator. This may advance the Iterator once.
     * @return The first value of this Iterator, or null if this Iterator has no (more) values.
     */
    default T first()
    {
        this.start();
        return this.hasCurrent() ? this.getCurrent() : null;
    }

    /**
     * Get the first value in this Iterator that isMatch the provided condition.
     * @param condition The condition to run against each of the values in this Iterator.
     * @return The first value of this Iterator that isMatch the provided condition, or null if this
     * Iterator has no values that match the condition.
     */
    default T first(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        T result = null;
        boolean foundResult = false;

        T current;
        if (this.hasCurrent())
        {
            current = this.getCurrent();
            if (condition.run(current))
            {
                result = current;
                foundResult = true;
            }
        }

        if (!foundResult)
        {
            while (this.next())
            {
                current = this.getCurrent();
                if (condition.run(current))
                {
                    result = current;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Get the last value in this Iterator. This will iterate through all of the values in this
     * Iterator.
     * @return The last value of this Iterator, or null if this Iterator has no (more) values.
     */
    default T last()
    {
        T result = null;

        if (this.hasCurrent())
        {
            result = getCurrent();
        }

        while (next())
        {
            result = getCurrent();
        }

        return result;
    }

    /**
     * Get the last value in this Iterator that isMatch the provided condition.
     * @param condition The condition to run against each of the values in this Iterator.
     * @return The last value of this Iterator that isMatch the provided condition, or null if this
     * Iterator has no values that match the condition.
     */
    default T last(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        T result = null;

        T current;
        if (hasCurrent())
        {
            current = getCurrent();
            if (condition.run(current))
            {
                result = getCurrent();
            }
        }

        while (next())
        {
            current = getCurrent();
            if (condition.run(current))
            {
                result = current;
            }
        }

        return result;
    }

    /**
     * Get whether or not this Iterator contains the provided value using the standard equals()
     * method to compare values.
     * @param value The value to look for in this Iterator.
     * @return Whether or not this Iterator contains the provided value.
     */
    default boolean contains(T value)
    {
        return contains((T iteratorValue) -> Comparer.equal(iteratorValue, value));
    }

    /**
     * Get whether or not this Iterator contains a value that isMatch the provided condition.
     * @param condition The condition to check against the values in this Iterator.
     * @return Whether or not this Iterator contains a value that isMatch the provided condition.
     */
    default boolean contains(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        boolean result = false;

        if (hasCurrent())
        {
            result = condition.run(getCurrent());
        }

        while (!result && next())
        {
            result = condition.run(getCurrent());
        }

        return result;
    }

    /**
     * Create a new Iterator that will iterate over no more than the provided number of values create
     * this Iterator.
     * @param toTake The number of values to take create this Iterator.
     * @return A new Iterator that will iterate over no more than the provided number of values create
     * this Iterator.
     */
    default Iterator<T> take(int toTake)
    {
        return new TakeIterator<>(this, toTake);
    }

    /**
     * Create a new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     */
    default Iterator<T> skip(int toSkip)
    {
        return new SkipIterator<>(this, toSkip);
    }

    /**
     * Create a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true. The returned Iterator will start at the
     * element after the element that made the condition true.
     * @param condition The condition.
     * @return a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true.
     */
    default Iterator<T> skipUntil(Function1<T,Boolean> condition)
    {
        return new SkipUntilIterator<>(this, condition);
    }

    /**
     * Create a new Iterator that only returns the values create this Iterator that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned create the created Iterator.
     * @return An Iterator that only returns the values create this Iterator that satisfy the given
     * condition.
     */
    default Iterator<T> where(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return new WhereIterator<>(this, condition);
    }

    /**
     * Convert this Iterator into an Iterator that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Iterator that returns values of type U instead of type T.
     */
    default <U> Iterator<U> map(Function1<T,U> conversion)
    {
        return new MapIterator<>(this, conversion);
    }

    /**
     * Convert this Iterator into an Iterator that only returns the values in this Iterator that are
     * of type or sub-classes of type U.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     * @return An Iterator that only returns the values in this Iterator that are of type of
     * sub-classes of type U.
     */
    default <U> Iterator<U> instanceOf(Class<U> type)
    {
        return new InstanceOfIterator<>(this, type);
    }

    /**
     * Create a java.util.Iterator that will iterate over this Iterator.
     * @return A java.util.Iterator that will iterate over this Iterator.
     */
    default java.util.Iterator<T> iterator()
    {
        return IteratorToJavaIteratorAdapter.create(this);
    }

    /**
     * Create an Array from the values in this Iterator.
     * @return An Array from the values in this Iterator.
     */
    default Array<T> toArray()
    {
        return Array.create(this);
    }

    /**
     * Create a List from the values in this Iterator.
     * @return A List from the values in this Iterator.
     */
    default List<T> toList()
    {
        return List.create(this);
    }

    /**
     * Create a Set from the values in this Iterator.
     * @return A Set from the values in this Iterator.
     */
    default Set<T> toSet()
    {
        return Set.create(this);
    }

    /**
     * Create a Map from the values in this Iterator.
     * @param getKey The function that will select the key from a value.
     * @param <K> The type of value that will serve as the keys of the map.
     * @param <V> The type of value that will serve as the values of the map.
     * @return A Map from the values in this Iterator.
     */
    default <K,V> MutableMap<K,V> toMap(Function1<T,K> getKey, Function1<T,V> getValue)
    {
        return Map.create(this, getKey, getValue);
    }

    /**
     * Create an empty iterator.
     * @param <T> The type of values to iterate over.
     * @return The empty iterator.
     */
    static <T> Iterator<T> empty()
    {
        return new EmptyIterator<>();
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static Iterator<Byte> createFromBytes(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new ByteArrayIterator(values);
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static Iterator<Byte> create(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return new ByteArrayIterator(values, startIndex, length);
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static Iterator<Character> create(char[] values)
    {
        return new CharacterArrayIterator(values);
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static Iterator<Integer> create(int[] values)
    {
        return new IntegerArrayIterator(values);
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @param <T> The type of the values.
     * @return The iterator that will iterate over the provided values.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> create(T... values)
    {
        return Iterable.create(values).iterate();
    }

    /**
     * Get whether or not the provided iterator is null or empty.
     * @param iterator The iterator to check.
     * @param <T> The type of values in the Iterator.
     * @return Whether or not the provided iterator is null or empty.
     */
    static <T> boolean isNullOrEmpty(Iterator<T> iterator)
    {
        return iterator == null || !iterator.any();
    }
}