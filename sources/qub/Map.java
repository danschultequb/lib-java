package qub;

/**
 * A mapping data structure that maps values of type TKey to values of type TValue.
 * @param <TKey> The type of keys in the map.
 * @param <TValue> The type of values in the map.
 */
public interface Map<TKey,TValue> extends Iterable<MapEntry<TKey,TValue>>
{
    /**
     * Create a new {@link MutableMap}.
     * @param <TKey> The type of keys stored in the created {@link MutableMap}.
     * @param <TValue> The type of values stored in the created {@link MutableMap}.
     */
    public static <TKey,TValue> MutableMap<TKey,TValue> create()
    {
        return MutableMap.create();
    }

    /**
     * Create a new {@link MutableMap}.
     * @param <TKey> The type of keys stored in the created {@link MutableMap}.
     * @param <TValue> The type of values stored in the created {@link MutableMap}.
     */
    public static <TKey,TValue> MutableMap<TKey,TValue> create(Iterable<MapEntry<TKey,TValue>> entries)
    {
        return Map.<TKey,TValue>create().setAll(entries);
    }

    /**
     * Create a new {@link MutableMap}.
     * @param <TKey> The type of keys stored in the created {@link MutableMap}.
     * @param <TValue> The type of values stored in the created {@link MutableMap}.
     */
    public static <TKey,TValue> MutableMap<TKey,TValue> create(Iterator<MapEntry<TKey,TValue>> entries)
    {
        return Map.<TKey,TValue>create().setAll(entries);
    }

    /**
     * Create a {@link MutableMap} from the values in the provided {@link Iterable}.
     * @param getKey The {@link Function1} that will select the key.
     * @param getValue The {@link Function1} that will select the value.
     * @param <TKey> The type of value that will serve as the keys of the {@link MutableMap}.
     * @param <TValue> The type of value that will serve as the values of the {@link MutableMap}.
     */
    public static <T,TKey,TValue> MutableMap<TKey,TValue> create(Iterable<T> values, Function1<T,TKey> getKey, Function1<T,TValue> getValue)
    {
        return Map.create(values.iterate(), getKey, getValue);
    }

    /**
     * Create a {@link MutableMap} from the values in the provided {@link Iterator}.
     * @param getKey The {@link Function1} that will select the key.
     * @param getValue The {@link Function1} that will select the value.
     * @param <TKey> The type of value that will serve as the keys of the {@link MutableMap}.
     * @param <TValue> The type of value that will serve as the values of the {@link MutableMap}.
     */
    public static <T,TKey,TValue> MutableMap<TKey,TValue> create(Iterator<T> values, Function1<T,TKey> getKey, Function1<T,TValue> getValue)
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
    public boolean containsKey(TKey key);

    /**
     * Get the value associated with the provided key, or null if the key doesn't exist in the map.
     * @param key The key to get the value for.
     * @return The value associated with the provided key, or null if the key doesn't exist in the
     * map.
     */
    public Result<TValue> get(TKey key);

    /**
     * Iterate over the keys of this {@link Map}.
     */
    public default Iterator<TKey> iterateKeys()
    {
        return this.iterate().map(MapEntry::getKey);
    }

    /**
     * Iterate over the values of this {@link Map}.
     */
    public default Iterator<TValue> iterateValues()
    {
        return this.iterate().map(MapEntry::getValue);
    }

    @Override
    public default boolean equals(Iterable<MapEntry<TKey,TValue>> rhs)
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
