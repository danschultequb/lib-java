package qub;

public class SaveableValue<T> implements Value<T>, Saveable
{
    private final Value<T> value;

    private SaveableValue(Value<T> value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = value;
    }

    public static <T> SaveableValue<T> create()
    {
        return SaveableValue.create(Value.create());
    }

    public static <T> SaveableValue<T> create(T initialValue)
    {
        return SaveableValue.create(Value.create(initialValue));
    }

    public static <T> SaveableValue<T> create(Value<T> value)
    {
        return new SaveableValue<>(value);
    }

    @Override
    public SaveableValue<T> clear()
    {
        this.value.clear();
        return this;
    }

    @Override
    public SaveableValue<T> set(T value)
    {
        this.value.set(value);
        return this;
    }

    @Override
    public boolean hasValue()
    {
        return this.value.hasValue();
    }

    @Override
    public T get()
    {
        return this.value.get();
    }

    @Override
    public Save save()
    {
        final Action0 onRestore;
        if (this.hasValue())
        {
            final T currentValue = this.get();
            onRestore = () -> this.set(currentValue);
        }
        else
        {
            onRestore = this::clear;
        }
        final Save result = Save.create(onRestore);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return this.value.equals(rhs);
    }
}
