package qub;

public abstract class LockedMapBase<TKey,TValue> implements Map<TKey,TValue>
{
    private final Map<TKey,TValue> innerMap;
    private final SpinMutex mutex;

    protected LockedMapBase(Map<TKey,TValue> innerMap)
    {
        this.innerMap = innerMap;
        this.mutex = new SpinMutex();
    }

    @Override
    public TValue get(final TKey key)
    {
        return mutex.criticalSection(() -> innerMap.get(key));
    }

    @Override
    public void set(final TKey key, final TValue value)
    {
        mutex.criticalSection(() -> innerMap.set(key, value));
    }

    @Override
    public boolean remove(final TKey key)
    {
        return mutex.criticalSection(() -> innerMap.remove(key));
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return mutex.criticalSection(() -> innerMap.getKeys());
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return mutex.criticalSection(() -> innerMap.getValues());
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return mutex.criticalSection(() -> innerMap.iterate());
    }
}
