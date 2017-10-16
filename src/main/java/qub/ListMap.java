package qub;

public class ListMap<TKey,TValue> extends MapBase<TKey,TValue>
{
    private final List<ListMapEntry<TKey,TValue>> entries;

    public ListMap()
    {
        entries = new ArrayList<>();
    }

    private ListMapEntry<TKey,TValue> getEntry(final TKey key)
    {
        return entries.first(new Function1<ListMapEntry<TKey, TValue>, Boolean>()
        {
            @Override
            public Boolean run(ListMapEntry<TKey, TValue> entry)
            {
                final TKey entryKey = entry.getKey();
                return Comparer.equal(entryKey, key);
            }
        });
    }

    @Override
    public TValue get(TKey key)
    {
        final ListMapEntry<TKey,TValue> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public void set(TKey key, TValue value)
    {
        final ListMapEntry<TKey,TValue> entry = getEntry(key);
        if (entry == null)
        {
            entries.add(new ListMapEntry<TKey, TValue>(key, value));
        }
        else
        {
            entry.setValue(value);
        }
    }

    @Override
    public boolean remove(final TKey key)
    {
        final ListMapEntry<TKey,TValue> removedEntry = entries.removeFirst(new Function1<ListMapEntry<TKey, TValue>, Boolean>()
        {
            @Override
            public Boolean run(ListMapEntry<TKey, TValue> entry)
            {
                final TKey entryKey = entry.getKey();
                return Comparer.equal(entryKey, key);
            }
        });
        return removedEntry != null;
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return entries.map(new Function1<ListMapEntry<TKey, TValue>, TKey>()
        {
            @Override
            public TKey run(ListMapEntry<TKey, TValue> entry)
            {
                return entry.getKey();
            }
        });
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return entries.map(new Function1<ListMapEntry<TKey, TValue>, TValue>()
        {
            @Override
            public TValue run(ListMapEntry<TKey, TValue> entry)
            {
                return entry.getValue();
            }
        });
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return entries.map(new Function1<ListMapEntry<TKey,TValue>, MapEntry<TKey,TValue>>()
        {
            @Override
            public MapEntry<TKey, TValue> run(ListMapEntry<TKey, TValue> entry)
            {
                return entry;
            }
        }).iterate();
    }
}
