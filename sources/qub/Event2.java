package qub;

/**
 * An event that will be run with two arguments.
 */
public interface Event2<T1,T2> extends Event0
{
    /**
     * Create a new event.
     * @return A new event.
     */
    static <T1,T2> RunnableEvent2<T1,T2> create()
    {
        return RunnableEvent2.create();
    }

    /**
     * Add the provided callback to this event. When this event is triggered, the provided callback
     * will be run.
     * @param callback The callback to add.
     * @return A Disposable that can be disposed to remove the provided callback from this event.
     */
    Disposable subscribe(Action2<T1,T2> callback);

    /**
     * Add the provided callback to this event. When this event is triggered, the provided callback
     * will be run.
     * @param callback The callback to add.
     * @return A Disposable that can be disposed to remove the provided callback from this event.
     */
    @Override
    default Disposable subscribe(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.subscribe((T1 arg1, T2 arg2) -> { callback.run(); });
    }
}
