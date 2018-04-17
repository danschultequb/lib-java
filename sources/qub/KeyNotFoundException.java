package qub;

/**
 * When searching through a set of key and value pairs, no entry was found that matches the provided
 * key.
 */
public class KeyNotFoundException extends RuntimeException
{
    /**
     * The key that wasn't found.
     */
    private final Object key;

    /**
     * Create a new KeyNotFoundException with the provided key that wasn't found.
     * @param key The key that wasn't found.
     */
    public KeyNotFoundException(Object key)
    {
        super("Could not find an entry with the key: " + key);

        this.key = key;
    }

    /**
     * Get the key that wasn't found.
     * @return The key that wasn't found.
     */
    public Object getKey()
    {
        return key;
    }
}
