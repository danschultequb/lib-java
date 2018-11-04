package qub;

public class LockedMap<TKey,TValue> implements Map<TKey,TValue>
{
    private final Map<TKey,TValue> innerMap;
    private final Mutex mutex;

    public LockedMap(Map<TKey,TValue> innerMap)
    {
        this(innerMap, new SpinMutex());
    }

    public LockedMap(Map<TKey,TValue> innerMap, Mutex mutex)
    {
        this.innerMap = innerMap;
        this.mutex = mutex;
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return mutex.criticalSection(() -> innerMap.containsKey(key));
    }

    @Override
    public TValue get(TKey key)
    {
        return mutex.criticalSection(() ->  innerMap.get(key));
    }

    @Override
    public void set(TKey key, TValue value)
    {
        mutex.criticalSection(() -> innerMap.set(key, value));
    }

    @Override
    public boolean remove(TKey key)
    {
        return mutex.criticalSection(() -> innerMap.remove(key));
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return mutex.criticalSection(innerMap::getKeys);
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return mutex.criticalSection(innerMap::getValues);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return mutex.criticalSection(innerMap::iterate);
    }

    @Override
    public boolean any()
    {
        return mutex.criticalSection(innerMap::any);
    }

    @Override
    public int getCount()
    {
        return mutex.criticalSection(innerMap::getCount);
    }

    @Override
    public MapEntry<TKey, TValue> first()
    {
        return mutex.criticalSection(() -> innerMap.first());
    }

    @Override
    public MapEntry<TKey, TValue> first(Function1<MapEntry<TKey, TValue>, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerMap.first(condition));
    }

    @Override
    public MapEntry<TKey, TValue> last()
    {
        return mutex.criticalSection(() -> innerMap.last());
    }

    @Override
    public MapEntry<TKey, TValue> last(Function1<MapEntry<TKey, TValue>, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerMap.last(condition));
    }

    @Override
    public boolean contains(MapEntry<TKey, TValue> value)
    {
        return mutex.criticalSection(() -> innerMap.contains(value));
    }

    @Override
    public boolean contains(Function1<MapEntry<TKey, TValue>, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerMap.contains(condition));
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
