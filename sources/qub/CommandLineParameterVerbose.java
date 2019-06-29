package qub;

public class CommandLineParameterVerbose extends CommandLineParameterBoolean
{
    private CharacterWriteStream writeStream;
    private Clock clock;
    private boolean showTimestamp;

    public CommandLineParameterVerbose(CharacterWriteStream writeStream, Clock clock)
    {
        this(writeStream, clock, false);
    }

    public CommandLineParameterVerbose(CharacterWriteStream writeStream, Clock clock, boolean unspecifiedValue)
    {
        super("verbose", unspecifiedValue);

        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotNull(clock, "clock");

        this.writeStream = writeStream;
        this.clock = clock;

        setDescription("Whether or not to show verbose logs.");
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

    public CommandLineParameterVerbose setWriteStream(CharacterWriteStream writeStream)
    {
        this.writeStream = writeStream;
        return this;
    }

    public CommandLineParameterVerbose setClock(Clock clock)
    {
        this.clock = clock;
        return this;
    }

    public CommandLineParameterVerbose setShowTimestamp(boolean showTimestamp)
    {
        this.showTimestamp = showTimestamp;
        return this;
    }

    public Result<Void> writeLine(String message)
    {
        PreCondition.assertNotNull(message, "message");
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotDisposed(writeStream, "writeStream.isDisposed()");
        PreCondition.assertTrue(!showTimestamp || clock != null, "!showTimestamp || clock != null");

        return Result.create(() ->
        {
            if (getValue().await())
            {
                String line = "VERBOSE";
                if (showTimestamp)
                {
                    line += "(" + clock.getCurrentDateTime().getMillisecondsSinceEpoch() + ")";
                }
                line += ": " + message;
                writeStream.writeLine(line).await();
            }
        });
    }
}
