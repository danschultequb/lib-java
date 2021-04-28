package qub;

/**
 * A ProcessBuilder that builds up a process for invocation.
 */
public class BasicProcessBuilder implements ProcessBuilder
{
    private final ProcessFactory factory;
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private ByteReadStream redirectedInputStream;
    private Action1<ByteReadStream> redirectOutputAction;
    private Action1<ByteReadStream> redirectErrorAction;
    private CharacterWriteStream verbose;

    public BasicProcessBuilder(ProcessFactory factory, Path executablePath, Path workingFolderPath)
    {
        PreCondition.assertNotNull(factory, "factory");
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        this.factory = factory;
        this.executablePath = executablePath;
        this.arguments = List.create();
        this.workingFolderPath = workingFolderPath;
    }

    @Override
    public BasicProcessBuilder setVerbose(CharacterWriteStream verbose)
    {
        this.verbose = verbose;
        return this;
    }

    @Override
    public Result<Integer> run()
    {
        return this.factory.run(
            this.executablePath,
            this.arguments,
            this.workingFolderPath,
            this.redirectedInputStream,
            this.redirectOutputAction,
            this.redirectErrorAction,
            this.verbose);
    }

    @Override
    public Result<? extends ChildProcess> start()
    {
        return this.factory.start(
            this.executablePath,
            this.arguments,
            this.workingFolderPath,
            this.redirectedInputStream,
            this.redirectOutputAction,
            this.redirectErrorAction,
            this.verbose);
    }

    @Override
    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    @Override
    public String getCommand()
    {
        return ProcessFactory.getCommand(this.executablePath, this.arguments, this.workingFolderPath);
    }

    @Override
    public BasicProcessBuilder addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

        return this;
    }

    @Override
    public BasicProcessBuilder addArguments(String... arguments)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.addArguments(arguments);
    }

    @Override
    public BasicProcessBuilder addArguments(Iterator<String> arguments)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.addArguments(arguments);
    }

    @Override
    public BasicProcessBuilder addArguments(Iterable<String> arguments)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.addArguments(arguments);
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.arguments;
    }


    @Override
    public BasicProcessBuilder setWorkingFolder(String workingFolderPath)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.setWorkingFolder(workingFolderPath);
    }


    @Override
    public BasicProcessBuilder setWorkingFolder(Path workingFolderPath)
    {
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");
        PreCondition.assertTrue(workingFolderPath.isRooted(), "workingFolderPath.isRooted()");

        this.workingFolderPath = workingFolderPath;

        return this;
    }


    @Override
    public BasicProcessBuilder setWorkingFolder(Folder workingFolder)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.setWorkingFolder(workingFolder);
    }


    @Override
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }


    @Override
    public BasicProcessBuilder redirectInput(ByteReadStream redirectedInputStream)
    {
        this.redirectedInputStream = redirectedInputStream;
        return this;
    }


    @Override
    public BasicProcessBuilder redirectOutput(Action1<ByteReadStream> redirectOutputAction)
    {
        this.redirectOutputAction = redirectOutputAction;
        return this;
    }


    @Override
    public BasicProcessBuilder redirectOutput(ByteWriteStream redirectedOutputStream)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.redirectOutput(redirectedOutputStream);
    }


    @Override
    public BasicProcessBuilder redirectOutputLines(Action1<String> onOutputLine)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.redirectOutputLines(onOutputLine);
    }


    @Override
    public BasicProcessBuilder redirectError(Action1<ByteReadStream> redirectErrorAction)
    {
        this.redirectErrorAction = redirectErrorAction;
        return this;
    }


    @Override
    public BasicProcessBuilder redirectError(ByteWriteStream redirectedErrorStream)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.redirectError(redirectedErrorStream);
    }


    @Override
    public BasicProcessBuilder redirectErrorLines(Action1<String> onErrorLine)
    {
        return (BasicProcessBuilder)ProcessBuilder.super.redirectErrorLines(onErrorLine);
    }
}
