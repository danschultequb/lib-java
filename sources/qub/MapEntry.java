package qub;

/**
 * A single key/value pair within a {@link Map}.
 * @param <TKey> The type of the key.
 * @param <TValue> The type of the value.
 */
public interface MapEntry<TKey,TValue>
{
    /**
     * Create a new {@link MutableMapEntry} with the provided key and value.
     * @param key The key for the new {@link MutableMapEntry}.
     * @param value The value for the new {@link MutableMapEntry}.
     * @param <TKey> The type of key stored in the new {@link MutableMapEntry}.
     * @param <TValue> The type of value stored in the new {@link MutableMapEntry}.
     */
    public static <TKey,TValue> MutableMapEntry<TKey,TValue> create(TKey key, TValue value)
    {
        return MutableMapEntry.create(key, value);
    }

    /**
     * Get the key of this {@link MapEntry}.
     */
    TKey getKey();

    /**
     * Get the value of this {@link MapEntry}.
     */
    TValue getValue();

    /**
     * Get the {@link String} representation of the provided {@link MapEntry}.
     * @param entry The {@link MapEntry} to get the {@link String} representation of.
     */
    public static <TKey,TValue> String toString(MapEntry<TKey,TValue> entry)
    {
        PreCondition.assertNotNull(entry, "entry");

        return Objects.toString(entry.getKey()) + ":" + Objects.toString(entry.getValue());
    }

    /**
     * Get whether the provided {@link MapEntry} and {@link Object} are equal.
     * @param lhs The {@link MapEntry} to compare.
     * @param rhs The {@link Object} to compare.
     */
    @SuppressWarnings("unchecked")
    public static <TKey,TValue> boolean equals(MapEntry<TKey,TValue> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof MapEntry && lhs.equals((MapEntry<TKey,TValue>)rhs);
    }

    /**
     * Get whether this {@link MapEntry} equals the provided {@link MapEntry}.
     * @param rhs The {@link MapEntry} to compare against this {@link MapEntry}.
     */
    public default boolean equals(MapEntry<TKey,TValue> rhs)
    {
        return rhs != null &&
            Comparer.equal(this.getKey(), rhs.getKey()) &&
            Comparer.equal(this.getValue(), rhs.getValue());
    }
}
