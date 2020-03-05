package qub;

/**
 * A property that can change its value.
 * @param <T> The type of value that this property holds.
 */
public interface MutableProperty<T> extends Property<T>, Value<T>
{
    /**
     * Create a new MutableProperty with no initial value.
     * @param <T> The type of value that the property will hold.
     * @return The new MutableProperty with no initial value.
     */
    static <T> MutableProperty<T> create()
    {
        return MutableObjectProperty.create();
    }

    /**
     * Create a new MutableProperty with the provided initial value.
     * @param initialValue The initial value of the new MutableProperty.
     * @param <T> The type of value that the property will hold.
     * @return The new MutableProperty with the provided initial value.
     */
    static <T> MutableProperty<T> create(T initialValue)
    {
        return MutableObjectProperty.create(initialValue);
    }

    /**
     * Clear any value that has been assigned to this object.
     */
    MutableProperty<T> clear();

    /**
     * Set the value that this MutableProperty contains.
     * @param value The value to set.
     */
    MutableProperty<T> set(T value);
}
