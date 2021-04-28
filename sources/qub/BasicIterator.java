package qub;

public class BasicIterator<T> implements Iterator<T>, IteratorActions<T>
{
    private final Action2<IteratorActions<T>,Getter<T>> getNextValuesAction;
    private final Queue<T> valuesToReturn;
    private boolean started;

    private BasicIterator(Action2<IteratorActions<T>,Getter<T>> getNextValuesAction)
    {
        PreCondition.assertNotNull(getNextValuesAction, "getNextValuesAction");

        this.getNextValuesAction = getNextValuesAction;
        this.valuesToReturn = Queue.create();
    }

    /**
     * Create an Iterator that will invoke the provided action to determine what its next value(s)
     * should be.
     * @param getNextValuesAction The action that will be invoked to determine what the next
     *                            value(s) for the returned Iterator should be.
     * @param <T> The type of values returned by the Iterator.
     * @return An Iterator that will invoke the provided action to determine what its next value(s)
     * should be.
     */
    public static <T> BasicIterator<T> create(Action1<IteratorActions<T>> getNextValuesAction)
    {
        PreCondition.assertNotNull(getNextValuesAction, "getNextValuesAction");

        return BasicIterator.create((IteratorActions<T> actions, Getter<T> currentValue) -> { getNextValuesAction.run(actions); });
    }

    /**
     * Create an Iterator that will invoke the provided action to determine what its next value(s)
     * should be.
     * @param getNextValuesAction The action that will be invoked to determine what the next
     *                            value(s) for the returned Iterator should be.
     * @param <T> The type of values returned by the Iterator.
     * @return An Iterator that will invoke the provided action to determine what its next value(s)
     * should be.
     */
    public static <T> BasicIterator<T> create(Action2<IteratorActions<T>,Getter<T>> getNextValuesAction)
    {
        PreCondition.assertNotNull(getNextValuesAction, "getNextValuesAction");

        return new BasicIterator<>(getNextValuesAction);
    }

    @Override
    public boolean hasStarted()
    {
        return this.started;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted() && this.valuesToReturn.any();
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.valuesToReturn.peek().await();
    }

    @Override
    public boolean next()
    {
        boolean getNextValue = false;
        final Value<T> currentValue = Value.create();
        if (!this.hasStarted())
        {
            this.started = true;
            getNextValue = true;
        }
        else if (this.valuesToReturn.any())
        {
            currentValue.set(this.valuesToReturn.dequeue().await());
            getNextValue = !this.valuesToReturn.any();
        }

        if (getNextValue)
        {
            this.getNextValuesAction.run(this, currentValue);
        }

        return this.hasCurrent();
    }

    @Override
    public void returnValues(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.valuesToReturn.enqueueAll(values);
    }
}
