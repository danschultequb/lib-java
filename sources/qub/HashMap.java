package qub;

public class HashMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private Array<HashMapEntry<TKey,TValue>> entries;

    private HashMap(int initialCapacity)
    {
        this.entries = Array.createWithLength(initialCapacity);
    }

    public static <TKey,TValue> HashMap<TKey,TValue> create()
    {
        return HashMap.createWithCapacity(10);
    }

    public static <TKey,TValue> HashMap<TKey,TValue> createWithCapacity(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 1, "initialCapacity");

        return new HashMap<>(initialCapacity);
    }

    @Override
    public Iterator<MapEntry<TKey, TValue>> iterate()
    {
        return this.entries.iterate()
            .where(Comparer::isNotNull)
            .map((HashMapEntry<TKey,TValue> entry) -> entry);
    }

    private Result<Integer> getKeyIndex(TKey key)
    {
        return Result.create(() ->
        {
            int result = -1;

            final int keyHashCode = Hash.getHashCode(key);
            final int startIndex = keyHashCode % this.entries.getCount();
            int currentIndex = startIndex;
            while (true)
            {
                final HashMapEntry<TKey,TValue> currentEntry = this.entries.get(currentIndex);
                if (currentEntry == null)
                {
                    break;
                }
                else if (keyHashCode == currentEntry.getKeyHashCode() &&
                    Comparer.equal(key, currentEntry.getKey()))
                {
                    result = currentIndex;
                    break;
                }
                else
                {
                    currentIndex = (currentIndex + 1) % this.entries.getCount();
                    if (currentIndex == startIndex)
                    {
                        break;
                    }
                }
            }

            if (result == -1)
            {
                throw Map.createNotFoundException(key);
            }

            return result;
        });
    }

    @Override
    public boolean containsKey(TKey key)
    {
        return this.getKeyIndex(key)
            .then(() -> true)
            .catchError(NotFoundException.class, () -> false)
            .await();
    }

    @Override
    public Result<TValue> get(TKey key)
    {
        return Result.create(() ->
        {
            final int keyIndex = this.getKeyIndex(key).await();
            final HashMapEntry<TKey,TValue> entry = this.entries.get(keyIndex);
            return entry.getValue();
        });
    }

    @Override
    public MutableMap<TKey, TValue> clear()
    {
        final int capacity = this.entries.getCount();
        for (int i = 0; i < capacity; i++)
        {
            this.entries.set(i, null);
        }

        return this;
    }

    @Override
    public HashMap<TKey, TValue> set(MapEntry<TKey, TValue> entry)
    {
        return (HashMap<TKey,TValue>)MutableMap.super.set(entry);
    }

    private static <TKey,TValue> boolean setEntry(Array<HashMapEntry<TKey,TValue>> entries, HashMapEntry<TKey,TValue> entry)
    {
        PreCondition.assertNotNullAndNotEmpty(entries, "entries");
        PreCondition.assertNotNull(entry, "entry");

        boolean result = false;

        final int keyHashCode = entry.getKeyHashCode();
        int startIndex = keyHashCode % entries.getCount();
        int currentIndex = startIndex;
        while (true)
        {
            final HashMapEntry<TKey,TValue> currentEntry = entries.get(currentIndex);
            if (currentEntry == null)
            {
                entries.set(currentIndex, entry);
                result = true;
                break;
            }
            else if (keyHashCode == currentEntry.getKeyHashCode() &&
                Comparer.equal(entry.getKey(), currentEntry.getKey()))
            {
                currentEntry.setValue(entry.getValue());
                result = true;
                break;
            }
            else
            {
                currentIndex = (currentIndex + 1) % entries.getCount();
                if (currentIndex == startIndex)
                {
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public HashMap<TKey, TValue> set(TKey key, TValue value)
    {
        final HashMapEntry<TKey,TValue> entry = HashMapEntry.create(key, value);
        if (!HashMap.setEntry(this.entries, entry))
        {
            final Array<HashMapEntry<TKey,TValue>> newEntries = Array.createWithLength((this.entries.getCount() * 2) + 1);
            for (final HashMapEntry<TKey,TValue> existingEntry : this.entries)
            {
                HashMap.setEntry(newEntries, existingEntry);
            }
            HashMap.setEntry(newEntries, entry);

            this.entries = newEntries;
        }
        return this;
    }

    @Override
    public HashMap<TKey, TValue> setAll(Iterable<MapEntry<TKey, TValue>> mapEntries)
    {
        return (HashMap<TKey,TValue>)MutableMap.super.setAll(mapEntries);
    }

    @Override
    public Result<TValue> remove(TKey key)
    {
        return Result.create(() ->
        {
            final int keyIndex = this.getKeyIndex(key).await();
            final HashMapEntry<TKey,TValue> entry = this.entries.get(keyIndex);
            this.entries.set(keyIndex, null);
            return entry.getValue();
        });
    }

    @Override
    public boolean equals(Object obj)
    {
        return Iterable.equals(this, obj);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
