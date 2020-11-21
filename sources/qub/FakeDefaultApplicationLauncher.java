package qub;

/**
 * A fake DefaultApplicationLauncher implementation that does nothing.
 */
public class FakeDefaultApplicationLauncher implements DefaultApplicationLauncher
{
    private FakeDefaultApplicationLauncher()
    {
    }

    public static FakeDefaultApplicationLauncher create()
    {
        return new FakeDefaultApplicationLauncher();
    }

    @Override
    public Result<Void> openFileWithDefaultApplication(Path filePathToOpen)
    {
        PreCondition.assertNotNull(filePathToOpen, "filePathToOpen");

        return Result.success();
    }
}
