package qub;

public class TestResources extends BasicDisposable
{
    private final DesktopProcess process;
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

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = super.dispose().await();
            if (result)
            {
                for (final Disposable disposable : this.toDispose)
                {
                    disposable.dispose().await();
                }
            }
            return result;
        });
    }

    public FakeDesktopProcess createFakeDesktopProcess(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotDisposed(this, "this");

        final FakeDesktopProcess result = FakeDesktopProcess.create(arguments);
        this.toDispose.add(result);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertNotDisposed(result, "result");

        return result;
    }

    public DesktopProcess getProcess()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process;
    }

    public AsyncScheduler getMainAsyncRunner()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getMainAsyncRunner();
    }

    public AsyncScheduler getParallelAsyncRunner()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getParallelAsyncRunner();
    }

    public Random getRandom()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getRandom();
    }

    public FileSystem getFileSystem()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getFileSystem();
    }

    public Folder getCurrentFolder()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getCurrentFolder();
    }

    public TemporaryFolder getTemporaryFolder()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFolderInner(null, false);
    }

    public TemporaryFolder getTemporaryFolder(boolean ensureFolderExists)
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFolderInner(null, ensureFolderExists);
    }

    public TemporaryFolder getTemporaryFolder(String path)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFolder(Path.parse(path));
    }

    public TemporaryFolder getTemporaryFolder(String path, boolean ensureFolderExists)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFolder(Path.parse(path), ensureFolderExists);
    }

    public TemporaryFolder getTemporaryFolder(Path path)
    {
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFolderInner(path, false);
    }

    public TemporaryFolder getTemporaryFolder(Path path, boolean ensureFolderExists)
    {
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertNotDisposed(this, "this");

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

    private TemporaryFolder getTemporaryFolderInner(Path path, boolean ensureFolderExists)
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
            this.toDispose.add(Disposable.create(() ->
            {
                testTempFolder.delete()
                    .catchError(FolderNotFoundException.class)
                    .await();
            }));
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

    public TemporaryFile getTemporaryFile()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFile(false);
    }

    public TemporaryFile getTemporaryFile(boolean ensureFileExists)
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFileInner(null, ensureFileExists);
    }

    public TemporaryFile getTemporaryFile(String path)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFile(Path.parse(path), false);
    }

    public TemporaryFile getTemporaryFile(String path, boolean ensureFileExists)
    {
        PreCondition.assertNotNullAndNotEmpty(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFile(Path.parse(path), ensureFileExists);
    }

    public TemporaryFile getTemporaryFile(Path path)
    {
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFileInner(path, false);
    }

    public TemporaryFile getTemporaryFile(Path path, boolean ensureFileExists)
    {
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertNotDisposed(this, "this");

        return this.getTemporaryFileInner(path, ensureFileExists);
    }

    private TemporaryFile getTemporaryFileInner(Path path, boolean ensureFileExists)
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
            this.toDispose.add(Disposable.create(() ->
            {
                testTempFolder.delete()
                    .catchError(FolderNotFoundException.class)
                    .await();
            }));
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
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getQubFolder().await();
    }

    public Network getNetwork()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getNetwork();
    }

    public EnvironmentVariables getEnvironmentVariables()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getEnvironmentVariables();
    }

    public Clock getClock()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getClock();
    }

    public Iterable<Display> getDisplays()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getDisplays();
    }

    public Map<String,String> getSystemProperties()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getSystemProperties();
    }

    public TypeLoader getTypeLoader()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getTypeLoader();
    }

    public ChildProcessRunner getProcessFactory()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.process.getChildProcessRunner();
    }
}
