package qub;

public class CommandLineConfigurationActionParameters
{
    private Folder dataFolder;
    private DefaultApplicationLauncher defaultApplicationLauncher;
    private Path configurationSchemaFileRelativePath;
    private Path configurationFileRelativePath;
    private JSONSchema configurationSchema;
    private JSONObject defaultConfiguration;

    private CommandLineConfigurationActionParameters()
    {
    }

    public static CommandLineConfigurationActionParameters create()
    {
        return new CommandLineConfigurationActionParameters();
    }

    public Folder getDataFolder()
    {
        return this.dataFolder;
    }

    public CommandLineConfigurationActionParameters setDataFolder(Folder dataFolder)
    {
        PreCondition.assertNotNull(dataFolder, "dataFolder");

        this.dataFolder = dataFolder;

        return this;
    }

    public DefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        return this.defaultApplicationLauncher;
    }

    public CommandLineConfigurationActionParameters setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        PreCondition.assertNotNull(defaultApplicationLauncher, "defaultApplicationLauncher");

        this.defaultApplicationLauncher = defaultApplicationLauncher;

        return this;
    }

    public Path getConfigurationSchemaFileRelativePath()
    {
        return this.configurationSchemaFileRelativePath;
    }

    public CommandLineConfigurationActionParameters setConfigurationSchemaFileRelativePath(String configurationSchemaFileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(configurationSchemaFileRelativePath, "configurationSchemaFileRelativePath");

        return this.setConfigurationSchemaFileRelativePath(Path.parse(configurationSchemaFileRelativePath));
    }

    public CommandLineConfigurationActionParameters setConfigurationSchemaFileRelativePath(Path configurationSchemaFileRelativePath)
    {
        PreCondition.assertNotNull(configurationSchemaFileRelativePath, "configurationSchemaFileRelativePath");
        PreCondition.assertFalse(configurationSchemaFileRelativePath.isRooted(), "configurationSchemaFileRelativePath.isRooted()");

        this.configurationSchemaFileRelativePath = configurationSchemaFileRelativePath;

        return this;
    }

    public Path getConfigurationFileRelativePath()
    {
        return this.configurationFileRelativePath;
    }

    public CommandLineConfigurationActionParameters setConfigurationFileRelativePath(String configurationFileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(configurationFileRelativePath, "configurationFileRelativePath");

        return this.setConfigurationFileRelativePath(Path.parse(configurationFileRelativePath));
    }

    public CommandLineConfigurationActionParameters setConfigurationFileRelativePath(Path configurationFileRelativePath)
    {
        PreCondition.assertNotNull(configurationFileRelativePath, "configurationFileRelativePath");
        PreCondition.assertFalse(configurationFileRelativePath.isRooted(), "configurationFileRelativePath.isRooted()");

        this.configurationFileRelativePath = configurationFileRelativePath;

        return this;
    }

    public JSONSchema getConfigurationSchema()
    {
        return this.configurationSchema;
    }

    public CommandLineConfigurationActionParameters setConfigurationSchema(JSONSchema configurationSchema)
    {
        PreCondition.assertNotNull(configurationSchema, "configurationSchema");

        this.configurationSchema = configurationSchema;

        return this;
    }

    public JSONObject getDefaultConfiguration()
    {
        return this.defaultConfiguration;
    }

    public CommandLineConfigurationActionParameters setDefaultConfiguration(JSONObject defaultConfiguration)
    {
        PreCondition.assertNotNull(defaultConfiguration, "defaultConfiguration");

        this.defaultConfiguration = defaultConfiguration;

        return this;
    }

    public JSONObject toJson()
    {
        final JSONObject result = JSONObject.create();

        if (this.dataFolder != null)
        {
            result.setString("dataFolder", this.dataFolder.toString());
        }
        if (this.defaultApplicationLauncher != null)
        {
            result.setString("defaultApplicationLauncher", "not null");
        }
        if (this.configurationSchemaFileRelativePath != null)
        {
            result.setString("configurationSchemaFileRelativePath", this.configurationSchemaFileRelativePath.toString());
        }
        if (this.configurationFileRelativePath != null)
        {
            result.setString("configurationFileRelativePath", this.configurationFileRelativePath.toString());
        }
        if (this.configurationSchema != null)
        {
            result.setObject("configurationSchema", this.configurationSchema.toJson());
        }
        if (this.defaultConfiguration != null)
        {
            result.setObject("defaultConfiguration", this.defaultConfiguration);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CommandLineConfigurationActionParameters && this.equals((CommandLineConfigurationActionParameters)rhs);
    }

    public boolean equals(CommandLineConfigurationActionParameters rhs)
    {
        return rhs != null &&
            Comparer.equal(this.dataFolder, rhs.dataFolder) &&
            Comparer.equal(this.defaultApplicationLauncher, rhs.defaultApplicationLauncher) &&
            Comparer.equal(this.configurationSchemaFileRelativePath, rhs.configurationSchemaFileRelativePath) &&
            Comparer.equal(this.configurationFileRelativePath, rhs.configurationFileRelativePath) &&
            Comparer.equal(this.configurationSchema, rhs.configurationSchema) &&
            Comparer.equal(this.defaultConfiguration, rhs.defaultConfiguration);
    }

    @Override
    public String toString()
    {
        return this.toJson().toString();
    }
}
