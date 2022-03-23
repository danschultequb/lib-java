package qub;

public class RealDesktopProcess extends DesktopProcessBase<RealDesktopProcess>
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

        this.setProcessId(() -> java.lang.ProcessHandle.current().pid());
        this.setChildProcessRunner(() -> RealChildProcessRunner.create(this));
        this.setMainClassFullName(() ->
        {
            final String javaApplicationArguments = this.getSystemProperty("sun.java.command").await();
            final int firstSpaceIndex = javaApplicationArguments.indexOf(' ');
            final String result = firstSpaceIndex == -1 ? javaApplicationArguments : javaApplicationArguments.substring(0, firstSpaceIndex);

            PostCondition.assertNotNullAndNotEmpty(result, "result");

            return result;
        });
        this.setOutputWriteStream(() -> CharacterWriteStream.create(OutputStreamToByteWriteStream.create(java.lang.System.out)));
        this.setErrorWriteStream(() -> CharacterWriteStream.create(OutputStreamToByteWriteStream.create(java.lang.System.err)));
        this.setInputReadStream(() -> CharacterToByteReadStream.create(InputStreamToByteReadStream.create(java.lang.System.in)));
        this.setRandom(() -> new JavaRandom());
        this.setFileSystem(() -> JavaFileSystem.create());
        this.setNetwork(() -> JavaNetwork.create(this.getClock()));
        this.setCurrentFolderPath(() -> Path.parse(java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString()));
        this.setEnvironmentVariables(() ->
        {
            final MutableEnvironmentVariables environmentVariables = EnvironmentVariables.create();
            for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
            {
                environmentVariables.set(entry.getKey(), entry.getValue());
            }
            return environmentVariables;
        });
        this.setSynchronization(() -> new Synchronization());
        this.setClock(() -> JavaClock.create(this.getParallelAsyncRunner()));
        this.setDisplays(() ->
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
        });
        this.setDefaultApplicationLauncher(() -> RealDefaultApplicationLauncher.create());
        this.setSystemProperties(() ->
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
        });
        this.setTypeLoader(() -> JavaTypeLoader.create());
    }
}
