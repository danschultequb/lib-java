package qub;

public class TemporaryFile extends File implements Disposable
{
    private final Disposable disposable;

    private TemporaryFile(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);

        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertTrue(path.isRooted(), "path.isRooted()");

        this.disposable = Disposable.create(() ->
        {
            fileSystem.deleteFile(path)
                .catchError(NotFoundException.class)
                .await();
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
}
