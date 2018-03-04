package qub;

public abstract class IteratorBase<T> implements Iterator<T>
{
    @Override
    public void ensureHasStarted()
    {
        IteratorBase.ensureHasStarted(this);
    }

    @Override
    public T takeCurrent()
    {
        return IteratorBase.takeCurrent(this);
    }

    @Override
    public boolean any()
    {
        return IteratorBase.any(this);
    }

    @Override
    public int getCount()
    {
        return IteratorBase.getCount(this);
    }

    @Override
    public T first()
    {
        return IteratorBase.first(this);
    }

    @Override
    public T first(Function1<T, Boolean> condition)
    {
        return IteratorBase.first(this, condition);
    }

    @Override
    public T last()
    {
        return IteratorBase.last(this);
    }

    @Override
    public T last(Function1<T, Boolean> condition)
    {
        return IteratorBase.last(this, condition);
    }

    @Override
    public boolean contains(T value)
    {
        return IteratorBase.contains(this, value);
    }

    @Override
    public boolean contains(Function1<T, Boolean> condition)
    {
        return IteratorBase.contains(this, condition);
    }

    @Override
    public Iterator<T> take(int toTake)
    {
        return IteratorBase.take(this, toTake);
    }

    @Override
    public Iterator<T> skip(int toSkip)
    {
        return IteratorBase.skip(this, toSkip);
    }

    @Override
    public Iterator<T> skipUntil(Function1<T, Boolean> condition)
    {
        return IteratorBase.skipUntil(this, condition);
    }

    @Override
    public Iterator<T> where(Function1<T, Boolean> condition)
    {
        return IteratorBase.where(this, condition);
    }

    @Override
    public <U> Iterator<U> map(Function1<T, U> conversion)
    {
        return IteratorBase.map(this, conversion);
    }

    @Override
    public <U> Iterator<U> instanceOf(Class<U> type)
    {
        return IteratorBase.instanceOf(this, type);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return IteratorBase.iterator(this);
    }

    /**
     * Ensure that this Iterator has started. If it hasn't started, then it will be moved to the
     * next value.
     */
    public static <T> void ensureHasStarted(Iterator<T> iterator)
    {
        if (!iterator.hasStarted())
        {
            iterator.next();
        }
    }

    /**
     * Return the current value for this Iterator and advance this Iterator to the next value.
     * @return The current value for this Iterator.
     */
    public static <T> T takeCurrent(Iterator<T> iterator)
    {
        final T current = iterator.getCurrent();
        iterator.next();
        return current;
    }

    /**
     * Get whether or not this Iterator contains any values. This function may move this Iterator
     * forward one position, but it can be called multiple times without consuming any of the
     * values in this Iterator.
     * @return Whether or not this Iterator contains any values.
     */
    public static <T> boolean any(Iterator<T> iterator)
    {
        return iterator.hasCurrent() || iterator.next();
    }

    /**
     * Get the number of values that are in this Iterator. This will iterate through all of the
     * values in this Iterator. Use this method only if you care how many values are in the
     * Iterator, not what the values actually are.
     * @return The number of values that are in this Iterator.
     */
    public static <T> int getCount(Iterator<T> iterator)
    {
        int result = iterator.hasCurrent() ? 1 : 0;
        while (iterator.next()) {
            ++result;
        }
        return result;
    }

    /**
     * Get the first value in this Iterator. This may advance the Iterator once.
     * @return The first value of this Iterator, or null if this Iterator has no (more) values.
     */
    public static <T> T first(Iterator<T> iterator)
    {
        if (!iterator.hasStarted())
        {
            iterator.next();
        }
        return iterator.getCurrent();
    }

    /**
     * Get the first value in this Iterator that matches the provided condition.
     * @param condition The condition to run against each of the values in this Iterator.
     * @return The first value of this Iterator that matches the provided condition, or null if this
     * Iterator has no values that match the condition.
     */
    public static <T> T first(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            if (iterator.hasCurrent() && condition.run(iterator.getCurrent()))
            {
                result = iterator.getCurrent();
            }
            else
            {
                while (iterator.next())
                {
                    if (condition.run(iterator.getCurrent()))
                    {
                        result = iterator.getCurrent();
                        break;
                    }
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
    public static <T> T last(Iterator<T> iterator)
    {
        T result = null;

        if (iterator.hasCurrent())
        {
            result = iterator.getCurrent();
        }

        while (iterator.next())
        {
            result = iterator.getCurrent();
        }

        return result;
    }

    /**
     * Get the last value in this Iterator that matches the provided condition.
     * @param condition The condition to run against each of the values in this Iterator.
     * @return The last value of this Iterator that matches the provided condition, or null if this
     * Iterator has no values that match the condition.
     */
    public static <T> T last(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            if (iterator.hasCurrent() && condition.run(iterator.getCurrent()))
            {
                result = iterator.getCurrent();
            }

            while (iterator.next())
            {
                if (condition.run(iterator.getCurrent()))
                {
                    result = iterator.getCurrent();
                }
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
    public static <T> boolean contains(Iterator<T> iterator, final T value)
    {
        return iterator.contains(new Function1<T, Boolean>()
        {
            @Override
            public Boolean run(T iteratorValue)
            {
                return Comparer.equal(iteratorValue, value);
            }
        });
    }

    /**
     * Get whether or not this Iterator contains a value that matches the provided condition.
     * @param condition The condition to check against the values in this Iterator.
     * @return Whether or not this Iterator contains a value that matches the provided condition.
     */
    public static <T> boolean contains(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        boolean result = false;

        if (condition != null)
        {
            if (iterator.hasCurrent())
            {
                result = condition.run(iterator.getCurrent());
            }

            while (!result && iterator.next())
            {
                if (condition.run(iterator.getCurrent()))
                {
                    result = condition.run(iterator.getCurrent());
                }
            }
        }

        return result;
    }

    /**
     * Create a new Iterator that will iterate over no more than the provided number of values from
     * this Iterator.
     * @param toTake The number of values to take from this Iterator.
     * @return A new Iterator that will iterate over no more than the provided number of values from
     * this Iterator.
     */
    public static <T> Iterator<T> take(Iterator<T> iterator, int toTake)
    {
        return new TakeIterator<>(iterator, toTake);
    }

    /**
     * Create a new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     */
    public static <T> Iterator<T> skip(Iterator<T> iterator, int toSkip)
    {
        return new SkipIterator<>(iterator, toSkip);
    }

    /**
     * Create a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true. The returned Iterator will start at the
     * element after the element that made the condition true.
     * @param condition The condition.
     * @return a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true.
     */
    public static <T> Iterator<T> skipUntil(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        return new SkipUntilIterator<>(iterator, condition);
    }

    /**
     * Create a new Iterator that only returns the values from this Iterator that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned from the created Iterator.
     * @return An Iterator that only returns the values from this Iterator that satisfy the given
     * condition.
     */
    public static <T> Iterator<T> where(Iterator<T> iterator, Function1<T,Boolean> condition)
    {
        return condition == null ? iterator : new WhereIterator<>(iterator, condition);
    }

    /**
     * Convert this Iterator into an Iterator that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Iterator that returns values of type U instead of type T.
     */
    public static <T,U> Iterator<U> map(Iterator<T> iterator, Function1<T,U> conversion)
    {
        return new MapIterator<>(iterator, conversion);
    }

    /**
     * Convert this Iterator into an Iterator that only returns the values in this Iterator that are
     * of type or sub-classes of type U.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     * @return An Iterator that only returns the values in this Iterator that are of type of
     * sub-classes of type U.
     */
    public static <T,U> Iterator<U> instanceOf(Iterator<T> iterator, Class<U> type)
    {
        return new InstanceOfIterator<>(iterator, type);
    }

    /**
     * Create a java.util.Iterator that will iterate over this Iterator.
     * @return A java.util.Iterator that will iterate over this Iterator.
     */
    public static <T> java.util.Iterator<T> iterator(Iterator<T> iterator)
    {
        return new IteratorToJavaIteratorAdapter<>(iterator);
    }
}
