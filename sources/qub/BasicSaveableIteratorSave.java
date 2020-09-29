package qub;

public class BasicSaveableIteratorSave implements Save
{
    private final BasicSaveableIterator<?> iterator;
    private final Disposable disposable;
    private final boolean hasStarted;
    private final int bufferCurrentIndex;

    private BasicSaveableIteratorSave(BasicSaveableIterator<?> iterator)
    {
        PreCondition.assertNotNull(iterator, "iterator");

        this.iterator = iterator;
        this.disposable = Disposable.create(() -> this.iterator.disposeSave(this));
        this.hasStarted = iterator.hasStarted();
        this.bufferCurrentIndex = iterator.getBufferCurrentIndex();
    }

    public static BasicSaveableIteratorSave create(BasicSaveableIterator<?> iterator)
    {
        return new BasicSaveableIteratorSave(iterator);
    }

    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    public int getBufferCurrentIndex()
    {
        return this.bufferCurrentIndex;
    }

    @Override
    public Result<Void> restore()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            this.iterator.restoreSave(this);
        });
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
