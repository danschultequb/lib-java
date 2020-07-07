package qub;

/**
 * A distance value that can change.
 */
public interface DynamicDistance
{
    /**
     * Get the current distance value of this object.
     * @return The current distance value of this object.
     */
    Distance getValue();

    /**
     * Register the provided callback to be run when this object's distance changes.
     * @param callback The callback to run when this object's distance changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onChanged(Action0 callback);

    /**
     * Create a new FixedDynamicDistance from the provided Distance.
     * @param distance The Distance that the new FixedDynamicDistance will return.
     * @return The new FixedDynamicDistance.
     */
    static FixedDynamicDistance fixed(Distance distance)
    {
        return FixedDynamicDistance.create(distance);
    }
}
