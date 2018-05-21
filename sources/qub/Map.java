package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface Map<TKey,TValue> extends Iterable<MapEntry<TKey,TValue>>
{
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
    TValue get(TKey key);

    /**
     * Set the association between the provided key and value.
     * @param key The key.
     * @param value The value.
     */
    void set(TKey key, TValue value);

    /**
     * Remove the entry with the provided key. Return whether or not the entry was removed by this
     * function call.
     * @param key The key of the entry to remove.
     * @return Whether or not the entry was removed by this function call.
     */
    boolean remove(TKey key);

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
}
