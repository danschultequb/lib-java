package qub;

/**
 * An event that will be run with no arguments.
 */
public class BasicRunnableEvent2<T1,T2> implements RunnableEvent2<T1,T2>
{
    private final List<Action2<T1,T2>> callbacks;

    /**
     * Create a new event.
     */
    private BasicRunnableEvent2()
    {
        this.callbacks = List.create();
    }

    /**
     * Create a new event.
     */
    public static <T1,T2> BasicRunnableEvent2<T1,T2> create()
    {
        return new BasicRunnableEvent2<>();
    }

    @Override
    public Disposable subscribe(Action2<T1,T2> callback)
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
    public void run(T1 arg1, T2 arg2)
    {
        for (final Action2<T1,T2> callback : this.callbacks)
        {
            callback.run(arg1, arg2);
        }
    }
}
