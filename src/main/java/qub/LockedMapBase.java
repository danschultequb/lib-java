package qub;

public abstract class LockedMapBase<TKey,TValue> extends MapBase<TKey,TValue>
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
        return mutex.criticalSection(new Function0<TValue>()
        {
            @Override
            public TValue run()
            {
                return innerMap.get(key);
            }
        });
    }

    @Override
    public void set(final TKey key, final TValue value)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerMap.set(key, value);
            }
        });
    }

    @Override
    public boolean remove(final TKey key)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerMap.remove(key);
            }
        });
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return mutex.criticalSection(new Function0<Iterable<TKey>>()
        {
            @Override
            public Iterable<TKey> run()
            {
                return innerMap.getKeys();
            }
        });
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return mutex.criticalSection(new Function0<Iterable<TValue>>()
        {
            @Override
            public Iterable<TValue> run()
            {
                return innerMap.getValues();
            }
        });
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return mutex.criticalSection(new Function0<Iterator<MapEntry<TKey, TValue>>>()
        {
            @Override
            public Iterator<MapEntry<TKey, TValue>> run()
            {
                return innerMap.iterate();
            }
        });
    }
}
