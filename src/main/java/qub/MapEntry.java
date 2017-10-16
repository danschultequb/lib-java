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
}
