package qub;

public abstract class ProcessBase implements Process
{
    private final Value<CharacterToByteWriteStream> outputWriteStream;
    private final Value<CharacterToByteWriteStream> errorWriteStream;
    private final Value<CharacterToByteReadStream> inputReadStream;
    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<Network> network;
    private final Value<Folder> currentFolder;
    private final Value<EnvironmentVariables> environmentVariables;
    private final Value<Synchronization> synchronization;
    private final Value<Clock> clock;
    private final Value<Iterable<Display>> displays;
    private final Value<TypeLoader> typeLoader;
    private final Value<DefaultApplicationLauncher> defaultApplicationLauncher;
    private final Value<Map<String,String>> systemProperties;

    private final AsyncScheduler mainAsyncRunner;
    private final AsyncScheduler parallelAsyncRunner;

    private volatile boolean disposed;

    protected ProcessBase(AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        this.outputWriteStream = Value.create();
        this.errorWriteStream = Value.create();
        this.inputReadStream = Value.create();

        this.random = Value.create();
        this.fileSystem = Value.create();
        this.network = Value.create();
        this.currentFolder = Value.create();
        this.environmentVariables = Value.create();
        this.synchronization = Value.create();
        this.clock = Value.create();
        this.displays = Value.create();
        this.typeLoader = Value.create();
        this.defaultApplicationLauncher = Value.create();
        this.systemProperties = Value.create();

        this.mainAsyncRunner = mainAsyncRunner;
        CurrentThread.setAsyncRunner(mainAsyncRunner);

        this.parallelAsyncRunner = new ParallelAsyncRunner();
    }

    @Override
    public AsyncScheduler getMainAsyncRunner()
    {
        return this.mainAsyncRunner;
    }

    @Override
    public AsyncScheduler getParallelAsyncRunner()
    {
        return this.parallelAsyncRunner;
    }

    @Override
    public CharacterToByteWriteStream getOutputWriteStream()
    {
        return this.outputWriteStream.getOrSet(this::createDefaultOutputWriteStream);
    }

    protected abstract CharacterToByteWriteStream createDefaultOutputWriteStream();

    @Override
    public CharacterToByteWriteStream getErrorWriteStream()
    {
        return this.errorWriteStream.getOrSet(this::createDefaultErrorWriteStream);
    }

    protected abstract CharacterToByteWriteStream createDefaultErrorWriteStream();

    /**
     * Get the CharacterToByteReadStream that is assigned to this Console.
     * @return The CharacterToByteReadStream that is assigned to this Console.
     */
    public CharacterToByteReadStream getInputReadStream()
    {
        return this.inputReadStream.getOrSet(this::createDefaultInputReadStream);
    }

    protected abstract CharacterToByteReadStream createDefaultInputReadStream();

    @Override
    public Random getRandom()
    {
        return this.random.getOrSet(this::createDefaultRandom);
    }

    protected abstract Random createDefaultRandom();

    @Override
    public FileSystem getFileSystem()
    {
        return this.fileSystem.getOrSet(this::createDefaultFileSystem);
    }

    protected abstract FileSystem createDefaultFileSystem();

    @Override
    public Network getNetwork()
    {
        return this.network.getOrSet(this::createDefaultNetwork);
    }

    protected abstract Network createDefaultNetwork();

    @Override
    public Folder getCurrentFolder()
    {
        PreCondition.assertNotNull(this.getFileSystem(), "this.getFileSystem()");

        return this.currentFolder.getOrSet(this::createDefaultCurrentFolder);
    }

    protected abstract Folder createDefaultCurrentFolder();

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    public EnvironmentVariables getEnvironmentVariables()
    {
        return this.environmentVariables.getOrSet(this::createDefaultEnvironmentVariables);
    }

    protected abstract EnvironmentVariables createDefaultEnvironmentVariables();

    /**
     * Get the Synchronization factory for creating synchronization objects.
     * @return The Synchronization factory for creating synchronization objects.
     */
    public Synchronization getSynchronization()
    {
        return this.synchronization.getOrSet(this::createDefaultSynchronization);
    }

    protected abstract Synchronization createDefaultSynchronization();

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    public Clock getClock()
    {
        return this.clock.getOrSet(this::createDefaultClock);
    }

    protected abstract Clock createDefaultClock();

    /**
     * Get the displays that have been assigned to this Process.
     * @return The displays that have been assigned to this Process.
     */
    public Iterable<Display> getDisplays()
    {
        return this.displays.getOrSet(this::createDefaultDisplays);
    }

    protected abstract Iterable<Display> createDefaultDisplays();

    /**
     * Get the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @return The DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     */
    public DefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        return this.defaultApplicationLauncher.getOrSet(this::createDefaultApplicationLauncher);
    }

    protected abstract DefaultApplicationLauncher createDefaultApplicationLauncher();

    @Override
    public Map<String,String> getSystemProperties()
    {
        return this.systemProperties.getOrSet(this::createDefaultSystemProperties);
    }

    protected abstract Map<String,String> createDefaultSystemProperties();

    @Override
    public TypeLoader getTypeLoader()
    {
        return this.typeLoader.getOrSet(this::createDefaultTypeLoader);
    }

    protected abstract TypeLoader createDefaultTypeLoader();

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = !this.disposed;
            if (!result)
            {
                this.disposed = true;

                if (this.inputReadStream.hasValue())
                {
                    this.inputReadStream.get().dispose().await();
                }

                if (this.outputWriteStream.hasValue())
                {
                    this.outputWriteStream.get().dispose().await();
                }

                if (this.errorWriteStream.hasValue())
                {
                    this.errorWriteStream.get().dispose().await();
                }
            }
            return result;
        });
    }
}
