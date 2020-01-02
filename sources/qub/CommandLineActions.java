package qub;

/**
 * A collection of CommandLineAction objects that can be used for an application.
 */
public class CommandLineActions
{
    private String applicationName;
    private String applicationDescription;
    private final List<CommandLineAction> actions;

    public CommandLineActions()
    {
        this.actions = List.create();
    }

    /**
     * Set the name of the application that these actions apply to.
     * @param applicationName The name of the application that these actions apply to.
     * @return This object for method chaining.
     */
    public CommandLineActions setApplicationName(String applicationName)
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
     * Set the description of the application that these actions apply to.
     * @param applicationDescription The description of the application that these actions apply to.
     * @return This object for method chaining.
     */
    public CommandLineActions setApplicationDescription(String applicationDescription)
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

    public Result<CommandLineAction> getAction(String actionName)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");

        CommandLineAction matchingAction = null;
        for (final CommandLineAction action : this.actions)
        {
            if (action.containsActionName(actionName, false))
            {
                matchingAction = action;
                break;
            }
        }

        return matchingAction == null
            ? Result.error(new NotFoundException("No action was found with the name " + Strings.escapeAndQuote(actionName) + "."))
            : Result.success(matchingAction);
    }

    /**
     * Get whether or not this CommandLineActions object contains an action with either the name or
     * an alias that matches the provided actionName.
     * @param actionName The name of the action to look for.
     * @return Whether or not this CommandLineActions object contains an action with either the name
     * or an alias that matches the provided actionName.
     */
    public boolean containsActionName(String actionName)
    {
        return this.getAction(actionName)
            .then(() -> true)
            .catchError(() -> false)
            .await();
    }

    public CommandLineAction addAction(String actionName, Action1<Process> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainAction, "mainAction");
        PreCondition.assertFalse(this.containsActionName(actionName), "this.containsActionName(actionName)");

        final CommandLineAction result = new CommandLineAction(actionName, mainAction)
            .setParentActions(this);
        this.actions.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public CommandLineAction addAction(String actionName, Function1<Process,Integer> mainFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainFunction, "mainFunction");

        return this.addAction(actionName, Process.getMainAction(mainFunction));
    }

    public <TParameters> CommandLineAction addAction(String actionName, Function1<Process,TParameters> getParametersFunction, Action1<TParameters> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainAction, "mainAction");

        return this.addAction(actionName, Process.getMainAction(getParametersFunction, mainAction));
    }

    public <TParameters> CommandLineAction addAction(String actionName, Function1<Process,TParameters> getParametersFunction, Function1<TParameters,Integer> mainFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainFunction, "mainFunction");

        return this.addAction(actionName, Process.getMainAction(getParametersFunction, mainFunction));
    }

    /**
     * Run the action from this collection that matches the action argument from the provided
     * process. If no action argument is found, then the help message for this action collection
     * will be displayed.
     * @param process The process that is attempting to run an action.
     */
    public void run(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        final CommandLineParameters parameters = process.createCommandLineParameters()
            .setApplicationName(this.getApplicationName())
            .setApplicationDescription(this.getApplicationDescription());
        final CommandLineParameter<String> actionParameter = parameters.addPositionString("action")
            .addAlias("a")
            .setDescription("The name of the action to invoke.")
            .setRequired(true)
            .setValueName("<action-name>");
        final CommandLineParameterHelp helpParameter = parameters.addHelp();

        final String actionName = actionParameter.removeValue().await();
        final CommandLineAction action = Strings.isNullOrEmpty(actionName)
            ? null
            : this.getAction(actionName)
                .catchError(NotFoundException.class)
                .await();
        if (action == null)
        {
            process.getCommandLineArguments().addNamedArgument("?");
        }
        if (helpParameter.showApplicationHelpLines(process).await())
        {
            if (this.actions.any())
            {
                final IndentedCharacterWriteStream output = new IndentedCharacterWriteStream(process.getOutputCharacterWriteStream());
                output.writeLine().await();
                output.writeLine("Actions:").await();
                output.indent(() ->
                {
                    for (final CommandLineAction a : this.actions.order((CommandLineAction lhs, CommandLineAction rhs) -> Strings.lessThan(lhs.getName(), rhs.getName())))
                    {
                        output.write(a.getName()).await();
                        if (!Strings.isNullOrEmpty(a.getDescription()))
                        {
                            output.write(": " + a.getDescription()).await();
                        }
                        output.writeLine();
                    }
                });
            }
        }
        else
        {
            action.run(process);
        }
    }
}
