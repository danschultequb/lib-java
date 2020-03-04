package qub;

/**
 * An event that will be run with no arguments.
 */
public class RunnableEvent0 implements Event0, Action0
{
    private final List<Action0> callbacks;

    /**
     * Create a new runnable event.
     */
    private RunnableEvent0()
    {
        this.callbacks = List.create();
    }

    /**
     * Create a new runnable event.
     */
    public static RunnableEvent0 create()
    {
        return new RunnableEvent0();
    }

    @Override
    public Disposable add(Action0 callback)
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
    @Override
    public void run()
    {
        for (final Action0 callback : this.callbacks)
        {
            callback.run();
        }
    }
}
