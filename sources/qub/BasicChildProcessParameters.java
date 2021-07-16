package qub;

/**
 * Parameters that can be specified to alter how an executable will be run.
 */
public class BasicChildProcessParameters implements ChildProcessParameters
{
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private ByteReadStream inputStream;
    private Action1<ByteReadStream> outputStreamHandler;
    private Action1<ByteReadStream> errorStreamHandler;

    protected BasicChildProcessParameters(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertFalse(executablePath.endsWith('/') || executablePath.endsWith('\\'), "executablePath.endsWith('/') || executablePath.endsWith('\\')");
        PreCondition.assertNotNull(arguments, "arguments");

        this.executablePath = executablePath;
        this.arguments = List.create(arguments);
    }

    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    public static BasicChildProcessParameters create(Path executablePath, Iterable<String> arguments)
    {
        return new BasicChildProcessParameters(executablePath, arguments);
    }

    @Override
    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.arguments;
    }

    @Override
    public BasicChildProcessParameters insertArgument(int index, String argument)
    {
        PreCondition.assertBetween(0, index, this.arguments.getCount(), "index");
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.insert(index, argument);

        return this;
    }

    @Override
    public BasicChildProcessParameters addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

        return this;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }

    @Override
    public BasicChildProcessParameters setWorkingFolderPath(Path workingFolderPath)
    {
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");
        PreCondition.assertTrue(workingFolderPath.isRooted(), "workingFolderPath.isRooted()");

        this.workingFolderPath = workingFolderPath;

        return this;
    }

    @Override
    public ByteReadStream getInputStream()
    {
        return this.inputStream;
    }

    @Override
    public BasicChildProcessParameters setInputStream(ByteReadStream inputStream)
    {
        PreCondition.assertNotNull(inputStream, "inputStream");
        PreCondition.assertNotDisposed(inputStream, "inputStream");

        this.inputStream = inputStream;

        return this;
    }

    @Override
    public Action1<ByteReadStream> getOutputStreamHandler()
    {
        return this.outputStreamHandler;
    }

    @Override
    public BasicChildProcessParameters setOutputStreamHandler(Action1<ByteReadStream> outputStreamHandler)
    {
        PreCondition.assertNotNull(outputStreamHandler, "outputStreamHandler");

        this.outputStreamHandler = outputStreamHandler;

        return this;
    }

    @Override
    public Action1<ByteReadStream> getErrorStreamHandler()
    {
        return this.errorStreamHandler;
    }

    @Override
    public BasicChildProcessParameters setErrorStreamHandler(Action1<ByteReadStream> errorStreamHandler)
    {
        PreCondition.assertNotNull(errorStreamHandler, "errorStreamHandler");

        this.errorStreamHandler = errorStreamHandler;

        return this;
    }
}
