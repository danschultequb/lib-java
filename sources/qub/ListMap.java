package qub;

public class ListMap<TKey,TValue> extends MapBase<TKey,TValue>
{
    private final List<MutableMapEntry<TKey,TValue>> entries;

    public ListMap()
    {
        entries = new ArrayList<>();
    }

    private MutableMapEntry<TKey,TValue> getEntry(final TKey key)
    {
        return entries.first(new Function1<MutableMapEntry<TKey, TValue>, Boolean>()
        {
            @Override
            public Boolean run(MutableMapEntry<TKey, TValue> entry)
            {
                return Comparer.equal(entry.getKey(), key);
            }
        });
    }

    public ListMap<TKey,TValue> clone()
    {
        final ListMap<TKey,TValue> result = new ListMap<TKey,TValue>();
        for (final MapEntry<TKey,TValue> entry : this)
        {
            result.set(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return getEntry(key) != null;
    }

    @Override
    public TValue get(TKey key)
    {
        final MutableMapEntry<TKey,TValue> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public void set(TKey key, TValue value)
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
    }

    @Override
    public boolean remove(final TKey key)
    {
        return entries.removeFirst(new Function1<MutableMapEntry<TKey, TValue>, Boolean>()
        {
            @Override
            public Boolean run(MutableMapEntry<TKey, TValue> entry)
            {
                return Comparer.equal(entry.getKey(), key);
            }
        }) != null;
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return entries.map(new Function1<MutableMapEntry<TKey, TValue>, TKey>()
        {
            @Override
            public TKey run(MutableMapEntry<TKey, TValue> entry)
            {
                return entry.getKey();
            }
        });
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return entries.map(new Function1<MutableMapEntry<TKey, TValue>, TValue>()
        {
            @Override
            public TValue run(MutableMapEntry<TKey, TValue> entry)
            {
                return entry.getValue();
            }
        });
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return entries.iterate().map(new Function1<MutableMapEntry<TKey, TValue>, MapEntry<TKey, TValue>>()
        {
            @Override
            public MapEntry<TKey, TValue> run(MutableMapEntry<TKey, TValue> entry)
            {
                return entry;
            }
        });
    }
}
