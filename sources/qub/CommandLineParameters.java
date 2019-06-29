package qub;

/**
 * A collection of command-line parameters that can be used for an application.
 */
public class CommandLineParameters
{
    private final List<CommandLineParameterBase<?>> parameters;
    private int nextParameterPosition;
    private CommandLineArguments arguments;
    private CommandLineParameterList anonymousParameterList;

    /**
     * Create a new CommandLineParameters object.
     */
    public CommandLineParameters()
    {
        this.parameters = List.create();
    }

    /**
     * Set the command line arguments that this CommandLineParameters object will get its values
     * from.
     * @param arguments The arguments that were passed to the application on the command line.
     * @return This object for method chaining.
     */
    public CommandLineParameters setArguments(CommandLineArguments arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        this.arguments = arguments;
        for (final CommandLineParameterBase<?> parameter : parameters)
        {
            parameter.setArguments(arguments);
        }

        return this;
    }

    /**
     * Get all of the parameters in their assigned order (position parameters before non-position
     * parameters).
     * @return All of the parameters in their assigned order.
     */
    private Iterable<CommandLineParameterBase<?>> getOrderedParameters()
    {
        final List<CommandLineParameterBase<?>> result = List.create(getPositionParameters());
        result.addAll(getNonPositionParameters());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the parameters that have an assigned position on the command-line.
     * @return The parameters that have an assigned position on the command-line.
     */
    private Indexable<CommandLineParameterBase<?>> getPositionParameters()
    {
        final Indexable<CommandLineParameterBase<?>> result = parameters.where(CommandLineParameterBase::hasIndex).toList();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the parameters that do not have an assigned position on the command-line.
     * @return The parameters that do not have an assigned position on the command-line.
     */
    private Iterable<CommandLineParameterBase<?>> getNonPositionParameters()
    {
        final Iterable<CommandLineParameterBase<?>> result = parameters.where(Functions.not(CommandLineParameterBase::hasIndex));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Add the provided parameter.
     * @param parameter The parameter to add to this CommandLineParameters object.
     * @param <T> The type of value that the parameter will return.
     * @param <TParameter> The Type of the parameter that will be added and returned.
     * @return The parameter that was provided.
     */
    public <T,TParameter extends CommandLineParameterBase<T>> TParameter add(TParameter parameter)
    {
        PreCondition.assertNotNull(parameter, "parameter");

        parameters.add(parameter);
        if (arguments != null)
        {
            parameter.setArguments(arguments);
        }

        return parameter;
    }

    /**
     * Add an optional command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public <T> CommandLineParameter<T> add(String parameterName, Function1<String,Result<T>> parseArgumentValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(parseArgumentValue, "parseArgumentValue");

        return add(new CommandLineParameter<>(parameterName, null, parseArgumentValue));
    }

    /**
     * Add an optional command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public <T> CommandLineParameter<T> addPositional(String parameterName, Function1<String,Result<T>> parseArgumentValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(parseArgumentValue, "parseArgumentValue");

        return add(new CommandLineParameter<>(parameterName, nextParameterPosition++, parseArgumentValue));
    }

    /**
     * Add an optional String-valued command line parameter that can be provided without providing
     * a parameter name in a specific position.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameter<String> addPositionString(String parameterName)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNull(anonymousParameterList, "anonymousParameterList");

        return addPositional(parameterName, Result::success);
    }

    public <T> CommandLineParameterList<T> addPositionList(String parameterName, Function1<String,Result<T>> parseArgumentValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(parseArgumentValue, "parseArgumentValue");
        PreCondition.assertNull(anonymousParameterList, "anonymousParameterList");

        final CommandLineParameterList<T> result = add(new CommandLineParameterList<>(parameterName, nextParameterPosition++, parseArgumentValue));
        anonymousParameterList = result;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Add an optional String-valued command line parameter list that can be provided without
     * providing a parameter name in a specific position.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameterList<String> addPositionStringList(String parameterName)
    {
        return addPositionList(parameterName, Result::success);
    }

    /**
     * Add an optional String-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameter<String> addString(String parameterName)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");

        return add(parameterName, Result::success)
            .setValueRequired(true);
    }

    /**
     * Add an optional Folder-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameter<Folder> addFolder(String parameterName, Process process)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(process, "process");

        return add(parameterName, getFolderParser(process))
            .setValueRequired(true);
    }

    /**
     * Add an optional Folder-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameter<Folder> addPositionalFolder(String parameterName, Process process)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(process, "process");

        return addPositional(parameterName, getFolderParser(process))
            .setValueRequired(true);
    }

    private static Function1<String,Result<Folder>> getFolderParser(Process process)
    {
        return (String value) ->
        {
            return Result.create(() ->
            {
                Folder folder;
                if (Strings.isNullOrEmpty(value))
                {
                    folder = process.getCurrentFolder().await();
                }
                else
                {
                    final FileSystem fileSystem = process.getFileSystem();
                    final Path path = Path.parse(value);
                    if (path.isRooted())
                    {
                        folder = fileSystem.getFolder(path).await();
                    }
                    else
                    {
                        final Path rootedPath = process.getCurrentFolderPath().resolve(path).await();
                        folder = fileSystem.getFolder(rootedPath).await();
                    }
                }
                return folder;
            });
        };
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> CommandLineParameter<T> addEnum(String parameterName, T defaultValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(defaultValue, "defaultValue");

        final CommandLineParameter<T> result = add(parameterName, (String value) ->
        {
            return Result.create(() ->
            {
                T enumResult = defaultValue;
                if (!Strings.isNullOrEmpty(value))
                {
                    for (final T possibleEnumValue : (T[])defaultValue.getClass().getEnumConstants())
                    {
                        if (possibleEnumValue.toString().equalsIgnoreCase(value))
                        {
                            enumResult = possibleEnumValue;
                            break;
                        }
                    }
                }
                return enumResult;
            });
        });
        result.setValueRequired(true);
        return result;
    }

    /**
     * Add an optional boolean-valued command line parameter. If this parameter is not specified on
     * the command line, then it will default to the provided unspecifiedValue. If it is specified
     * but isn't provided a value, then it will default to true.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameterBoolean addBoolean(String parameterName, boolean unspecifiedValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");

        return add(new CommandLineParameterBoolean(parameterName, unspecifiedValue));
    }

    /**
     * Add an optional boolean-valued command line parameter. If this parameter is not specified on
     * the command line, then it will default to false. If it is specified but isn't provided a
     * value, then it will default to true.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameterBoolean addBoolean(String parameterName)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");

        return addBoolean(parameterName, false);
    }

    /**
     * Add a verbose command line parameter.
     * @return The verbose command line parameter.
     */
    public CommandLineParameterVerbose addVerbose(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        return add(new CommandLineParameterVerbose(process.getOutputCharacterWriteStream(), process.getClock()));
    }

    /**
     * Add a debug command line parameter.
     * @return The debug command line parameter.
     */
    public CommandLineParameterBoolean addDebug()
    {
        return addBoolean("debug")
            .setDescription("Whether or not to run this application in debug mode.");
    }

    /**
     * Add a profiler command line parameter.
     * @return The profiler command line parameter.
     */
    public CommandLineParameterProfiler addProfiler(Process process, Class<?> classToAttachTo)
    {
        return add(new CommandLineParameterProfiler(process, classToAttachTo));
    }

    /**
     * Add a help command line parameter.
     * @return The help command line parameter.
     * @return
     */
    public CommandLineParameterBoolean addHelp()
    {
        return addBoolean("help")
            .setDescription("Show the help message for this application.")
            .addAlias("?");
    }

    /**
     * Get the string that shows how this application can be used.
     * @param commandName The name of this application.
     * @return The string that shows how this application can be used.
     */
    public String getUsageString(String commandName)
    {
        PreCondition.assertNotNullAndNotEmpty(commandName, "commandName");

        String result = commandName;
        for (final CommandLineParameterBase<?> parameter : getOrderedParameters())
        {
            result += " " + parameter.getUsageString();
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the lines that explain how to run this application from the command line.
     * @param commandName The name of this application.
     * @return The lines that explain how to run this application from the command line.
     */
    public Iterable<String> getHelpLines(String commandName, String commandDescription)
    {
        PreCondition.assertNotNullAndNotEmpty(commandName, "commandName");
        PreCondition.assertNotNullAndNotEmpty(commandDescription, "commandDescription");

        final List<String> result = List.create();
        result.add("Usage: " + getUsageString(commandName));
        result.add("  " + commandDescription);
        for (final CommandLineParameterBase<?> parameter : getOrderedParameters())
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
    public Result<Void> writeHelpLines(Process process, String commandName, String commandDescription)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNullAndNotEmpty(commandName, "commandName");
        PreCondition.assertNotNullAndNotEmpty(commandDescription, "commandDescription");

        return Result.create(() ->
        {
            final CharacterWriteStream writeStream = process.getOutputCharacterWriteStream();
            for (final String helpLine : getHelpLines(commandName, commandDescription))
            {
                writeStream.writeLine(helpLine).await();
            }
        });
    }
}
