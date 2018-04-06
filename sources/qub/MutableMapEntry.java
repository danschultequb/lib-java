package qub;

public class MutableMapEntry<TKey,TValue> implements MapEntry<TKey,TValue>
{
    private final TKey key;
    private TValue value;

    public MutableMapEntry(TKey key, TValue value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public TKey getKey()
    {
        return key;
    }

    @Override
    public TValue getValue()
    {
        return value;
    }

    public void setValue(TValue value)
    {
        this.value = value;
    }
}
