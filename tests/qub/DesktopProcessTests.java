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

            runner.testGroup("getProcessBuilder(String)", () ->
            {
                runner.test("with null string", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        test.assertThrows(() -> process.getProcessBuilder((String)null),
                            new PreConditionFailure("executablePath cannot be null."));
                    }
                });

                runner.test("with empty string", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        test.assertThrows(() -> process.getProcessBuilder(""),
                            new PreConditionFailure("executablePath cannot be empty."));
                    }
                });

                runner.test("with path with file extension relative to the current folder", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("project.json").await();
                        test.assertNotNull(builder);
                        test.assertEqual(Path.parse("project.json"), builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with path without file extension relative to the current folder", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("project").await();
                        test.assertNotNull(builder);
                        test.assertEqual(Path.parse("project"), builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with rooted path with file extension", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().getFile("project.json").await().getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath).await();
                        test.assertNotNull(builder);
                        test.assertEqual(executablePath, builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with rooted path without file extension", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().getFile("project").await().getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath).await();
                        test.assertNotNull(builder);
                        test.assertEqual(executablePath, builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with rooted path to executable", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe").await();
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                        }
                    }
                });

                runner.test("with path with file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac.exe").await();
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac").await();
                            test.assertTrue(builder.getExecutablePath().getSegments().last().startsWith("javac"));
                            test.assertEqual(Iterable.create(), builder.getArguments());

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            builder.redirectOutput(output);

                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            builder.redirectError(error);

                            test.assertEqual(2, builder.run().await());

                            final String outputString = output.getText().await();
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertTrue(outputString.contains("javac <options> <source files>"), "Process output (" + outputString + ") should have contained \"javac <options> <source files>\".");
                            test.assertTrue(outputString.contains("Terminate compilation if warnings occur"), "Process output (" + outputString + ") should have contained \"Terminate compilation if warnings occur\".");

                            test.assertEqual("", error.getText().await());
                        }
                    }
                });

                runner.test("with non-existing working folder", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").await();

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        builder.redirectOutput(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        builder.redirectError(error);

                        final Folder workingFolder = process.getCurrentFolder().getFolder("I don't exist").await();
                        builder.setWorkingFolder(workingFolder);
                        test.assertThrows(() -> builder.run().await(),
                            new FolderNotFoundException(workingFolder));
                        test.assertEqual("", output.getText().await());
                        test.assertEqual("", error.getText().await());
                    }
                });

                runner.test("with existing working folder (a child folder of the current folder)", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").await();

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        builder.redirectOutput(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        builder.redirectError(error);

                        final Folder currentFolder = process.getCurrentFolder();
                        final Folder workingFolder = currentFolder.createFolder("temp2").await();
                        try
                        {
                            builder.setWorkingFolder(workingFolder);
                            test.assertEqual(2, builder.run().await());

                            final String outputString = output.getText().await();
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertContains(outputString, "javac <options> <source files>");
                            test.assertContains(outputString, "Terminate compilation if warnings occur");

                            final String errorString = error.getText().await();
                            test.assertEqual("", errorString);
                        }
                        finally
                        {
                            workingFolder.delete().await();
                        }
                    }
                });

                runner.test("with existing working folder (the current folder)", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").await();

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        builder.redirectOutput(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        builder.redirectError(error);

                        final Folder workingFolder = process.getCurrentFolder();
                        builder.setWorkingFolder(workingFolder);
                        test.assertEqual(2, builder.run().await());

                        test.assertLinesEqual(
                            Iterable.create(
                                "Usage: javac <options> <source files>",
                                "where possible options include:",
                                "  @<filename>                  Read options and filenames from file",
                                "  -Akey[=value]                Options to pass to annotation processors",
                                "  --add-modules <module>(,<module>)*",
                                "        Root modules to resolve in addition to the initial modules, or all modules",
                                "        on the module path if <module> is ALL-MODULE-PATH.",
                                "  --boot-class-path <path>, -bootclasspath <path>",
                                "        Override location of bootstrap class files",
                                "  --class-path <path>, -classpath <path>, -cp <path>",
                                "        Specify where to find user class files and annotation processors",
                                "  -d <directory>               Specify where to place generated class files",
                                "  -deprecation",
                                "        Output source locations where deprecated APIs are used",
                                "  --enable-preview",
                                "        Enable preview language features. To be used in conjunction with either -source or --release.",
                                "  -encoding <encoding>         Specify character encoding used by source files",
                                "  -endorseddirs <dirs>         Override location of endorsed standards path",
                                "  -extdirs <dirs>              Override location of installed extensions",
                                "  -g                           Generate all debugging info",
                                "  -g:{lines,vars,source}       Generate only some debugging info",
                                "  -g:none                      Generate no debugging info",
                                "  -h <directory>",
                                "        Specify where to place generated native header files",
                                "  --help, -help, -?            Print this help message",
                                "  --help-extra, -X             Print help on extra options",
                                "  -implicit:{none,class}",
                                "        Specify whether or not to generate class files for implicitly referenced files",
                                "  -J<flag>                     Pass <flag> directly to the runtime system",
                                "  --limit-modules <module>(,<module>)*",
                                "        Limit the universe of observable modules",
                                "  --module <module>(,<module>)*, -m <module>(,<module>)*",
                                "        Compile only the specified module(s), check timestamps",
                                "  --module-path <path>, -p <path>",
                                "        Specify where to find application modules",
                                "  --module-source-path <module-source-path>",
                                "        Specify where to find input source files for multiple modules",
                                "  --module-version <version>",
                                "        Specify version of modules that are being compiled",
                                "  -nowarn                      Generate no warnings",
                                "  -parameters",
                                "        Generate metadata for reflection on method parameters",
                                "  -proc:{none,only}",
                                "        Control whether annotation processing and/or compilation is done.",
                                "  -processor <class1>[,<class2>,<class3>...]",
                                "        Names of the annotation processors to run; bypasses default discovery process",
                                "  --processor-module-path <path>",
                                "        Specify a module path where to find annotation processors",
                                "  --processor-path <path>, -processorpath <path>",
                                "        Specify where to find annotation processors",
                                "  -profile <profile>",
                                "        Check that API used is available in the specified profile",
                                "  --release <release>",
                                "        Compile for the specified Java SE release. Supported releases: 7, 8, 9, 10, 11, 12, 13, 14, 15",
                                "  -s <directory>               Specify where to place generated source files",
                                "  --source <release>, -source <release>",
                                "        Provide source compatibility with the specified Java SE release. Supported releases: 7, 8, 9, 10, 11, 12, 13, 14, 15",
                                "  --source-path <path>, -sourcepath <path>",
                                "        Specify where to find input source files",
                                "  --system <jdk>|none          Override location of system modules",
                                "  --target <release>, -target <release>",
                                "        Generate class files suitable for the specified Java SE release. Supported releases: 7, 8, 9, 10, 11, 12, 13, 14, 15",
                                "  --upgrade-module-path <path>",
                                "        Override location of upgradeable modules",
                                "  -verbose                     Output messages about what the compiler is doing",
                                "  --version, -version          Version information",
                                "  -Werror                      Terminate compilation if warnings occur",
                                ""),
                            output);
                        test.assertLinesEqual(
                            Iterable.create(),
                            error);
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable with redirected error", (Test test) ->
                {
                    try (final DesktopProcess process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac").await();
                            builder.addArgument("notfound.java");

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            builder.redirectOutput(output);

                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            builder.redirectError(error);

                            test.assertEqual(2, builder.run().await());

                            test.assertLinesEqual(
                                Iterable.create(),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(
                                    "error: file not found: notfound.java",
                                    "Usage: javac <options> <source files>",
                                    "use --help for a list of possible options"),
                                error);
                        }
                    }
                });
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
