package qub;

public class DynamicObjectValue<T> extends ObjectValue<T> implements DynamicValue<T>
{
    private final RunnableEvent2<T,T> changed;

    private DynamicObjectValue()
    {
        this.changed = Event2.create();
    }

    public static <T> DynamicObjectValue<T> create()
    {
        return new DynamicObjectValue<>();
    }

    public static <T> DynamicObjectValue<T> create(T initialValue)
    {
        return DynamicObjectValue.<T>create().set(initialValue);
    }

    @Override
    public DynamicObjectValue<T> set(T value)
    {
        final boolean hasValue = this.hasValue();
        final T previous = (hasValue ? this.get() : null);
        if (!hasValue || !Comparer.equal(value, previous))
        {
            super.set(value);
            this.changed.run(previous, value);
        }

        PostCondition.assertTrue(this.hasValue(), "this.hasValue()");

        return this;
    }

    @Override
    public DynamicObjectValue<T> clear()
    {
        if (this.hasValue())
        {
            final T previous = this.get();
            super.clear();
            this.changed.run(previous, null);
        }
        return this;
    }

    @Override
    public Disposable onChanged(Action2<T, T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.changed.subscribe(action);
    }
}
