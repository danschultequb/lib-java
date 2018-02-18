package qub;

public class ListMap<TKey,TValue> implements Map<TKey,TValue>
{
    private final List<ListMapEntry<TKey,TValue>> entries;

    public ListMap()
    {
        entries = new ArrayList<>();
    }

    private ListMapEntry<TKey,TValue> getEntry(final TKey key)
    {
        return entries.first(entry -> Comparer.equal(entry.getKey(), key));
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
            entries.add(new ListMapEntry<>(key, value));
        }
        else
        {
            entry.setValue(value);
        }
    }

    @Override
    public boolean remove(final TKey key)
    {
        return entries.removeFirst(entry -> Comparer.equal(entry.getKey(), key)) != null;
    }

    @Override
    public Iterable<TKey> getKeys()
    {
        return entries.map(ListMapEntry::getKey);
    }

    @Override
    public Iterable<TValue> getValues()
    {
        return entries.map(ListMapEntry::getValue);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return entries.iterate().map(entry -> (MapEntry<TKey,TValue>)entry);
    }
}
