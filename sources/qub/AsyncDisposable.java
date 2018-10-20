package qub;

/**
 * A Disposable that can be disposed asynchronously.
 */
public interface AsyncDisposable extends Disposable
{
    /**
     * The AsyncRunner that this object can be asynchronously disposed on.
     * @return
     */
    AsyncRunner getAsyncRunner();

    /**
     * Asynchronously dispose of this object by calling dispose() on this object's AsyncRunner.
     * @return The AsyncTask that will dispose of this object.
     */
    default AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return getAsyncRunner().scheduleSingle(this::dispose);
    }
}
