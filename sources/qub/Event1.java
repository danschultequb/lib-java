package qub;

/**
 * An event that will be run with one argument.
 */
public interface Event1<T>
{
    /**
     * Create a new event.
     * @return A new event.
     */
    static <T> RunnableEvent1<T> create()
    {
        return RunnableEvent1.create();
    }

    /**
     * Add the provided callback to this event. When this event is triggered, the provided callback
     * will be run.
     * @param callback The callback to add.
     * @return A Disposable that can be disposed to remove the provided callback from this event.
     */
    Disposable add(Action1<T> callback);
}
