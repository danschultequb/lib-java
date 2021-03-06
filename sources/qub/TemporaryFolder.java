package qub;

public class TemporaryFolder extends Folder implements Disposable
{
    private final Disposable disposable;

    private TemporaryFolder(FileSystem fileSystem, Path folderPath)
    {
        super(fileSystem, folderPath);

        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(folderPath, "folderPath");
        PreCondition.assertTrue(folderPath.isRooted(), "folderPath.isRooted()");

        this.disposable = Disposable.create(() ->
        {
            fileSystem.deleteFolder(folderPath)
                .catchError(NotFoundException.class)
                .await();
        });
    }

    public static TemporaryFolder get(FileSystem fileSystem, String path)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNullAndNotEmpty(path, "path");

        return TemporaryFolder.get(fileSystem, Path.parse(path));
    }

    public static TemporaryFolder get(FileSystem fileSystem, Path path)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertTrue(path.isRooted(), "path.isRooted()");

        return new TemporaryFolder(fileSystem, path);
    }

    public static TemporaryFolder get(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        return TemporaryFolder.get(folder.getFileSystem(), folder.getPath());
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
