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
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerMap.containsKey(key);
        }
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

    @Override
    public boolean any()
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerMap.any();
            }
        });
    }

    @Override
    public int getCount()
    {
        return mutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return innerMap.getCount();
            }
        });
    }

    @Override
    public MapEntry<TKey, TValue> first()
    {
        return mutex.criticalSection(new Function0<MapEntry<TKey, TValue>>()
        {
            @Override
            public MapEntry<TKey, TValue> run()
            {
                return innerMap.first();
            }
        });
    }

    @Override
    public MapEntry<TKey, TValue> first(final Function1<MapEntry<TKey, TValue>, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<MapEntry<TKey, TValue>>()
        {
            @Override
            public MapEntry<TKey, TValue> run()
            {
                return innerMap.first(condition);
            }
        });
    }

    @Override
    public MapEntry<TKey, TValue> last()
    {
        return mutex.criticalSection(new Function0<MapEntry<TKey, TValue>>()
        {
            @Override
            public MapEntry<TKey, TValue> run()
            {
                return innerMap.last();
            }
        });
    }

    @Override
    public MapEntry<TKey, TValue> last(final Function1<MapEntry<TKey, TValue>, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<MapEntry<TKey, TValue>>()
        {
            @Override
            public MapEntry<TKey, TValue> run()
            {
                return innerMap.last(condition);
            }
        });
    }

    @Override
    public boolean contains(final MapEntry<TKey, TValue> value)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerMap.contains(value);
            }
        });
    }

    @Override
    public boolean contains(final Function1<MapEntry<TKey, TValue>, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerMap.contains(condition);
            }
        });
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
