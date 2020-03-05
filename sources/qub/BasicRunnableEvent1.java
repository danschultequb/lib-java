package qub;

/**
 * An event that will be run with no arguments.
 */
public class BasicRunnableEvent1<T> implements RunnableEvent1<T>
{
    private final List<Action1<T>> callbacks;

    /**
     * Create a new event.
     */
    private BasicRunnableEvent1()
    {
        this.callbacks = List.create();
    }

    /**
     * Create a new event.
     */
    public static <T> BasicRunnableEvent1<T> create()
    {
        return new BasicRunnableEvent1<>();
    }

    @Override
    public Disposable subscribe(Action1<T> callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        this.callbacks.add(callback);
        final Disposable result = BasicDisposable.create(() -> this.callbacks.remove(callback));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public void run(T arg)
    {
        for (final Action1<T> callback : this.callbacks)
        {
            callback.run(arg);
        }
    }
}
