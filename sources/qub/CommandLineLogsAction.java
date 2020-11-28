package qub;

public interface CommandLineLogsAction
{
    String actionName = "logs";
    String actionDescription = "Show the logs folder.";

    /**
     * Add a logs action to the provided CommandLineActions object.
     * @param actions The CommandLineActions object to add the logs action to.
     */
    static <TProcess extends QubProcess> CommandLineAction<TProcess> addAction(CommandLineActions<TProcess> actions)
    {
        PreCondition.assertNotNull(actions, "actions");

        return actions.addAction(CommandLineLogsAction.actionName, CommandLineLogsAction::run)
            .setDescription(CommandLineLogsAction.actionDescription);
    }

    static Folder getQubProjectLogsFolder(Folder projectDataFolder)
    {
        PreCondition.assertNotNull(projectDataFolder, "projectDataFolder");

        return projectDataFolder.getFolder("logs").await();
    }

    static void run(QubProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final Folder projectDataFolder = process.getQubProjectDataFolder().await();
        final Folder projectLogsFolder = CommandLineLogsAction.getQubProjectLogsFolder(projectDataFolder);
        if (!projectLogsFolder.exists().await())
        {
            process.getOutputWriteStream().writeLine("The logs folder (" + projectLogsFolder + ") doesn't exist.").await();
        }
        else
        {
            final Path projectLogsFolderPath = projectLogsFolder.getPath();
            final DefaultApplicationLauncher applicationLauncher = process.getDefaultApplicationLauncher();
            applicationLauncher.openFileWithDefaultApplication(projectLogsFolderPath).await();
        }
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param projectDataFolder The data folder for the running application/project.
     */
    static LogStreams addLogStream(Folder projectDataFolder, CharacterToByteWriteStream output, VerboseCharacterToByteWriteStream verbose)
    {
        return CommandLineLogsAction.addLogStream(projectDataFolder, output, null, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param projectDataFolder The data folder for the running application/project.
     */
    static LogStreams addLogStream(Folder projectDataFolder, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
    {
        PreCondition.assertNotNull(projectDataFolder, "projectDataFolder");

        final File logFile = CommandLineLogsAction.getLogFile(projectDataFolder);
        return CommandLineLogsAction.addLogStream(logFile, output, error, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     */
    static LogStreams addLogStream(File logFile, CharacterToByteWriteStream output, VerboseCharacterToByteWriteStream verbose)
    {
        return CommandLineLogsAction.addLogStream(logFile, output, null, verbose);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     */
    static LogStreams addLogStream(File logFile, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
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

    static File getLogFile(Folder projectDataFolder)
    {
        PreCondition.assertNotNull(projectDataFolder, "projectDataFolder");

        final Folder projectLogsFolder = CommandLineLogsAction.getQubProjectLogsFolder(projectDataFolder);
        final int logFileCount = projectLogsFolder.getFiles()
            .then((Iterable<File> logFiles) -> logFiles.getCount())
            .catchError(NotFoundException.class, () -> 0)
            .await();
        final String logFileName = (logFileCount + 1) + ".log";
        final File result = projectLogsFolder.getFile(logFileName).await();

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
