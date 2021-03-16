package qub;

public interface FakeDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, (DesktopProcess process) ->
            {
                return FakeDefaultApplicationLauncher.create(process.getFileSystem());
            });

            runner.testGroup("openFileWithDefaultApplication(String)", () ->
            {
                runner.test("with file that exists", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder currentFolder = process.getCurrentFolder();
                        try (final TemporaryFile file = TemporaryFile.get(currentFolder.createFile("fake-file.txt").await()))
                        {
                            final FakeDefaultApplicationLauncher launcher = FakeDefaultApplicationLauncher.create(file.getFileSystem());
                            test.assertNull(launcher.openFileWithDefaultApplication(file).await());
                            test.assertEqual(
                                Iterable.create(
                                    file.getPath()),
                                launcher.getPathsOpened());
                        }
                    }
                });
            });

            runner.testGroup("openFolderWithDefaultApplication(String)", () ->
            {
                runner.test("with file that exists", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder currentFolder = process.getCurrentFolder();
                        try (final TemporaryFolder folder = TemporaryFolder.get(currentFolder.createFolder("fake-folder").await()))
                        {
                            final FakeDefaultApplicationLauncher launcher = FakeDefaultApplicationLauncher.create(folder.getFileSystem());
                            test.assertNull(launcher.openFolderWithDefaultApplication(folder).await());
                            test.assertEqual(
                                Iterable.create(
                                    folder.getPath()),
                                launcher.getPathsOpened());
                        }
                    }
                });
            });
        });
    }
}
