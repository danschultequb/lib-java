package qub;

public interface Disposable extends java.lang.AutoCloseable
{
    /**
     * Create a new DisposableList that will wrap around the provided disposables.
     * @return The new DisposableList.
     */
    static DisposableList create(Disposable... disposables)
    {
        return DisposableList.create(disposables);
    }

    /**
     * Create a new Disposable that will invoke the provided action when it is disposed.
     * @param onDispose The action to invoke when the returned Disposable is disposed.
     * @return The new Disposable.
     */
    static Disposable create(Action0 onDispose)
    {
        return DisposableAction.create(onDispose);
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
