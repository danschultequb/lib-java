package qub;

/**
 * A base-type that makes it easier to wrap a ChildProcessRunner for a specific executable or
 * application.
 */
public abstract class ChildProcessRunnerWrapper<TWrapper, TParameters extends ChildProcessParameters>
{
    private final ChildProcessRunner childProcessRunner;
    private final Function1<Path,TParameters> parametersCreator;
    private Path executablePath;

    protected ChildProcessRunnerWrapper(ChildProcessRunner childProcessRunner, Function1<Path,TParameters> parametersCreator, String executablePath)
    {
        this(childProcessRunner, parametersCreator, Path.parse(executablePath));
    }

    protected ChildProcessRunnerWrapper(ChildProcessRunner childProcessRunner, Function1<Path,TParameters> parametersCreator, Path executablePath)
    {
        PreCondition.assertNotNull(childProcessRunner, "childProcessRunner");
        PreCondition.assertNotNull(parametersCreator, "parametersCreator");
        PreCondition.assertNotNull(executablePath, "executablePath");

        this.childProcessRunner = childProcessRunner;
        this.parametersCreator = parametersCreator;
        this.executablePath = executablePath;
    }

    /**
     * Get the executable path that will be used to invoke the child process.
     * @return The executable path that will be used to invoke the child process.
     */
    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    /**
     * Set the executable path that will be used to invoke the child process.
     * @param executablePath The executable path that will be used to invoke the child process.
     * @return This object for method chaining.
     */
    public TWrapper setExecutablePath(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return this.setExecutablePath(Path.parse(executablePath));
    }

    /**
     * Set the executable path that will be used to invoke the child process.
     * @param executablePath The executable path that will be used to invoke the child process.
     * @return This object for method chaining.
     */
    @SuppressWarnings("unchecked")
    public TWrapper setExecutablePath(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        this.executablePath = executablePath;

        return (TWrapper)this;
    }

    /**
     * Set the executable path that will be used to invoke the child process.
     * @param executable The executable file that will be used to invoke the child process.
     * @return This object for method chaining.
     */
    public TWrapper setExecutablePath(File executable)
    {
        PreCondition.assertNotNull(executable, "executable");

        return this.setExecutablePath(executable.getPath());
    }

    /**
     * Add any default parameter values to the parameters that are created by the provided creator
     * function, then return the newly created parameters object.
     * @param creator A creator function that will create a new parameters object.
     * @param <T> The type of parameters object to create and return.
     * @return The new parameters object with all default values added to it.
     */
    protected <T extends TParameters> T addParameterDefaults(Function1<Path,T> creator)
    {
        return creator.run(this.executablePath);
    }

    /**
     * Run the executable with the provided arguments.
     * @param arguments The arguments to pass to the executable.
     * @return The result of running the executable.
     */
    public Result<Integer> run(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(Iterable.create(arguments));
    }

    /**
     * Run the executable with the provided arguments.
     * @param arguments The arguments to pass to the executable.
     * @return The result of running the executable.
     */
    public Result<Integer> run(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        return this.childProcessRunner.run(this.addParameterDefaults(this.parametersCreator).addArguments(arguments));
    }

    public Result<Integer> run(TParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return this.childProcessRunner.run(parameters);
    }

    public Result<Integer> run(Action1<TParameters> parametersSetup)
    {
        return this.run(this.parametersCreator, parametersSetup);
    }

    protected <T extends TParameters> Result<Integer> run(Function1<Path,T> parametersCreator, Action1<T> parametersSetup)
    {
        PreCondition.assertNotNull(parametersCreator, "parametersCreator");
        PreCondition.assertNotNull(parametersSetup, "parametersSetup");

        final T parameters = this.addParameterDefaults(parametersCreator);
        parametersSetup.run(parameters);
        return this.run(parameters);
    }

    /**
     * Start the executable with the provided arguments.
     * @param arguments The arguments to pass to the executable.
     * @return The result of starting the executable.
     */
    public Result<? extends ChildProcess> start(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(Iterable.create(arguments));
    }

    /**
     * Start the executable with the provided arguments.
     * @param arguments The arguments to pass to the executable.
     * @return The result of running the executable.
     */
    public Result<? extends ChildProcess> start(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        return this.childProcessRunner.start(this.addParameterDefaults(this.parametersCreator).addArguments(arguments));
    }

    public Result<? extends ChildProcess> start(TParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return this.childProcessRunner.start(parameters);
    }

    public Result<? extends ChildProcess> start(Action1<TParameters> parametersSetup)
    {
        return this.start(this.parametersCreator, parametersSetup);
    }

    protected <T extends TParameters> Result<? extends ChildProcess> start(Function1<Path,T> parametersCreator, Action1<T> parametersSetup)
    {
        PreCondition.assertNotNull(parametersCreator, "parametersCreator");
        PreCondition.assertNotNull(parametersSetup, "parametersSetup");

        final T parameters = this.addParameterDefaults(parametersCreator);
        parametersSetup.run(parameters);
        return this.start(parameters);
    }
}
