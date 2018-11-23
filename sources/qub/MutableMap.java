package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface MutableMap<TKey,TValue> extends Map<TKey,TValue>
{
    /**
     * Create a new MutableMap.
     * @param <TKey> The type of keys stored in the created MutableMap.
     * @param <TValue> The type of values stored in the created MutableMap.
     * @return The created MutableMap.
     */
    static <TKey,TValue> MutableMap<TKey,TValue> create()
    {
        return new ListMap<>();
    }

    /**
     * Remove all of the existing entries from this MutableMap.
     */
    void clear();

    /**
     * Set the association between the provided key and value.
     * @param key The key.
     * @param value The value.
     * @return This Map for chaining methods.
     */
    MutableMap<TKey,TValue> set(TKey key, TValue value);

    /**
     * Set all of the entries in the provided Iterable as key value pairs in this Map.
     * @param entries
     */
    default MutableMap<TKey,TValue> setAll(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        for (final MapEntry<TKey,TValue> entry : entries)
        {
            set(entry.getKey(), entry.getValue());
        }

        return this;
    }

    /**
     * Remove the entry with the provided key. Return the removed entity if it was found.
     * @param key The key of the entry to remove.
     * @return The removed entry.
     */
    Result<TValue> remove(TKey key);
}
