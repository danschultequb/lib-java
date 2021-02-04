package qub;

public interface FakeDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, (Test test) ->
            {
                return FakeDefaultApplicationLauncher.create(test.getFileSystem());
            });

            runner.testGroup("openFileWithDefaultApplication(String)", () ->
            {
                runner.test("with file that exists", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final File file = currentFolder.getFile("fake-file.txt").await();
                    test.assertFalse(file.exists().await());
                    try
                    {
                        file.create().await();

                        final FakeDefaultApplicationLauncher launcher = FakeDefaultApplicationLauncher.create(file.getFileSystem());
                        test.assertNull(launcher.openFileWithDefaultApplication(file).await());
                        test.assertEqual(
                            Iterable.create(
                                file.getPath()),
                            launcher.getPathsOpened());
                    }
                    finally
                    {
                        file.delete().await();
                    }
                });
            });

            runner.testGroup("openFolderWithDefaultApplication(String)", () ->
            {
                runner.test("with file that exists", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final Folder folder = currentFolder.getFolder("fake-folder").await();
                    test.assertFalse(folder.exists().await());
                    try
                    {
                        folder.create().await();

                        final FakeDefaultApplicationLauncher launcher = FakeDefaultApplicationLauncher.create(folder.getFileSystem());
                        test.assertNull(launcher.openFolderWithDefaultApplication(folder).await());
                        test.assertEqual(
                            Iterable.create(
                                folder.getPath()),
                            launcher.getPathsOpened());
                    }
                    finally
                    {
                        folder.delete().await();
                    }
                });
            });
        });
    }
}
