package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface Map<TKey,TValue> extends Iterable<MapEntry<TKey,TValue>>
{
    /**
     * Remove all of the existing entries from this Map.
     */
    void clear();

    /**
     * Get whether or not the provided key exists in this Map.
     * @param key The key to check.
     * @return Whether or not the provided key exists in this Map.
     */
    boolean containsKey(TKey key);

    /**
     * Get the value associated with the provided key, or null if the key doesn't exist in the map.
     * @param key The key to get the value for.
     * @return The value associated with the provided key, or null if the key doesn't exist in the
     * map.
     */
    Result<TValue> get(TKey key);

    /**
     * Set the association between the provided key and value.
     * @param key The key.
     * @param value The value.
     */
    void set(TKey key, TValue value);

    /**
     * Set all of the entries in the provided Iterable as key value pairs in this Map.
     * @param entries
     */
    default void setAll(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        for (final MapEntry<TKey,TValue> entry : entries)
        {
            set(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Remove the entry with the provided key. Return the removed entity if it was found.
     * @param key The key of the entry to remove.
     * @return The removed entry.
     */
    Result<TValue> remove(TKey key);

    /**
     * Get the keys of this Map.
     * @return The keys of this Map.
     */
    Iterable<TKey> getKeys();

    /**
     * Get the values of this Map.
     * @return The values of this Map.
     */
    Iterable<TValue> getValues();

    @Override
    default boolean equals(Iterable<MapEntry<TKey,TValue>> rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            result = (this.getCount() == rhs.getCount());
            if (result)
            {
                for (final MapEntry<TKey,TValue> rhsEntry : rhs)
                {
                    if (!this.containsKey(rhsEntry.getKey()) || !Comparer.equal(this.get(rhsEntry.getKey()), rhsEntry.getValue()))
                    {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
