package qub;

public class CommandLineParameterVerbose extends CommandLineParameterBoolean
{
    private final CharacterToByteWriteStream writeStream;

    public CommandLineParameterVerbose(CharacterToByteWriteStream writeStream)
    {
        this(writeStream, false);
    }

    public CommandLineParameterVerbose(CharacterToByteWriteStream writeStream, boolean unspecifiedValue)
    {
        super("verbose", unspecifiedValue);

        PreCondition.assertNotNull(writeStream, "writeStream");

        this.writeStream = writeStream;

        this.setDescription("Whether or not to show verbose logs.");
        this.addAlias("v");
    }

    @Override
    public CommandLineParameterVerbose setDescription(String description)
    {
        return (CommandLineParameterVerbose)super.setDescription(description);
    }

    @Override
    public CommandLineParameterVerbose setAliases(Iterable<String> aliases)
    {
        return (CommandLineParameterVerbose)super.setAliases(aliases);
    }

    @Override
    public CommandLineParameterVerbose setAliases(String... aliases)
    {
        return (CommandLineParameterVerbose)super.setAliases(aliases);
    }

    @Override
    public CommandLineParameterVerbose addAlias(String alias)
    {
        return (CommandLineParameterVerbose)super.addAlias(alias);
    }

    @Override
    public CommandLineParameterVerbose addAliases(String... aliases)
    {
        return (CommandLineParameterVerbose)super.addAliases(aliases);
    }

    @Override
    public CommandLineParameterVerbose addAliases(Iterable<String> aliases)
    {
        return (CommandLineParameterVerbose)super.addAliases(aliases);
    }

    @Override
    public CommandLineParameterVerbose setValueName(String valueName)
    {
        return (CommandLineParameterVerbose)super.setValueName(valueName);
    }

    @Override
    public CommandLineParameterVerbose setRequired(boolean required)
    {
        return (CommandLineParameterVerbose)super.setRequired(required);
    }

    @Override
    public CommandLineParameterVerbose setValueRequired(boolean valueRequired)
    {
        return (CommandLineParameterVerbose)super.setValueRequired(valueRequired);
    }

    @Override
    public CommandLineParameterVerbose setArguments(CommandLineArguments arguments)
    {
        return (CommandLineParameterVerbose)super.setArguments(arguments);
    }

    public boolean isVerbose()
    {
        return this.getValue().await();
    }

    public Result<VerboseCharacterToByteWriteStream> getVerboseCharacterToByteWriteStream()
    {
        return Result.create(() ->
        {
            return VerboseCharacterToByteWriteStream.create(this.writeStream)
                .setIsVerbose(this.isVerbose());
        });
    }
}
