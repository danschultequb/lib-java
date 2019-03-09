package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface Map<TKey,TValue> extends Iterable<MapEntry<TKey,TValue>>
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
                    if (!this.containsKey(rhsEntry.getKey()) || !Comparer.equal(this.get(rhsEntry.getKey()).awaitError(), rhsEntry.getValue()))
                    {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }

    default NotFoundException createNotFoundException(TKey key)
    {
        return new NotFoundException("Could not find the provided key (" + key + ") in this Map.");
    }

    default Result<TValue> createNotFoundResult(TKey key)
    {
        return Result.error(createNotFoundException(key));
    }
}
