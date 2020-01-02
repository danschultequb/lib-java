package qub;

/**
 * An action for a command line application.
 */
public class CommandLineAction
{
    private final String name;
    private final Action1<Process> mainAction;
    private final List<String> aliases;
    private String description;
    private CommandLineActions parentActions;

    /**
     * Create a new CommandLineAction with the provided name and action.
     * @param name The name of the CommandLineAction.
     * @param mainAction The behavior/action of the CommandLineAction.
     */
    public CommandLineAction(String name, Action1<Process> mainAction)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNull(mainAction, "mainAction");

        this.name = name;
        this.mainAction = mainAction;
        this.aliases = List.create();
    }

    /**
     * Get the name of this CommandLineAction.
     * @return The name of this CommandLineAction;
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get the aliases of this CommandLineAction.
     * @return The aliases of this CommandLineAction.
     */
    public Iterable<String> getAliases()
    {
        return this.aliases;
    }

    /**
     * Get whether or not the provided action name already exists. If this action has been added to
     * a CommandLineActions collection, then both this action and all of the other actions in the
     * collection will be searched.
     * @param actionName The name to check.
     * @return Whether or not the provided action name already exists.
     */
    public boolean containsActionName(String actionName)
    {
        return this.containsActionName(actionName, true);
    }

    /**
     * Get whether or not the provided action name already exists. If this action has been added to
     * a CommandLineActions collection and checkOtherActions is true, then both this action and all
     * of the other actions in the collection will be searched.
     * @param actionName The name to check.
     * @param checkOtherActions Whether or not to check other actions that are in the action
     *                          collection.
     * @return Whether or not the provided action name already exists.
     */
    public boolean containsActionName(String actionName, boolean checkOtherActions)
    {
        PreCondition.assertNotNullAndNotEmpty(actionName, "actionName");

        return checkOtherActions && this.parentActions != null
            ? this.parentActions.containsActionName(actionName)
            : this.name.equalsIgnoreCase(actionName) || this.aliases.contains(actionName, Comparer::equalIgnoreCase);
    }

    public CommandLineAction addAlias(String alias)
    {
        PreCondition.assertNotNullAndNotEmpty(alias, "alias");
        PreCondition.assertFalse(this.containsActionName(alias), "this.aliasAlreadyExists(alias)");

        this.aliases.add(alias);

        return this;
    }

    public CommandLineAction addAliases(String... aliases)
    {
        PreCondition.assertNotNullAndNotEmpty(aliases, "aliases");

        for (final String alias : aliases)
        {
            this.addAlias(alias);
        }

        return this;
    }

    public CommandLineAction addAliases(Iterable<String> aliases)
    {
        PreCondition.assertNotNullAndNotEmpty(aliases, "aliases");

        for (final String alias : aliases)
        {
            this.addAlias(alias);
        }

        return this;
    }

    public String getDescription()
    {
        return this.description;
    }

    public CommandLineAction setDescription(String description)
    {
        this.description = description;

        return this;
    }

    public CommandLineAction setParentActions(CommandLineActions parentActions)
    {
        this.parentActions = parentActions;

        return this;
    }

    public void run(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        this.mainAction.run(process);
    }
}
