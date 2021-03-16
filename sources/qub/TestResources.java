package qub;

public class TestResources implements Disposable
{
    private final DesktopProcess process;
    private boolean isDisposed;
    private final List<Disposable> toDispose;

    private TestResources(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        this.process = process;
        this.toDispose = List.create();
    }

    public static TestResources create(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return new TestResources(process);
    }

    public FakeDesktopProcess getFakeDesktopProcess()
    {
        final FakeDesktopProcess result = FakeDesktopProcess.create();
        this.toDispose.add(result);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertNotDisposed(result, "result");

        return result;
    }

    public Process getProcess()
    {
        return this.process;
    }

    public Iterable<Display> getDisplays()
    {
        return this.process.getDisplays();
    }

    public Clock getClock()
    {
        return this.process.getClock();
    }

    public AsyncScheduler getMainAsyncRunner()
    {
        return this.process.getMainAsyncRunner();
    }

    public AsyncScheduler getParallelAsyncRunner()
    {
        return this.process.getParallelAsyncRunner();
    }

    public EnvironmentVariables getEnvironmentVariables()
    {
        return this.process.getEnvironmentVariables();
    }

    public FileSystem getFileSystem()
    {
        return this.process.getFileSystem();
    }

    public Folder getCurrentFolder()
    {
        return this.process.getCurrentFolder();
    }

    public Folder getTemporaryFolder()
    {
        return this.getTemporaryFolderInner(null, false);
    }

    public Folder getTemporaryFolder(boolean ensureFolderExists)
    {
        return this.getTemporaryFolderInner(null, ensureFolderExists);
    }

    public Folder getTemporaryFolder(String path)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");

        return this.getTemporaryFolder(Path.parse(path));
    }

    public Folder getTemporaryFolder(String path, boolean ensureFolderExists)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");

        return this.getTemporaryFolder(Path.parse(path), ensureFolderExists);
    }

    public Folder getTemporaryFolder(Path path)
    {
        PreCondition.assertNotNull(path, "path");

        return this.getTemporaryFolderInner(path, false);
    }

    public Folder getTemporaryFolder(Path path, boolean ensureFolderExists)
    {
        PreCondition.assertNotNull(path, "path");

        return this.getTemporaryFolderInner(path, ensureFolderExists);
    }

    private Folder getTestTempFolder()
    {
        final EnvironmentVariables environmentVariables = this.process.getEnvironmentVariables();
        final String tempFolderPath = environmentVariables.get("TEMP").await();
        final FileSystem fileSystem = this.process.getFileSystem();
        final Folder tempFolder = fileSystem.getFolder(tempFolderPath).await();

        final Random random = this.process.getRandom();
        Folder result = null;
        while (result == null)
        {
            result = tempFolder.createFolder(Integers.toString(random.getRandomInteger()))
                .catchError(FolderAlreadyExistsException.class)
                .await();
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertTrue(result.exists().await(), "result.exists().await()");

        return result;
    }

    private Folder getTemporaryFolderInner(Path path, boolean ensureFolderExists)
    {
        TemporaryFolder result;

        if (path != null && path.isRooted())
        {
            final FileSystem fileSystem = this.process.getFileSystem();
            result = TemporaryFolder.get(fileSystem, path);
        }
        else
        {
            final Folder testTempFolder = this.getTestTempFolder();

            if (path == null)
            {
                final Random random = this.process.getRandom();
                path = Path.parse(Integers.toString(random.getRandomInteger()));
            }

            result = TemporaryFolder.get(testTempFolder.getFolder(path).await());
            result.onDisposed(() -> testTempFolder.delete().await());
        }

        if (ensureFolderExists)
        {
            result.create().catchError(AlreadyExistsException.class).await();
        }

        this.toDispose.add(result);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertNotDisposed(result, "result");

        return result;
    }

    public File getTemporaryFile()
    {
        return this.getTemporaryFile(false);
    }

    public File getTemporaryFile(boolean ensureFileExists)
    {
        return this.getTemporaryFileInner(null, ensureFileExists);
    }

    public File getTemporaryFile(String path)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");

        return this.getTemporaryFile(Path.parse(path), false);
    }

    public File getTemporaryFile(String path, boolean ensureFileExists)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");

        return this.getTemporaryFile(Path.parse(path), ensureFileExists);
    }

    public File getTemporaryFile(Path path)
    {
        PreCondition.assertNotNull(path, "path");

        return this.getTemporaryFileInner(path, false);
    }

    public File getTemporaryFile(Path path, boolean ensureFileExists)
    {
        PreCondition.assertNotNull(path, "path");

        return this.getTemporaryFileInner(path, ensureFileExists);
    }

    private File getTemporaryFileInner(Path path, boolean ensureFileExists)
    {
        TemporaryFile result;

        final FileSystem fileSystem = this.process.getFileSystem();

        if (path != null && path.isRooted())
        {
            result = TemporaryFile.get(fileSystem, path);
        }
        else
        {
            final Folder testTempFolder = this.getTestTempFolder();

            if (path == null)
            {
                final Random random = this.process.getRandom();
                path = Path.parse(Integers.toString(random.getRandomInteger()));
            }

            result = TemporaryFile.get(testTempFolder.getFile(path).await());
            result.onDisposed(() -> testTempFolder.delete().await());
        }

        if (ensureFileExists)
        {
            result.create().catchError(AlreadyExistsException.class).await();
        }

        this.toDispose.add(result);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertNotDisposed(result, "result");

        return result;
    }

    public QubFolder getQubFolder()
    {
        return this.process.getQubFolder().await();
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
            final boolean result = !this.isDisposed;
            if (result)
            {
                this.isDisposed = true;

                for (final Disposable disposable : this.toDispose)
                {
                    disposable.dispose().await();
                }
            }
            return result;
        });
    }
}
