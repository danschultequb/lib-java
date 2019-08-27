package qub;

public class ConcurrentHashMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private final java.util.concurrent.ConcurrentHashMap<TKey,TValue> javaMap;

    public ConcurrentHashMap()
    {
        javaMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<TKey,TValue> clone()
    {
        final ConcurrentHashMap<TKey,TValue> result = new ConcurrentHashMap<>();
        for (final MapEntry<TKey,TValue> entry : this)
        {
            result.set(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public void clear()
    {
        javaMap.clear();
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return javaMap.containsKey(key);
    }

    @Override
    public Result<TValue> get(TKey key)
    {
        return javaMap.containsKey(key) ? Result.success(javaMap.get(key)) : createNotFoundResult(key);
    }

    @Override
    public ConcurrentHashMap<TKey,TValue> set(TKey key, TValue value)
    {
        javaMap.put(key, value);
        return this;
    }

    @Override
    public Result<TValue> remove(TKey key)
    {
        return javaMap.containsKey(key) ? Result.success(javaMap.remove(key)) : createNotFoundResult(key);
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return Array.create(iterate().map(MapEntry::getKey));
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return Array.create(iterate().map(MapEntry::getValue));
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return JavaIteratorToIteratorAdapter.create(javaMap.entrySet().iterator())
            .map((java.util.Map.Entry<TKey, TValue> javaMapEntry) ->
                new MutableMapEntry<>(javaMapEntry.getKey(), javaMapEntry.getValue()));
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
