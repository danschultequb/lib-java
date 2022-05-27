package qub;

public abstract class ProcessBase<T extends MutableProcess> implements MutableProcess
{
    private final LazyValue<CharacterToByteWriteStream> outputWriteStream;
    private final LazyValue<CharacterToByteWriteStream> errorWriteStream;
    private final LazyValue<CharacterToByteReadStream> inputReadStream;
    private final LazyValue<Random> random;
    private final LazyValue<FileSystem> fileSystem;
    private final LazyValue<Network> network;
    private final LazyValue<Path> currentFolderPath;
    private final LazyValue<EnvironmentVariables> environmentVariables;
    private final LazyValue<Synchronization> synchronization;
    private final LazyValue<Clock> clock;
    private final LazyValue<Iterable<Display>> displays;
    private final LazyValue<TypeLoader> typeLoader;
    private final LazyValue<DefaultApplicationLauncher> defaultApplicationLauncher;
    private final LazyValue<Map<String,String>> systemProperties;

    private final AsyncScheduler mainAsyncRunner;
    private final AsyncScheduler parallelAsyncRunner;

    private final Disposable disposable;

    protected ProcessBase(AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        this.outputWriteStream = LazyValue.create();
        this.errorWriteStream = LazyValue.create();
        this.inputReadStream = LazyValue.create();
        this.random = LazyValue.create();
        this.fileSystem = LazyValue.create();
        this.network = LazyValue.create();
        this.currentFolderPath = LazyValue.create();
        this.environmentVariables = LazyValue.create();
        this.synchronization = LazyValue.create();
        this.clock = LazyValue.create();
        this.displays = LazyValue.create();
        this.typeLoader = LazyValue.create();
        this.defaultApplicationLauncher = LazyValue.create();
        this.systemProperties = LazyValue.create();

        this.mainAsyncRunner = mainAsyncRunner;
        CurrentThread.setAsyncRunner(mainAsyncRunner);

        this.parallelAsyncRunner = ParallelAsyncRunner.create();

        this.disposable = Disposable.create(() ->
        {
            if (this.inputReadStream.hasValue())
            {
                final Disposable inputReadStream = this.inputReadStream.get();
                if (inputReadStream != null)
                {
                    inputReadStream.dispose().await();
                }
            }

            if (this.outputWriteStream.hasValue())
            {
                final Disposable outputWriteStream = this.outputWriteStream.get();
                if (outputWriteStream != null)
                {
                    outputWriteStream.dispose().await();
                }
            }

            if (this.errorWriteStream.hasValue())
            {
                final Disposable errorWriteStream = this.errorWriteStream.get();
                if (errorWriteStream != null)
                {
                    errorWriteStream.dispose().await();
                }
            }
        });
    }

    @Override
    public AsyncScheduler getMainAsyncRunner()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.mainAsyncRunner;
    }

    @Override
    public AsyncScheduler getParallelAsyncRunner()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.parallelAsyncRunner;
    }

    @Override
    public CharacterToByteWriteStream getOutputWriteStream()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.outputWriteStream.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setOutputWriteStream(CharacterToByteWriteStream outputWriteStream)
    {
        return (T)MutableProcess.super.setOutputWriteStream(outputWriteStream);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setOutputWriteStream(Function0<CharacterToByteWriteStream> outputWriteStream)
    {
        this.outputWriteStream.set(outputWriteStream);

        return (T)this;
    }

    @Override
    public CharacterToByteWriteStream getErrorWriteStream()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.errorWriteStream.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setErrorWriteStream(CharacterToByteWriteStream errorWriteStream)
    {
        return (T)MutableProcess.super.setErrorWriteStream(errorWriteStream);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setErrorWriteStream(Function0<CharacterToByteWriteStream> errorWriteStream)
    {
        this.errorWriteStream.set(errorWriteStream);

        return (T)this;
    }

    /**
     * Get the CharacterToByteReadStream that is assigned to this Console.
     * @return The CharacterToByteReadStream that is assigned to this Console.
     */
    public CharacterToByteReadStream getInputReadStream()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.inputReadStream.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setInputReadStream(CharacterToByteReadStream inputReadStream)
    {
        return (T)MutableProcess.super.setInputReadStream(inputReadStream);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setInputReadStream(Function0<CharacterToByteReadStream> inputReadStream)
    {
        this.inputReadStream.set(inputReadStream);

        return (T)this;
    }

    @Override
    public Random getRandom()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.random.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setRandom(Random random)
    {
        return (T)MutableProcess.super.setRandom(random);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setRandom(Function0<Random> random)
    {
        this.random.set(random);

        return (T)this;
    }

    @Override
    public FileSystem getFileSystem()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.fileSystem.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFileSystem(FileSystem fileSystem)
    {
        return (T)MutableProcess.super.setFileSystem(fileSystem);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFileSystem(Function0<FileSystem> fileSystem)
    {
        this.fileSystem.set(fileSystem);

        return (T)this;
    }

    @Override
    public Network getNetwork()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.network.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setNetwork(Network network)
    {
        return (T)MutableProcess.super.setNetwork(network);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setNetwork(Function0<Network> network)
    {
        this.network.set(network);

        return (T)this;
    }

    @Override
    public Path getCurrentFolderPath()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.currentFolderPath.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCurrentFolderPath(Path currentFolderPath)
    {
        return (T)MutableProcess.super.setCurrentFolderPath(currentFolderPath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCurrentFolderPath(Folder currentFolder)
    {
        return (T)MutableProcess.super.setCurrentFolderPath(currentFolder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setCurrentFolderPath(Function0<Path> currentFolderPath)
    {
        this.currentFolderPath.set(currentFolderPath);

        return (T)this;
    }

    @Override
    public EnvironmentVariables getEnvironmentVariables()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.environmentVariables.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        return (T)MutableProcess.super.setEnvironmentVariables(environmentVariables);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setEnvironmentVariables(Function0<EnvironmentVariables> environmentVariables)
    {
        this.environmentVariables.set(environmentVariables);

        return (T)this;
    }

    @Override
    public Synchronization getSynchronization()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.synchronization.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setSynchronization(Synchronization synchronization)
    {
        return (T)MutableProcess.super.setSynchronization(synchronization);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setSynchronization(Function0<Synchronization> synchronization)
    {
        this.synchronization.set(synchronization);

        return (T)this;
    }

    @Override
    public Clock getClock()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.clock.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setClock(Clock clock)
    {
        return (T)MutableProcess.super.setClock(clock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setClock(Function0<Clock> clock)
    {
        this.clock.set(clock);

        return (T)this;
    }

    @Override
    public Iterable<Display> getDisplays()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.displays.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setDisplays(Iterable<Display> displays)
    {
        return (T)MutableProcess.super.setDisplays(displays);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setDisplays(Function0<Iterable<Display>> displays)
    {
        this.displays.set(displays);

        return (T)this;
    }

    @Override
    public DefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.defaultApplicationLauncher.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        return (T)MutableProcess.super.setDefaultApplicationLauncher(defaultApplicationLauncher);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setDefaultApplicationLauncher(Function0<DefaultApplicationLauncher> defaultApplicationLauncher)
    {
        this.defaultApplicationLauncher.set(defaultApplicationLauncher);

        return (T)this;
    }

    @Override
    public Map<String,String> getSystemProperties()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.systemProperties.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setSystemProperties(Map<String, String> systemProperties)
    {
        return (T)MutableProcess.super.setSystemProperties(systemProperties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setSystemProperties(Function0<Map<String, String>> systemProperties)
    {
        this.systemProperties.set(systemProperties);

        return (T)this;
    }

    @Override
    public TypeLoader getTypeLoader()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.typeLoader.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setTypeLoader(TypeLoader typeLoader)
    {
        return (T)MutableProcess.super.setTypeLoader(typeLoader);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setTypeLoader(Function0<TypeLoader> typeLoader)
    {
        this.typeLoader.set(typeLoader);

        return (T)this;
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
