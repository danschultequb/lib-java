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
    private final MutableMap<String,String> systemProperties;

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
        this.systemProperties = Map.create();

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

    protected CharacterToByteWriteStream createDefaultOutputWriteStream()
    {
        return CharacterWriteStream.create(new OutputStreamToByteWriteStream(System.out));
    }

    /**
     * Set the write stream that is assigned to this Process's output stream.
     * @param outputWriteStream The write stream that is assigned to this Process's output.
     * @return This object for method chaining.
     */
    protected ProcessBase setOutputWriteStream(ByteWriteStream outputWriteStream)
    {
        PreCondition.assertNotNull(outputWriteStream, "outputWriteStream");

        return this.setOutputWriteStream(CharacterToByteWriteStream.create(outputWriteStream));
    }

    /**
     * Set the write stream that is assigned to this Process's output stream.
     * @param outputWriteStream The write stream that is assigned to this Process's output.
     * @return This object for method chaining.
     */
    public ProcessBase setOutputWriteStream(CharacterToByteWriteStream outputWriteStream)
    {
        PreCondition.assertNotNull(outputWriteStream, "outputWriteStream");

        this.outputWriteStream.set(outputWriteStream);

        return this;
    }

    @Override
    public CharacterToByteWriteStream getErrorWriteStream()
    {
        return this.errorWriteStream.getOrSet(this::createDefaultErrorWriteStream);
    }

    protected CharacterToByteWriteStream createDefaultErrorWriteStream()
    {
        return CharacterWriteStream.create(new OutputStreamToByteWriteStream(System.err));
    }

    /**
     * Set the write stream that is assigned to this Process's error stream.
     * @param errorWriteStream The write stream that is assigned to this Process's error stream.
     * @return This object for method chaining.
     */
    protected ProcessBase setErrorWriteStream(ByteWriteStream errorWriteStream)
    {
        PreCondition.assertNotNull(errorWriteStream, "errorWriteStream");

        return this.setErrorWriteStream(CharacterToByteWriteStream.create(errorWriteStream));
    }

    /**
     * Set the write stream that is assigned to this Process's error stream.
     * @param errorWriteStream The write stream that is assigned to this Process's error stream.
     * @return This object for method chaining.
     */
    public ProcessBase setErrorWriteStream(CharacterToByteWriteStream errorWriteStream)
    {
        PreCondition.assertNotNull(errorWriteStream, "errorWriteStream");

        this.errorWriteStream.set(errorWriteStream);

        return this;
    }

    /**
     * Get the CharacterToByteReadStream that is assigned to this Console.
     * @return The CharacterToByteReadStream that is assigned to this Console.
     */
    public CharacterToByteReadStream getInputReadStream()
    {
        return this.inputReadStream.getOrSet(this::createDefaultInputReadStream);
    }

    protected CharacterToByteReadStream createDefaultInputReadStream()
    {
        return CharacterToByteReadStream.create(new InputStreamToByteReadStream(System.in));
    }

    /**
     * Set the ByteReadStream that is assigned to this Process's input.
     * @param inputReadStream The ByteReadStream that is assigned to this Process's input.
     * @return This object for method chaining.
     */
    protected ProcessBase setInputReadStream(ByteReadStream inputReadStream)
    {
        PreCondition.assertNotNull(inputReadStream, "inputReadStream");

        return this.setInputReadStream(CharacterToByteReadStream.create(inputReadStream));
    }

    /**
     * Set the CharacterToByteReadStream that is assigned to this Process's input.
     * @param inputReadStream The CharacterToByteReadStream that is assigned to this Process's
     *                                 input.
     * @return This object for method chaining.
     */
    protected ProcessBase setInputReadStream(CharacterToByteReadStream inputReadStream)
    {
        PreCondition.assertNotNull(inputReadStream, "inputReadStream");

        this.inputReadStream.set(inputReadStream);

        return this;
    }

    /**
     * Set the Random number generator assigned to this Process.
     * @param random The Random number generator assigned to this Process.
     */
    protected ProcessBase setRandom(Random random)
    {
        PreCondition.assertNotNull(random, "random");

        this.random.set(random);
        return this;
    }

    @Override
    public Random getRandom()
    {
        return this.random.getOrSet(this::createDefaultRandom);
    }

    protected Random createDefaultRandom()
    {
        return new JavaRandom();
    }

    @Override
    public FileSystem getFileSystem()
    {
        return this.fileSystem.getOrSet(this::createDefaultFileSystem);
    }

    protected FileSystem createDefaultFileSystem()
    {
        return new JavaFileSystem();
    }

    /**
     * Set the FileSystem that is assigned to this Process.
     * @param fileSystem The FileSystem that will be assigned to this Process.
     */
    protected ProcessBase setFileSystem(FileSystem fileSystem, String currentFolderPath)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNullAndNotEmpty(currentFolderPath, "currentFolderPath");

        return this.setFileSystem(fileSystem, Path.parse(currentFolderPath));
    }

    /**
     * Set the FileSystem that is assigned to this Process.
     * @param fileSystem The FileSystem that will be assigned to this Process.
     */
    protected ProcessBase setFileSystem(FileSystem fileSystem, Path currentFolderPath)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(currentFolderPath, "currentFolderPath");
        PreCondition.assertTrue(currentFolderPath.isRooted(), "currentFolderPath.isRooted()");

        this.fileSystem.set(fileSystem);
        this.currentFolder.set(fileSystem.getFolder(currentFolderPath).await());

        return this;
    }

    @Override
    public Network getNetwork()
    {
        return this.network.getOrSet(this::createDefaultNetwork);
    }

    protected Network createDefaultNetwork()
    {
        return JavaNetwork.create(this.getClock());
    }

    /**
     * Set the Network object that will be used for this Process.
     * @param network The Network object that will be used for this Process.
     * @return This object for method chaining.
     */
    protected ProcessBase setNetwork(Network network)
    {
        PreCondition.assertNotNull(network, "network");

        this.network.set(network);
        return this;
    }

    /**
     * Set the path to the folder that this Process is currently running in.
     * @param currentFolderPathString The folder to the path that this Process is currently running in.
     * @return This object for method chaining.
     */
    protected ProcessBase setCurrentFolderPathString(String currentFolderPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(currentFolderPathString, "currentFolderPathString");

        return this.setCurrentFolderPath(Path.parse(currentFolderPathString));
    }

    /**
     * Set the path to the folder that this Process is currently running in.
     * @param currentFolderPath The folder to the path that this Process is currently running in.
     * @return This object for method chaining.
     */
    protected ProcessBase setCurrentFolderPath(Path currentFolderPath)
    {
        PreCondition.assertNotNull(currentFolderPath, "currentFolderPath");
        PreCondition.assertTrue(currentFolderPath.isRooted(), "currentFolderPath.isRooted()");
        PreCondition.assertNotNull(this.getFileSystem(), "this.getFileSystem()");

        return this.setCurrentFolder(this.getFileSystem().getFolder(currentFolderPath).await());
    }

    /**
     * Set the folder that this Process is currently running in.
     * @param currentFolder The folder that this Process is currently running in.
     * @return This object for method chaining.
     */
    protected ProcessBase setCurrentFolder(Folder currentFolder)
    {
        PreCondition.assertNotNull(currentFolder, "currentFolder");
        PreCondition.assertSame(this.getFileSystem(), currentFolder.getFileSystem(), "currentFolder.getFileSystem()");

        this.currentFolder.set(currentFolder);

        return this;
    }

    @Override
    public Folder getCurrentFolder()
    {
        PreCondition.assertNotNull(this.getFileSystem(), "this.getFileSystem()");

        return this.currentFolder.getOrSet(this::createDefaultCurrentFolder);
    }

    protected Folder createDefaultCurrentFolder()
    {
        final String currentFolderPathString = java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString();
        return this.getFileSystem().getFolder(currentFolderPathString).await();
    }

    /**
     * Set the environment variables that will be used by this Application.
     * @param environmentVariables The environment variables that will be used by this application.
     */
    protected ProcessBase setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        this.environmentVariables.set(environmentVariables);
        return this;
    }

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    public EnvironmentVariables getEnvironmentVariables()
    {
        return this.environmentVariables.getOrSet(this::createDefaultEnvironmentVariables);
    }

    protected EnvironmentVariables createDefaultEnvironmentVariables()
    {
        final EnvironmentVariables environmentVariables = new EnvironmentVariables();
        for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
        {
            environmentVariables.set(entry.getKey(), entry.getValue());
        }
        return environmentVariables;
    }

    /**
     * Get the value of the provided environment variable.
     * @param variableName The name of the environment variable.
     */
    public Result<String> getEnvironmentVariable(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.getEnvironmentVariables().get(variableName);
    }

    /**
     * Get the Synchronization factory for creating synchronization objects.
     * @return The Synchronization factory for creating synchronization objects.
     */
    public Synchronization getSynchronization()
    {
        return this.synchronization.getOrSet(this::createDefaultSynchronization);
    }

    protected Synchronization createDefaultSynchronization()
    {
        return new Synchronization();
    }

    /**
     * Set the Clock object that this ProcessBase will use.
     * @param clock The Clock object that this ProcessBase will use.
     */
    protected ProcessBase setClock(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        this.clock.set(clock);
        return this;
    }

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    public Clock getClock()
    {
        return this.clock.getOrSet(this::createDefaultClock);
    }

    protected Clock createDefaultClock()
    {
        return new JavaClock(this.getParallelAsyncRunner());
    }

    /**
     * Set the displays Iterable that this ProcessBase will use.
     * @param displays The displays Iterable that this ProcessBase will use.
     */
    protected ProcessBase setDisplays(Iterable<Display> displays)
    {
        this.displays.set(displays);
        return this;
    }

    /**
     * Get the displays that have been assigned to this Process.
     * @return The displays that have been assigned to this Process.
     */
    public Iterable<Display> getDisplays()
    {
        return this.displays.getOrSet(this::createDefaultDisplays);
    }

    protected Iterable<Display> createDefaultDisplays()
    {
        final java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        final int dpi = toolkit.getScreenResolution();

        final java.awt.GraphicsEnvironment graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        final java.awt.GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
        final List<Display> displayList = List.create();

        if (graphicsDevices != null)
        {
            for (final java.awt.GraphicsDevice graphicsDevice : graphicsDevices)
            {
                if (graphicsDevice != null)
                {
                    final java.awt.GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
                    final java.awt.geom.AffineTransform defaultTransform = graphicsConfiguration.getDefaultTransform();
                    final double horizontalScale = defaultTransform.getScaleX();
                    final double verticalScale = defaultTransform.getScaleY();

                    final java.awt.DisplayMode displayMode = graphicsDevice.getDisplayMode();
                    displayList.add(new Display(displayMode.getWidth(), displayMode.getHeight(), dpi, dpi, horizontalScale, verticalScale));
                }
            }
        }

        return displayList;
    }

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

    protected DefaultApplicationLauncher createDefaultApplicationLauncher()
    {
        return new RealDefaultApplicationLauncher();
    }

    /**
     * Set the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @param defaultApplicationLauncher The DefaultApplicationLauncher that will be used to open files with their
     *                                   registered default application.
     * @return This object for method chaining.
     */
    protected ProcessBase setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        PreCondition.assertNotNull(defaultApplicationLauncher, "defaultApplicationLauncher");

        this.defaultApplicationLauncher.set(defaultApplicationLauncher);

        return this;
    }

    protected ProcessBase setSystemProperty(String systemPropertyName, String systemPropertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(systemPropertyName, "systemPropertyName");
        PreCondition.assertNotNull(systemPropertyValue, "systemPropertyValue");

        this.systemProperties.set(systemPropertyName, systemPropertyValue);

        return this;
    }

    @Override
    public Map<String,String> getSystemProperties()
    {
        final java.util.Properties properties = java.lang.System.getProperties();
        for (final java.util.Map.Entry<Object,Object> property : properties.entrySet())
        {
            final String propertyName = Objects.toString(property.getKey());
            if (!this.systemProperties.containsKey(propertyName))
            {
                this.systemProperties.set(propertyName, Objects.toString(property.getValue()));
            }
        }

        return this.systemProperties;
    }

    @Override
    public Result<String> getSystemProperty(String systemPropertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(systemPropertyName, "systemPropertyName");

        return this.systemProperties.get(systemPropertyName)
                .catchError(NotFoundException.class, () ->
                {
                    final String systemPropertyValue = java.lang.System.getProperty(systemPropertyName);
                    if (systemPropertyValue == null)
                    {
                        throw new NotFoundException("No system property found with the name " + Strings.escapeAndQuote(systemPropertyName) + ".");
                    }
                    this.systemProperties.set(systemPropertyName, systemPropertyValue);
                    return systemPropertyValue;
                });
    }

    @Override
    public TypeLoader getTypeLoader()
    {
        return this.typeLoader.getOrSet(this::createDefaultTypeLoader);
    }

    protected TypeLoader createDefaultTypeLoader()
    {
        return JavaTypeLoader.create();
    }

    /**
     * Set the TypeLoader that will be used by this process to load and get information about types.
     * @param typeLoader The TypeLoader that will be used by this process to load and get information about types.
     * @return This object for method chaining.
     */
    protected ProcessBase setTypeLoader(TypeLoader typeLoader)
    {
        PreCondition.assertNotNull(typeLoader, "typeLoader");

        this.typeLoader.set(typeLoader);

        return this;
    }

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
