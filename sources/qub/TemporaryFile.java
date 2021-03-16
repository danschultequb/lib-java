package qub;

public class TemporaryFile extends File implements Disposable
{
    private final RunnableEvent0 onDisposed;
    private final DisposableAction disposable;

    private TemporaryFile(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);

        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertTrue(path.isRooted(), "path.isRooted()");

        this.onDisposed = Event0.create();
        this.disposable = DisposableAction.create(() ->
        {
            fileSystem.deleteFile(path)
                .catchError(NotFoundException.class)
                .await();
            this.onDisposed.run();
        });
    }

    public static TemporaryFile get(FileSystem fileSystem, String path)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNullAndNotEmpty(path, "path");

        return TemporaryFile.get(fileSystem, Path.parse(path));
    }

    public static TemporaryFile get(FileSystem fileSystem, Path path)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertTrue(path.isRooted(), "path.isRooted()");

        return new TemporaryFile(fileSystem, path);
    }

    public static TemporaryFile get(File file)
    {
        PreCondition.assertNotNull(file, "file");

        return TemporaryFile.get(file.getFileSystem(), file.getPath());
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

    /**
     * Subscribe the provided callback to be invoked when this TemporaryFile is disposed.
     * @param callback The action to invoke when this TemporaryFile is disposed.
     * @return The Disposable to dispose to unsubscribe the provided callback.
     */
    public Disposable onDisposed(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.onDisposed.subscribe(callback);
    }
}
