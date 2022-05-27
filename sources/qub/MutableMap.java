package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface MutableMap<TKey,TValue> extends Map<TKey,TValue>
{
    /**
     * Remove all of the existing entries create this MutableMap.
     */
    public MutableMap<TKey,TValue> clear();

    /**
     * Get the value associated with the provided key. If the key doesn't exist in this Map, then
     * the valueCreator function will be run and the returned value will be associated with the
     * provided key. The final associated value for the provided key will be returned.
     * @param key The key to get the associated value for.
     * @param valueCreator The function to use to create the value to associate with the provided
     *                     key if the key doesn't exist in this Map.
     * @return The value associated with the provided key.
     */
    public default Result<TValue> getOrSet(TKey key, Function0<TValue> valueCreator)
    {
        PreCondition.assertNotNull(valueCreator, "valueCreator");

        return this.get(key)
            .catchError(NotFoundException.class, () ->
            {
                final TValue value = valueCreator.run();
                this.set(key, value);
                return value;
            });
    }

    /**
     * Set the association between the provided entry's key and value.
     * @param entry The entry to set.
     * @return This Map for chaining methods.
     */
    public default MutableMap<TKey,TValue> set(MapEntry<TKey,TValue> entry)
    {
        PreCondition.assertNotNull(entry, "entry");

        return this.set(entry.getKey(), entry.getValue());
    }

    /**
     * Set the association between the provided key and value.
     * @param key The key.
     * @param value The value.
     * @return This Map for chaining methods.
     */
    public MutableMap<TKey,TValue> set(TKey key, TValue value);

    /**
     * Set all of the entries in the provided Iterable as key value pairs in this Map.
     * @param entries The entries to set in this {@link MutableMap}.
     */
    public default MutableMap<TKey,TValue> setAll(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        for (final MapEntry<TKey,TValue> entry : entries)
        {
            this.set(entry.getKey(), entry.getValue());
        }

        return this;
    }

    /**
     * Remove the entry with the provided key. Return the removed entity if it was found.
     * @param key The key of the entry to remove.
     * @return The removed entry.
     */
    public Result<TValue> remove(TKey key);
}
