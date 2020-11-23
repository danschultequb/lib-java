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
    static Indexable<CharacterWriteStream> addLogStreams(Folder projectDataFolder, Iterable<CharacterWriteStream> streams)
    {
        PreCondition.assertNotNull(projectDataFolder, "projectDataFolder");
        PreCondition.assertNotNullAndNotEmpty(streams, "streams");


        final Folder projectLogsFolder = CommandLineLogsAction.getQubProjectLogsFolder(projectDataFolder);
        final int logFileCount = projectLogsFolder.getFiles()
            .then((Iterable<File> logFiles) -> logFiles.getCount())
            .catchError(NotFoundException.class, () -> 0)
            .await();
        final String logFileName = (logFileCount + 1) + ".log";
        final CharacterWriteStream logStream = projectLogsFolder.getFile(logFileName).await()
            .getContentsCharacterWriteStream().await();

        final List<CharacterWriteStream> result = List.create();
        for (CharacterWriteStream stream : streams)
        {
            if (stream instanceof VerboseCharacterWriteStream)
            {
                result.add(CharacterWriteStreamList.create(stream, new VerboseCharacterWriteStream(true, logStream)));
            }
            else if (stream != null)
            {
                result.add(CharacterWriteStreamList.create(stream, logStream));
            }
            else
            {
                result.add(null);
            }
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");
        PostCondition.assertEqual(streams.getCount(), result.getCount(), "result.getCount()");

        return result;
    }
}
