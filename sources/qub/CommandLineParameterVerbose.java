package qub;

public class CommandLineParameterVerbose extends CommandLineParameterBoolean
{
    private CharacterToByteWriteStream writeStream;

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
        super.setDescription(description);
        return this;
    }

    @Override
    public CommandLineParameterVerbose setAliases(Iterable<String> aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterVerbose setAliases(String... aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterVerbose addAlias(String alias)
    {
        super.addAlias(alias);
        return this;
    }

    @Override
    public CommandLineParameterVerbose addAliases(String... aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterVerbose addAliases(Iterable<String> aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterVerbose setValueName(String valueName)
    {
        super.setValueName(valueName);
        return this;
    }

    @Override
    public CommandLineParameterVerbose setRequired(boolean required)
    {
        super.setRequired(required);
        return this;
    }

    @Override
    public CommandLineParameterVerbose setValueRequired(boolean valueRequired)
    {
        super.setValueRequired(valueRequired);
        return this;
    }

    @Override
    public CommandLineParameterVerbose setArguments(CommandLineArguments arguments)
    {
        super.setArguments(arguments);
        return this;
    }

    public CommandLineParameterVerbose setWriteStream(CharacterToByteWriteStream writeStream)
    {
        this.writeStream = writeStream;
        return this;
    }

    public Result<VerboseCharacterToByteWriteStream> getVerboseCharacterToByteWriteStream()
    {
        return Result.create(() ->
        {
            return VerboseCharacterToByteWriteStream.create(this.writeStream)
                .setIsVerbose(this.getValue().await());
        });
    }

    public Result<Void> writeLine(String message)
    {
        PreCondition.assertNotNull(message, "message");
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotDisposed(writeStream, "writeStream");

        return Result.create(() ->
        {
            if (getValue().await())
            {
                writeStream.writeLine("VERBOSE: " + message).await();
            }
        });
    }
}
