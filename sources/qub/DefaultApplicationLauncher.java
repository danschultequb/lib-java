package qub;

/**
 * An object that opens files with their registered default application.
 */
public interface DefaultApplicationLauncher
{
    /**
     * Open the provided path with the registered default application.
     * @param pathToOpen The path to open.
     * @return The result of opening the file path.
     */
    default Result<Void> openWithDefaultApplication(String pathToOpen)
    {
        PreCondition.assertNotNullAndNotEmpty(pathToOpen, "pathToOpen");

        return this.openWithDefaultApplication(Path.parse(pathToOpen));
    }

    /**
     * Open the provided path with the registered default application.
     * @param pathToOpen The path to open.
     * @return The result of opening the file path.
     */
    Result<Void> openWithDefaultApplication(Path pathToOpen);

    /**
     * Open the provided file with the registered default application.
     * @param fileToOpen The file to open.
     * @return The result of opening the file.
     */
    Result<Void> openFileWithDefaultApplication(File fileToOpen);

    /**
     * Open the provided folder with the registered default application.
     * @param folderToOpen The folder to open.
     * @return The result of opening the folder.
     */
    Result<Void> openFolderWithDefaultApplication(Folder folderToOpen);
}
