package qub;

/**
 * A class that represents a collection of environment variables.
 */
public interface EnvironmentVariables
{
    static MutableEnvironmentVariables create()
    {
        return MutableEnvironmentVariables.create();
    }

    /**
     * Get the value of the environment variable with the provided name.
     * @param variableName The name of the environment variable.
     */
    Result<String> get(String variableName);

    /**
     * Resolve all of the environment variable references in the provided String.
     * @param value The String to resolve all of the environment variable references in.
     * @return The resolved String.
     */
    default String resolve(String value)
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
}
