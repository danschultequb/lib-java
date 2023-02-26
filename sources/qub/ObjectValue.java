package qub;

public class ObjectValue<T> implements Value<T>
{
    private final java.util.concurrent.atomic.AtomicReference<T> value;
    private volatile boolean hasValue;

    public ObjectValue()
    {
        this.value = new java.util.concurrent.atomic.AtomicReference<>();
        this.hasValue = false;
    }

    public ObjectValue(T value)
    {
        this.value = new java.util.concurrent.atomic.AtomicReference<>(value);
        hasValue = true;
    }

    public static <T> ObjectValue<T> create()
    {
        return new ObjectValue<>();
    }

    public static <T> ObjectValue<T> create(T value)
    {
        return new ObjectValue<>(value);
    }

    @Override
    public ObjectValue<T> set(T value)
    {
        this.value.set(value);
        this.hasValue = true;
        return this;
    }

    public boolean hasValue()
    {
        return this.hasValue;
    }

    @Override
    public T get()
    {
        PreCondition.assertTrue(this.hasValue(), "this.hasValue()");

        return this.value.get();
    }

    /**
     * Clear any value that may have been set.
     */
    public ObjectValue<T> clear()
    {
        this.value.set(null);
        this.hasValue = false;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        final boolean result;
        if (rhs instanceof Value)
        {
            result = this.equals((Value<T>)rhs);
        }
        else
        {
            result = this.hasValue() && Comparer.equal(this.get(), rhs);
        }
        return result;
    }

    public boolean equals(Value<T> rhs)
    {
        return rhs != null &&
            this.hasValue() == rhs.hasValue() &&
            (!hasValue() || Comparer.equal(this.get(), rhs.get()));
    }

    @Override
    public String toString()
    {
        return !this.hasValue() ? "no value" : Objects.toString(this.get());
    }
}
