package qub;

/**
 * A {@link Value} type that can be listened to for changes.
 * @param <T> The type of value stored in this {@link DynamicValue}.
 */
public interface DynamicValue<T> extends Value<T>
{
    /**
     * Create a new {@link DynamicValue} object that stores the provided type.
     * @param <T> The type that the created {@link DynamicValue} will contain.
     */
    public static <T> DynamicValue<T> create()
    {
        return DynamicObjectValue.create();
    }

    /**
     * Create a new {@link DynamicValue} object that stores the provided type.
     * @param initialValue The initial value to store in the new {@link DynamicValue}.
     * @param <T> The type that the created {@link DynamicValue} will contain.
     */
    public static <T> DynamicValue<T> create(T initialValue)
    {
        return DynamicObjectValue.create(initialValue);
    }

    @Override
    public DynamicValue<T> set(T value);

    @Override
    public DynamicValue<T> clear();

    /**
     * Run the provided {@link Action0} when this {@link DynamicValue}'s value changes.
     * @param action The {@link Action0} to run when this {@link DynamicValue}'s value changes.
     * @return A {@link Disposable} that can be disposed to unregister the provided {@link Action0}.
     */
    public default Disposable onChanged(Action0 action)
    {
        return this.onChanged((T previousValue, T newValue) -> action.run());
    }

    /**
     * Run the provided {@link Action1} when this {@link DynamicValue}'s value changes. The new
     * value will be passed as the argument to the {@link Action1}.
     * @param action The {@link Action1} to run when this {@link DynamicValue}'s value changes.
     * @return A {@link Disposable} that can be disposed to unregister the provided {@link Action0}.
     */
    public default Disposable onChanged(Action1<T> action)
    {
        return this.onChanged((T previousValue, T newValue) -> action.run(newValue));
    }

    /**
     * Run the provided {@link Action2} when this {@link DynamicValue}'s value changes. The previous
     * and new values will be passed as the arguments to the {@link Action2}.
     * @param action The {@link Action2} to run when this {@link DynamicValue}'s value changes.
     * @return A {@link Disposable} that can be disposed to unregister the provided {@link Action0}.
     */
    public Disposable onChanged(Action2<T,T> action);
}
