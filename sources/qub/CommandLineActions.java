package qub;

/**
 * A collection of CommandLineAction objects that can be used for an application.
 */
public class CommandLineActions
{
    private DesktopProcess process;
    private String applicationName;
    private String applicationDescription;
    private final List<CommandLineAction> actions;

    private CommandLineActions()
    {
        this.actions = List.create();
    }

    public static CommandLineActions create()
    {
        return new CommandLineActions();
    }

    public DesktopProcess getProcess()
    {
        return this.process;
    }

    public CommandLineActions setProcess(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        this.process = process;

        return this;
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

    /**
     * Get the full action name of the provided action name. The full action name is the
     * application's name followed by the action path to reach the provided action.
     * @param actionName The name of the action to get the full action of.
     * @return The full action name.
     */
    public String getFullActionName(String actionName)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");

        final CharacterList fullActionName = CharacterList.create();

        final String applicationName = this.getApplicationName();
        if (!Strings.isNullOrEmpty(applicationName))
        {
            fullActionName.addAll(applicationName)
                  .add(' ');
        }
        fullActionName.addAll(actionName);
        final String result = fullActionName.toString();

        PostCondition.assertNotNullAndNotEmpty(result, "result");
        PostCondition.assertEndsWith(result, actionName, "result");

        return result;
    }

    public CommandLineAction getDefaultAction()
    {
        return this.actions.first(CommandLineAction::isDefaultAction);
    }

    public boolean hasDefaultAction()
    {
        return this.getDefaultAction() != null;
    }

    public Result<CommandLineAction> getAction(String actionName)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");

        return Result.create(() ->
        {
            final CommandLineAction matchingAction = this.actions
                .first((CommandLineAction action) -> action.containsActionName(actionName, false));
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

    public CommandLineActions addAction(Action1<CommandLineActions> actionAdder)
    {
        PreCondition.assertNotNull(actionAdder, "actionAdder");

        actionAdder.run(this);

        return this;
    }

    public CommandLineAction addAction(String actionName, Action1<DesktopProcess> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainAction, "mainAction");
        PreCondition.assertFalse(this.containsActionName(actionName), "this.containsActionName(actionName)");

        return this.addAction(actionName, (DesktopProcess process, CommandLineAction action) -> mainAction.run(process));
    }

    public CommandLineAction addAction(String actionName, Action2<DesktopProcess,CommandLineAction> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainAction, "mainAction");
        PreCondition.assertFalse(this.containsActionName(actionName), "this.containsActionName(actionName)");

        final CommandLineAction result = CommandLineAction.create(actionName, mainAction)
            .setParentActions(this);
        this.actions.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public CommandLineAction addAction(String actionName, Function1<DesktopProcess,Integer> mainFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(mainFunction, "mainFunction");

        return this.addAction(actionName, DesktopProcess.getMainAction(mainFunction));
    }

    public <TParameters> CommandLineAction addAction(String actionName, Function1<DesktopProcess,TParameters> getParametersFunction, Action1<TParameters> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainAction, "mainAction");

        return this.addAction(actionName, DesktopProcess.getMainAction(getParametersFunction, mainAction));
    }

    public <TParameters> CommandLineAction addAction(String actionName, Function1<DesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> mainFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainFunction, "mainFunction");

        return this.addAction(actionName, DesktopProcess.getMainAction(getParametersFunction, mainFunction));
    }

    public <TParameters> CommandLineAction addAction(String actionName, Function2<DesktopProcess,CommandLineAction,TParameters> getParametersFunction, Action1<TParameters> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainAction, "mainAction");

        final Value<CommandLineAction> actionValue = Value.create();
        actionValue.set(this.addAction(
            actionName,
            DesktopProcess.getMainAction(
                (DesktopProcess process) -> getParametersFunction.run(process, actionValue.get()),
                mainAction)));
        final CommandLineAction result = actionValue.get();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public <TParameters> CommandLineAction addAction(String actionName, Function2<DesktopProcess,CommandLineAction,TParameters> getParametersFunction, Function1<TParameters,Integer> mainFunction)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(mainFunction, "mainFunction");

        final Value<CommandLineAction> actionValue = Value.create();
        actionValue.set(this.addAction(
            actionName,
            DesktopProcess.getMainAction(
                (DesktopProcess process) -> getParametersFunction.run(process, actionValue.get()),
                mainFunction)));
        final CommandLineAction result = actionValue.get();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public void run()
    {
        PreCondition.assertNotNull(this.getProcess(), "this.getProcess()");

        this.run(this.getProcess());
    }

    /**
     * Run the action from this collection that matches the action argument from the provided
     * process. If no action argument is found, then the help message for this action collection
     * will be displayed.
     */
    public void run(DesktopProcess process)
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

        final CommandLineAction defaultAction = this.getDefaultAction();
        CommandLineAction actionToRun = null;
        if (Strings.isNullOrEmpty(actionName))
        {
            if (!helpParameter.getValue().await())
            {
                actionToRun = defaultAction;
            }
        }
        else
        {
            actionToRun = this.getAction(actionName)
                .catchError(NotFoundException.class)
                .await();
            if (actionToRun == null)
            {
                actionToRun = defaultAction;

                if (actionToRun == null)
                {
                    final CharacterWriteStream output = process.getOutputWriteStream();
                    output.writeLine("Unrecognized action: " + Strings.escapeAndQuote(actionName)).await();
                    output.writeLine().await();
                }
            }
        }

        if (actionToRun != null)
        {
            actionToRun.run(process);
        }
        else
        {
            final IndentedCharacterWriteStream output = IndentedCharacterWriteStream.create(process.getOutputWriteStream());

            helpParameter.writeApplicationHelpLines(output).await();

            if (this.actions.any())
            {
                output.writeLine().await();
                output.writeLine("Actions:").await();
                output.indent(() ->
                {
                    final CharacterTable actionsTable = CharacterTable.create();
                    final Iterable<CommandLineAction> orderedActions = this.actions
                        .order((CommandLineAction lhs, CommandLineAction rhs) -> Strings.lessThan(lhs.getName(), rhs.getName()));
                    for (final CommandLineAction action : orderedActions)
                    {
                        String actionNameCellText = action.getName();
                        if (action.isDefaultAction())
                        {
                            actionNameCellText += " (default)";
                        }
                        actionNameCellText += ':';

                        String actionDescriptionCellText = action.getDescription();
                        if (Strings.isNullOrEmpty(actionDescriptionCellText))
                        {
                            actionDescriptionCellText = "(No description provided)";
                        }

                        actionsTable.addRow(actionNameCellText, actionDescriptionCellText);
                    }
                    actionsTable.toString(output, CharacterTableFormat.consise).await();
                });
                output.writeLine().await();
            }

            process.setExitCode(-1);
        }
    }
}
