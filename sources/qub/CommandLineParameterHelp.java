package qub;

public class CommandLineParameterHelp extends CommandLineParameterBoolean
{
    private final CommandLineParameters parameters;
    private boolean forceShowApplicationHelpLines;

    public CommandLineParameterHelp(CommandLineParameters parameters)
    {
        super("help");

        PreCondition.assertNotNull(parameters, "parameters");

        this.parameters = parameters;

        this.setDescription("Show the help message for this application.");
        this.addAlias("?");
    }

    /**
     * Get whether or not this parameter will show the application's help lines, even if no --help
     * or --? command line arguments are provided.
     * @return Whether or not this parameter will show the application's help lines, even if no
     * --help or --? command line arguments are provided.
     */
    public boolean getForceShowApplicationHelpLines()
    {
        return this.forceShowApplicationHelpLines;
    }

    /**
     * Set whether or not this parameter will show the application's help lines, even if no --help
     * or --? command line arguments are provided.
     * @param forceShowApplicationHelpLines Whether or not this parameter will show the
     *                                      application's help lines, even if no --help or --?
     *                                      command line arguments are provided.
     * @return This object for method chaining.
     */
    public CommandLineParameterHelp setForceShowApplicationHelpLines(boolean forceShowApplicationHelpLines)
    {
        this.forceShowApplicationHelpLines = forceShowApplicationHelpLines;

        return this;
    }

    /**
     * Show the help lines that explain how to run this application from the command line if a help
     * command line argument was provided the Process.
     * @param process The process that contains a CharacterWriteStream that the help lines will be
     *                written to.
     * @return The result of writing the help lines.
     */
    public Result<Boolean> showApplicationHelpLines(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return Result.create(() ->
        {
            final boolean showHelpLines = this.getForceShowApplicationHelpLines() || this.getValue().await();
            if (showHelpLines)
            {
                this.writeApplicationHelpLines(IndentedCharacterWriteStream.create(process.getOutputWriteStream())).await();

                process.setExitCode(-1);
            }
            return showHelpLines;
        });
    }

    /**
     * Write the help lines that explain how to run this application from the command line.
     * @param writeStream The process that contains a CharacterWriteStream that the help lines will be
     *                written to.
     * @return The number of characters that were written.
     */
    public Result<Integer> writeApplicationHelpLines(IndentedCharacterWriteStream writeStream)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");

        return Result.create(() ->
        {
            int result = 0;

            final String applicationName = this.parameters.getApplicationName();
            final String applicationDescription = this.parameters.getApplicationDescription();
            final Iterable<CommandLineParameterBase<?>> parameters = this.parameters.getOrderedParameters();

            if (!Strings.isNullOrEmpty(applicationName))
            {
                result += writeStream.write("Usage: ").await();
                result += writeStream.write(applicationName).await();
                for (final CommandLineParameterBase<?> parameter : parameters)
                {
                    result += writeStream.write(" " + parameter.getUsageString()).await();
                }
                result += writeStream.writeLine().await();
            }

            writeStream.increaseIndent();
            try
            {
                if (!Strings.isNullOrEmpty(applicationDescription))
                {
                    result += writeStream.writeLine(applicationDescription).await();
                }
                if (parameters != null)
                {
                    final CharacterTable parameterTable = CharacterTable.create();
                    for (final CommandLineParameterBase<?> parameter : parameters)
                    {
                        String parameterNameCellText = "--" + parameter.getName();
                        final Iterable<String> aliases = parameter.getAliases();
                        if (!Iterable.isNullOrEmpty(aliases))
                        {
                            parameterNameCellText += "(" + Strings.join(',', aliases) + ")";
                        }
                        parameterNameCellText += ":";

                        String parameterDescriptionCellText = parameter.getDescription();
                        if (Strings.isNullOrEmpty(parameterDescriptionCellText))
                        {
                            parameterDescriptionCellText = "(No description provided)";
                        }

                        parameterTable.addRow(parameterNameCellText, parameterDescriptionCellText);
                    }
                    result += parameterTable.toString(writeStream, CharacterTableFormat.consise).await();
                }
            }
            finally
            {
                writeStream.decreaseIndent();
            }

            result += writeStream.writeLine().await();

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }
}
