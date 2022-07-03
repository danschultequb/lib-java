package qub;

/**
 * A type that can iterate over a collection of values.
 * @param <T> The type of value that the {@link Iterator} returns.
 */
public interface Iterator<T> extends java.lang.Iterable<T>
{
    /**
     * Create an {@link Iterator} with no values.
     * @param <T> The type of values to iterate over.
     */
    public static <T> Iterator<T> create()
    {
        return EmptyIterator.create();
    }

    /**
     * Create an {@link Iterator} for the provided values.
     * @param values The values to iterate over.
     */
    public static Iterator<Byte> createFromBytes(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return ByteArrayIterator.create(values);
    }

    /**
     * Create an {@link Iterator} for the provided values.
     * @param values The values to iterate over.
     * @PreCondition values != null
     * @PreCondition 0 <= startIndex && startIndex < values.length
     * @PreCondition 0 <= length && length < values.length - startIndex
     * @PostCondition result != null
     * @PostCondition result.hasStarted() == false
     */
    public static Iterator<Byte> create(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return ByteArrayIterator.create(values, startIndex, length);
    }

    /**
     * Create an {@link Iterator} for the provided values.
     * @param values The values to iterate over.
     * @PreCondition values != null
     */
    public static Iterator<Character> create(char[] values)
    {
        PreCondition.assertNotNull(values, "values");

        return new CharacterArrayIterator(values);
    }

    /**
     * Create an {@link Iterator} for the provided values.
     * @param values The values to iterate over.
     */
    public static Iterator<Integer> create(int[] values)
    {
        return IntegerArrayIterator.create(values);
    }

    /**
     * Create an {@link Iterator} for the provided values.
     * @param values The values to iterate over.
     * @param <T> The type of the values.
     */
    @SafeVarargs
    public static <T> Iterator<T> create(T... values)
    {
        return Iterable.create(values).iterate();
    }

    /**
     * Create an {@link Iterator} that will invoke the provided {@link Action1} to determine what
     * its next value(s) should be.
     * @param getNextValuesAction The {@link Action1} that will be invoked to determine what the
     *                            next value(s) for the returned {@link Iterator} should be.
     * @param <T> The type of values returned by the {@link Iterator}.
     * @PreCondition getNextValuesAction != null
     */
    public static <T> Iterator<T> create(Action1<IteratorActions<T>> getNextValuesAction)
    {
        PreCondition.assertNotNull(getNextValuesAction, "getNextValuesAction");

        return BasicIterator.create(getNextValuesAction);
    }

    /**
     * Create an {@link Iterator} that will invoke the provided {@link Action2} to determine what
     * its next value(s) should be.
     * @param getNextValuesAction The {@link Action2} that will be invoked to determine what the
     *                            next value(s) for the returned {@link Iterator} should be.
     * @param <T> The type of values returned by the {@link Iterator}.
     */
    public static <T> Iterator<T> create(Action2<IteratorActions<T>,Getter<T>> getNextValuesAction)
    {
        PreCondition.assertNotNull(getNextValuesAction, "getNextValuesAction");

        return BasicIterator.create(getNextValuesAction);
    }

    /**
     * Advance to this {@link Iterator}'s first value if it hasn't started yet. If it has already
     * started, do nothing.
     * @return This {@link Iterator} for method chaining.
     */
    public default Iterator<T> start()
    {
        if (!this.hasStarted())
        {
            this.next();
        }
        return this;
    }

    /**
     * Whether this {@link Iterator} has begun iterating over its values.
     */
    public boolean hasStarted();

    /**
     * Whether this {@link Iterator} has a current value.
     */
    public boolean hasCurrent();

    /**
     * Get the current value that this {@link Iterator} is pointing at.
     */
    public T getCurrent();

    /**
     * Advance this {@link Iterator} to its next value and return whether a new value was found.
     */
    public boolean next();

    /**
     * Return the current value for this {@link Iterator} and advance to the next value.
     */
    public default T takeCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        final T result = this.getCurrent();
        this.next();
        return result;
    }

    /**
     * Get whether this {@link Iterator} contains any values. This function may start this
     * {@link Iterator} if it hadn't started iterating yet, but this function can be called multiple
     * times without consuming any of the values in this {@link Iterator}.
     */
    public default boolean any()
    {
        return this.start().hasCurrent();
    }

    /**
     * Get the number of values that are in this {@link Iterator}. This will iterate through all the
     * values in this {@link Iterator}. Use this method only if you care how many values are in the
     * {@link Iterator}, not what the values actually are.
     */
    public default int getCount()
    {
        int result = this.hasCurrent() ? 1 : 0;
        while (this.next())
        {
            ++result;
        }
        return result;
    }

    /**
     * Get the first value in this {@link Iterator}. This may advance the {@link Iterator} once if
     * it hasn't been started yet.
     * @exception EmptyException if the {@link Iterator} is empty.
     */
    public default Result<T> first()
    {
        return Result.create(() ->
        {
            if (!this.start().hasCurrent())
            {
                throw new EmptyException();
            }
            return this.getCurrent();
        });
    }

    /**
     * Get the first value in this {@link Iterator} that matches the provided condition.
     * @param condition The condition to run against each of the values in this {@link Iterator}.
     * @exception NotFoundException if no matching value is found.
     */
    public default Result<T> first(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return Result.create(() ->
        {
            T result = null;
            boolean foundResult = false;

            for (final T value : this)
            {
                if (condition.run(value))
                {
                    result = value;
                    foundResult = true;
                    break;
                }
            }

            if (!foundResult)
            {
                throw new NotFoundException("Could not find a value in this " + Types.getTypeName(Iterator.class) + " that matches the provided condition.");
            }

            return result;
        });
    }

    /**
     * Get the last value in this {@link Iterator}.
     * @exception EmptyException if the {@link Iterator} is empty.
     */
    public default Result<T> last()
    {
        return Result.create(() ->
        {
            T result = null;
            boolean foundResult = false;

            for (final T value : this)
            {
                result = value;
                foundResult = true;
            }

            if (!foundResult)
            {
                throw new EmptyException();
            }

            return result;
        });
    }

    /**
     * Get the last value in this {@link Iterator} that matches the provided condition.
     * @param condition The condition to run against each of the values in this {@link Iterator}.
     * @exception NotFoundException if no matching value is found.
     */
    public default Result<T> last(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return Result.create(() ->
        {
            T result = null;
            boolean foundResult = false;

            for (final T value : this)
            {
                if (condition.run(value))
                {
                    result = value;
                    foundResult = true;
                }
            }

            if (!foundResult)
            {
                throw new NotFoundException("Could not find a value in this " + Types.getTypeName(Iterator.class) + " that matches the provided condition.");
            }

            return result;
        });
    }

    /**
     * Get whether this {@link Iterator} contains the provided value using {@link Comparer}.equal()
     * to compare the values.
     * @param value The value to look for in this {@link Iterator}.
     */
    public default Result<Boolean> contains(T value)
    {
        return this.contains((T iteratorValue) -> Comparer.equal(iteratorValue, value));
    }

    /**
     * Get whether this {@link Iterator} contains a value that matches the provided condition.
     * @param condition The condition to run against each of the values in this {@link Iterator}.
     */
    public default Result<Boolean> contains(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return this.first(condition)
            .then(() -> true)
            .catchError(NotFoundException.class, () -> false);
    }

    /**
     * Create a new {@link Iterator} that will iterate over no more than the provided number of
     * values from this {@link Iterator}.
     * @param toTake The number of values to take from this {@link Iterator}.
     */
    public default Iterator<T> take(int toTake)
    {
        return TakeIterator.create(this, toTake);
    }

    /**
     * Create a new {@link Iterator} that will take and return elements in this {@link Iterator}
     * until it finds an element that matches the provided condition.
     * @param condition The condition to run against the values in this {@link Iterator}.
     */
    public default Iterator<T> takeUntil(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return this.takeWhile(Functions.not(condition));
    }

    /**
     * Create a new {@link Iterator} that will take and return elements in this {@link Iterator}
     * until it finds an element that makes the provided condition false.
     * @param condition The condition to run against the values in this {@link Iterator}.
     */
    public default Iterator<T> takeWhile(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return TakeWhileIterator.create(this, condition);
    }

    /**
     * Create a new {@link Iterator} that will skip over the first toSkip number of elements in this
     * {@link Iterator} and then iterate over the remaining elements.
     * @param toSkip The number of elements to skip.
     */
    public default Iterator<T> skip(int toSkip)
    {
        return SkipIterator.create(this, toSkip);
    }

    /**
     * Create a new {@link Iterator} that will skip over the elements in this {@link Iterator} until
     * it finds an element that makes the provided condition true.
     * @param condition The condition to run against the values in this {@link Iterator}.
     */
    public default Iterator<T> skipUntil(Function1<T,Boolean> condition)
    {
        return SkipUntilIterator.create(this, condition);
    }

    /**
     * Create a new {@link Iterator} that only returns the values from this {@link Iterator} that
     * satisfy the given condition.
     * @param condition The condition values must satisfy to be returned from the created
     * {@link Iterator}.
     */
    public default Iterator<T> where(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return WhereIterator.create(this, condition);
    }

    /**
     * Convert this {@link Iterator} into an {@link Iterator> that returns values of type {@link U}
     * instead of type {@link T}.
     * @param conversion The {@link Function1} to use to convert values of type {@link T} to type
     * {@link U}.
     * @param <U> The type to convert values of type {@link T} to.
     */
    public default <U> Iterator<U> map(Function1<T,U> conversion)
    {
        return MapIterator.create(this, conversion);
    }

    /**
     * Convert this {@link Iterator} into an {@link Iterator} that only returns values that are of
     * type or sub-classes of type {@link U}.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     */
    public default <U> Iterator<U> instanceOf(Class<U> type)
    {
        return InstanceOfIterator.create(this, type);
    }

    /**
     * Create a {@link java.util.Iterator} that will iterate over this {@link Iterator}.
     */
    public default java.util.Iterator<T> iterator()
    {
        return IteratorToJavaIteratorAdapter.create(this);
    }

    /**
     * Create an {@link Array} from the values in this {@link Iterator}.
     */
    public default Array<T> toArray()
    {
        return Array.create(this);
    }

    /**
     * Create a {@link List} from the values in this {@link Iterator}.
     */
    public default List<T> toList()
    {
        return List.create(this);
    }

    /**
     * Create a {@link Set} from the values in this {@link Iterator}.
     */
    public default Set<T> toSet()
    {
        return Set.create(this);
    }

    /**
     * Create a {@link MutableMap} from the values in this {@link Iterator}.
     * @param getKey The {@link Function1} that will select the key from a value.
     * @param <K> The type of value that will serve as the keys of the {@link MutableMap}.
     * @param <V> The type of value that will serve as the values of the {@link MutableMap}.
     */
    public default <K,V> MutableMap<K,V> toMap(Function1<T,K> getKey, Function1<T,V> getValue)
    {
        return Map.create(this, getKey, getValue);
    }

    /**
     * Create a {@link CustomIterator} that will wrap around this {@link Iterator}.
     */
    public default CustomIterator<T> customize()
    {
        return CustomIterator.create(this);
    }

    /**
     * Create a {@link CustomIterator} that will wrap around this {@link Iterator}.
     * @param nextFunction The {@link Function1} that will be called when the returned
     *                     {@link CustomIterator}'s next() method is called.
     */
    public default CustomIterator<T> customize(Function1<Iterator<T>,Boolean> nextFunction)
    {
        return this.customize().setNextFunction(nextFunction);
    }

    /**
     * Get an {@link Iterator} that will invoke the provided {@link Action0} whenever a new value is
     * iterated to.
     * @param onValueAction The {@link Action0} to run when a new value is iterated to.
     */
    public default OnValueIterator<T> onValue(Action0 onValueAction)
    {
        return OnValueIterator.create(this, onValueAction);
    }

    /**
     * Get an {@link Iterator} that will invoke the provided {@link Action1} whenever a new value is
     * iterated to.
     * @param onValueAction The {@link Action1} to run when a new value is iterated to.
     */
    public default OnValueIterator<T> onValue(Action1<T> onValueAction)
    {
        return OnValueIterator.create(this, onValueAction);
    }

    /**
     * Get an {@link CatchErrorIterator} that will ignore any errors that occur while iterating.
     */
    public default CatchErrorIterator<T,Throwable> catchError()
    {
        return CatchErrorIterator.create(this);
    }

    /**
     * Run the provided {@link Action0} on any errors that occur while iterating.
     * @param catchErrorAction The {@link Action0} to run if this {@link Iterator} has an error
     *                         while iterating.
     */
    public default CatchErrorIterator<T,Throwable> catchError(Action0 catchErrorAction)
    {
        return CatchErrorIterator.create(this, catchErrorAction);
    }

    /**
     * Run the provided {@link Action1} on any errors that occur while iterating.
     * @param catchErrorAction The {@link Action1} to run if this {@link Iterator} has an error
     *                         while iterating.
     */
    public default CatchErrorIterator<T,Throwable> catchError(Action1<Throwable> catchErrorAction)
    {
        return CatchErrorIterator.create(this, catchErrorAction);
    }

    /**
     * Ignore any errors of the provided type that occur while iterating.
     * @param errorType The type of error to catch.
     * @param <TError> The type of error to catch.
     */
    public default <TError extends Throwable> CatchErrorIterator<T,TError> catchError(Class<TError> errorType)
    {
        return CatchErrorIterator.create(this, errorType);
    }

    /**
     * Run the provided {@link Action0} on any errors of the provided type that occur while
     * iterating.
     * @param errorType The type of error to catch.
     * @param catchErrorAction The {@link Action0} to run if this {@link Iterator} has an error of
     *                         the provided type while iterating.
     * @param <TError> The type of error to catch.
     */
    public default <TError extends Throwable> CatchErrorIterator<T,TError> catchError(Class<TError> errorType, Action0 catchErrorAction)
    {
        return CatchErrorIterator.create(this, errorType, catchErrorAction);
    }

    /**
     * Run the provided {@link Action1} on any errors of the provided type that occur while
     * iterating.
     * @param errorType The type of error to catch.
     * @param catchErrorAction The {@link Action1} to run if this {@link Iterator} has an error of
     *                         the provided type while iterating.
     * @param <TError> The type of error to catch.
     */
    public default <TError extends Throwable> CatchErrorIterator<T,TError> catchError(Class<TError> errorType, Action1<TError> catchErrorAction)
    {
        return CatchErrorIterator.create(this, errorType, catchErrorAction);
    }

    /**
     * Run the provided {@link Action0} if this {@link Iterator} has an error while iterating.
     * @param onErrorAction The {@link Action0} to run if this {@link Iterator} has an error while
     *               iterating.
     */
    public default OnErrorIterator<T,Throwable> onError(Action0 onErrorAction)
    {
        return OnErrorIterator.create(this, onErrorAction);
    }

    /**
     * Run the provided {@link Action1} if this {@link Iterator} has an error while iterating.
     * @param onErrorAction The {@link Action1} to run if this {@link Iterator} has an error while
     *               iterating.
     */
    public default OnErrorIterator<T,Throwable> onError(Action1<Throwable> onErrorAction)
    {
        return OnErrorIterator.create(this, onErrorAction);
    }

    /**
     * Run the provided {@link Action0} if this {@link Iterator} has an error of the provided type
     * while iterating.
     * @param errorType The type of error to run the provided {@link Action0} for.
     * @param onErrorAction The {@link Action0} to run if this {@link Iterator} has an error of the
     *               provided type while iterating.
     */
    public default <TError extends Throwable> Iterator<T> onError(Class<TError> errorType, Action0 onErrorAction)
    {
        return OnErrorIterator.create(this, errorType, onErrorAction);
    }

    /**
     * Run the provided {@link Action1} if this {@link Iterator} has an error of the provided type
     * while iterating.
     * @param errorType The type of error to run the provided {@link Action1} for.
     * @param onErrorAction The {@link Action1} to run if this {@link Iterator} has an error of the
     *               provided type while iterating.
     */
    public default <TError extends Throwable> Iterator<T> onError(Class<TError> errorType, Action1<TError> onErrorAction)
    {
        return OnErrorIterator.create(this, errorType, onErrorAction);
    }

    /**
     * If this {@link Iterator} has an error while iterating, catch it and convert it to the error
     * returned by the provided {@link Function0}.
     * @param convertErrorFunction The {@link Function0} that will return the new error.
     */
    public default ConvertErrorIterator<T,Throwable> convertError(Function0<? extends Throwable> convertErrorFunction)
    {
        return ConvertErrorIterator.create(this, convertErrorFunction);
    }

    /**
     * If this {@link Iterator} has an error while iterating, catch it and convert it to the error
     * returned by the provided {@link Function1}.
     * @param convertErrorFunction The {@link Function1} that will return the new error.
     */
    public default ConvertErrorIterator<T,Throwable> convertError(Function1<Throwable,? extends Throwable> convertErrorFunction)
    {
        return ConvertErrorIterator.create(this, convertErrorFunction);
    }

    /**
     * If this {@link Iterator} has an error of the provided type while iterating, catch it and
     * convert it to the error returned by the provided {@link Function0}.
     * @param errorType The type of error to convert.
     * @param convertErrorFunction The {@link Function0} that will return the new error.
     */
    public default <TError extends Throwable> ConvertErrorIterator<T,TError> convertError(Class<TError> errorType, Function0<? extends Throwable> convertErrorFunction)
    {
        return ConvertErrorIterator.create(this, errorType, convertErrorFunction);
    }

    /**
     * If this {@link Iterator} has an error of the provided type while iterating, catch it and
     * convert it to the error returned by the provided {@link Function1}.
     * @param errorType The type of error to convert.
     * @param convertErrorFunction The {@link Function1} that will return the new error.
     */
    public default <TError extends Throwable> ConvertErrorIterator<T,TError> convertError(Class<TError> errorType, Function1<TError,? extends Throwable> convertErrorFunction)
    {
        return ConvertErrorIterator.create(this, errorType, convertErrorFunction);
    }

    /**
     * Iterate through each of the values in this {@link Iterator}.
     */
    public default void await()
    {
        while (this.next())
        {
        }
    }

    /**
     * Get whether the provided {@link Iterator} is null or empty.
     * @param iterator The {@link Iterator} to check.
     */
    public static boolean isNullOrEmpty(Iterator<?> iterator)
    {
        return iterator == null || !iterator.any();
    }
}