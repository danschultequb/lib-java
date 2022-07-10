package qub;

public class JavaHashMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private final java.util.HashMap<TKey,TValue> javaMap;

    private JavaHashMap()
    {
        this.javaMap = new java.util.HashMap<>();
    }

    public static <TKey,TValue> JavaHashMap<TKey,TValue> create()
    {
        return new JavaHashMap<>();
    }

    /**
     * Create a new {@link JavaHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link JavaHashMap}.
     */
    @SafeVarargs
    public static <TKey,TValue> JavaHashMap<TKey,TValue> create(MapEntry<TKey,TValue>... entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return JavaHashMap.create(Iterable.create(entries));
    }

    /**
     * Create a new {@link JavaHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link JavaHashMap}.
     */
    public static <TKey,TValue> JavaHashMap<TKey,TValue> create(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return JavaHashMap.<TKey,TValue>create().setAll(entries.iterate());
    }

    /**
     * Create a new {@link JavaHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link JavaHashMap}.
     */
    public static <TKey,TValue> JavaHashMap<TKey,TValue> create(Iterator<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return JavaHashMap.<TKey,TValue>create().setAll(entries);
    }

    @Override
    public JavaHashMap<TKey,TValue> clear()
    {
        this.javaMap.clear();
        return this;
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return this.javaMap.containsKey(key);
    }

    @Override
    public Result<TValue> get(TKey key)
    {
        return this.javaMap.containsKey(key) ? Result.success(this.javaMap.get(key)) : Map.createNotFoundResult(key);
    }

    @Override
    public JavaHashMap<TKey,TValue> set(TKey key, TValue value)
    {
        this.javaMap.put(key, value);
        return this;
    }

    @Override
    public JavaHashMap<TKey, TValue> set(MapEntry<TKey, TValue> entry)
    {
        return (JavaHashMap<TKey, TValue>)MutableMap.super.set(entry);
    }

    @Override
    public JavaHashMap<TKey, TValue> setAll(Iterable<? extends MapEntry<TKey, TValue>> mapEntries)
    {
        return (JavaHashMap<TKey, TValue>)MutableMap.super.setAll(mapEntries);
    }

    @Override
    public JavaHashMap<TKey, TValue> setAll(Iterator<? extends MapEntry<TKey, TValue>> mapEntries)
    {
        return (JavaHashMap<TKey, TValue>)MutableMap.super.setAll(mapEntries);
    }

    @Override
    public Result<TValue> remove(TKey key)
    {
        return this.javaMap.containsKey(key) ? Result.success(this.javaMap.remove(key)) : Map.createNotFoundResult(key);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return JavaIteratorToIteratorAdapter.create(this.javaMap.entrySet().iterator())
            .map((java.util.Map.Entry<TKey, TValue> javaMapEntry) ->
                MapEntry.create(javaMapEntry.getKey(), javaMapEntry.getValue()));
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
