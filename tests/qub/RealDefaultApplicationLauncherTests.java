package qub;

public interface RealDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RealDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, (DesktopProcess process) -> RealDefaultApplicationLauncher.create());

            runner.testGroup("openFileWithDefaultApplication(File)", () ->
            {
                runner.test("with file that doesn't have a registered default application",
                    runner.skip("opens default application dialog launcher"),
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFile("fake-file.iml", true)),
                    (Test test, File file) ->
                    {
                        final RealDefaultApplicationLauncher launcher = RealDefaultApplicationLauncher.create();
                        test.assertNull(launcher.openFileWithDefaultApplication(file).await());
                    });

                runner.test("with file that has a registered default application",
                    runner.skip("opens default application"),
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFile("fake-file.txt", true)),
                    (Test test, File file) ->
                    {
                        final RealDefaultApplicationLauncher launcher = RealDefaultApplicationLauncher.create();
                        test.assertNull(launcher.openFileWithDefaultApplication(file).await());
                    });
            });

            runner.testGroup("openFolderWithDefaultApplication(Folder)", () ->
            {
                runner.test("with non-existing folder",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(false)),
                    (Test test, Folder folder) ->
                {
                    final RealDefaultApplicationLauncher launcher = RealDefaultApplicationLauncher.create();
                    test.assertThrows(() -> launcher.openFolderWithDefaultApplication(folder).await(),
                        new FolderNotFoundException(folder));
                });

                runner.test("with existing folder",
                    runner.skip("opens default application"),
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder folder) ->
                {
                    final RealDefaultApplicationLauncher launcher = RealDefaultApplicationLauncher.create();
                    test.assertNull(launcher.openFolderWithDefaultApplication(folder).await());
                });
            });
        });
    }
}
