package qub;

/**
 * A fake DefaultApplicationLauncher implementation that does nothing.
 */
public class FakeDefaultApplicationLauncher implements DefaultApplicationLauncher
{
    @Override
    public Result<Void> openFileWithDefaultApplication(Path filePathToOpen)
    {
        PreCondition.assertNotNull(filePathToOpen, "filePathToOpen");

        return Result.success();
    }
}
