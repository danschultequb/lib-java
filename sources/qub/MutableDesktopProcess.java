package qub;

public interface MutableDesktopProcess extends MutableProcess, DesktopProcess
{
    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    static void run(String[] args, Action1<MutableDesktopProcess> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        MutableDesktopProcess.run(() -> RealDesktopProcess.create(args), main);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    static void run(Function0<? extends MutableDesktopProcess> processCreator, Action1<MutableDesktopProcess> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        final MutableDesktopProcess process = processCreator.run();
        try
        {
            main.run(process);
        }
        catch (Throwable error)
        {
            Exceptions.writeErrorString(process.getErrorWriteStream(), error).await();
            process.setExitCode(1);
        }
        finally
        {
            process.dispose().await();
            java.lang.System.exit(process.getExitCode());
        }
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    static void run(String[] args, Function1<MutableDesktopProcess,Integer> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        MutableDesktopProcess.run(() -> RealDesktopProcess.create(args), MutableDesktopProcess.getMainAction(main));
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    static void run(Function0<? extends MutableDesktopProcess> processCreator, Function1<MutableDesktopProcess,Integer> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        MutableDesktopProcess.run(processCreator, MutableDesktopProcess.getMainAction(main));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(String[] arguments, Function1<MutableDesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        MutableDesktopProcess.run(arguments, MutableDesktopProcess.getMainAction(getParametersFunction, runAction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(Function0<? extends MutableDesktopProcess> processCreator, Function1<MutableDesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        MutableDesktopProcess.run(processCreator, MutableDesktopProcess.getMainAction(getParametersFunction, runAction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runFunction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(String[] arguments, Function1<MutableDesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        MutableDesktopProcess.run(arguments, MutableDesktopProcess.getMainAction(getParametersFunction, runFunction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runFunction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(Function0<? extends MutableDesktopProcess> processCreator, Function1<MutableDesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        MutableDesktopProcess.run(processCreator, MutableDesktopProcess.getMainAction(getParametersFunction, runFunction));
    }

    static Action1<MutableDesktopProcess> getMainAction(Function1<MutableDesktopProcess,Integer> runFunction)
    {
        PreCondition.assertNotNull(runFunction, "runFunction");

        return (MutableDesktopProcess process) ->
        {
            process.setExitCode(runFunction.run(process));
        };
    }

    static <TParameters> Action1<MutableDesktopProcess> getMainAction(Function1<MutableDesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (MutableDesktopProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                runAction.run(parameters);
            }
        };
    }

    static <TParameters> Action1<MutableDesktopProcess> getMainAction(Function1<MutableDesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (MutableDesktopProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                process.setExitCode(runAction.run(parameters));
            }
        };
    }

    /**
     * Set the output stream that is assigned to this {@link MutableDesktopProcess}.
     * @param outputWriteStream The output stream that is assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setOutputWriteStream(CharacterToByteWriteStream outputWriteStream)
    {
        return (MutableDesktopProcess)MutableProcess.super.setOutputWriteStream(outputWriteStream);
    }

    /**
     * Set the output stream that is assigned to this {@link MutableDesktopProcess}.
     * @param outputWriteStream The output stream that is assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setOutputWriteStream(Function0<CharacterToByteWriteStream> outputWriteStream);

    /**
     * Set the error stream that is assigned to this {@link MutableDesktopProcess}.
     * @param errorWriteStream The error stream that is assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setErrorWriteStream(CharacterToByteWriteStream errorWriteStream)
    {
        return (MutableDesktopProcess)MutableProcess.super.setErrorWriteStream(errorWriteStream);
    }

    /**
     * Set the error stream that is assigned to this {@link MutableDesktopProcess}.
     * @param errorWriteStream The error stream that is assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setErrorWriteStream(Function0<CharacterToByteWriteStream> errorWriteStream);

    /**
     * Set the input stream that is assigned to this {@link MutableDesktopProcess}.
     * @param inputReadStream The input stream that is assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setInputReadStream(CharacterToByteReadStream inputReadStream)
    {
        return (MutableDesktopProcess)MutableProcess.super.setInputReadStream(inputReadStream);
    }

    /**
     * Set the input stream that is assigned to this {@link MutableDesktopProcess}.
     * @param inputReadStream The input stream that is assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setInputReadStream(Function0<CharacterToByteReadStream> inputReadStream);

    /**
     * Set the random number generator assigned to this {@link MutableDesktopProcess}.
     * @param random The random number generator assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setRandom(Random random)
    {
        return (MutableDesktopProcess)MutableProcess.super.setRandom(random);
    }

    /**
     * Set the random number generator assigned to this {@link MutableDesktopProcess}.
     * @param random The random number generator assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setRandom(Function0<Random> random);

    /**
     * Set the file system assigned to this {@link MutableDesktopProcess}.
     * @param fileSystem The file system assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setFileSystem(FileSystem fileSystem)
    {
        return (MutableDesktopProcess)MutableProcess.super.setFileSystem(fileSystem);
    }

    /**
     * Set the file system assigned to this {@link MutableDesktopProcess}.
     * @param fileSystem The file system assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setFileSystem(Function0<FileSystem> fileSystem);

    /**
     * Set the {@link Network} assigned to this {@link MutableDesktopProcess}.
     * @param network The {@link Network} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setNetwork(Network network)
    {
        return (MutableDesktopProcess)MutableProcess.super.setNetwork(network);
    }

    /**
     * Set the {@link Network} assigned to this {@link MutableDesktopProcess}.
     * @param network The {@link Network} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setNetwork(Function0<Network> network);

    /**
     * Set the current folder path assigned to this {@link MutableDesktopProcess}.
     * @param currentFolderPath The current folder path assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setCurrentFolderPath(String currentFolderPath)
    {
        return (MutableDesktopProcess)MutableProcess.super.setCurrentFolderPath(currentFolderPath);
    }

    /**
     * Set the current folder path assigned to this {@link MutableDesktopProcess}.
     * @param currentFolderPath The current folder path assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setCurrentFolderPath(Path currentFolderPath)
    {
        return (MutableDesktopProcess)MutableProcess.super.setCurrentFolderPath(currentFolderPath);
    }

    /**
     * Set the current folder path assigned to this {@link MutableDesktopProcess}.
     * @param currentFolderPath The current folder path assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setCurrentFolderPath(Function0<Path> currentFolderPath);

    /**
     * Set the {@link EnvironmentVariables} assigned to this {@link MutableDesktopProcess}.
     * @param environmentVariables The {@link EnvironmentVariables} assigned to this
     * {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        return (MutableDesktopProcess)MutableProcess.super.setEnvironmentVariables(environmentVariables);
    }

    /**
     * Set the {@link EnvironmentVariables} assigned to this {@link MutableDesktopProcess}.
     * @param environmentVariables The {@link EnvironmentVariables} assigned to this
     * {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setEnvironmentVariables(Function0<EnvironmentVariables> environmentVariables);

    /**
     * Set the {@link Synchronization} assigned to this {@link MutableDesktopProcess}.
     * @param synchronization The {@link Synchronization} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setSynchronization(Synchronization synchronization)
    {
        return (MutableDesktopProcess)MutableProcess.super.setSynchronization(synchronization);
    }

    /**
     * Set the {@link Synchronization} assigned to this {@link MutableDesktopProcess}.
     * @param synchronization The {@link Synchronization} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setSynchronization(Function0<Synchronization> synchronization);

    /**
     * Set the {@link Clock} assigned to this {@link MutableDesktopProcess}.
     * @param clock The {@link Clock} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setClock(Clock clock)
    {
        return (MutableDesktopProcess)MutableProcess.super.setClock(clock);
    }

    /**
     * Set the {@link Clock} assigned to this {@link MutableDesktopProcess}.
     * @param clock The {@link Clock} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setClock(Function0<Clock> clock);

    /**
     * Set the displays assigned to this {@link MutableDesktopProcess}.
     * @param displays The displays assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setDisplays(Iterable<Display> displays)
    {
        return (MutableDesktopProcess)MutableProcess.super.setDisplays(displays);
    }

    /**
     * Set the displays assigned to this {@link MutableDesktopProcess}.
     * @param displays The displays assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setDisplays(Function0<Iterable<Display>> displays);

    /**
     * Set the {@link DefaultApplicationLauncher} assigned to this {@link MutableDesktopProcess}.
     * @param defaultApplicationLauncher The {@link DefaultApplicationLauncher} assigned to this
     * {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        return (MutableDesktopProcess)MutableProcess.super.setDefaultApplicationLauncher(defaultApplicationLauncher);
    }

    /**
     * Set the {@link DefaultApplicationLauncher} assigned to this {@link MutableDesktopProcess}.
     * @param defaultApplicationLauncher The {@link DefaultApplicationLauncher} assigned to this
     * {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setDefaultApplicationLauncher(Function0<DefaultApplicationLauncher> defaultApplicationLauncher);

    /**
     * Set the system properties assigned to this {@link MutableDesktopProcess}.
     * @param systemProperties The system properties assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setSystemProperties(Map<String,String> systemProperties)
    {
        return (MutableDesktopProcess)MutableProcess.super.setSystemProperties(systemProperties);
    }

    /**
     * Set the system properties assigned to this {@link MutableDesktopProcess}.
     * @param systemProperties The system properties assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setSystemProperties(Function0<Map<String,String>> systemProperties);

    /**
     * Set the {@link TypeLoader} assigned to this {@link MutableDesktopProcess}.
     * @param typeLoader The {@link TypeLoader} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setTypeLoader(TypeLoader typeLoader)
    {
        return (MutableDesktopProcess)MutableProcess.super.setTypeLoader(typeLoader);
    }

    /**
     * Set the {@link TypeLoader} assigned to this {@link MutableDesktopProcess}.
     * @param typeLoader The {@link TypeLoader} assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setTypeLoader(Function0<TypeLoader> typeLoader);

    /**
     * Set the {@link ChildProcessRunner} assigned to this {@link MutableDesktopProcess}.
     * @param childProcessRunner The {@link ChildProcessRunner} assigned to this
     * {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setChildProcessRunner(ChildProcessRunner childProcessRunner)
    {
        return this.setChildProcessRunner(() -> childProcessRunner);
    }

    /**
     * Set the {@link ChildProcessRunner} assigned to this {@link MutableDesktopProcess}.
     * @param childProcessRunner The {@link ChildProcessRunner} assigned to this
     * {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setChildProcessRunner(Function0<ChildProcessRunner> childProcessRunner);

    /**
     * Set the main class full name assigned to this {@link MutableDesktopProcess}.
     * @param mainClassFullName The main class assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    default MutableDesktopProcess setMainClassFullName(String mainClassFullName)
    {
        return this.setMainClassFullName(() -> mainClassFullName);
    }

    /**
     * Set the main class full name assigned to this {@link MutableDesktopProcess}.
     * @param mainClassFullName The main class assigned to this {@link MutableDesktopProcess}.
     * @return This object for method chaining.
     */
    MutableDesktopProcess setMainClassFullName(Function0<String> mainClassFullName);
}
