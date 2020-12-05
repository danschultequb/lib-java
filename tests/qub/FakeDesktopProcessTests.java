package qub;

public interface FakeDesktopProcessTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDesktopProcess.class, () ->
        {
            final Function1<Test,? extends FakeDesktopProcess> creator = (Test test) ->
            {
                final FakeDesktopProcess process = FakeDesktopProcess.create();

                process.setEnvironmentVariables(new EnvironmentVariables()
                    .set("path", "fake-path"));

                final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(process.getClock());
                fileSystem.createRoot("/").await();
                final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                final File compiledSourcesFile = qubFolder.getCompiledSourcesFile("fake", "main-java", "13").await();
                compiledSourcesFile.create().await();
                process.setFileSystem(fileSystem, "/");

                process.setProcessFactory(
                    FakeProcessFactory.create(process)
                        .add(FakeProcessRun.get("C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe")
                            .setFunction(2))
                        .add(FakeProcessRun.get("javac.exe")
                            .setFunction(2))
                        .add(FakeProcessRun.get("javac")
                            .setFunction((ByteWriteStream output) ->
                            {
                                CharacterToByteWriteStream.create(output).writeLines(
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
                                        ""))
                                    .await();
                                return 2;
                            }))
                        .add(FakeProcessRun.get("javac")
                            .addArgument("notfound.java")
                            .setFunction((ByteWriteStream output, ByteWriteStream error) ->
                            {
                                CharacterToByteWriteStream.create(error).writeLines(
                                    Iterable.create(
                                        "error: file not found: notfound.java",
                                        "Usage: javac <options> <source files>",
                                        "use --help for a list of possible options"))
                                    .await();
                                return 2;
                            })));

                process.setTypeLoader(FakeTypeLoader.create()
                    .addTypeContainer("fake.MainClassFullName", compiledSourcesFile.toString()));

                return process;
            };

            DesktopProcessTests.test(runner, creator);

            runner.testGroup("setInputReadStream(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        test.assertThrows(() -> process.setInputReadStream(null),
                            new PreConditionFailure("inputReadStream cannot be null."));
                        test.assertNotNull(process.getInputReadStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        process.setInputReadStream(ByteReadStream.create(CharacterEncoding.UTF_8.encodeCharacters("hello there my good friend\nHow are you?\r\nI'm alright.").await()));

                        final CharacterToByteReadStream readStream = process.getInputReadStream();
                        test.assertEqual(new byte[]{ 104, 101, 108, 108, 111 }, readStream.readBytes(5).await());

                        final byte[] byteBuffer = new byte[2];
                        test.assertEqual(2, readStream.readBytes(byteBuffer).await());
                        test.assertEqual(new byte[]{ 32, 116 }, byteBuffer);

                        test.assertEqual(1, readStream.readBytes(byteBuffer, 1, 1).await());
                        test.assertEqual(new byte[]{ 32, 104 }, byteBuffer);

                        test.assertEqual(new char[]{ 'e', 'r', 'e' }, readStream.readCharacters(3).await());

                        final char[] characterBuffer = new char[4];
                        test.assertEqual(4, readStream.readCharacters(characterBuffer).await());
                        test.assertEqual(new char[]{ ' ', 'm', 'y', ' ' }, characterBuffer);

                        test.assertEqual(2, readStream.readCharacters(characterBuffer, 0, 2).await());
                        test.assertEqual(new char[]{ 'g', 'o', 'y', ' ' }, characterBuffer);

                        test.assertEqual("od fr", readStream.readString(5).await());
                    }
                });
            });

            runner.test("setRandom()", (Test test) ->
            {
                try (final FakeDesktopProcess process = creator.run(test))
                {
                    test.assertThrows(() -> process.setRandom(null),
                        new PreConditionFailure("random cannot be null."));
                    test.assertNotNull(process.getRandom());

                    final FixedRandom random = new FixedRandom(1);
                    final FakeDesktopProcess setRandomResult = process.setRandom(random);
                    test.assertSame(process, setRandomResult);
                    test.assertSame(random, process.getRandom());
                }
            });

            runner.testGroup("setFileSystem(FileSystem,String)", () ->
            {
                runner.test("with null fileSystem", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final FileSystem oldFileSystem = process.getFileSystem();
                        final Folder oldCurrentFolder = process.getCurrentFolder();
                        test.assertThrows(() -> process.setFileSystem(null, "/"),
                            new PreConditionFailure("fileSystem cannot be null."));
                        test.assertSame(oldFileSystem, process.getFileSystem());
                        test.assertSame(oldCurrentFolder, process.getCurrentFolder());
                    }
                });

                runner.test("with null currentFolderPath", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final FileSystem fileSystem = process.getFileSystem();
                        final Folder oldCurrentFolder = process.getCurrentFolder();
                        test.assertThrows(() -> process.setFileSystem(fileSystem, (String)null),
                            new PreConditionFailure("currentFolderPath cannot be null."));
                        test.assertSame(fileSystem, process.getFileSystem());
                        test.assertSame(oldCurrentFolder, process.getCurrentFolder());
                    }
                });

                runner.test("with null currentFolderPath", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final FileSystem fileSystem = process.getFileSystem();
                        final Folder oldCurrentFolder = process.getCurrentFolder();
                        test.assertThrows(() -> process.setFileSystem(fileSystem, ""),
                            new PreConditionFailure("currentFolderPath cannot be empty."));
                        test.assertSame(fileSystem, process.getFileSystem());
                        test.assertSame(oldCurrentFolder, process.getCurrentFolder());
                    }
                });

                runner.test("with relative currentFolderPath", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final FileSystem fileSystem = process.getFileSystem();
                        final Folder oldCurrentFolder = process.getCurrentFolder();
                        test.assertThrows(() -> process.setFileSystem(fileSystem, "hello/there/"),
                            new PreConditionFailure("currentFolderPath.isRooted() cannot be false."));
                        test.assertSame(fileSystem, process.getFileSystem());
                        test.assertSame(oldCurrentFolder, process.getCurrentFolder());
                    }
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        final FakeDesktopProcess setFileSystemResult = process.setFileSystem(fileSystem, "/");
                        test.assertSame(process, setFileSystemResult);
                        test.assertSame(fileSystem, process.getFileSystem());
                        test.assertEqual("/", process.getCurrentFolderPathString());
                    }
                });
            });

            runner.testGroup("setNetwork(Network)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        test.assertThrows(() -> process.setNetwork(null),
                            new PreConditionFailure("network cannot be null."));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final ManualClock clock = ManualClock.create();
                        final FakeNetwork network = new FakeNetwork(clock);
                        final FakeDesktopProcess setNetworkResult = process.setNetwork(network);
                        test.assertSame(process, setNetworkResult);
                        test.assertSame(network, process.getNetwork());
                    }
                });
            });

            runner.testGroup("setCurrentFolderPathString(String)", () ->
            {
                runner.test("with null string", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final Folder old = process.getCurrentFolder();
                        test.assertThrows(() -> process.setCurrentFolderPathString(null),
                            new PreConditionFailure("currentFolderPathString cannot be null."));
                        test.assertEqual(old, process.getCurrentFolder());
                    }
                });

                runner.test("with empty string", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final Folder old = process.getCurrentFolder();
                        test.assertThrows(() -> process.setCurrentFolderPathString(""),
                            new PreConditionFailure("currentFolderPathString cannot be empty."));
                        test.assertEqual(old, process.getCurrentFolder());
                    }
                });

                runner.test("with relative path", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final Folder old = process.getCurrentFolder();
                        test.assertThrows(() -> process.setCurrentFolderPathString("hello there"),
                            new PreConditionFailure("currentFolderPath.isRooted() cannot be false."));
                        test.assertEqual(old, process.getCurrentFolder());
                    }
                });

                runner.test("with rooted path", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final FakeDesktopProcess setCurrentFolderPathStringResult = process.setCurrentFolderPathString("/hello/");
                        test.assertSame(process, setCurrentFolderPathStringResult);
                        test.assertEqual("/hello/", process.getCurrentFolderPathString());
                    }
                });
            });

            runner.testGroup("setCurrentFolderPath(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final Folder old = process.getCurrentFolder();
                        test.assertThrows(() -> process.setCurrentFolderPath(null),
                            new PreConditionFailure("currentFolderPath cannot be null."));
                        test.assertEqual(old, process.getCurrentFolder());
                    }
                });

                runner.test("with relative path", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final Folder old = process.getCurrentFolder();
                        test.assertThrows(() -> process.setCurrentFolderPath(Path.parse("hello there")),
                            new PreConditionFailure("currentFolderPath.isRooted() cannot be false."));
                        test.assertEqual(old, process.getCurrentFolder());
                    }
                });

                runner.test("with rooted path", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final FakeDesktopProcess setCurrentFolderPathStringResult = process.setCurrentFolderPath(Path.parse("/hello/"));
                        test.assertSame(process, setCurrentFolderPathStringResult);
                        test.assertEqual(Path.parse("/hello/"), process.getCurrentFolderPath());
                    }
                });
            });

            runner.test("setEnvironmentVariables()", (Test test) ->
            {
                try (final FakeDesktopProcess process = creator.run(test))
                {
                    final EnvironmentVariables envVars = new EnvironmentVariables();
                    test.assertSame(process, process.setEnvironmentVariables(envVars));
                    test.assertSame(envVars, process.getEnvironmentVariables());
                }
            });

            runner.testGroup("setClock(Clock)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        test.assertThrows(() -> process.setClock(null),
                            new PreConditionFailure("clock cannot be null."));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = creator.run(test))
                    {
                        final ManualClock clock = ManualClock.create(DateTime.create(123, 4, 5), test.getMainAsyncRunner());
                        final FakeDesktopProcess setClockResult = process.setClock(clock);
                        test.assertSame(process, setClockResult);
                        test.assertSame(clock, process.getClock());
                    }
                });
            });
        });
    }
}
