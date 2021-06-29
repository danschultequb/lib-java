package qub;

public class RealDesktopProcess extends DesktopProcessBase
{
    /**
     * Create a new RealDesktopProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new RealDesktopProcess.
     */
    public static RealDesktopProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return RealDesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new RealDesktopProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new RealDesktopProcess.
     */
    public static RealDesktopProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return RealDesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new RealDesktopProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new RealDesktopProcess.
     */
    public static RealDesktopProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return RealDesktopProcess.create(commandLineArguments, ManualAsyncRunner.create());
    }

    /**
     * Create a new RealDesktopProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new RealDesktopProcess.
     */
    public static RealDesktopProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return new RealDesktopProcess(commandLineArguments, mainAsyncRunner);
    }
    
    protected RealDesktopProcess(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        super(commandLineArguments, mainAsyncRunner);
    }

    @Override
    protected long getProcessIdValue()
    {
        return java.lang.ProcessHandle.current().pid();
    }

    @Override
    protected ChildProcessRunner createDefaultChildProcessRunner()
    {
        return RealChildProcessRunner.create(this);
    }

    @Override
    protected String createDefaultMainClassFullName()
    {
        final String javaApplicationArguments = this.getSystemProperty("sun.java.command").await();
        final int firstSpaceIndex = javaApplicationArguments.indexOf(' ');
        final String result = firstSpaceIndex == -1 ? javaApplicationArguments : javaApplicationArguments.substring(0, firstSpaceIndex);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    @Override
    protected CharacterToByteWriteStream createDefaultOutputWriteStream()
    {
        return CharacterWriteStream.create(OutputStreamToByteWriteStream.create(java.lang.System.out));
    }

    @Override
    public RealDesktopProcess setShouldDisposeOutputWriteStream(boolean shouldDisposeOutputWriteStream)
    {
        return (RealDesktopProcess)super.setShouldDisposeOutputWriteStream(shouldDisposeOutputWriteStream);
    }

    @Override
    protected CharacterToByteWriteStream createDefaultErrorWriteStream()
    {
        return CharacterWriteStream.create(OutputStreamToByteWriteStream.create(java.lang.System.err));
    }

    @Override
    public RealDesktopProcess setShouldDisposeErrorWriteStream(boolean shouldDisposeErrorWriteStream)
    {
        return (RealDesktopProcess)super.setShouldDisposeErrorWriteStream(shouldDisposeErrorWriteStream);
    }

    @Override
    protected CharacterToByteReadStream createDefaultInputReadStream()
    {
        return CharacterToByteReadStream.create(new InputStreamToByteReadStream(java.lang.System.in));
    }

    @Override
    public RealDesktopProcess setShouldDisposeInputReadStream(boolean shouldDisposeInputReadStream)
    {
        return (RealDesktopProcess)super.setShouldDisposeInputReadStream(shouldDisposeInputReadStream);
    }

    @Override
    protected Random createDefaultRandom()
    {
        return new JavaRandom();
    }

    @Override
    protected FileSystem createDefaultFileSystem()
    {
        return JavaFileSystem.create();
    }

    @Override
    protected Network createDefaultNetwork()
    {
        return JavaNetwork.create(this.getClock());
    }

    @Override
    protected Folder createDefaultCurrentFolder()
    {
        final String currentFolderPathString = java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString();
        return this.getFileSystem().getFolder(currentFolderPathString).await();
    }

    @Override
    protected EnvironmentVariables createDefaultEnvironmentVariables()
    {
        final MutableEnvironmentVariables environmentVariables = EnvironmentVariables.create();
        for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
        {
            environmentVariables.set(entry.getKey(), entry.getValue());
        }
        return environmentVariables;
    }

    @Override
    protected Synchronization createDefaultSynchronization()
    {
        return new Synchronization();
    }

    @Override
    protected Clock createDefaultClock()
    {
        return JavaClock.create(this.getParallelAsyncRunner());
    }

    @Override
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

    @Override
    protected DefaultApplicationLauncher createDefaultApplicationLauncher()
    {
        return RealDefaultApplicationLauncher.create();
    }

    @Override
    protected Map<String, String> createDefaultSystemProperties()
    {
        final MutableMap<String,String> result = Map.create();

        final java.util.Properties properties = java.lang.System.getProperties();
        for (final java.util.Map.Entry<Object,Object> property : properties.entrySet())
        {
            final String propertyName = Objects.toString(property.getKey());
            if (!result.containsKey(propertyName))
            {
                result.set(propertyName, Objects.toString(property.getValue()));
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    protected TypeLoader createDefaultTypeLoader()
    {
        return JavaTypeLoader.create();
    }
}
