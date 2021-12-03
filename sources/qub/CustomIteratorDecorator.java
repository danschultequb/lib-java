package qub;

public class CustomIteratorDecorator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private Function1<Iterator<T>,Boolean> hasStartedFunction;
    private Function1<Iterator<T>,Boolean> hasCurrentFunction;
    private Function1<Iterator<T>,T> getCurrentFunction;
    private Function1<Iterator<T>,Boolean> nextFunction;

    private CustomIteratorDecorator(Iterator<T> innerIterator)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");

        this.innerIterator = innerIterator;
        this.hasStartedFunction = Iterator::hasStarted;
        this.hasCurrentFunction = Iterator::hasCurrent;
        this.getCurrentFunction = Iterator::getCurrent;
        this.nextFunction = Iterator::next;
    }

    public static <T> CustomIteratorDecorator<T> create(Iterator<T> innerIterator)
    {
        return new CustomIteratorDecorator<>(innerIterator);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStartedFunction.run(this.innerIterator);
    }

    /**
     * Set the function that will be invoked when hasStarted() is called.
     * @param hasStartedFunction The function that will be invoked when hasStarted() is called.
     * @return This object for method chaining.
     */
    public CustomIteratorDecorator<T> setHasStartedFunction(Function1<Iterator<T>,Boolean> hasStartedFunction)
    {
        PreCondition.assertNotNull(hasStartedFunction, "hasStartedFunction");

        this.hasStartedFunction = hasStartedFunction;

        return this;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasCurrentFunction.run(this.innerIterator);
    }

    /**
     * Set the function that will be invoked when hasCurrent() is called.
     * @param hasCurrentFunction The function that will be invoked when hasCurrent() is called.
     * @return This object for method chaining.
     */
    public CustomIteratorDecorator<T> setHasCurrentFunction(Function1<Iterator<T>,Boolean> hasCurrentFunction)
    {
        PreCondition.assertNotNull(hasCurrentFunction, "hasStartedFunction");

        this.hasStartedFunction = hasCurrentFunction;

        return this;
    }

    @Override
    public T getCurrent()
    {
        return this.getCurrentFunction.run(this.innerIterator);
    }

    /**
     * Set the function that will be invoked when getCurrent() is called.
     * @param getCurrentFunction The function that will be invoked when getCurrent() is called.
     * @return This object for method chaining.
     */
    public CustomIteratorDecorator<T> setGetCurrentFunction(Function1<Iterator<T>,T> getCurrentFunction)
    {
        PreCondition.assertNotNull(getCurrentFunction, "getCurrentFunction");

        this.getCurrentFunction = getCurrentFunction;

        return this;
    }

    @Override
    public boolean next()
    {
        return this.nextFunction.run(this.innerIterator);
    }

    /**
     * Set the function that will be invoked when next() is called.
     * @param nextFunction The function that will be invoked when next() is called.
     * @return This object for method chaining.
     */
    public CustomIteratorDecorator<T> setNextFunction(Function1<Iterator<T>,Boolean> nextFunction)
    {
        PreCondition.assertNotNull(nextFunction, "nextFunction");

        this.nextFunction = nextFunction;

        return this;
    }
}
