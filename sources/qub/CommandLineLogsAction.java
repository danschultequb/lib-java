package qub;

public interface CommandLineLogsAction
{
    String actionName = "logs";
    String actionDescription = "Show the logs folder.";

    /**
     * Add a logs action to the provided CommandLineActions object.
     * @param actions The CommandLineActions object to add the logs action to.
     */
    static <TProcess extends QubProcess> CommandLineAction<TProcess> add(CommandLineActions<TProcess> actions)
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
     * @param streams The streams that will be combined with the created log stream.
     * @return The combined streams in the same order that they appeared in the provided streams Iterable.
     */
    static LogCharacterWriteStreams addLogStream(Folder projectDataFolder, CharacterWriteStream... streams)
    {
        PreCondition.assertNotNull(projectDataFolder, "projectDataFolder");
        PreCondition.assertNotNullAndNotEmpty(streams, "streams");
        
        return CommandLineLogsAction.addLogStream(projectDataFolder, Iterable.create(streams));
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param projectDataFolder The data folder for the running application/project.
     * @param streams The streams that will be combined with the created log stream.
     * @return The combined streams in the same order that they appeared in the provided streams Iterable.
     */
    static LogCharacterWriteStreams addLogStream(Folder projectDataFolder, Iterable<CharacterWriteStream> streams)
    {
        PreCondition.assertNotNull(projectDataFolder, "projectDataFolder");
        PreCondition.assertNotNullAndNotEmpty(streams, "streams");

        final File logFile = CommandLineLogsAction.getLogFile(projectDataFolder);
        return CommandLineLogsAction.addLogStream(logFile, streams);
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     * @param streams The streams that will be combined with the created log stream.
     * @return The combined streams in the same order that they appeared in the provided streams Iterable.
     */
    static LogCharacterWriteStreams addLogStream(File logFile, CharacterWriteStream... streams)
    {
        PreCondition.assertNotNull(logFile, "logFile");
        PreCondition.assertNotNullAndNotEmpty(streams, "streams");

        return CommandLineLogsAction.addLogStream(logFile, Iterable.create(streams));
    }

    /**
     * Combine the provided streams with a log stream so that any characters that are written to
     * the provided streams will also be written to log stream.
     * @param logFile The file that the combined streams will log to.
     * @param streams The streams that will be combined with the created log stream.
     * @return The combined streams in the same order that they appeared in the provided streams Iterable.
     */
    static LogCharacterWriteStreams addLogStream(File logFile, Iterable<CharacterWriteStream> streams)
    {
        PreCondition.assertNotNull(logFile, "logFile");
        PreCondition.assertNotNullAndNotEmpty(streams, "streams");

        final CharacterWriteStream logStream = logFile.getContentsCharacterWriteStream().await();

        final List<CharacterWriteStream> combinedStreams = List.create();
        for (CharacterWriteStream stream : streams)
        {
            if (stream instanceof VerboseCharacterWriteStream)
            {
                combinedStreams.add(CharacterWriteStreamList.create(stream, new VerboseCharacterWriteStream(true, logStream)));
            }
            else if (stream != null)
            {
                combinedStreams.add(CharacterWriteStreamList.create(stream, logStream));
            }
            else
            {
                combinedStreams.add(null);
            }
        }
        final LogCharacterWriteStreams result = LogCharacterWriteStreams.create(logFile, logStream, combinedStreams);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(streams.getCount(), result.getCombinedStreamsCount(), "result.getCombinedStreamsCount()");

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
