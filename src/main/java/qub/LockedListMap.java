package qub;

/**
 * A Map that runs all of its operations within its own mutex's critical section.
 * @param <TKey> The type of the keys in the map.
 * @param <TValue> The type of the values in the map.
 */
public class LockedListMap<TKey,TValue> extends LockedMapBase<TKey,TValue>
{
    public LockedListMap()
    {
        super(new ListMap<TKey, TValue>());
    }
}
