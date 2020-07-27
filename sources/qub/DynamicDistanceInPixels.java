package qub;

/**
 * A distance value in pixels that can change.
 */
public interface DynamicDistanceInPixels
{
    /**
     * Get the current distance value of this object in pixels.
     * @return The current distance value of this object in pixels.
     */
    int getValue();

    /**
     * Register the provided callback to be run when this object's distance changes.
     * @param callback The callback to run when this object's distance changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onChanged(Action0 callback);
}
