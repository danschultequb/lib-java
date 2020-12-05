package qub;

public class FakeDesktopProcess extends DesktopProcess
{
    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return FakeDesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return FakeDesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return FakeDesktopProcess.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return new FakeDesktopProcess(commandLineArguments, mainAsyncRunner);
    }

    protected FakeDesktopProcess(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        super(commandLineArguments, mainAsyncRunner);
    }

    @Override
    protected long createDefaultProcessId()
    {
        return 1;
    }

    @Override
    public FakeDesktopProcess setProcessId(long processId)
    {
        return (FakeDesktopProcess)super.setProcessId(processId);
    }

    @Override
    protected ProcessFactory createDefaultProcessFactory()
    {
        return FakeProcessFactory.create(this);
    }

    @Override
    public FakeDesktopProcess setProcessFactory(ProcessFactory processFactory)
    {
        return (FakeDesktopProcess)super.setProcessFactory(processFactory);
    }

    @Override
    public FakeDesktopProcess setSystemProperty(String systemPropertyName, String systemPropertyValue)
    {
        return (FakeDesktopProcess)super.setSystemProperty(systemPropertyName, systemPropertyValue);
    }

    @Override
    public FakeDesktopProcess setJVMClasspath(String jvmClasspath)
    {
        return (FakeDesktopProcess)super.setJVMClasspath(jvmClasspath);
    }

    @Override
    protected CharacterToByteWriteStream createDefaultOutputWriteStream()
    {
        return InMemoryCharacterToByteStream.create();
    }

    @Override
    public FakeDesktopProcess setOutputWriteStream(ByteWriteStream outputWriteStream)
    {
        return (FakeDesktopProcess)super.setOutputWriteStream(outputWriteStream);
    }

    @Override
    public FakeDesktopProcess setOutputWriteStream(CharacterToByteWriteStream outputWriteStream)
    {
        return (FakeDesktopProcess)super.setOutputWriteStream(outputWriteStream);
    }

    @Override
    public CharacterToByteWriteStream createDefaultErrorWriteStream()
    {
        return InMemoryCharacterToByteStream.create();
    }

    @Override
    public FakeDesktopProcess setErrorWriteStream(ByteWriteStream errorWriteStream)
    {
        return (FakeDesktopProcess)super.setErrorWriteStream(errorWriteStream);
    }

    @Override
    public FakeDesktopProcess setErrorWriteStream(CharacterToByteWriteStream errorWriteStream)
    {
        return (FakeDesktopProcess)super.setErrorWriteStream(errorWriteStream);
    }

    @Override
    protected CharacterToByteReadStream createDefaultInputReadStream()
    {
        return InMemoryCharacterToByteStream.create().endOfStream();
    }

    @Override
    public FakeDesktopProcess setInputReadStream(ByteReadStream inputReadStream)
    {
        return (FakeDesktopProcess)super.setInputReadStream(inputReadStream);
    }

    @Override
    public FakeDesktopProcess setInputReadStream(CharacterToByteReadStream inputReadStream)
    {
        return (FakeDesktopProcess)super.setInputReadStream(inputReadStream);
    }

    @Override
    public FakeDesktopProcess setRandom(Random random)
    {
        return (FakeDesktopProcess)super.setRandom(random);
    }

    @Override
    protected Random createDefaultRandom()
    {
        return new FixedRandom(1);
    }

    @Override
    protected FileSystem createDefaultFileSystem()
    {
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(this.getClock());
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    @Override
    public FakeDesktopProcess setFileSystem(FileSystem fileSystem, String currentFolderPath)
    {
        return (FakeDesktopProcess)super.setFileSystem(fileSystem, currentFolderPath);
    }

    @Override
    public FakeDesktopProcess setFileSystem(FileSystem fileSystem, Path currentFolderPath)
    {
        return (FakeDesktopProcess)super.setFileSystem(fileSystem, currentFolderPath);
    }

    @Override
    protected Network createDefaultNetwork()
    {
        return new FakeNetwork(this.getClock());
    }

    @Override
    public FakeDesktopProcess setNetwork(Network network)
    {
        return (FakeDesktopProcess)super.setNetwork(network);
    }

    @Override
    public FakeDesktopProcess setCurrentFolderPathString(String currentFolderPathString)
    {
        return (FakeDesktopProcess)super.setCurrentFolderPathString(currentFolderPathString);
    }

    @Override
    public FakeDesktopProcess setCurrentFolderPath(Path currentFolderPath)
    {
        return (FakeDesktopProcess)super.setCurrentFolderPath(currentFolderPath);
    }

    @Override
    public FakeDesktopProcess setCurrentFolder(Folder currentFolder)
    {
        return (FakeDesktopProcess)super.setCurrentFolder(currentFolder);
    }

    @Override
    protected Folder createDefaultCurrentFolder()
    {
        return this.getFileSystem().getFolder("/").await();
    }

    @Override
    public FakeDesktopProcess setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        return (FakeDesktopProcess)super.setEnvironmentVariables(environmentVariables);
    }

    @Override
    protected EnvironmentVariables createDefaultEnvironmentVariables()
    {
        return new EnvironmentVariables();
    }

    @Override
    protected Synchronization createDefaultSynchronization()
    {
        return new Synchronization();
    }

    @Override
    public FakeDesktopProcess setClock(Clock clock)
    {
        return (FakeDesktopProcess)super.setClock(clock);
    }

    @Override
    protected Clock createDefaultClock()
    {
        return ManualClock.create(DateTime.epoch, this.getMainAsyncRunner());
    }

    @Override
    public FakeDesktopProcess setDisplays(Iterable<Display> displays)
    {
        return (FakeDesktopProcess)super.setDisplays(displays);
    }

    @Override
    protected Iterable<Display> createDefaultDisplays()
    {
        return Iterable.create(new Display(1000, 1000, 100, 100));
    }

    @Override
    protected DefaultApplicationLauncher createDefaultApplicationLauncher()
    {
        return FakeDefaultApplicationLauncher.create();
    }

    @Override
    public FakeDesktopProcess setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        return (FakeDesktopProcess)super.setDefaultApplicationLauncher(defaultApplicationLauncher);
    }

    @Override
    public FakeDesktopProcess setMainClassFullName(String mainClassFullName)
    {
        return (FakeDesktopProcess)super.setMainClassFullName(mainClassFullName);
    }

    @Override
    public String createDefaultMainClassFullName()
    {
        return "fake.MainClassFullName";
    }

    @Override
    protected TypeLoader createDefaultTypeLoader()
    {
        return FakeTypeLoader.create();
    }

    @Override
    public FakeDesktopProcess setTypeLoader(TypeLoader typeLoader)
    {
        return (FakeDesktopProcess)super.setTypeLoader(typeLoader);
    }
}
