package qub;

public class FakeDesktopProcess extends DesktopProcessBase<FakeDesktopProcess>
{
    /**
     * Create a new JavaProcess object with the provided command line arguments.
     */
    public static FakeDesktopProcess create(AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return FakeDesktopProcess.create(CommandLineArguments.create(), mainAsyncRunner);
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     *
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return FakeDesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     *
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return FakeDesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     *
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static FakeDesktopProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return FakeDesktopProcess.create(commandLineArguments, ManualAsyncRunner.create());
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     *
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

        this.setCurrentFolderPath("/");
        this.setChildProcessRunner(() -> FakeChildProcessRunner.create(this));
        this.setInputReadStream(InMemoryCharacterToByteStream.create().endOfStream());
        this.setOutputWriteStream(InMemoryCharacterToByteStream.create());
        this.setErrorWriteStream(InMemoryCharacterToByteStream.create());
        this.setClock(() -> ManualClock.create(DateTime.epoch, this.getMainAsyncRunner()));
        this.setFileSystem(() ->
        {
            final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(this.getClock());
            fileSystem.createRoot("/").await();
            return fileSystem;
        });
        this.setNetwork(() -> FakeNetwork.create(this.getClock()));
        this.setDefaultApplicationLauncher(() -> FakeDefaultApplicationLauncher.create(this.getFileSystem()));
        this.setSystemProperties(Map.<String,String>create()
            .set("java.version", "fake-java-version")
            .set("java.class.path", "fake-class-path")
            .set("os.name", "fake-os-name")
            .set("sun.java.command", "fake-sun-java-command"));
        this.setTypeLoader(() ->
        {
            final InMemoryFileSystem fileSystem = this.getFileSystem();
            final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
            final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake-publisher", "fake-project", "8").await();
            final File compiledSourcesFile = projectVersionFolder.createFile("fake-project.jar").await();

            final FakeTypeLoader result = FakeTypeLoader.create();
            result.addTypeContainer(this.getMainClassFullName(), compiledSourcesFile);

            PostCondition.assertNotNull(result, "result");

            return result;
        });
        this.setEnvironmentVariables(EnvironmentVariables.create());
        this.setSynchronization(new Synchronization());
        this.setRandom(new JavaRandom());
        this.setDisplays(Iterable.create(new Display(1000, 1000, 100, 100)));
        this.setMainClassFullName("fake.MainClassFullName");
        this.setProcessId(1);
    }

    @Override
    public FakeChildProcessRunner getChildProcessRunner()
    {
        return (FakeChildProcessRunner)super.getChildProcessRunner();
    }

    @Override
    public InMemoryCharacterToByteStream getOutputWriteStream()
    {
        return (InMemoryCharacterToByteStream)super.getOutputWriteStream();
    }

    @Override
    public InMemoryCharacterToByteStream getErrorWriteStream()
    {
        return (InMemoryCharacterToByteStream)super.getErrorWriteStream();
    }

    @Override
    public InMemoryFileSystem getFileSystem()
    {
        return (InMemoryFileSystem)super.getFileSystem();
    }

    @Override
    public FakeNetwork getNetwork()
    {
        return (FakeNetwork)super.getNetwork();
    }

    @Override
    public ManualClock getClock()
    {
        return (ManualClock)super.getClock();
    }

    @Override
    public FakeDefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        return (FakeDefaultApplicationLauncher)super.getDefaultApplicationLauncher();
    }

    @Override
    public FakeTypeLoader getTypeLoader()
    {
        return (FakeTypeLoader)super.getTypeLoader();
    }

    @Override
    public MutableEnvironmentVariables getEnvironmentVariables()
    {
        return (MutableEnvironmentVariables)super.getEnvironmentVariables();
    }

    public FakeDesktopProcess setEnvironmentVariable(String variableName, String variableValue)
    {
        this.getEnvironmentVariables().set(variableName, variableValue);

        return this;
    }
}
