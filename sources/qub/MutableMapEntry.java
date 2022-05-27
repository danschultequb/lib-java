package qub;

/**
 * A map entry that can change its value.
 * @param <TKey> The type of key stored in this MutableMapEntry.
 * @param <TValue> The type of value stored in this MutableMapEntry.
 */
public class MutableMapEntry<TKey,TValue> implements MapEntry<TKey,TValue>
{
    private final TKey key;
    private TValue value;

    /**
     * Create a new MutableMapEntry with the provided key and value.
     * @param key The key for the new MutableMapEntry.
     * @param value The value for the new MutableMapEntry.
     */
    public MutableMapEntry(TKey key, TValue value)
    {
        this.key = key;
        this.value = value;
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
     * Set the value for this MutableMapEntry.
     * @param value The new value for this MutableMapEntry.
     */
    public void setValue(TValue value)
    {
        this.value = value;
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
