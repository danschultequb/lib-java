package qub;

public interface CommandLineConfigurationAction
{
    /**
     * Add a configuration action to the provided CommandLineActions object.
     * @param actions The CommandLineActions object to add the configuration action to.
     */
    static CommandLineAction addAction(CommandLineActions actions)
    {
        PreCondition.assertNotNull(actions, "actions");

        return CommandLineConfigurationAction.addAction(actions, CommandLineConfigurationActionParameters.create());
    }

    /**
     * Add a configuration action to the provided CommandLineActions object.
     * @param actions The CommandLineActions object to add the configuration action to.
     */
    static CommandLineAction addAction(CommandLineActions actions, CommandLineConfigurationActionParameters parameters)
    {
        PreCondition.assertNotNull(actions, "actions");
        PreCondition.assertNotNull(parameters, "parameters");

        return actions.addAction("configuration",
                (DesktopProcess process, CommandLineAction action) -> CommandLineConfigurationAction.getParameters(process, action, parameters),
                CommandLineConfigurationAction::run)
            .addAlias("config")
            .setDescription("Open the configuration file for this application.");
    }

    static CommandLineConfigurationActionParameters getParameters(DesktopProcess process, CommandLineAction action, CommandLineConfigurationActionParameters parameters)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(parameters, "parameters");

        final CommandLineParameters commandLineParameters = action.createCommandLineParameters(process);
        final CommandLineParameterHelp helpParameter = commandLineParameters.addHelp();

        CommandLineConfigurationActionParameters result = null;
        if (!helpParameter.showApplicationHelpLines(process).await())
        {
            result = parameters;
            if (result.getDataFolder() == null)
            {
                result.setDataFolder(process.getQubProjectDataFolder().await());
            }
            if (result.getDefaultApplicationLauncher() == null)
            {
                result.setDefaultApplicationLauncher(process.getDefaultApplicationLauncher());
            }
            if (result.getConfigurationSchemaFileRelativePath() == null && result.getConfigurationSchema() != null)
            {
                result.setConfigurationSchemaFileRelativePath("configuration.schema.json");
            }
            if (result.getConfigurationFileRelativePath() == null)
            {
                result.setConfigurationFileRelativePath("configuration.json");
            }
        }

        return result;
    }

    static void run(CommandLineConfigurationActionParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");
        PreCondition.assertNotNull(parameters.getDataFolder(), "parameters.getDataFolder()");
        PreCondition.assertNotNull(parameters.getDefaultApplicationLauncher(), "parameters.getDefaultApplicationLauncher()");
        PreCondition.assertNotNull(parameters.getConfigurationFileRelativePath(), "parameters.getConfigurationFileRelativePath()");

        final Folder dataFolder = parameters.getDataFolder();
        final DefaultApplicationLauncher defaultApplicationLauncher = parameters.getDefaultApplicationLauncher();

        final Path configurationSchemaFileRelativePath = parameters.getConfigurationSchemaFileRelativePath();
        final JSONSchema configurationSchema = parameters.getConfigurationSchema();

        if (configurationSchemaFileRelativePath != null && configurationSchema != null)
        {
            final File configurationSchemaJsonFile = dataFolder.getFile(configurationSchemaFileRelativePath).await();
            try (final CharacterWriteStream configurationSchemaJsonWriteStream = configurationSchemaJsonFile.getContentsCharacterWriteStream().await())
            {
                configurationSchema.toString(configurationSchemaJsonWriteStream, JSONFormat.pretty).await();
            }
        }

        final Path configurationFileRelativePath = parameters.getConfigurationFileRelativePath();
        final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
        final JSONObject defaultConfiguration = parameters.getDefaultConfiguration();
        if (!configurationFile.exists().await())
        {
            try (final CharacterWriteStream configurationJsonWriteStream = configurationFile.getContentsCharacterWriteStream().await())
            {
                if (defaultConfiguration != null)
                {
                    defaultConfiguration.toString(configurationJsonWriteStream, JSONFormat.pretty).await();
                }
            }
        }

        defaultApplicationLauncher.openFileWithDefaultApplication(configurationFile).await();
    }
}
