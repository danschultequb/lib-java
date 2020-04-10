package qub;

/**
 * A ProcessBuilderDecorator that makes it easier to derive new types of ProcessBuilders.
 * @param <T> The type of the derived process builder.
 */
public abstract class ProcessBuilderDecorator<T extends ProcessBuilder> implements ProcessBuilder
{
    private final ProcessBuilder processBuilder;

    protected ProcessBuilderDecorator(ProcessBuilder processBuilder)
    {
        PreCondition.assertNotNull(processBuilder, "processBuilder");

        this.processBuilder = processBuilder;
    }

    @Override
    public Result<Integer> run()
    {
        return this.processBuilder.run();
    }

    @Override
    public Path getExecutablePath()
    {
        return this.processBuilder.getExecutablePath();
    }

    @Override
    public String getCommand()
    {
        return this.processBuilder.getCommand();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArgument(String argument)
    {
        this.processBuilder.addArgument(argument);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArguments(String... arguments)
    {
        this.processBuilder.addArguments(arguments);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArguments(Iterable<String> arguments)
    {
        this.processBuilder.addArguments(arguments);
        return (T)this;
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.processBuilder.getArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(String workingFolderPath)
    {
        this.processBuilder.setWorkingFolder(workingFolderPath);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(Path workingFolderPath)
    {
        this.processBuilder.setWorkingFolder(workingFolderPath);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(Folder workingFolder)
    {
        this.processBuilder.setWorkingFolder(workingFolder);
        return (T)this;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.processBuilder.getWorkingFolderPath();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectInput(ByteReadStream redirectedInputStream)
    {
        this.processBuilder.redirectInput(redirectedInputStream);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectOutput(Action1<ByteReadStream> redirectOutputAction)
    {
        this.processBuilder.redirectOutput(redirectOutputAction);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectOutput(ByteWriteStream redirectedOutputStream)
    {
        this.processBuilder.redirectOutput(redirectedOutputStream);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectOutputLines(Action1<String> onOutputLine)
    {
        this.processBuilder.redirectOutputLines(onOutputLine);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectError(Action1<ByteReadStream> redirectErrorAction)
    {
        this.processBuilder.redirectError(redirectErrorAction);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectError(ByteWriteStream redirectedErrorStream)
    {
        this.processBuilder.redirectError(redirectedErrorStream);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectErrorLines(Action1<String> onErrorLine)
    {
        this.processBuilder.redirectErrorLines(onErrorLine);
        return (T)this;
    }
}
