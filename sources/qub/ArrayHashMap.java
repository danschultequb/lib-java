package qub;

public class ArrayHashMap<TKey,TValue> implements MutableMap<TKey,TValue>
{
    private static final int defaultInitialCapacity = 10;

    private Array<HashMapEntry<TKey,TValue>> entries;

    private ArrayHashMap(int initialCapacity)
    {
        this.entries = Array.createWithLength(initialCapacity);
    }

    public static <TKey,TValue> ArrayHashMap<TKey,TValue> create()
    {
        return ArrayHashMap.createWithCapacity(ArrayHashMap.defaultInitialCapacity);
    }

    public static <TKey,TValue> ArrayHashMap<TKey,TValue> createWithCapacity(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 1, "initialCapacity");

        return new ArrayHashMap<>(initialCapacity);
    }

    /**
     * Create a new {@link ArrayHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link ArrayHashMap}.
     */
    @SafeVarargs
    public static <TKey,TValue> ArrayHashMap<TKey,TValue> create(MapEntry<TKey,TValue>... entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return ArrayHashMap.create(Iterable.create(entries));
    }

    /**
     * Create a new {@link ArrayHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link ArrayHashMap}.
     */
    public static <TKey,TValue> ArrayHashMap<TKey,TValue> create(Iterable<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        final int initialCapacity = Math.maximum(ArrayHashMap.defaultInitialCapacity, entries.getCount());
        return ArrayHashMap.<TKey,TValue>createWithCapacity(initialCapacity)
            .setAll(entries.iterate());
    }

    /**
     * Create a new {@link ArrayHashMap} with the provided initial entries.
     * @param entries The initial entries in the returned {@link ArrayHashMap}.
     */
    public static <TKey,TValue> ArrayHashMap<TKey,TValue> create(Iterator<MapEntry<TKey,TValue>> entries)
    {
        PreCondition.assertNotNull(entries, "entries");

        return ArrayHashMap.<TKey,TValue>create().setAll(entries);
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
            final int entriesCount = this.entries.getCount();
            final int startIndex = Math.modulo(keyHashCode, entriesCount);
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
                    currentIndex = Math.modulo(currentIndex + 1, entriesCount);
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
    public ArrayHashMap<TKey, TValue> set(MapEntry<TKey, TValue> entry)
    {
        return (ArrayHashMap<TKey,TValue>)MutableMap.super.set(entry);
    }

    private static <TKey,TValue> boolean setEntry(Array<HashMapEntry<TKey,TValue>> entries, HashMapEntry<TKey,TValue> entry)
    {
        PreCondition.assertNotNullAndNotEmpty(entries, "entries");
        PreCondition.assertNotNull(entry, "entry");

        boolean result = false;

        final int keyHashCode = entry.getKeyHashCode();
        final int entriesCount = entries.getCount();
        int startIndex = Math.modulo(keyHashCode, entriesCount);
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
                currentIndex = Math.modulo(currentIndex + 1, entriesCount);
                if (currentIndex == startIndex)
                {
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public ArrayHashMap<TKey, TValue> set(TKey key, TValue value)
    {
        final HashMapEntry<TKey,TValue> entry = HashMapEntry.create(key, value);
        if (!ArrayHashMap.setEntry(this.entries, entry))
        {
            final Array<HashMapEntry<TKey,TValue>> newEntries = Array.createWithLength((this.entries.getCount() * 2) + 1);
            for (final HashMapEntry<TKey,TValue> existingEntry : this.entries)
            {
                ArrayHashMap.setEntry(newEntries, existingEntry);
            }
            ArrayHashMap.setEntry(newEntries, entry);

            this.entries = newEntries;
        }
        return this;
    }

    @Override
    public ArrayHashMap<TKey, TValue> setAll(Iterable<? extends MapEntry<TKey, TValue>> mapEntries)
    {
        return (ArrayHashMap<TKey,TValue>)MutableMap.super.setAll(mapEntries);
    }

    @Override
    public ArrayHashMap<TKey, TValue> setAll(Iterator<? extends MapEntry<TKey, TValue>> mapEntries)
    {
        return (ArrayHashMap<TKey,TValue>)MutableMap.super.setAll(mapEntries);
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
