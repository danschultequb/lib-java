package qub;

public class ConcurrentHashMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private final java.util.concurrent.ConcurrentHashMap<TKey,TValue> javaMap;

    private ConcurrentHashMap()
    {
        this.javaMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public static <TKey,TValue> ConcurrentHashMap<TKey,TValue> create()
    {
        return new ConcurrentHashMap<>();
    }

    @Override
    public ConcurrentHashMap<TKey,TValue> clear()
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
    public ConcurrentHashMap<TKey,TValue> set(TKey key, TValue value)
    {
        this.javaMap.put(key, value);
        return this;
    }

    @Override
    public Result<TValue> remove(TKey key)
    {
        return this.javaMap.containsKey(key) ? Result.success(this.javaMap.remove(key)) : Map.createNotFoundResult(key);
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return this.iterate().map(MapEntry::getKey).toList();
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return this.iterate().map(MapEntry::getValue).toList();
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
