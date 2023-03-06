package qub;

/**
 * A wrapper around a value of type T.
 * @param <T> The type of the inner value.
 */
public interface Value<T> extends Getter<T>, Setter<T>
{
    /**
     * Create a new {@link Value} object that stores the provided type.
     * @param <T> The type that the created {@link Value} will contain.
     */
    public static <T> ObjectValue<T> create()
    {
        return ObjectValue.create();
    }

    /**
     * Create a new {@link Value} object that contains the provided value.
     * @param value The value to assign to the new {@link Value} object.
     * @param <T> The type that the created {@link Value} will contain.
     */
    public static <T> ObjectValue<T> create(T value)
    {
        return ObjectValue.create(value);
    }

    /**
     * Create a new {@link BooleanValue} object that contains the provided value.
     * @param value The value to assign to the new {@link BooleanValue} object.
     * @return The new Value object.
     */
    public static BooleanValue create(boolean value)
    {
        return BooleanValue.create(value);
    }

    /**
     * Create a new {@link BooleanValue} object that contains the provided value.
     * @param value The value to assign to the new {@link BooleanValue} object.
     */
    public static BooleanValue create(java.lang.Boolean value)
    {
        return BooleanValue.create(value);
    }

    /**
     * Create a new {@link IntegerValue} object that contains the provided value.
     * @param value The value to assign to the new {@link IntegerValue} object.
     */
    public static IntegerValue create(int value)
    {
        return IntegerValue.create(value);
    }

    /**
     * Create a new {@link IntegerValue} object that contains the provided value.
     * @param value The value to assign to the new {@link IntegerValue} object.
     */
    public static IntegerValue create(java.lang.Integer value)
    {
        return IntegerValue.create(value);
    }

    /**
     * Create a new {@link DynamicValue} object that stores the provided type.
     * @param <T> The type that the created {@link DynamicValue} will contain.
     */
    public static <T> DynamicValue<T> createDynamic()
    {
        return DynamicValue.create();
    }

    /**
     * Create a new {@link DynamicValue} object that stores the provided type.
     * @param initialValue The initial value to store in the new {@link DynamicValue}.
     * @param <T> The type that the created {@link DynamicValue} will contain.
     */
    public static <T> DynamicValue<T> createDynamic(T initialValue)
    {
        return DynamicValue.create(initialValue);
    }

    /**
     * Clear any value that has been assigned to this object.
     */
    public Value<T> clear();

    /**
     * Set the value that this {@link Value} contains.
     * @param value The value to set.
     */
    public Value<T> set(T value);

    /**
     * If this {@link Value} has a value already, then return that value. If not, then set this
     * {@link Value}'s value to the provided initial value and then return that value.
     * @param initialValue The value that this {@link Value} will be initialized to if no value has
     *                     been set.
     */
    public default T getOrSet(T initialValue)
    {
        return this.getOrSet(() -> initialValue);
    }

    /**
     * If this {@link Value} has a value already, then return that value. If not, then set this
     * {@link Value}'s value to the result of the provided creator {@link Function0} and then return
     * that value.
     * @param creator The {@link Function0} that will be used to initialize this {@link Value} if no
     *                value exists.
     */
    public default T getOrSet(Function0<? extends T> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        if (!this.hasValue())
        {
            this.set(creator.run());
        }
        return this.get();
    }

    /**
     * A version of a {@link Value} that returns its own type from chainable methods.
     * @param <T> The type of value stored in this {@link Value.Typed}.
     * @param <ValueT> The real type of this {@link Value.Typed}.
     */
    public static interface Typed<T,ValueT extends Value<T>> extends Value<T>
    {
        @Override
        public ValueT clear();

        @Override
        public ValueT set(T value);
    }
}
