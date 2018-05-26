package qub;

public abstract class MapBase<TKey,TValue> extends IterableBase<MapEntry<TKey,TValue>> implements Map<TKey,TValue>
{
    @Override
    public boolean equals(Iterable<MapEntry<TKey,TValue>> rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            result = (this.getCount() == rhs.getCount());
            if (result)
            {
                for (final MapEntry<TKey,TValue> rhsEntry : rhs)
                {
                    if (!this.containsKey(rhsEntry.getKey()) || !Comparer.equal(this.get(rhsEntry.getKey()), rhsEntry.getValue()))
                    {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
