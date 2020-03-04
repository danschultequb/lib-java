package qub;

/**
 * An event that will be run with no arguments.
 */
public class RunnableEvent1<T> implements Event1<T>
{
    private final List<Action1<T>> callbacks;

    /**
     * Create a new event.
     */
    private RunnableEvent1()
    {
        this.callbacks = List.create();
    }

    /**
     * Create a new event.
     */
    public static <T> RunnableEvent1<T> create()
    {
        return new RunnableEvent1<>();
    }

    @Override
    public Disposable add(Action1<T> callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        this.callbacks.add(callback);
        final Disposable result = BasicDisposable.create(() -> this.callbacks.remove(callback));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Run the callbacks that have been registered to this event.
     */
    public void run(T arg)
    {
        for (final Action1<T> callback : this.callbacks)
        {
            callback.run(arg);
        }
    }
}
