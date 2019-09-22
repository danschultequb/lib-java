package qub;

/**
 * A class that represents a collection of environment variables.
 */
public class EnvironmentVariables
{
    private ListMap<String,String> variables;

    /**
     * Create a new EnvironmentVariables object.
     */
    public EnvironmentVariables()
    {
        this.variables = ListMap.create(String::equalsIgnoreCase);
    }

    /**
     * Set an environment variable with the provided name and value.
     * @param variableName The name of the environment variable.
     * @param variableValue The value of the environment variable.
     * @return This object for method chaining.
     */
    public EnvironmentVariables set(String variableName, String variableValue)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");
        PreCondition.assertNotNull(variableValue, "variableValue");

        this.variables.set(variableName, variableValue);

        return this;
    }

    /**
     * Get the value of the environment variable with the provided name.
     * @param variableName The name of the environment variable.
     * @return
     */
    public Result<String> get(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.variables.get(variableName)
            .convertError(NotFoundException.class, () -> new NotFoundException("No environment variable named " + Strings.escapeAndQuote(variableName) + " found."));
    }
}
