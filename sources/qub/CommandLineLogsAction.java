package qub;

public interface CommandLineLogsAction
{
    static Folder getLogsFolderFromProcess(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final Folder dataFolder = process.getQubProjectDataFolder().await();
        return CommandLineLogsAction.getLogsFolderFromDataFolder(dataFolder);
    }

    static Folder getLogsFolderFromDataFolder(Folder dataFolder)
    {
        PreCondition.assertNotNull(dataFolder, "dataFolder");

        return dataFolder.getFolder("logs").await();
    }

    /**
     * Add a logs action to the provided CommandLineActions object.
     * @param actions The CommandLineActions object to add the logs action to.
     */
    static CommandLineAction addAction(CommandLineActions actions)
    {
        PreCondition.assertNotNull(actions, "actions");

        return CommandLineLogsAction.addAction(actions, CommandLineLogsActionParameters.create());
    }

    /**
     * Add a logs action to the provided CommandLineActions object.
     * @param actions The CommandLineActions object to add the logs action to.
     */
    static CommandLineAction addAction(CommandLineActions actions, CommandLineLogsActionParameters parameters)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(parameters, "parameters");

        return actions.addAction("logs",
                (DesktopProcess process, CommandLineAction action) -> CommandLineLogsAction.getParameters(process, action, parameters),
                CommandLineLogsAction::run)
            .setDescription("Show the logs folder.");
    }

    static CommandLineLogsActionParameters getParameters(DesktopProcess process, CommandLineAction action, CommandLineLogsActionParameters parameters)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(parameters, "parameters");

        final CommandLineParameters commandLineParameters = action.createCommandLineParameters(process);
        final CommandLineParameter<Path> openWithParameter = commandLineParameters.addPath("openWith")
            .setDescription("The application to use to open the logs folder.");
        final CommandLineParameterHelp helpParameter = commandLineParameters.addHelp();

        CommandLineLogsActionParameters result = null;
        if (!helpParameter.showApplicationHelpLines(process).await())
        {
            result = parameters;

            if (result.getLogsFolder() == null)
            {
                result.setLogsFolder(CommandLineLogsAction.getLogsFolderFromProcess(process));
            }

            if (result.getOutput() == null)
            {
                result.setOutput(process.getOutputWriteStream());
            }

            if (result.getDefaultApplicationLauncher() == null)
            {
                result.setDefaultApplicationLauncher(process.getDefaultApplicationLauncher());
            }

            if (result.getProcessFactory() == null)
            {
                result.setProcessFactory(process.getProcessFactory());
            }

            final Path openWith = openWithParameter.getValue().await();
            if (openWith != null)
            {
                result.setOpenWith(openWith);
            }
        }

        return result;
    }

    static void run(CommandLineLogsActionParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");
        PreCondition.assertNotNull(parameters.getLogsFolder(), "parameters.getLogsFolder()");
        PreCondition.assertNotNull(parameters.getOutput(), "parameters.getOutput()");
        PreCondition.assertNotNull(parameters.getDefaultApplicationLauncher(), "parameters.getDefaultApplicationLauncher()");
        PreCondition.assertNotNull(parameters.getProcessFactory(), "parameters.getProcessFactory()");

        final Folder logsFolder = parameters.getLogsFolder();
        final CharacterWriteStream output = parameters.getOutput();
        final DefaultApplicationLauncher defaultApplicationLauncher = parameters.getDefaultApplicationLauncher();
        final ProcessFactory processFactory = parameters.getProcessFactory();
        final Path openWith = parameters.getOpenWith();

        if (!logsFolder.exists().await())
        {
            output.writeLine("The logs folder (" + logsFolder + ") doesn't exist.").await();
        }
        else if (openWith == null)
        {
            defaultApplicationLauncher.openFolderWithDefaultApplication(logsFolder).await();
        }
        else
        {
            processFactory.getProcessBuilder(openWith).await()
                .addArgument(logsFolder.toString())
                .run()
                .await();
        }
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param projectDataFolder The data folder for the running application/project.
     */
    static LogStreams addLogStreamFromDataFolder(Folder projectDataFolder, CharacterToByteWriteStream output, VerboseCharacterToByteWriteStream verbose)
    {
        return CommandLineLogsAction.addLogStreamFromDataFolder(projectDataFolder, output, null, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param dataFolder The data folder for the running application/project.
     */
    static LogStreams addLogStreamFromDataFolder(Folder dataFolder, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
    {
        PreCondition.assertNotNull(dataFolder, "projectDataFolder");

        final Folder logsFolder = CommandLineLogsAction.getLogsFolderFromDataFolder(dataFolder);
        return CommandLineLogsAction.addLogStreamFromLogsFolder(logsFolder, output, error, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logsFolder The logs folder for the running application/project.
     */
    static LogStreams addLogStreamFromLogsFolder(Folder logsFolder, CharacterToByteWriteStream output, VerboseCharacterToByteWriteStream verbose)
    {
        return CommandLineLogsAction.addLogStreamFromLogsFolder(logsFolder, output, null, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logsFolder The logs folder for the running application/project.
     */
    static LogStreams addLogStreamFromLogsFolder(Folder logsFolder, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
    {
        PreCondition.assertNotNull(logsFolder, "logsFolder");

        final File logFile = CommandLineLogsAction.getLogFileFromLogsFolder(logsFolder);
        return CommandLineLogsAction.addLogStreamFromLogFile(logFile, output, error, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     */
    static LogStreams addLogStreamFromLogFile(File logFile, CharacterToByteWriteStream output, VerboseCharacterToByteWriteStream verbose)
    {
        return CommandLineLogsAction.addLogStreamFromLogFile(logFile, output, null, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     */
    @Deprecated
    static LogStreams addLogStream(File logFile, CharacterToByteWriteStream output, VerboseCharacterToByteWriteStream verbose)
    {
        return CommandLineLogsAction.addLogStreamFromLogFile(logFile, output, null, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     */
    static LogStreams addLogStreamFromLogFile(File logFile, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
    {
        PreCondition.assertNotNull(logFile, "logFile");

        final CharacterToByteWriteStream logStream = logFile.getContentsCharacterWriteStream(OpenWriteType.CreateOrAppend).await();

        if (output != null)
        {
            output = CharacterToByteWriteStreamList.create(output, logStream);
        }

        if (error != null)
        {
            error = CharacterToByteWriteStreamList.create(error, LinePrefixCharacterToByteWriteStream.create(logStream).setLinePrefix("ERROR: "));
        }

        if (verbose != null)
        {
            verbose = VerboseCharacterToByteWriteStream.create(CharacterToByteWriteStreamList.create(verbose, VerboseCharacterToByteWriteStream.create(logStream)))
                .setLinePrefix("");
        }

        final LogStreams result = LogStreams.create(logFile, logStream, output, error, verbose);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static File getLogFileFromProcess(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final Folder logsFolder = CommandLineLogsAction.getLogsFolderFromProcess(process);
        return CommandLineLogsAction.getLogFileFromLogsFolder(logsFolder);
    }

    static File getLogFileFromDataFolder(Folder dataFolder)
    {
        PreCondition.assertNotNull(dataFolder, "dataFolder");

        final Folder logsFolder = CommandLineLogsAction.getLogsFolderFromDataFolder(dataFolder);
        return CommandLineLogsAction.getLogFileFromLogsFolder(logsFolder);
    }

    static File getLogFileFromLogsFolder(Folder logsFolder)
    {
        PreCondition.assertNotNull(logsFolder, "logsFolder");

        final int logFileCount = logsFolder.getFiles()
            .then((Iterable<File> logFiles) -> logFiles.getCount())
            .catchError(NotFoundException.class, () -> 0)
            .await();
        final String logFileName = (logFileCount + 1) + ".log";
        final File result = logsFolder.getFile(logFileName).await();

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
