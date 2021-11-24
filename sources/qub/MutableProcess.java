package qub;

/**
 * A {@link Process} that can have its properties changed.
 */
public interface MutableProcess extends Process
{
    /**
     * Set the output stream that is assigned to this {@link MutableProcess}.
     * @param outputWriteStream The output stream that is assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setOutputWriteStream(CharacterToByteWriteStream outputWriteStream)
    {
        return this.setOutputWriteStream(() -> outputWriteStream);
    }

    /**
     * Set the output stream that is assigned to this {@link MutableProcess}.
     * @param outputWriteStream The output stream that is assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setOutputWriteStream(Function0<CharacterToByteWriteStream> outputWriteStream);

    /**
     * Set the error stream that is assigned to this {@link MutableProcess}.
     * @param errorWriteStream The error stream that is assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setErrorWriteStream(CharacterToByteWriteStream errorWriteStream)
    {
        return this.setErrorWriteStream(() -> errorWriteStream);
    }

    /**
     * Set the error stream that is assigned to this {@link MutableProcess}.
     * @param errorWriteStream The error stream that is assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setErrorWriteStream(Function0<CharacterToByteWriteStream> errorWriteStream);

    /**
     * Set the input stream that is assigned to this {@link MutableProcess}.
     * @param inputReadStream The input stream that is assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setInputReadStream(CharacterToByteReadStream inputReadStream)
    {
        return this.setInputReadStream(() -> inputReadStream);
    }

    /**
     * Set the input stream that is assigned to this {@link MutableProcess}.
     * @param inputReadStream The input stream that is assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setInputReadStream(Function0<CharacterToByteReadStream> inputReadStream);

    /**
     * Set the random number generator assigned to this {@link MutableProcess}.
     * @param random The random number generator assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setRandom(Random random)
    {
        return this.setRandom(() -> random);
    }

    /**
     * Set the random number generator assigned to this {@link MutableProcess}.
     * @param random The random number generator assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setRandom(Function0<Random> random);

    /**
     * Set the file system assigned to this {@link MutableProcess}.
     * @param fileSystem The file system assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setFileSystem(FileSystem fileSystem)
    {
        return this.setFileSystem(() -> fileSystem);
    }

    /**
     * Set the file system assigned to this {@link MutableProcess}.
     * @param fileSystem The file system assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setFileSystem(Function0<FileSystem> fileSystem);

    /**
     * Set the {@link Network} assigned to this {@link MutableProcess}.
     * @param network The {@link Network} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setNetwork(Network network)
    {
        return this.setNetwork(() -> network);
    }

    /**
     * Set the {@link Network} assigned to this {@link MutableProcess}.
     * @param network The {@link Network} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setNetwork(Function0<Network> network);

    /**
     * Set the current folder path assigned to this {@link MutableProcess}.
     * @param currentFolderPath The current folder path assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setCurrentFolderPath(String currentFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(currentFolderPath, "currentFolderPath");

        return this.setCurrentFolderPath(Path.parse(currentFolderPath));
    }

    /**
     * Set the current folder path assigned to this {@link MutableProcess}.
     * @param currentFolderPath The current folder path assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setCurrentFolderPath(Path currentFolderPath)
    {
        PreCondition.assertNotNull(currentFolderPath, "currentFolderPath");
        PreCondition.assertTrue(currentFolderPath.isRooted(), "currentFolderPath.isRooted()");

        return this.setCurrentFolderPath(() -> currentFolderPath);
    }

    /**
     * Set the current folder path assigned to this {@link MutableProcess}.
     * @param currentFolderPath The current folder path assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setCurrentFolderPath(Function0<Path> currentFolderPath);

    /**
     * Set the {@link EnvironmentVariables} assigned to this {@link MutableProcess}.
     * @param environmentVariables The {@link EnvironmentVariables} assigned to this
     * {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        return this.setEnvironmentVariables(() -> environmentVariables);
    }

    /**
     * Set the {@link EnvironmentVariables} assigned to this {@link MutableProcess}.
     * @param environmentVariables The {@link EnvironmentVariables} assigned to this
     * {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setEnvironmentVariables(Function0<EnvironmentVariables> environmentVariables);

    /**
     * Set the {@link Synchronization} assigned to this {@link MutableProcess}.
     * @param synchronization The {@link Synchronization} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setSynchronization(Synchronization synchronization)
    {
        return this.setSynchronization(() -> synchronization);
    }

    /**
     * Set the {@link Synchronization} assigned to this {@link MutableProcess}.
     * @param synchronization The {@link Synchronization} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setSynchronization(Function0<Synchronization> synchronization);

    /**
     * Set the {@link Clock} assigned to this {@link MutableProcess}.
     * @param clock The {@link Clock} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setClock(Clock clock)
    {
        return this.setClock(() -> clock);
    }

    /**
     * Set the {@link Clock} assigned to this {@link MutableProcess}.
     * @param clock The {@link Clock} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setClock(Function0<Clock> clock);

    /**
     * Set the displays assigned to this {@link MutableProcess}.
     * @param displays The displays assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setDisplays(Iterable<Display> displays)
    {
        return this.setDisplays(() -> displays);
    }

    /**
     * Set the displays assigned to this {@link MutableProcess}.
     * @param displays The displays assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setDisplays(Function0<Iterable<Display>> displays);

    /**
     * Set the {@link DefaultApplicationLauncher} assigned to this {@link MutableProcess}.
     * @param defaultApplicationLauncher The {@link DefaultApplicationLauncher} assigned to this
     * {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        return this.setDefaultApplicationLauncher(() -> defaultApplicationLauncher);
    }

    /**
     * Set the {@link DefaultApplicationLauncher} assigned to this {@link MutableProcess}.
     * @param defaultApplicationLauncher The {@link DefaultApplicationLauncher} assigned to this
     * {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setDefaultApplicationLauncher(Function0<DefaultApplicationLauncher> defaultApplicationLauncher);

    /**
     * Set the system properties assigned to this {@link MutableProcess}.
     * @param systemProperties The system properties assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setSystemProperties(Map<String,String> systemProperties)
    {
        return this.setSystemProperties(() -> systemProperties);
    }

    /**
     * Set the system properties assigned to this {@link MutableProcess}.
     * @param systemProperties The system properties assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setSystemProperties(Function0<Map<String,String>> systemProperties);

    /**
     * Set the {@link TypeLoader} assigned to this {@link MutableProcess}.
     * @param typeLoader The {@link TypeLoader} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    default MutableProcess setTypeLoader(TypeLoader typeLoader)
    {
        return this.setTypeLoader(() -> typeLoader);
    }

    /**
     * Set the {@link TypeLoader} assigned to this {@link MutableProcess}.
     * @param typeLoader The {@link TypeLoader} assigned to this {@link MutableProcess}.
     * @return This object for method chaining.
     */
    MutableProcess setTypeLoader(Function0<TypeLoader> typeLoader);
}
