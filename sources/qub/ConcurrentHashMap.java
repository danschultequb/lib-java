package qub;

import java.util.Map;

public class ConcurrentHashMap<TKey,TValue> extends MapBase<TKey,TValue>
{
    private final java.util.concurrent.ConcurrentHashMap<TKey,TValue> javaMap;

    public ConcurrentHashMap()
    {
        javaMap = new java.util.concurrent.ConcurrentHashMap<TKey,TValue>();
    }

    public ConcurrentHashMap<TKey,TValue> clone()
    {
        final ConcurrentHashMap<TKey,TValue> result = new ConcurrentHashMap<TKey,TValue>();
        for (final MapEntry<TKey,TValue> entry : this)
        {
            result.set(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return javaMap.containsKey(key);
    }

    @Override
    public TValue get(TKey key)
    {
        return javaMap.get(key);
    }

    @Override
    public void set(TKey key, TValue value)
    {
        javaMap.put(key, value);
    }

    @Override
    public boolean remove(TKey key)
    {
        return javaMap.remove(key) != null;
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return Array.fromValues(iterate()
            .map(new Function1<MapEntry<TKey,TValue>, TKey>()
            {
                @Override
                public TKey run(MapEntry<TKey, TValue> entry)
                {
                    return entry.getKey();
                }
            }));
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return Array.fromValues(iterate()
            .map(new Function1<MapEntry<TKey,TValue>, TValue>()
            {
                @Override
                public TValue run(MapEntry<TKey, TValue> entry)
                {
                    return entry.getValue();
                }
            }));
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return JavaIteratorToIteratorAdapter.wrap(javaMap.entrySet().iterator())
            .map(new Function1<Map.Entry<TKey,TValue>, MapEntry<TKey,TValue>>()
            {
                @Override
                public MapEntry<TKey, TValue> run(Map.Entry<TKey, TValue> javaMapEntry)
                {
                    return new MutableMapEntry<TKey,TValue>(javaMapEntry.getKey(), javaMapEntry.getValue());
                }
            });
    }
}
