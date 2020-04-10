package qub;

/**
 * A collection of command-line parameters that can be used for an application.
 */
public class CommandLineParameters
{
    private String applicationName;
    private String applicationDescription;
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
     * Set the name of the application that these parameters apply to.
     * @param applicationName The name of the application that these parameters apply to.
     * @return This object for method chaining.
     */
    public CommandLineParameters setApplicationName(String applicationName)
    {
        this.applicationName = applicationName;

        return this;
    }

    /**
     * Get the name of the application.
     * @return The name of the application.
     */
    public String getApplicationName()
    {
        return this.applicationName;
    }

    /**
     * Set the description of the application that these parameters apply to.
     * @param applicationDescription The description of the application that these parameters apply
     *                               to.
     * @return This object for method chaining.
     */
    public CommandLineParameters setApplicationDescription(String applicationDescription)
    {
        this.applicationDescription = applicationDescription;

        return this;
    }

    /**
     * Get the description of the application that these parameters apply to.
     * @return The description of the application that these parameters apply to.
     */
    public String getApplicationDescription()
    {
        return this.applicationDescription;
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
    public Iterable<CommandLineParameterBase<?>> getOrderedParameters()
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

        return this.addString(parameterName, (String)null);
    }

    /**
     * Add an optional String-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @param defaultValue The value that will be assigned to this parameter if no matching argument
     *                     is specified.
     * @return The new command line parameter.
     */
    public CommandLineParameter<String> addString(String parameterName, String defaultValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");

        return this.addString(parameterName, () -> defaultValue);
    }

    /**
     * Add an optional String-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @param defaultValueFunction The function that will be run to get the value that will be
     *                             assigned to this parameter if no matching argument is specified.
     * @return The new command line parameter.
     */
    public CommandLineParameter<String> addString(String parameterName, Function0<String> defaultValueFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(defaultValueFunction, "defaultValueFunction");

        final CommandLineParameter<String> result = this.add(parameterName, (String argumentValue) ->
        {
            return Result.success(argumentValue == null ? defaultValueFunction.run() : argumentValue);
        });
        return result.setValueRequired(true);
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

        return this.add(parameterName, getFolderParser(process))
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
                    Path path = Path.parse(value);
                    if (!path.isRooted())
                    {
                        path = process.getCurrentFolderPath().resolve(path).await();
                    }

                    final FileSystem fileSystem = process.getFileSystem();
                    folder = fileSystem.getFolder(path).await();
                }
                return folder;
            });
        };
    }

    /**
     * Add an optional File-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameter<File> addFile(String parameterName, Process process)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(process, "process");

        return add(parameterName, CommandLineParameters.getFileParser(process))
            .setValueRequired(true);
    }

    /**
     * Add an optional File-valued command line parameter.
     * @param parameterName The name of the parameter.
     * @return The new command line parameter.
     */
    public CommandLineParameter<File> addPositionalFile(String parameterName, Process process)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(process, "process");

        return addPositional(parameterName, CommandLineParameters.getFileParser(process))
            .setValueRequired(true);
    }

    private static Function1<String,Result<File>> getFileParser(Process process)
    {
        return (String value) ->
        {
            return Result.create(() ->
            {
                File file = null;
                if (!Strings.isNullOrEmpty(value))
                {
                    Path path = Path.parse(value);
                    if (!path.isRooted())
                    {
                        path = process.getCurrentFolderPath().resolve(path).await();
                    }

                    final FileSystem fileSystem = process.getFileSystem();
                    file = fileSystem.getFile(path).await();
                }
                return file;
            });
        };
    }

    /**
     * Add an Enum command line parameter.
     * @param parameterName The name of the command line parameter.
     * @param defaultValue The Enum value that will be used if the command line parameter has no
 *                         argument or if the argument is empty.
     * @param <T> The type of Enum the parameter will use.
     * @return The new CommandLineParameter object.
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> CommandLineParameter<T> addEnum(String parameterName, T defaultValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(defaultValue, "defaultValue");

        return this.addEnum(parameterName, defaultValue, defaultValue);
    }

    /**
     * Add an Enum command line parameter.
     * @param parameterName The name of the command line parameter.
     * @param unspecifiedValue The Enum value that will be used if the command line parameter has no
     *                         argument.
     * @param emptyValue The Enum value that will be used if the command line parameter has no
     *                   argument value.
     * @param <T> The type of Enum the parameter will use.
     * @return The new CommandLineParameter object.
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> CommandLineParameter<T> addEnum(String parameterName, T unspecifiedValue, T emptyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(parameterName, "parameterName");
        PreCondition.assertNotNull(unspecifiedValue, "unspecifiedValue");
        PreCondition.assertNotNull(emptyValue, "emptyValue");

        final CommandLineParameter<T> result = this.add(parameterName, (String value) ->
        {
            Result<T> enumResult;
            if (value == null)
            {
                enumResult = Result.success(unspecifiedValue);
            }
            else if (Comparer.equal(value, ""))
            {
                enumResult = Result.success(emptyValue);
            }
            else
            {
                enumResult = Enums.parse((Class<T>)unspecifiedValue.getClass(), value)
                    .catchError(NotFoundException.class, () -> unspecifiedValue);
            }
            return enumResult;
        });
        result.setValueRequired(true);

        PostCondition.assertNotNull(result, "result");

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

        return this.addBoolean(parameterName, false);
    }

    /**
     * Add a verbose command line parameter.
     * @return The verbose command line parameter.
     */
    public CommandLineParameterVerbose addVerbose(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        return this.add(new CommandLineParameterVerbose(process.getOutputWriteStream()));
    }

    /**
     * Add a debug command line parameter.
     * @return The debug command line parameter.
     */
    public CommandLineParameterBoolean addDebug()
    {
        return this.addBoolean("debug")
            .setDescription("Whether or not to run this application in debug mode.");
    }

    /**
     * Add a profiler command line parameter.
     * @return The profiler command line parameter.
     */
    public CommandLineParameterProfiler addProfiler(Process process, Class<?> classToAttachTo)
    {
        return this.add(new CommandLineParameterProfiler(process, classToAttachTo));
    }

    /**
     * Add a help command line parameter.
     * @return The help command line parameter.
     */
    public CommandLineParameterHelp addHelp()
    {
        return add(new CommandLineParameterHelp(this));
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
            final CharacterWriteStream writeStream = process.getOutputWriteStream();
            for (final String helpLine : getHelpLines(commandName, commandDescription))
            {
                writeStream.writeLine(helpLine).await();
            }
        });
    }
}
