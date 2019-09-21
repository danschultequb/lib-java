package qub;

/**
 * A class that represents a collection of environment variables.
 */
public class EnvironmentVariables extends ListMap<String,String>
{
    /**
     * Create a new EnvironmentVariables object.
     */
    public EnvironmentVariables()
    {
        super(String::equalsIgnoreCase);
    }
}
