package qub;

/**
 * A {@link Map} implementation that stores its entries in a {@link List}.
 * @param <TKey> The type of keys that are stored in this {@link ListMap}.
 * @param <TValue> The type of values that are associated with the keys in this {@link ListMap}.
 */
public class ListMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private final List<MutableMapEntry<TKey,TValue>> entries;
    private final EqualFunction<TKey> equalFunction;

    /**
     * Create a new {@link ListMap} that will use the provided {@link EqualFunction} to determine
     * whether two keys are equal.
     * @param equalFunction The {@link EqualFunction} that will be used to determine whether two
     *                      keys are equal.
     */
    private ListMap(EqualFunction<TKey> equalFunction)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");

        this.equalFunction = equalFunction;
        this.entries = List.create();
    }

    /**
     * Create a new {@link ListMap} that will compare keys by using the {@link Comparer}.equal()
     * function.
     * @param entries The initial entries in the returned {@link ListMap}.
     */
    @SafeVarargs
    public static <TKey,TValue> ListMap<TKey,TValue> create(MapEntry<TKey,TValue>... entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return ListMap.create(Iterable.create(entries));
    }

    /**
     * Create a new {@link ListMap} that will compare keys by using the {@link Comparer}.equal()
     * function.
     * @param entries The initial entries in the returned {@link ListMap}.
     */
    public static <TKey,TValue> ListMap<TKey,TValue> create(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return ListMap.create(entries.iterate());
    }

    /**
     * Create a new {@link ListMap} that will compare keys by using the {@link Comparer}.equal()
     * function.
     * @param entries The initial entries in the returned {@link ListMap}.
     */
    public static <TKey,TValue> ListMap<TKey,TValue> create(Iterator<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return ListMap.create(Comparer::equal, entries);
    }

    /**
     * Create a new {@link ListMap} that will use the provided {@link EqualFunction} to determine
     * whether two keys are equal.
     * @param equalFunction The {@link EqualFunction} that will be used to determine whether two
     *                      keys are equal.
     * @param entries The initial entries in the returned {@link ListMap}.
     */
    @SafeVarargs
    public static <TKey,TValue> ListMap<TKey,TValue> create(EqualFunction<TKey> equalFunction, MapEntry<TKey,TValue>... entries)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(entries, "entries");

        return ListMap.create(equalFunction, Iterable.create(entries));
    }

    /**
     * Create a new {@link ListMap} that will use the provided {@link EqualFunction} to determine
     * whether two keys are equal.
     * @param equalFunction The {@link EqualFunction} that will be used to determine whether two
     *                      keys are equal.
     * @param entries The initial entries in the returned {@link ListMap}.
     */
    public static <TKey,TValue> ListMap<TKey,TValue> create(EqualFunction<TKey> equalFunction, Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(entries, "entries");

        return ListMap.create(equalFunction, entries.iterate());
    }

    /**
     * Create a new {@link ListMap} that will use the provided {@link EqualFunction} to determine
     * whether two keys are equal.
     * @param equalFunction The {@link EqualFunction} that will be used to determine whether two
     *                      keys are equal.
     * @param entries The initial entries in the returned {@link ListMap}.
     */
    public static <TKey,TValue> ListMap<TKey,TValue> create(EqualFunction<TKey> equalFunction, Iterator<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(entries, "entries");

        final ListMap<TKey,TValue> result = new ListMap<>(equalFunction);
        for (final MapEntry<TKey,TValue> entry : entries)
        {
            result.set(entry);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the entry with the provided key.
     * @param key The key of the entry to get.
     * @return The entry with the provided key, or null if no entry was found.
     */
    private MutableMapEntry<TKey,TValue> getEntry(TKey key)
    {
        return entries.first(entry -> this.equalFunction.run(entry.getKey(), key));
    }

    @Override
    public ListMap<TKey,TValue> clear()
    {
        this.entries.clear();
        return this;
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return this.getEntry(key) != null;
    }

    @Override
    public Result<TValue> get(TKey key)
    {
        final MutableMapEntry<TKey,TValue> entry = this.getEntry(key);
        return entry != null ? Result.success(entry.getValue()) : Map.createNotFoundResult(key);
    }

    @Override
    public ListMap<TKey,TValue> set(TKey key, TValue value)
    {
        final MutableMapEntry<TKey,TValue> entry = this.getEntry(key);
        if (entry == null)
        {
            this.entries.add(MapEntry.create(key, value));
        }
        else
        {
            entry.setValue(value);
        }
        return this;
    }

    @Override
    public Result<TValue> remove(TKey key)
    {
        final MapEntry<TKey,TValue> removedEntry = this.entries.removeFirst(entry -> Comparer.equal(entry.getKey(), key));
        return removedEntry != null ? Result.success(removedEntry.getValue()) : Map.createNotFoundResult(key);
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return this.entries.map(MutableMapEntry::getKey);
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return this.entries.map(MutableMapEntry::getValue);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return this.entries.iterate().map((MutableMapEntry<TKey, TValue> entry) -> entry);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
