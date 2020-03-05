package qub;

/**
 * An event that will be run with no arguments.
 */
public interface Event0
{
    /**
     * Create a new runnable event.
     * @return A new runnable event.
     */
    static RunnableEvent0 create()
    {
        return RunnableEvent0.create();
    }

    /**
     * Add the provided callback to this event. When this event is triggered, the provided callback
     * will be run.
     * @param callback The callback to add.
     * @return A Disposable that can be disposed to remove the provided callback from this event.
     */
    Disposable subscribe(Action0 callback);
}
