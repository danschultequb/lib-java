package qub;

/**
 * A real DefaultApplicationLauncher that requests that the operating system open files with their
 * registered default application.
 */
public class RealDefaultApplicationLauncher implements DefaultApplicationLauncher
{
    private RealDefaultApplicationLauncher()
    {
    }

    public static RealDefaultApplicationLauncher create()
    {
        return new RealDefaultApplicationLauncher();
    }

    @Override
    public Result<Void> openWithDefaultApplication(Path pathToOpen)
    {
        PreCondition.assertNotNull(pathToOpen, "pathToOpen");

        return this.openWithDefaultApplication(pathToOpen, pathToOpen.endsWith('/') || pathToOpen.endsWith('\\'));
    }

    @Override
    public Result<Void> openFileWithDefaultApplication(File fileToOpen)
    {
        PreCondition.assertNotNull(fileToOpen, "fileToOpen");

        return this.openWithDefaultApplication(fileToOpen.getPath(), false);
    }

    @Override
    public Result<Void> openFolderWithDefaultApplication(Folder folderToOpen)
    {
        PreCondition.assertNotNull(folderToOpen, "folderToOpen");

        return this.openWithDefaultApplication(folderToOpen.getPath(), true);
    }

    private Result<Void> openWithDefaultApplication(Path pathToOpen, boolean isFolderPath)
    {
        PreCondition.assertNotNull(pathToOpen, "pathToOpen");
        PreCondition.assertTrue(pathToOpen.isRooted(), "pathToOpen.isRooted()");

        return Result.create(() ->
        {
            final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            final java.io.File fileToOpen = new java.io.File(pathToOpen.toString());
            try
            {
                desktop.open(fileToOpen);
            }
            catch (java.lang.IllegalArgumentException e)
            {
                final String errorMessage = e.getMessage();
                if (Strings.startsWith(errorMessage, "The file:", CharacterComparer.CaseInsensitive) &&
                    Strings.endsWith(errorMessage, "doesn't exist."))
                {
                    if (isFolderPath)
                    {
                        throw new FolderNotFoundException(pathToOpen);
                    }
                    else
                    {
                        throw new FileNotFoundException(pathToOpen);
                    }
                }
                else
                {
                    throw Exceptions.asRuntime(e);
                }
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
        });
    }
}
