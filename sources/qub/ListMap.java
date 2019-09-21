package qub;

/**
 * A Map implementation that stores its entries in a List.
 * @param <TKey> The type of keys that are stored in this Map.
 * @param <TValue> The type of values that are associted with the keys in this Map.
 */
public class ListMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private final List<MutableMapEntry<TKey,TValue>> entries;
    private final Function2<TKey,TKey,Boolean> comparer;

    /**
     * Create a new ListMap that will used the provided function to compare whether or not two keys
     * are equal.
     * @param comparer The comparer that will be used to determine whether or not two keys are
     *                 equal.
     */
    protected ListMap(Function2<TKey,TKey,Boolean> comparer)
    {
        this.comparer = comparer;
        this.entries = List.create();
    }

    /**
     * Create a new ListMap that will compare keys by using their equals() function.
     */
    public static <TKey,TValue> ListMap<TKey,TValue> create()
    {
        return ListMap.create(Comparer::equal);
    }

    /**
     * Create a new ListMap that will used the provided function to compare whether or not two keys
     * are equal.
     * @param comparer The comparer that will be used to determine whether or not two keys are
     *                 equal.
     */
    public static <TKey,TValue> ListMap<TKey,TValue> create(Function2<TKey,TKey,Boolean> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        return new ListMap<>(comparer);
    }

    /**
     * Get the entry with the provided key.
     * @param key The key of the entry to get.
     * @return The entry with the provided key, or null if no entry was found.
     */
    private MutableMapEntry<TKey,TValue> getEntry(TKey key)
    {
        return entries.first(entry -> this.comparer.run(entry.getKey(), key));
    }

    /**
     * Clone the contents of this ListMap.
     * @return A new clone of this ListMap.
     */
    public ListMap<TKey,TValue> clone()
    {
        final ListMap<TKey,TValue> result = ListMap.create(this.comparer);
        for (final MapEntry<TKey,TValue> entry : this)
        {
            result.set(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public void clear()
    {
        entries.clear();
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return getEntry(key) != null;
    }

    @Override
    public Result<TValue> get(TKey key)
    {
        final MutableMapEntry<TKey,TValue> entry = getEntry(key);
        return entry != null ? Result.success(entry.getValue()) : createNotFoundResult(key);
    }

    @Override
    public ListMap<TKey,TValue> set(TKey key, TValue value)
    {
        final MutableMapEntry<TKey,TValue> entry = getEntry(key);
        if (entry == null)
        {
            entries.add(new MutableMapEntry<>(key, value));
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
        final MapEntry<TKey,TValue> removedEntry = entries.removeFirst(entry -> Comparer.equal(entry.getKey(), key));
        return removedEntry != null ? Result.success(removedEntry.getValue()) : createNotFoundResult(key);
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return entries.map(MutableMapEntry::getKey);
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return entries.map(MutableMapEntry::getValue);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return entries.iterate().map((MutableMapEntry<TKey, TValue> entry) -> (MapEntry<TKey,TValue>)entry);
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
