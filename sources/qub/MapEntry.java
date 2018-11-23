package qub;

/**
 * A single key/value pair within a Map.
 * @param <TKey> The type of the key.
 * @param <TValue> The type of the value.
 */
public interface MapEntry<TKey,TValue>
{
    /**
     * Get the key of this entry.
     * @return The key of this entry.
     */
    TKey getKey();

    /**
     * Get the value of this entry.
     * @return The value of this entry.
     */
    TValue getValue();

    static <TKey,TValue> String toString(MapEntry<TKey,TValue> entry)
    {
        PreCondition.assertNotNull(entry, "entry");

        return Objects.toString(entry.getKey()) + ":" + Objects.toString(entry.getValue());
    }

    @SuppressWarnings("unchecked")
    static <TKey,TValue> boolean equals(MapEntry<TKey,TValue> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof MapEntry && lhs.equals((MapEntry<TKey,TValue>)rhs);
    }

    /**
     * Get whether or not this MapEntry equals the provided MapEntry.
     * @param rhs The MapEntry to compare against this MapEntry.
     * @return Whether or not this MapEntry equals the provided MapEntry.
     */
    default boolean equals(MapEntry<TKey,TValue> rhs)
    {
        return rhs != null &&
            Comparer.equal(getKey(), rhs.getKey()) &&
            Comparer.equal(getValue(), rhs.getValue());
    }

    /**
     * Create a new MutableMapEntry with the provided key and value.
     * @param key The key for the new MutableMapEntry.
     * @param value The value for the new MutableMapEntry.
     * @param <TKey> The type of key stored in the new MutableMapEntry.
     * @param <TValue> The type of value stored in the new MutableMapEntry.
     * @return The new MutableMapEntry.
     */
    static <TKey,TValue> MutableMapEntry<TKey,TValue> create(TKey key, TValue value)
    {
        return new MutableMapEntry<>(key, value);
    }
}
