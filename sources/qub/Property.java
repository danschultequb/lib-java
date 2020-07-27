package qub;

/**
 * An immutable property.
 * @param <T> The type of value that this property holds.
 */
public interface Property<T> extends Getter<T>, Event1<T>, Event2<T,T>
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

    /**
     * Subscribe the provided callback to be invoked whenever this Property's value changes.
     * @param callback The callback to add. The first argument is the old value (or null if this
     *                 property didn't have an old value) and the second argument is the new
     *                 value (or null if this property doesn't have a new value).
     * @return A Disposable that can be disposed to remove the provided callback from this
     * Property.
     */
    @Override
    Disposable subscribe(Action2<T,T> callback);

    /**
     * Subscribe the provided callback to be invoked whenever this Property's value changes.
     * @param callback The callback to add. The argument is the new value (or null if this property
     *                 doesn't have a new value).
     * @return A Disposable that can be disposed to remove the provided callback from this
     * Property.
     */
    @Override
    default Disposable subscribe(Action1<T> callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.subscribe((T oldValue, T newValue) -> { callback.run(newValue); });
    }

    /**
     * Subscribe the provided callback to be invoked whenever this Property's value changes.
     * @param callback The callback to add.
     * @return A Disposable that can be disposed to remove the provided callback from this
     * Property.
     */
    @Override
    default Disposable subscribe(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.subscribe((T oldValue, T newValue) -> { callback.run(); });
    }
}
