package qub;

/**
 * A {@link MapEntry} that can change its value.
 * @param <TKey> The type of key stored in this {@link MutableMapEntry}.
 * @param <TValue> The type of value stored in this {@link MutableMapEntry}.
 */
public class MutableMapEntry<TKey,TValue> implements MapEntry<TKey,TValue>
{
    private final TKey key;
    private TValue value;

    protected MutableMapEntry(TKey key, TValue value)
    {
        this.key = key;
        this.value = value;
    }

    /**
     * Create a new {@link MutableMapEntry} with the provided key and value.
     * @param key The key for the new {@link MutableMapEntry}.
     * @param value The value for the new {@link MutableMapEntry}.
     */
    public static <TKey,TValue> MutableMapEntry<TKey,TValue> create(TKey key, TValue value)
    {
        return new MutableMapEntry<>(key, value);
    }

    @Override
    public TKey getKey()
    {
        return this.key;
    }

    @Override
    public TValue getValue()
    {
        return this.value;
    }

    /**
     * Set the value for this {@link MutableMapEntry}.
     * @param value The new value for this {@link MutableMapEntry}.
     */
    public MutableMapEntry<TKey,TValue> setValue(TValue value)
    {
        this.value = value;
        return this;
    }

    @Override
    public String toString()
    {
        return MapEntry.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return MapEntry.equals(this, rhs);
    }
}
