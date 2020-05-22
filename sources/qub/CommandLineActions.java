package qub;

/**
 * A collection of CommandLineAction objects that can be used for an application.
 */
public class CommandLineActions<TProcess extends Process>
{
    private String applicationName;
    private String applicationDescription;
    private final List<CommandLineAction<TProcess>> actions;

    private CommandLineActions()
    {
        this.actions = List.create();
    }

    public static <TProcess extends Process> CommandLineActions<TProcess> create()
    {
        return new CommandLineActions<>();
    }

    /**
     * Set the name of the application that these actions apply to.
     * @param applicationName The name of the application that these actions apply to.
     * @return This object for method chaining.
     */
    public CommandLineActions<TProcess> setApplicationName(String applicationName)
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
    public CommandLineActions<TProcess> setApplicationDescription(String applicationDescription)
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

    public CommandLineAction<TProcess> getDefaultAction()
    {
        return this.actions.first(CommandLineAction::isDefaultAction);
    }

    public boolean hasDefaultAction()
    {
        return this.getDefaultAction() != null;
    }

    public Result<CommandLineAction<TProcess>> getAction(String actionName)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");

        return Result.create(() ->
        {
            final CommandLineAction<TProcess> matchingAction = this.actions
                .first((CommandLineAction<TProcess> action) -> action.containsActionName(actionName, false));
            if (matchingAction == null)
            {
                throw new NotFoundException("No action was found with the name " + Strings.escapeAndQuote(actionName) + ".");
            }
            return matchingAction;
        });
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

    public CommandLineAction<TProcess> addAction(String actionName, Action1<TProcess> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainAction, "mainAction");
        PreCondition.assertFalse(this.containsActionName(actionName), "this.containsActionName(actionName)");

        final CommandLineAction<TProcess> result = CommandLineAction.create(actionName, mainAction)
            .setParentActions(this);
        this.actions.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public CommandLineAction<TProcess> addAction(String actionName, Function1<TProcess,Integer> mainFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainFunction, "mainFunction");

        return this.addAction(actionName, Process.getMainAction(mainFunction));
    }

    public <TParameters> CommandLineAction<TProcess> addAction(String actionName, Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainAction, "mainAction");

        return this.addAction(actionName, Process.getMainAction(getParametersFunction, mainAction));
    }

    public <TParameters> CommandLineAction<TProcess> addAction(String actionName, Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> mainFunction)
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
    public void run(TProcess process)
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
        CommandLineAction<TProcess> action = Strings.isNullOrEmpty(actionName)
            ? this.getDefaultAction()
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
                final IndentedCharacterWriteStream output = IndentedCharacterWriteStream.create(process.getOutputWriteStream());
                output.writeLine().await();
                output.writeLine("Actions:").await();
                output.indent(() ->
                {
                    for (final CommandLineAction<TProcess> a : this.actions.order((CommandLineAction<TProcess> lhs, CommandLineAction<TProcess> rhs) -> Strings.lessThan(lhs.getName(), rhs.getName())))
                    {
                        output.write(a.getName()).await();
                        if (a.isDefaultAction())
                        {
                            output.write(" (default)").await();
                        }
                        if (!Strings.isNullOrEmpty(a.getDescription()))
                        {
                            output.write(": " + a.getDescription()).await();
                        }
                        output.writeLine().await();
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
