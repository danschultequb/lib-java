package qub;

/**
 * An object that can be disposed.
 */
public interface Disposable extends java.lang.AutoCloseable
{
    /**
     * Create a new {@link Disposable} that will dispose of the provided {@link Disposable}s when
     * it is disposed.
     */
    public static Disposable create(Disposable... disposables)
    {
        PreCondition.assertNotNull(disposables, "disposables");

        return Disposable.create(Iterable.create(disposables));
    }

    /**
     * Create a new {@link Disposable} that will dispose of the provided {@link Disposable}s when
     * it is disposed.
     */
    public static Disposable create(Iterable<? extends Disposable> disposables)
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
     * Create a new {@link Disposable} that will invoke the provided {@link Action0} when it is
     * disposed.
     * @param action The {@link Action0} to invoke when the {@link Disposable} is disposed.
     */
    public static Disposable create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final Disposable result = DisposableAction.create(action);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public default void close()
    {
        this.dispose().await();
    }

    /**
     * Get whether this {@link Disposable} has been disposed.
     */
    public boolean isDisposed();

    /**
     * Dispose this {@link Disposable}. This function returns whether this Disposable was disposed.
     * If false is returned, then this {@link Disposable} was already disposed previously.
     */
    public Result<Boolean> dispose();
}
