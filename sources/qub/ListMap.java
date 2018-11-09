package qub;

public class ListMap<TKey,TValue> implements Map<TKey,TValue>
{
    private final List<MutableMapEntry<TKey,TValue>> entries;

    public ListMap()
    {
        entries = new ArrayList<>();
    }

    private MutableMapEntry<TKey,TValue> getEntry(final TKey key)
    {
        return entries.first(entry -> Comparer.equal(entry.getKey(), key));
    }

    public ListMap<TKey,TValue> clone()
    {
        final ListMap<TKey,TValue> result = new ListMap<>();
        for (final MapEntry<TKey,TValue> entry : this)
        {
            result.set(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public void clear()
    {
        entries.clear();
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
    public boolean remove(TKey key)
    {
        return entries.removeFirst(entry -> Comparer.equal(entry.getKey(), key)) != null;
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return entries.map(MutableMapEntry::getKey);
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return entries.map(MutableMapEntry::getValue);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return entries.iterate().map((MutableMapEntry<TKey, TValue> entry) -> (MapEntry<TKey,TValue>)entry);
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
