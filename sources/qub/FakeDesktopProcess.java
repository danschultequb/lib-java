package qub;

public class FakeDesktopProcess extends DesktopProcessBase
{
    private final Value<Path> currentFolderPath;
    private final Value<InMemoryCharacterToByteStream> input;

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

        this.currentFolderPath = Value.create(Path.parse("/"));
        this.input = Value.create(InMemoryCharacterToByteStream.create().endOfStream());
    }

    @Override
    protected long getProcessIdValue()
    {
        return 1;
    }

    @Override
    public FakeProcessFactory getProcessFactory()
    {
        return (FakeProcessFactory)super.getProcessFactory();
    }

    @Override
    protected FakeProcessFactory createDefaultProcessFactory()
    {
        return FakeProcessFactory.create(this);
    }

    @Override
    public InMemoryCharacterToByteStream getOutputWriteStream()
    {
        return (InMemoryCharacterToByteStream)super.getOutputWriteStream();
    }

    @Override
    protected InMemoryCharacterToByteStream createDefaultOutputWriteStream()
    {
        return InMemoryCharacterToByteStream.create();
    }

    @Override
    public InMemoryCharacterToByteStream getErrorWriteStream()
    {
        return (InMemoryCharacterToByteStream)super.getErrorWriteStream();
    }

    @Override
    public InMemoryCharacterToByteStream createDefaultErrorWriteStream()
    {
        return InMemoryCharacterToByteStream.create();
    }

    @Override
    public InMemoryCharacterToByteStream getInputReadStream()
    {
        return (InMemoryCharacterToByteStream)super.getInputReadStream();
    }

    @Override
    protected InMemoryCharacterToByteStream createDefaultInputReadStream()
    {
        PreCondition.assertTrue(this.input.hasValue(), "this.input.hasValue()");
        PreCondition.assertNotNull(this.input.get(), "this.input.get()");

        final InMemoryCharacterToByteStream result = this.input.get();
        this.input.clear();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public FakeDesktopProcess setDefaultInputReadStream(InMemoryCharacterToByteStream input)
    {
        PreCondition.assertTrue(this.input.hasValue(), "this.input.hasValue()");

        this.input.set(input);

        return this;
    }

    @Override
    public FixedRandom getRandom()
    {
        return (FixedRandom)super.getRandom();
    }

    @Override
    protected FixedRandom createDefaultRandom()
    {
        return new FixedRandom(1);
    }

    @Override
    public InMemoryFileSystem getFileSystem()
    {
        return (InMemoryFileSystem)super.getFileSystem();
    }

    @Override
    protected InMemoryFileSystem createDefaultFileSystem()
    {
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(this.getClock());
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    @Override
    protected Folder createDefaultCurrentFolder()
    {
        PreCondition.assertTrue(this.currentFolderPath.hasValue(), "this.currentFolderPath.hasValue()");

        final Path currentFolderPath = this.currentFolderPath.get();
        this.currentFolderPath.clear();

        return this.getFileSystem().getFolder(currentFolderPath).await();
    }

    public FakeDesktopProcess setDefaultCurrentFolder(String defaultCurrentFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(defaultCurrentFolderPath, "defaultCurrentFolderPath");

        return this.setDefaultCurrentFolder(Path.parse(defaultCurrentFolderPath));
    }

    public FakeDesktopProcess setDefaultCurrentFolder(Path defaultCurrentFolderPath)
    {
        PreCondition.assertNotNull(defaultCurrentFolderPath, "defaultCurrentFolderPath");
        PreCondition.assertTrue(defaultCurrentFolderPath.isRooted(), "defaultCurrentFolderPath.isRooted()");
        PreCondition.assertTrue(this.currentFolderPath.hasValue(), "this.currentFolderPath.hasValue()");

        this.currentFolderPath.set(defaultCurrentFolderPath);

        return this;
    }

    @Override
    public FakeNetwork getNetwork()
    {
        return (FakeNetwork)super.getNetwork();
    }

    @Override
    protected FakeNetwork createDefaultNetwork()
    {
        return new FakeNetwork(this.getClock());
    }

    @Override
    public ManualClock getClock()
    {
        return (ManualClock)super.getClock();
    }

    @Override
    protected ManualClock createDefaultClock()
    {
        return ManualClock.create(DateTime.epoch, this.getMainAsyncRunner());
    }

    @Override
    public FakeDefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        return (FakeDefaultApplicationLauncher)super.getDefaultApplicationLauncher();
    }

    @Override
    protected FakeDefaultApplicationLauncher createDefaultApplicationLauncher()
    {
        return FakeDefaultApplicationLauncher.create(this.getFileSystem());
    }

    @Override
    protected Map<String,String> createDefaultSystemProperties()
    {
        return Map.<String,String>create()
            .set("java.version", "fake-java-version")
            .set("java.class.path", "fake-class-path")
            .set("os.name", "fake-os-name")
            .set("sun.java.command", "fake-sun-java-command");
    }

    @Override
    public FakeTypeLoader getTypeLoader()
    {
        return (FakeTypeLoader)super.getTypeLoader();
    }

    @Override
    protected FakeTypeLoader createDefaultTypeLoader()
    {
        final InMemoryFileSystem fileSystem = this.getFileSystem();
        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
        final File compiledSourcesFile = qubFolder.getCompiledSourcesFile("fake-publisher", "fake-project", "8").await();
        compiledSourcesFile.create().await();

        final FakeTypeLoader result = FakeTypeLoader.create();
        result.addTypeContainer(this.getMainClassFullName(), compiledSourcesFile);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public MutableEnvironmentVariables getEnvironmentVariables()
    {
        return (MutableEnvironmentVariables)super.getEnvironmentVariables();
    }

    @Override
    protected MutableEnvironmentVariables createDefaultEnvironmentVariables()
    {
        return EnvironmentVariables.create();
    }

    @Override
    protected Synchronization createDefaultSynchronization()
    {
        return new Synchronization();
    }

    @Override
    protected Iterable<Display> createDefaultDisplays()
    {
        return Iterable.create(new Display(1000, 1000, 100, 100));
    }

    @Override
    public String createDefaultMainClassFullName()
    {
        return "fake.MainClassFullName";
    }
}