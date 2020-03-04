package qub;

/**
 * An event that will be run with two arguments.
 */
public interface Event2<T1,T2>
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
    Disposable add(Action2<T1,T2> callback);
}
