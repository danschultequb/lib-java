package qub;

/**
 * A distance value that can change.
 */
public interface DynamicDistance extends Disposable
{
    /**
     * Create a new DynamicDistance object from the provided getter and event subscription
     * functions.
     * @param valueGetter The function that will get the Distance value.
     * @param valueChangedSubscriptionFunction The function that will subscribe to change
     *                                         notifications about the Distance value.
     * @return A new DynamicDistance object.
     */
    static DynamicDistance create(Function0<Distance> valueGetter, Function1<Action0,Disposable> valueChangedSubscriptionFunction)
    {
        return BasicDynamicDistance.create(valueGetter, valueChangedSubscriptionFunction);
    }

    /**
     * Get the current distance value of this object.
     * @return The current distance value of this object.
     */
    Distance get();

    /**
     * Register the provided callback to be run when this object's distance changes.
     * @param callback The callback to run when this object's distance changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onChanged(Action0 callback);
}
