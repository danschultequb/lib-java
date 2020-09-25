package qub;

public class BasicSave implements Save
{
    private final Action0 onRestore;
    private final Disposable disposable;

    private BasicSave(Action0 onRestore, Action0 onDispose)
    {
        PreCondition.assertNotNull(onRestore, "onRestore");
        PreCondition.assertNotNull(onDispose, "onDispose");

        this.onRestore = onRestore;
        this.disposable = Disposable.create(onDispose);
    }

    public static BasicSave create(Action0 onRestore)
    {
        return BasicSave.create(onRestore, Action0.empty);
    }

    public static BasicSave create(Action0 onRestore, Action0 onDisposed)
    {
        return new BasicSave(onRestore, onDisposed);
    }

    @Override
    public Result<Void> restore()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(this.onRestore);
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposable.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.disposable.dispose();
    }
}
