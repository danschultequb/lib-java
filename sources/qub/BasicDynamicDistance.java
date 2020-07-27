package qub;

/**
 * A basic implementation of a DynamicDistance.
 */
public class BasicDynamicDistance implements DynamicDistance
{
    private final RunnableEvent0 changed;
    private final Function0<Distance> innerValueGetter;
    private final Disposable innerValueChangedSubscription;
    private Distance currentValue;

    private BasicDynamicDistance(Function0<Distance> valueGetter, Function1<Action0,Disposable> valueChangedSubscriptionFunction)
    {
        PreCondition.assertNotNull(valueGetter, "valueGetter");
        PreCondition.assertNotNull(valueChangedSubscriptionFunction, "valueChangedSubscriptionFunction");

        this.changed = Event0.create();
        this.innerValueGetter = valueGetter;
        this.innerValueChangedSubscription = valueChangedSubscriptionFunction.run(this::onInnerValueChanged);
        this.currentValue = this.innerValueGetter.run();
    }

    public static BasicDynamicDistance create(Function0<Distance> valueGetter, Function1<Action0,Disposable> valueChangedSubscriptionFunction)
    {
        return new BasicDynamicDistance(valueGetter, valueChangedSubscriptionFunction);
    }

    @Override
    public Distance get()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.currentValue;
    }

    @Override
    public Disposable onChanged(Action0 callback)
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.changed.subscribe(callback);
    }

    @Override
    public boolean isDisposed()
    {
        return this.innerValueChangedSubscription.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.innerValueChangedSubscription.dispose();
    }

    private void onInnerValueChanged()
    {
        final Distance newValue = this.innerValueGetter.run();
        if (!Comparer.equal(this.currentValue, newValue))
        {
            this.currentValue = newValue;
            this.changed.run();
        }
    }
}
