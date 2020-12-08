package qub;

/**
 * A class that represents a collection of environment variables.
 */
public class MutableEnvironmentVariables implements EnvironmentVariables
{
    private final MutableMap<String,String> variables;

    /**
     * Create a new EnvironmentVariables object.
     */
    private MutableEnvironmentVariables()
    {
        this.variables = ListMap.create(String::equalsIgnoreCase);
    }

    public static MutableEnvironmentVariables create()
    {
        return new MutableEnvironmentVariables();
    }

    /**
     * Set an environment variable with the provided name and value.
     * @param variableName The name of the environment variable.
     * @param variableValue The value of the environment variable.
     * @return This object for method chaining.
     */
    public MutableEnvironmentVariables set(String variableName, String variableValue)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");
        PreCondition.assertNotNull(variableValue, "variableValue");

        this.variables.set(variableName, variableValue);

        return this;
    }

    @Override
    public Result<String> get(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.variables.get(variableName)
            .convertError(NotFoundException.class, () -> new NotFoundException("No environment variable named " + Strings.escapeAndQuote(variableName) + " found."));
    }

    public JSONObject toJson()
    {
        final JSONObject result = JSONObject.create();
        for (final MapEntry<String,String> entry : this.variables)
        {
            result.setString(entry.getKey(), entry.getValue());
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return this.toJson().toString();
    }
}
