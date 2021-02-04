package qub;

/**
 * A fake DefaultApplicationLauncher implementation that does nothing.
 */
public class FakeDefaultApplicationLauncher implements DefaultApplicationLauncher
{
    private final FileSystem fileSystem;
    private final List<Path> pathsOpened;

    private FakeDefaultApplicationLauncher(FileSystem fileSystem)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");

        this.fileSystem = fileSystem;
        this.pathsOpened = List.create();
    }

    public static FakeDefaultApplicationLauncher create(FileSystem fileSystem)
    {
        return new FakeDefaultApplicationLauncher(fileSystem);
    }

    @Override
    public Result<Void> openWithDefaultApplication(Path pathToOpen)
    {
        PreCondition.assertNotNull(pathToOpen, "pathToOpen");
        PreCondition.assertTrue(pathToOpen.isRooted(), "pathToOpen.isRooted()");

        return this.openWithDefaultApplication(pathToOpen, pathToOpen.endsWith('/') || pathToOpen.endsWith('\\'));
    }

    @Override
    public Result<Void> openFileWithDefaultApplication(File fileToOpen)
    {
        PreCondition.assertNotNull(fileToOpen, "fileToOpen");

        return this.openWithDefaultApplication(fileToOpen.getPath(), false);
    }

    @Override
    public Result<Void> openFolderWithDefaultApplication(Folder folderToOpen)
    {
        PreCondition.assertNotNull(folderToOpen, "folderToOpen");

        return this.openWithDefaultApplication(folderToOpen.getPath(), true);
    }

    private Result<Void> openWithDefaultApplication(Path pathToOpen, boolean isFolderPath)
    {
        PreCondition.assertNotNull(pathToOpen, "pathToOpen");
        PreCondition.assertTrue(pathToOpen.isRooted(), "pathToOpen.isRooted()");

        return Result.create(() ->
        {
            if (isFolderPath)
            {
                if (!this.fileSystem.folderExists(pathToOpen).await())
                {
                    throw new FolderNotFoundException(pathToOpen);
                }
            }
            else
            {
                if (!this.fileSystem.fileExists(pathToOpen).await())
                {
                    throw new FileNotFoundException(pathToOpen);
                }
            }
            this.pathsOpened.add(pathToOpen);
        });
    }

    public Iterable<Path> getPathsOpened()
    {
        return this.pathsOpened;
    }
}
