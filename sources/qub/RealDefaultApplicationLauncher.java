package qub;

/**
 * A real DefaultApplicationLauncher that requests that the operating system open files with their
 * registered default application.
 */
public class RealDefaultApplicationLauncher implements DefaultApplicationLauncher
{
    @Override
    public Result<Void> openFileWithDefaultApplication(Path filePathToOpen)
    {
        PreCondition.assertNotNull(filePathToOpen, "filePathToOpen");

        return Result.create(() ->
        {
            try
            {
                java.awt.Desktop.getDesktop().open(new java.io.File(filePathToOpen.toString()));
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
        });
    }
}
