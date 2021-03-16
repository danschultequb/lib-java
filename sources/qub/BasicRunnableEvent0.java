package qub;

/**
 * An event that will be run with no arguments.
 */
public class BasicRunnableEvent0 implements RunnableEvent0
{
    private final List<Action0> callbacks;

    /**
     * Create a new runnable event.
     */
    private BasicRunnableEvent0()
    {
        this.callbacks = List.create();
    }

    /**
     * Create a new runnable event.
     */
    public static BasicRunnableEvent0 create()
    {
        return new BasicRunnableEvent0();
    }

    @Override
    public Disposable subscribe(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        this.callbacks.add(callback);
        final Disposable result = DisposableAction.create(() -> this.callbacks.remove(callback));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public void run()
    {
        if (this.callbacks.any())
        {
            for (final Action0 callback : List.create(this.callbacks))
            {
                callback.run();
            }
        }
    }
}
