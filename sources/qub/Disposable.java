package qub;

public interface Disposable extends java.lang.AutoCloseable
{
    /**
     * Create a new Disposable that will dispose of the provided disposables when it is disposed.
     * @return The new Disposable.
     */
    static Disposable create(Disposable... disposables)
    {
        PreCondition.assertNotNull(disposables, "disposables");

        return Disposable.create(Iterable.create(disposables));
    }

    /**
     * Create a new Disposable that will dispose of the provided disposables when it is disposed.
     * @return The new Disposable.
     */
    static Disposable create(Iterable<Disposable> disposables)
    {
        PreCondition.assertNotNull(disposables, "disposables");

        return Disposable.create(() ->
        {
            for (final Disposable disposable : disposables)
            {
                disposable.dispose().await();
            }
        });
    }

    /**
     * Create a new Disposable that will invoke the provided action when it is disposed.
     * @param action The action to invoke when the Disposable is disposed.
     * @return The new Disposable.
     */
    static Disposable create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final Disposable result = DisposableAction.create(action);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    default void close()
    {
        this.dispose().await();
    }

    /**
     * Get whether or not this Disposable has been disposed yet.
     * @return Whether or not this Disposable has been disposed yet.
     */
    boolean isDisposed();

    /**
     * Dispose this disposable. This function returns whether or not this Disposable was disposed.
     * If false is returned, the most likely scenario is that this Disposable was already disposed
     * previously.
     * @return Whether or not this Disposable was disposed.
     */
    Result<Boolean> dispose();
}
