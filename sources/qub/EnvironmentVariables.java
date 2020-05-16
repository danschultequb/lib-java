package qub;

/**
 * A class that represents a collection of environment variables.
 */
public class EnvironmentVariables
{
    private final ListMap<String,String> variables;

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

    /**
     * Resolve all of the environment variable references in the provided String.
     * @param value The String to resolve all of the environment variable references in.
     * @return The resolved String.
     */
    public String resolve(String value)
    {
        PreCondition.assertNotNull(value, "value");

        final CharacterList resultList = CharacterList.create();
        final CharacterList environmentVariableReference = CharacterList.create();
        for (final char c : Strings.iterate(value))
        {
            if (!environmentVariableReference.any())
            {
                if (c != '%')
                {
                    resultList.add(c);
                }
                else
                {
                    environmentVariableReference.add(c);
                }
            }
            else
            {
                if (c != '%')
                {
                    environmentVariableReference.add(c);
                }
                else
                {
                    final String environmentVariableName = environmentVariableReference.toString(true).substring(1);
                    if (environmentVariableName.length() == 0)
                    {
                        resultList.addAll("%%");
                    }
                    else
                    {
                        final String environmentVariableValue = this.get(environmentVariableName).catchError(NotFoundException.class).await();
                        if (!Strings.isNullOrEmpty(environmentVariableValue))
                        {
                            resultList.addAll(environmentVariableValue);
                        }
                    }
                    environmentVariableReference.clear();
                }
            }
        }
        if (environmentVariableReference.any())
        {
            resultList.addAll(environmentVariableReference);
            environmentVariableReference.clear();
        }

        final String result = resultList.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        final CharacterList resultList = CharacterList.create();

        resultList.add('{');
        boolean firstEntry = true;
        for (final MapEntry<String,String> entry : this.variables)
        {
            if (firstEntry)
            {
                firstEntry = false;
            }
            else
            {
                resultList.add(',');
            }
            resultList.addAll(Strings.escapeAndQuote(entry.getKey()) + ":" + Strings.escapeAndQuote(entry.getValue()));
        }
        resultList.add('}');

        final String result = resultList.toString(true);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }
}
