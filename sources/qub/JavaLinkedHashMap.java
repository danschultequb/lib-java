package qub;

public class JavaLinkedHashMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private final java.util.LinkedHashMap<TKey,TValue> javaMap;

    private JavaLinkedHashMap()
    {
        this.javaMap = new java.util.LinkedHashMap<>();
    }

    public static <TKey,TValue> JavaLinkedHashMap<TKey,TValue> create()
    {
        return new JavaLinkedHashMap<>();
    }

    /**
     * Create a new {@link JavaLinkedHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link JavaLinkedHashMap}.
     */
    @SafeVarargs
    public static <TKey,TValue> JavaLinkedHashMap<TKey,TValue> create(MapEntry<TKey,TValue>... entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return JavaLinkedHashMap.create(Iterable.create(entries));
    }

    /**
     * Create a new {@link JavaLinkedHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link JavaLinkedHashMap}.
     */
    public static <TKey,TValue> JavaLinkedHashMap<TKey,TValue> create(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return JavaLinkedHashMap.<TKey,TValue>create().setAll(entries.iterate());
    }

    /**
     * Create a new {@link JavaLinkedHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link JavaLinkedHashMap}.
     */
    public static <TKey,TValue> JavaLinkedHashMap<TKey,TValue> create(Iterator<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return JavaLinkedHashMap.<TKey,TValue>create().setAll(entries);
    }

    @Override
    public JavaLinkedHashMap<TKey,TValue> clear()
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
    public JavaLinkedHashMap<TKey,TValue> set(TKey key, TValue value)
    {
        this.javaMap.put(key, value);
        return this;
    }

    @Override
    public JavaLinkedHashMap<TKey, TValue> set(MapEntry<TKey, TValue> entry)
    {
        return (JavaLinkedHashMap<TKey, TValue>)MutableMap.super.set(entry);
    }

    @Override
    public JavaLinkedHashMap<TKey, TValue> setAll(Iterable<? extends MapEntry<TKey, TValue>> mapEntries)
    {
        return (JavaLinkedHashMap<TKey, TValue>)MutableMap.super.setAll(mapEntries);
    }

    @Override
    public JavaLinkedHashMap<TKey, TValue> setAll(Iterator<? extends MapEntry<TKey, TValue>> mapEntries)
    {
        return (JavaLinkedHashMap<TKey, TValue>)MutableMap.super.setAll(mapEntries);
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
