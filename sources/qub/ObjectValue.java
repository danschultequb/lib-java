package qub;

public class ObjectValue<T> implements Value<T>
{
    private final java.util.concurrent.atomic.AtomicReference<T> value;
    private volatile boolean hasValue;

    public ObjectValue()
    {
        value = new java.util.concurrent.atomic.AtomicReference<>();
        hasValue = false;
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

    @Override
    public void set(T value)
    {
        this.value.set(value);
        hasValue = true;
    }

    public boolean hasValue()
    {
        return hasValue;
    }

    @Override
    public T get()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return value.get();
    }

    /**
     * Clear any value that may have been set.
     */
    public void clear()
    {
        this.value.set(null);
        hasValue = false;
    }
}
