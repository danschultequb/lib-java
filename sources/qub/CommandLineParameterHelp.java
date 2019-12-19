package qub;

public class CommandLineParameterHelp extends CommandLineParameterBoolean
{
    private final CommandLineParameters parameters;

    public CommandLineParameterHelp(CommandLineParameters parameters)
    {
        super("help");

        this.parameters = parameters;

        this.setDescription("Show the help message for this application.");
        this.addAlias("?");
    }

    /**
     * Get the string that shows how this application can be used.
     * @return The string that shows how this application can be used.
     */
    public static String getApplicationUsageString(String applicationName, Iterable<CommandLineParameterBase<?>> parameters)
    {
        PreCondition.assertNotNullAndNotEmpty(applicationName, "applicationName");
        PreCondition.assertNotNull(parameters, "parameters");

        String result = applicationName;
        for (final CommandLineParameterBase<?> parameter : parameters)
        {
            result += " " + parameter.getUsageString();
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the lines that explain how to run this application from the command line.
     */
    public static Iterable<String> getApplicationHelpLines(String applicationName, String applicationDescription, Iterable<CommandLineParameterBase<?>> parameters)
    {
        final List<String> result = List.create();
        if (!Strings.isNullOrEmpty(applicationName))
        {
            result.add("Usage: " + CommandLineParameterHelp.getApplicationUsageString(applicationName, parameters));
        }
        if (!Strings.isNullOrEmpty(applicationDescription))
        {
            result.add("  " + applicationDescription);
        }
        if (parameters != null)
        {
            for (final CommandLineParameterBase<?> parameter : parameters)
            {
                result.add("  " + parameter.getHelpLine());
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Show the help lines that explain how to run this application from the command line if a help
     * command line argument was provided the IProcess.
     * @param process The process that contains a CharacterWriteStream that the help lines will be
     *                written to.
     * @return The result of writing the help lines.
     */
    public Result<Boolean> showApplicationHelpLines(IProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return Result.create(() ->
        {
            final boolean showHelpLines = this.getValue().await();
            if (showHelpLines)
            {
                final CharacterWriteStream writeStream = process.getOutputCharacterWriteStream();
                final Iterable<String> helpLines = CommandLineParameterHelp.getApplicationHelpLines(
                    this.parameters.getApplicationName(),
                    this.parameters.getApplicationDescription(),
                    this.parameters.getOrderedParameters());
                for (final String helpLine : helpLines)
                {
                    writeStream.writeLine(helpLine).await();
                }
                process.setExitCode(-1);
            }
            return showHelpLines;
        });
    }
}
