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

    static void run(QubProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final Folder qubBuildDataFolder = process.getQubProjectDataFolder().await();
        final Folder qubProjectLogsFolder = qubBuildDataFolder.getFolder("logs").await();
        if (!qubProjectLogsFolder.exists().await())
        {
            process.getOutputWriteStream().writeLine("The logs folder (" + qubProjectLogsFolder + ") doesn't exist.").await();
        }
        else
        {
            final Path qubProjectLogsFolderPath = qubProjectLogsFolder.getPath();
            final DefaultApplicationLauncher applicationLauncher = process.getDefaultApplicationLauncher();
            applicationLauncher.openFileWithDefaultApplication(qubProjectLogsFolderPath).await();
        }
    }
}
