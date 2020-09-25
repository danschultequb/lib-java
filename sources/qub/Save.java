package qub;

/**
 * A type that represents the state of a Saveable object.
 */
public interface Save extends Disposable
{
    static Save create(Action0 onRestore)
    {
        return BasicSave.create(onRestore);
    }

    static Save create(Action0 onRestore, Action0 onDisposed)
    {
        return BasicSave.create(onRestore, onDisposed);
    }

    /**
     * Restore this Save's state to its associated Saveable object.
     */
    Result<Void> restore();
}
