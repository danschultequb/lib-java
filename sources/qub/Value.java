package qub;

/**
 * A wrapper around a value of type T.
 * @param <T> The type of the inner value.
 */
public interface Value<T> extends Getter<T>, Setter<T>
{
    /**
     * Create a new Value object that stores the provided type.
     * @param <T> The type that the created Value will contain.
     * @return The new Value object.
     */
    static <T> ObjectValue<T> create()
    {
        return new ObjectValue<>();
    }

    /**
     * Create a new Value object that contains the provided value.
     * @param value The value to assign to the new Value object.
     * @param <T> The type that the created Value will contain.
     * @return The new Value object.
     */
    static <T> ObjectValue<T> create(T value)
    {
        return new ObjectValue<>(value);
    }

    /**
     * Create a new Value object that contains the provided value.
     * @param value The value to assign to the new Value object.
     * @return The new Value object.
     */
    static BooleanValue create(boolean value)
    {
        return new BooleanValue(value);
    }

    /**
     * Create a new Value object that contains the provided value.
     * @param value The value to assign to the new Value object.
     * @return The new Value object.
     */
    static BooleanValue create(java.lang.Boolean value)
    {
        return new BooleanValue(value);
    }

    /**
     * Create a new Value object that contains the provided value.
     * @param value The value to assign to the new Value object.
     * @return The new Value object.
     */
    static IntegerValue create(int value)
    {
        return new IntegerValue(value);
    }

    /**
     * Create a new Value object that contains the provided value.
     * @param value The value to assign to the new Value object.
     * @return The new Value object.
     */
    static IntegerValue create(java.lang.Integer value)
    {
        return new IntegerValue(value);
    }

    /**
     * Clear any value that has been assigned to this object.
     */
    Value<T> clear();

    /**
     * Set the value that this Value contains.
     * @param value The value to set.
     */
    Value<T> set(T value);

    /**
     * If this Value has a value already, then return that value. If not, then set this Value's
     * value to the provided initial value and then return that value.
     * @param initialValue The value that this Value will be initialized to if no value has been
     *                     set.
     * @return The value stored in this Value.
     */
    default T getOrSet(T initialValue)
    {
        return this.getOrSet(() -> initialValue);
    }

    /**
     * If this Value has a value already, then return that value. If not, then set this Value's
     * value to the result of the provided creator function and then return that value.
     * @param creator The function that will be used to initialize this Value if no value exists.
     * @return The value stored in this Value.
     */
    default T getOrSet(Function0<T> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        if (!this.hasValue())
        {
            this.set(creator.run());
        }
        return this.get();
    }
}
