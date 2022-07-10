package qub;

public class HashMapEntry<TKey,TValue> extends MutableMapEntry<TKey,TValue>
{
    private final int keyHashCode;

    private HashMapEntry(int keyHashCode, TKey key, TValue value)
    {
        super(key, value);

        this.keyHashCode = keyHashCode;
    }

    public static <TKey,TValue> HashMapEntry<TKey,TValue> create(TKey key, TValue value)
    {
        return HashMapEntry.create(Hash.getHashCode(key), key, value);
    }

    public static <TKey,TValue> HashMapEntry<TKey,TValue> create(int keyHashCode, TKey key, TValue value)
    {
        return new HashMapEntry<>(keyHashCode, key, value);
    }

    public int getKeyHashCode()
    {
        return this.keyHashCode;
    }
}
