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
    // @SafeVarargs
    static <TKey,TValue> MutableMap<TKey,TValue> create()
    {
        return Map.create(Iterable.create());
    }

    /**
     * Create a new MutableMap.
     * @param <TKey> The type of keys stored in the created MutableMap.
     * @param <TValue> The type of values stored in the created MutableMap.
     * @return The created MutableMap.
     */
    static <TKey,TValue> MutableMap<TKey,TValue> create(Iterable<MapEntry<TKey,TValue>> entries)
    {
        return Map.create(entries.iterate());
    }

    /**
     * Create a new MutableMap.
     * @param <TKey> The type of keys stored in the created MutableMap.
     * @param <TValue> The type of values stored in the created MutableMap.
     * @return The created MutableMap.
     */
    static <TKey,TValue> MutableMap<TKey,TValue> create(Iterator<MapEntry<TKey,TValue>> entries)
    {
        return ListMap.create(entries);
    }

    /**
     * Create a Map from the values in the provided Iterable.
     * @param getKey The function that will select the key.
     * @param getValue The function that will select the value.
     * @param <TKey> The type of value that will serve as the keys of the map.
     * @param <TValue> The type of value that will serve as the values of the map.
     * @return A Map from the values in the provided Iterable.
     */
    static <T,TKey,TValue> MutableMap<TKey,TValue> create(Iterable<T> values, Function1<T,TKey> getKey, Function1<T,TValue> getValue)
    {
        return Map.create(values.iterate(), getKey, getValue);
    }

    /**
     * Create a Map from the values in the provided Iterator.
     * @param getKey The function that will select the key.
     * @param getValue The function that will select the value.
     * @param <TKey> The type of value that will serve as the keys of the map.
     * @param <TValue> The type of value that will serve as the values of the map.
     * @return A Map from the values in the provided Iterator.
     */
    static <T,TKey,TValue> MutableMap<TKey,TValue> create(Iterator<T> values, Function1<T,TKey> getKey, Function1<T,TValue> getValue)
    {
        PreCondition.assertNotNull(getKey, "getKey");
        PreCondition.assertNotNull(getValue, "getValue");

        final MutableMap<TKey,TValue> result = Map.create();
        for (final T iteratorValue : values)
        {
            final TKey key = getKey.run(iteratorValue);
            final TValue value = getValue.run(iteratorValue);
            result.set(key, value);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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
                    if (!this.containsKey(rhsEntry.getKey()) || !Comparer.equal(this.get(rhsEntry.getKey()).await(), rhsEntry.getValue()))
                    {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }

    public static <TKey> NotFoundException createNotFoundException(TKey key)
    {
        return new NotFoundException("Could not find the provided key (" + key + ") in this " + Types.getTypeName(Map.class) + ".");
    }

    public static <TKey,TValue> Result<TValue> createNotFoundResult(TKey key)
    {
        return Result.error(Map.createNotFoundException(key));
    }
}
