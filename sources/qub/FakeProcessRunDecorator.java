package qub;

public class FakeProcessRunDecorator<T extends FakeProcessRun> implements FakeProcessRun
{
    private final FakeProcessRun fakeProcessRun;

    public FakeProcessRunDecorator(FakeProcessRun fakeProcessRun)
    {
        PreCondition.assertNotNull(fakeProcessRun, "fakeProcessRun");

        this.fakeProcessRun = fakeProcessRun;
    }

    @Override
    public Path getExecutablePath()
    {
        return this.fakeProcessRun.getExecutablePath();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArgument(String argument)
    {
        this.fakeProcessRun.addArgument(argument);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FakeProcessRun addArguments(String... arguments)
    {
        this.fakeProcessRun.addArguments(arguments);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FakeProcessRun addArguments(Iterable<String> arguments)
    {
        this.fakeProcessRun.addArguments(arguments);
        return (T)this;
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.fakeProcessRun.getArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(String workingFolderPath)
    {
        this.fakeProcessRun.setWorkingFolder(workingFolderPath);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(Path workingFolderPath)
    {
        this.fakeProcessRun.setWorkingFolder(workingFolderPath);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(Folder workingFolder)
    {
        this.fakeProcessRun.setWorkingFolder(workingFolder);
        return (T)this;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.fakeProcessRun.getWorkingFolderPath();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(int exitCode)
    {
        this.fakeProcessRun.setFunction(exitCode);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Action0 action)
    {
        this.fakeProcessRun.setFunction(action);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Function0<Integer> function)
    {
        this.fakeProcessRun.setFunction(function);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Action1<ByteWriteStream> action)
    {
        this.fakeProcessRun.setFunction(action);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Function1<ByteWriteStream,Integer> function)
    {
        this.fakeProcessRun.setFunction(function);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Action2<ByteWriteStream,ByteWriteStream> action)
    {
        this.fakeProcessRun.setFunction(action);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Function2<ByteWriteStream,ByteWriteStream,Integer> function)
    {
        this.fakeProcessRun.setFunction(function);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Action3<ByteReadStream,ByteWriteStream,ByteWriteStream> action)
    {
        this.fakeProcessRun.setFunction(action);
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setFunction(Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> function)
    {
        this.fakeProcessRun.setFunction(function);
        return (T)this;
    }

    @Override
    public Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> getFunction()
    {
        return this.fakeProcessRun.getFunction();
    }
}
