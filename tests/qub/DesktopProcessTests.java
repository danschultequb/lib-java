package qub;

public interface DesktopProcessTests
{
    static void test(TestRunner runner, Function0<? extends DesktopProcess> creator)
    {
        runner.testGroup(DesktopProcess.class, () ->
        {
            ProcessTests.test(runner, (Test test) -> creator.run());

            runner.test("getExitCode()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    test.assertEqual(0, process.getExitCode());
                }
            });

            runner.testGroup("setExitCode(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        test.assertSame(process, process.setExitCode(-1));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        test.assertSame(process, process.setExitCode(0));
                        test.assertEqual(0, process.getExitCode());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        test.assertSame(process, process.setExitCode(2));
                        test.assertEqual(2, process.getExitCode());
                    }
                });
            });

            runner.test("incrementExitCode()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    test.assertEqual(0, process.getExitCode());
                    test.assertSame(process, process.incrementExitCode());
                    test.assertEqual(1, process.getExitCode());
                }
            });

            runner.test("getCommandLineArguments()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final CommandLineArguments arguments = process.getCommandLineArguments();
                    test.assertNotNull(arguments);
                    test.assertSame(arguments, process.getCommandLineArguments());
                }
            });

            runner.test("onWindows()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    test.assertNotNull(process.onWindows().await());
                }
            });

            runner.test("getJVMClasspath()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    test.assertNotNullAndNotEmpty(process.getJVMClasspath().await());
                }
            });

            runner.test("getJavaVersion()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final VersionNumber version = process.getJavaVersion();
                    test.assertNotNull(version);
                }
            });

            runner.test("getQubFolder()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final QubFolder folder = process.getQubFolder().await();
                    test.assertNotNull(folder);
                    test.assertTrue(folder.exists().await());
                }
            });

            runner.test("getQubProjectVersionFolder()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final QubProjectVersionFolder projectVersionFolder = process.getQubProjectVersionFolder().await();
                    test.assertNotNull(projectVersionFolder);
                    test.assertTrue(projectVersionFolder.exists().await());
                }
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final String publisherName = process.getPublisherName().await();
                    test.assertNotNullAndNotEmpty(publisherName);
                }
            });

            runner.test("getProjectName()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final String projectName = process.getProjectName().await();
                    test.assertNotNullAndNotEmpty(projectName);
                }
            });

            runner.test("getVersion()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final VersionNumber version = process.getVersion().await();
                    test.assertNotNull(version);
                }
            });

            runner.test("getProjectDataFolder()", (Test test) ->
            {
                try (final DesktopProcess process = creator.run())
                {
                    final Folder qubProjectDataFolder = process.getQubProjectDataFolder().await();
                    test.assertNotNull(qubProjectDataFolder);
                    final File testFile = qubProjectDataFolder.getFile("test.txt").await();
                    testFile.setContentsAsString("hello").await();
                    try
                    {
                        final Folder projectDataFolder = process.getQubProjectFolder().await().getProjectDataFolder().await();
                        test.assertEqual("hello", projectDataFolder.getFileContentsAsString("test.txt").await());
                    }
                    finally
                    {
                        testFile.delete().await();
                    }
                }
            });
        });
    }
}
