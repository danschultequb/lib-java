package qub;

public class CustomIterator<T> extends IteratorWrapper<T>
{
    private Function1<Iterator<T>,Boolean> hasStartedFunction;
    private Function1<Iterator<T>,Boolean> hasCurrentFunction;
    private Function1<Iterator<T>,T> getCurrentFunction;
    private Function1<Iterator<T>,Boolean> nextFunction;

    private CustomIterator(Iterator<T> innerIterator)
    {
        super(innerIterator);

        this.hasStartedFunction = Iterator::hasStarted;
        this.hasCurrentFunction = Iterator::hasCurrent;
        this.getCurrentFunction = Iterator::getCurrent;
        this.nextFunction = Iterator::next;
    }

    public static <T> CustomIterator<T> create(Iterator<T> innerIterator)
    {
        return new CustomIterator<>(innerIterator);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStartedFunction.run(this.getInnerIterator());
    }

    /**
     * Set the {@link Function1} that will be invoked when hasStarted() is called.
     * @param hasStartedFunction The {@link Function1} that will be invoked when hasStarted() is
     *                           called.
     * @return This object for method chaining.
     */
    public CustomIterator<T> setHasStartedFunction(Function1<Iterator<T>,Boolean> hasStartedFunction)
    {
        PreCondition.assertNotNull(hasStartedFunction, "hasStartedFunction");

        this.hasStartedFunction = hasStartedFunction;

        return this;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasCurrentFunction.run(this.getInnerIterator());
    }

    /**
     * Set the {@link Function1} that will be invoked when hasCurrent() is called.
     * @param hasCurrentFunction The {@link Function1} that will be invoked when hasCurrent() is
     *                           called.
     * @return This object for method chaining.
     */
    public CustomIterator<T> setHasCurrentFunction(Function1<Iterator<T>,Boolean> hasCurrentFunction)
    {
        PreCondition.assertNotNull(hasCurrentFunction, "hasStartedFunction");

        this.hasStartedFunction = hasCurrentFunction;

        return this;
    }

    @Override
    public T getCurrent()
    {
        return this.getCurrentFunction.run(this.getInnerIterator());
    }

    /**
     * Set the {@link Function1} that will be invoked when getCurrent() is called.
     * @param getCurrentFunction The {@link Function1} that will be invoked when getCurrent() is
     *                           called.
     * @return This object for method chaining.
     */
    public CustomIterator<T> setGetCurrentFunction(Function1<Iterator<T>,T> getCurrentFunction)
    {
        PreCondition.assertNotNull(getCurrentFunction, "getCurrentFunction");

        this.getCurrentFunction = getCurrentFunction;

        return this;
    }

    @Override
    public boolean next()
    {
        return this.nextFunction.run(this.getInnerIterator());
    }

    /**
     * Set the {@link Function1} that will be invoked when next() is called.
     * @param nextFunction The {@link Function1} that will be invoked when next() is called.
     * @return This object for method chaining.
     */
    public CustomIterator<T> setNextFunction(Function1<Iterator<T>,Boolean> nextFunction)
    {
        PreCondition.assertNotNull(nextFunction, "nextFunction");

        this.nextFunction = nextFunction;

        return this;
    }
}
