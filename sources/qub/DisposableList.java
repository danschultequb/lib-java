package qub;

/**
 * A Disposable that will dispose of a group of other disposables when it is disposed.
 */
public class DisposableList implements List<Disposable>, Disposable
{
    private final List<Disposable> disposables;
    private boolean isDisposed;

    private DisposableList(Iterable<Disposable> disposables)
    {
        PreCondition.assertNotNull(disposables, "disposables");

        this.disposables = List.create();
        this.addAll(disposables);
    }

    public static DisposableList create(Disposable... disposables)
    {
        PreCondition.assertNotNull(disposables, "disposables");

        return DisposableList.create(Iterable.create(disposables));
    }

    public static DisposableList create(Iterable<Disposable> disposables)
    {
        PreCondition.assertNotNull(disposables, "disposables");

        return new DisposableList(disposables);
    }

    @Override
    public int getCount()
    {
        return this.disposables.getCount();
    }

    @Override
    public DisposableList set(int index, Disposable value)
    {
        PreCondition.assertNotDisposed(this, "this");
        PreCondition.assertNotNull(value, "value");

        this.disposables.set(index, value);
        return this;
    }

    @Override
    public DisposableList add(Disposable value)
    {
        PreCondition.assertNotDisposed(this, "this");
        PreCondition.assertNotNull(value, "value");

        this.disposables.add(value);

        return this;
    }

    @Override
    public DisposableList insert(int insertIndex, Disposable value)
    {
        PreCondition.assertNotDisposed(this, "this");

        this.disposables.insert(insertIndex, value);
        return this;
    }

    @Override
    public Disposable removeAt(int index)
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.disposables.removeAt(index);
    }

    @Override
    public DisposableList clear()
    {
        PreCondition.assertNotDisposed(this, "this");

        this.disposables.clear();
        return this;
    }

    @Override
    public boolean isDisposed()
    {
        return this.isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = !this.isDisposed;
            if (result)
            {
                this.isDisposed = true;
                for (final Disposable disposable : this.disposables)
                {
                    if (!disposable.isDisposed())
                    {
                        disposable.dispose().await();
                    }
                }
            }
            return result;
        });
    }

    @Override
    public Disposable get(int index)
    {
        return this.disposables.get(index);
    }

    @Override
    public Iterator<Disposable> iterate()
    {
        return this.disposables.iterate();
    }
}
