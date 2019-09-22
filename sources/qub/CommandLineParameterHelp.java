package qub;

public class CommandLineParameterHelp extends CommandLineParameterBoolean
{
    private final CommandLineParameters parameters;
    private String applicationDescription;

    public CommandLineParameterHelp(CommandLineParameters parameters)
    {
        super("help");

        this.parameters = parameters;

        this.setDescription("Show the help message for this application.");
        this.addAlias("?");
    }

    /**
     * Set the description for the current application.
     * @param applicationDescription The description for the current application.
     * @return This object for method chaining.
     */
    public CommandLineParameterHelp setApplicationDescription(String applicationDescription)
    {
        this.applicationDescription = applicationDescription;

        return this;
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
        result.add("Usage: " + CommandLineParameterHelp.getApplicationUsageString(applicationName, parameters));
        if (!Strings.isNullOrEmpty(applicationDescription))
        {
            result.add("  " + applicationDescription);
        }
        for (final CommandLineParameterBase<?> parameter : parameters)
        {
            result.add("  " + parameter.getHelpLine());
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Write the help lines that explain how to run this application from the command line.
     * @param process The process that contains a CharacterWriteStream that the help lines will be
     *                written to.
     * @return The result of writing the help lines.
     */
    public Result<Boolean> writeApplicationHelpLines(Process process)
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
                    this.applicationDescription,
                    this.parameters.getOrderedParameters());
                for (final String helpLine : helpLines)
                {
                    writeStream.writeLine(helpLine).await();
                }
            }
            return showHelpLines;
        });
    }
}
