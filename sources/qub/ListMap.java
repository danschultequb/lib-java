package qub;

public class ListMap<TKey,TValue> implements MutableMap<TKey,TValue>
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
    public Result<TValue> get(TKey key)
    {
        final MutableMapEntry<TKey,TValue> entry = getEntry(key);
        return entry != null ? Result.success(entry.getValue()) : Result.error(new NotFoundException(key));
    }

    @Override
    public ListMap<TKey,TValue> set(TKey key, TValue value)
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
        return this;
    }

    @Override
    public Result<TValue> remove(TKey key)
    {
        final MapEntry<TKey,TValue> removedEntry = entries.removeFirst(entry -> Comparer.equal(entry.getKey(), key));
        return removedEntry != null ? Result.success(removedEntry.getValue()) : Result.error(new NotFoundException(key));
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
