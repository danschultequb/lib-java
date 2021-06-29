package qub;

public abstract class ChildProcessParametersDecorator<T extends ChildProcessParameters> implements ChildProcessParameters
{
    private final ChildProcessParameters innerParameters;

    protected ChildProcessParametersDecorator(ChildProcessParameters innerParameters)
    {
        PreCondition.assertNotNull(innerParameters, "innerParameters");

        this.innerParameters = innerParameters;
    }

    protected ChildProcessParametersDecorator(Path executablePath)
    {
        this(ChildProcessParameters.create(executablePath));
    }

    @Override
    public Path getExecutablePath()
    {
        return this.innerParameters.getExecutablePath();
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.innerParameters.getArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArgument(String argument)
    {
        this.innerParameters.addArgument(argument);

        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArguments(String... arguments)
    {
        this.innerParameters.addArguments(arguments);

        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArguments(Iterable<String> arguments)
    {
        this.innerParameters.addArguments(arguments);

        return (T)this;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.innerParameters.getWorkingFolderPath();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolderPath(String workingFolderPath)
    {
        this.innerParameters.setWorkingFolderPath(workingFolderPath);

        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolderPath(Path workingFolderPath)
    {
        this.innerParameters.setWorkingFolderPath(workingFolderPath);

        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(Folder workingFolder)
    {
        this.innerParameters.setWorkingFolder(workingFolder);

        return (T)this;
    }

    @Override
    public ByteReadStream getInputStream()
    {
        return this.innerParameters.getInputStream();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setInputStream(ByteReadStream inputStream)
    {
        this.innerParameters.setInputStream(inputStream);

        return (T)this;
    }

    @Override
    public Action1<ByteReadStream> getOutputStreamHandler()
    {
        return this.innerParameters.getOutputStreamHandler();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setOutputStreamHandler(Action1<ByteReadStream> outputStreamHandler)
    {
        this.innerParameters.setOutputStreamHandler(outputStreamHandler);

        return (T)this;
    }

    @Override
    public Action1<ByteReadStream> getErrorStreamHandler()
    {
        return this.innerParameters.getErrorStreamHandler();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setErrorStreamHandler(Action1<ByteReadStream> errorStreamHandler)
    {
        this.innerParameters.setErrorStreamHandler(errorStreamHandler);

        return (T)this;
    }
}
