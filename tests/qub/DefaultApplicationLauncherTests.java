package qub;

public interface DefaultApplicationLauncherTests
{
    static void test(TestRunner runner, Function1<DesktopProcess,? extends DefaultApplicationLauncher> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(DefaultApplicationLauncher.class, () ->
        {
            runner.testGroup("openWithDefaultApplication(String)", () ->
            {
                final Action2<String,Throwable> openWithDefaultApplicationErrorTest = (String pathToOpen, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathToOpen), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final DefaultApplicationLauncher launcher = creator.run(process);
                            test.assertThrows(() -> launcher.openWithDefaultApplication(pathToOpen).await(),
                                expected);
                        }
                    });
                };

                openWithDefaultApplicationErrorTest.run(null, new PreConditionFailure("pathToOpen cannot be null."));
                openWithDefaultApplicationErrorTest.run("", new PreConditionFailure("pathToOpen cannot be empty."));
                openWithDefaultApplicationErrorTest.run("hello", new PreConditionFailure("pathToOpen.isRooted() cannot be false."));
                openWithDefaultApplicationErrorTest.run("/fake/file.txt", new FileNotFoundException("/fake/file.txt"));
                openWithDefaultApplicationErrorTest.run("/fake/folder", new FileNotFoundException("/fake/folder"));
                openWithDefaultApplicationErrorTest.run("\\fake\\folder", new FileNotFoundException("\\fake\\folder"));
                openWithDefaultApplicationErrorTest.run("/fake/folder/", new FolderNotFoundException("/fake/folder/"));
                openWithDefaultApplicationErrorTest.run("\\fake\\folder\\", new FolderNotFoundException("\\fake\\folder\\"));
            });

            runner.testGroup("openWithDefaultApplication(Path)", () ->
            {
                final Action2<Path,Throwable> openWithDefaultApplicationErrorTest = (Path pathToOpen, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathToOpen), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final DefaultApplicationLauncher launcher = creator.run(process);
                            test.assertThrows(() -> launcher.openWithDefaultApplication(pathToOpen).await(),
                                expected);
                        }
                    });
                };

                openWithDefaultApplicationErrorTest.run(null, new PreConditionFailure("pathToOpen cannot be null."));
                openWithDefaultApplicationErrorTest.run(Path.parse("hello"), new PreConditionFailure("pathToOpen.isRooted() cannot be false."));
                openWithDefaultApplicationErrorTest.run(Path.parse("/fake/file.txt"), new FileNotFoundException("/fake/file.txt"));
                openWithDefaultApplicationErrorTest.run(Path.parse("/fake/folder"), new FileNotFoundException("/fake/folder"));
                openWithDefaultApplicationErrorTest.run(Path.parse("\\fake\\folder"), new FileNotFoundException("\\fake\\folder"));
                openWithDefaultApplicationErrorTest.run(Path.parse("/fake/folder/"), new FolderNotFoundException("/fake/folder/"));
                openWithDefaultApplicationErrorTest.run(Path.parse("\\fake\\folder\\"), new FolderNotFoundException("\\fake\\folder\\"));
            });

            runner.testGroup("openFileWithDefaultApplication(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final DefaultApplicationLauncher launcher = creator.run(process);
                        test.assertThrows(() -> launcher.openFileWithDefaultApplication((File)null),
                            new PreConditionFailure("fileToOpen cannot be null."));
                    }
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder currentFolder = process.getCurrentFolder();
                        final File file = currentFolder.getFile("fake-file.txt").await();
                        test.assertFalse(file.exists().await());

                        final DefaultApplicationLauncher launcher = creator.run(process);
                        test.assertThrows(() -> launcher.openFileWithDefaultApplication(file).await(),
                            new FileNotFoundException(file));

                        test.assertFalse(file.exists().await());
                    }
                });
            });

            runner.testGroup("openFolderWithDefaultApplication(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final DefaultApplicationLauncher launcher = creator.run(process);
                        test.assertThrows(() -> launcher.openFolderWithDefaultApplication((Folder)null),
                            new PreConditionFailure("folderToOpen cannot be null."));
                    }
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder currentFolder = process.getCurrentFolder();
                        final Folder folder = currentFolder.getFolder("fake-folder").await();
                        test.assertFalse(folder.exists().await());

                        final DefaultApplicationLauncher launcher = creator.run(process);
                        test.assertThrows(() -> launcher.openFolderWithDefaultApplication(folder).await(),
                            new FolderNotFoundException(folder));

                        test.assertFalse(folder.exists().await());
                    }
                });
            });
        });
    }
}
