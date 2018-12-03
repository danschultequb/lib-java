package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface MutableMap<TKey,TValue> extends Map<TKey,TValue>
{
    /**
     * Remove all of the existing entries from this MutableMap.
     */
    void clear();

    /**
     * Get the value associated with the provided key. If the key doesn't exist in this Map, then
     * the valueCreator function will be run and the returned value will be associated with the
     * provided key. The final associated value for the provided key will be returned.
     * @param key The key to get the associated value for.
     * @param valueCreator The function to use to create the value to associate with the provided
     *                     key if the key doesn't exist in this Map.
     * @return The value associated with the provided key.
     */
    default Result<TValue> getOrSet(TKey key, Function0<TValue> valueCreator)
    {
        PreCondition.assertNotNull(valueCreator, "valueCreator");

        final Result<TValue> result = get(key)
            .catchError(NotFoundException.class, () ->
            {
                final TValue value = valueCreator.run();
                set(key, value);
                return value;
            });

        PostCondition.assertNotNull(result, "result");

        return result;
    }

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
