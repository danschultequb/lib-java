package qub;

/**
 * An immutable property.
 * @param <T> The type of value that this property holds.
 */
public interface Property<T> extends Getable<T>, Event2<T,T>
{
    /**
     * Create a new Value object that stores the provided type.
     * @param <T> The type that the created Value will contain.
     * @return The new Value object.
     */
    static <T> MutableProperty<T> create()
    {
        return MutableProperty.create();
    }

    /**
     * Create a new Value object that stores the provided type.
     * @param <T> The type that the created Value will contain.
     * @return The new Value object.
     */
    static <T> MutableProperty<T> create(T initialValue)
    {
        return MutableProperty.create(initialValue);
    }
}
