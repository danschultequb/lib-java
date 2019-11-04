package qub;

/**
 * An object that opens files with their registered default application.
 */
public interface DefaultApplicationLauncher
{
    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    default Result<Void> openFileWithDefaultApplication(String filePathToOpen)
    {
        PreCondition.assertNotNullAndNotEmpty(filePathToOpen, "filePathToOpen");

        return this.openFileWithDefaultApplication(Path.parse(filePathToOpen));
    }

    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    Result<Void> openFileWithDefaultApplication(Path filePathToOpen);

    /**
     * Open the provided file with the registered default application.
     * @param fileToOpen The file to open.
     * @return The result of opening the file.
     */
    default Result<Void> openFileWithDefaultApplication(File fileToOpen)
    {
        PreCondition.assertNotNull(fileToOpen, "fileToOpen");

        return this.openFileWithDefaultApplication(fileToOpen.getPath());
    }
}
