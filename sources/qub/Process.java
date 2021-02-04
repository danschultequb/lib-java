package qub;

/**
 * A Process object that exposes the platform functionality that a Java application can use.
 */
public interface Process extends Disposable
{
    /**
     * Get the AsyncRunner that runs tasks on the main thread.
     * @return The AsyncRunner that runs tasks on the main thread.
     */
    AsyncScheduler getMainAsyncRunner();

    /**
     * Get the AsyncRunner that runs tasks on background threads.
     * @return The AsyncRunner that runs tasks on background threads.
     */
    AsyncScheduler getParallelAsyncRunner();

    /**
     * Get the output stream that is assigned to this Process.
     * @return The output stream that is assigned to this Process.
     */
    CharacterToByteWriteStream getOutputWriteStream();

    /**
     * Get the error stream that is assigned to this Process.
     * @return The error stream that is assigned to this Process.
     */
    CharacterToByteWriteStream getErrorWriteStream();

    /**
     * Get the CharacterToByteReadStream that is assigned to this Process.
     * @return The CharacterToByteReadStream that is assigned to this Process.
     */
    CharacterToByteReadStream getInputReadStream();

    /**
     * Get the Random number generator assigned to this Process.
     * @return The Random number generator assigned to this Process.
     */
    Random getRandom();

    /**
     * Get the FileSystem assigned to this Process.
     * @return The FileSystem assigned to this Process.
     */
    FileSystem getFileSystem();

    /**
     * Get the Network object assigned to this Process.
     * @return The Network object assigned to this Process.
     */
    Network getNetwork();

    /**
     * Get the String representation of the folder path that this process is running under.
     * @return The String representation of the folder path that this process is running under.
     */
    default String getCurrentFolderPathString()
    {
        final String result = this.getCurrentFolderPath().toString();

        PostCondition.assertNotNullAndNotEmpty(result, "result");
        PostCondition.assertTrue(result.endsWith("/"), "result.endsWith(\"/\")");

        return result;
    }

    /**
     * Get the path to the folder that this Process is currently running in.
     * @return The path to the folder that this Process is currently running in.
     */
    default Path getCurrentFolderPath()
    {
        return this.getCurrentFolder().getPath();
    }

    /**
     * Get the folder that this Process is currently running in.
     * @return The folder that this Process is currently running in.
     */
    Folder getCurrentFolder();

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    EnvironmentVariables getEnvironmentVariables();

    /**
     * Get the value of the provided environment variable.
     * @param variableName The name of the environment variable.
     */
    default Result<String> getEnvironmentVariable(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.getEnvironmentVariables().get(variableName);
    }

    /**
     * Get the Synchronization factory for creating synchronization objects.
     * @return The Synchronization factory for creating synchronization objects.
     */
    Synchronization getSynchronization();

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    Clock getClock();

    /**
     * Get the displays that have been assigned to this Process.
     * @return The displays that have been assigned to this Process.
     */
    Iterable<Display> getDisplays();

    /**
     * Get the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @return The DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     */
    DefaultApplicationLauncher getDefaultApplicationLauncher();

    /**
     * Get the system properties of this process.
     * @return The system properties of this process.
     */
    Map<String,String> getSystemProperties();

    /**
     * Get the System property with the provided name.
     * @param systemPropertyName The name of the System property to get.
     * @return The value of the System property.
     */
    default Result<String> getSystemProperty(String systemPropertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(systemPropertyName, "systemPropertyName");

        return this.getSystemProperties().get(systemPropertyName)
            .convertError(NotFoundException.class, () -> new NotFoundException("No system property found with the name " + Strings.escapeAndQuote(systemPropertyName) + "."));
    }

    /**
     * Get the TypeLoader object that can be used to load types and also determine where types were loaded from.
     * @return The TypeLoader object that can be used to load types and also determine where types were loaded from.
     */
    TypeLoader getTypeLoader();
}
