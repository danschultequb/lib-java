package qub;

public interface RealDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RealDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, (Test test) -> RealDefaultApplicationLauncher.create());

            runner.testGroup("openFileWithDefaultApplication(File)", () ->
            {
                runner.test("with file that doesn't have a registered default application", runner.skip("opens default application dialog launcher"), (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final File file = currentFolder.getFile("fake-file.iml").await();
                    test.assertFalse(file.exists().await());
                    try
                    {
                        file.create().await();

                        final RealDefaultApplicationLauncher launcher = RealDefaultApplicationLauncher.create();
                        test.assertNull(launcher.openFileWithDefaultApplication(file).await());
                    }
                    finally
                    {
                        file.delete().catchError().await();
                    }
                });

                runner.test("with file that has a registered default application", runner.skip("opens default application"), (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final File file = currentFolder.getFile("fake-file.txt").await();
                    test.assertFalse(file.exists().await());
                    try
                    {
                        file.create().await();

                        final RealDefaultApplicationLauncher launcher = RealDefaultApplicationLauncher.create();
                        test.assertNull(launcher.openFileWithDefaultApplication(file).await());
                    }
                    finally
                    {
                        file.delete().catchError().await();
                    }
                });
            });
        });
    }
}
