package qub;

public interface RealChildProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RealChildProcessRunner.class, () ->
        {
            ChildProcessRunnerTests.test(runner, RealChildProcessRunner::create);

            runner.testGroup("create(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> RealChildProcessRunner.create((DesktopProcess)null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    process.setDefaultCurrentFolder("/fake/current/folder/");
                    process.getEnvironmentVariables()
                        .set("PATH", "/a;/b;/c")
                        .set("PATHEXT", ".exe;.fake");

                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    test.assertNotNull(processRunner);
                    test.assertEqual(process.getCurrentFolder(), processRunner.getCurrentFolder());
                    test.assertEqual(
                        Iterable.create(
                            "/a/",
                            "/b/",
                            "/c/"),
                        processRunner.getFoldersInPathEnvironmentVariable().map(Folder::toString));
                    test.assertEqual(
                        Iterable.create(".exe", ".fake"),
                        processRunner.getExecutableFileExtensions());
                });
            });

            runner.testGroup("create(AsyncRunner,Folder,Iterable<Folder>,Iterable<String>)", () ->
            {
                final Action6<String,AsyncRunner,Folder,Iterable<Folder>,Iterable<String>,Throwable> createErrorTest = (String testName, AsyncRunner parallelAsyncRunner, Folder currentFolder, Iterable<Folder> foldersToSearch, Iterable<String> executableFileExtensions, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertThrows(() -> RealChildProcessRunner.create(parallelAsyncRunner, currentFolder, foldersToSearch, executableFileExtensions),
                            expected);
                    });
                };

                createErrorTest.run("with null parallelAsyncRunner",
                    null,
                    InMemoryFileSystem.create().getFolder("/current/folder/").await(),
                    Iterable.create(),
                    Iterable.create(),
                    new PreConditionFailure("parallelAsyncRunner cannot be null."));
                createErrorTest.run("with null currentFolder",
                    ManualAsyncRunner.create(),
                    null,
                    Iterable.create(),
                    Iterable.create(),
                    new PreConditionFailure("currentFolder cannot be null."));
                createErrorTest.run("with null foldersInPathEnvironmentVariable",
                    ManualAsyncRunner.create(),
                    InMemoryFileSystem.create().getFolder("/current/folder/").await(),
                    null,
                    Iterable.create(),
                    new PreConditionFailure("foldersInPathEnvironmentVariable cannot be null."));
                createErrorTest.run("with null executableFileExtensions",
                    ManualAsyncRunner.create(),
                    InMemoryFileSystem.create().getFolder("/current/folder/").await(),
                    Iterable.create(),
                    null,
                    new PreConditionFailure("executableFileExtensions cannot be null."));

                runner.test("with valid arguments", (Test test) ->
                {
                    final AsyncRunner parallelAsyncRunner = ParallelAsyncRunner.create();
                    final Folder currentFolder = InMemoryFileSystem.create().getFolder("/current/folder/").await();
                    final Iterable<Folder> foldersToSearch = Iterable.create();
                    final Iterable<String> executableFileExtensions = Iterable.create();

                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(parallelAsyncRunner, currentFolder, foldersToSearch, executableFileExtensions);
                    test.assertNotNull(processRunner);
                    test.assertEqual(currentFolder, processRunner.getCurrentFolder());
                    test.assertSame(foldersToSearch, processRunner.getFoldersInPathEnvironmentVariable());
                    test.assertEqual(executableFileExtensions, processRunner.getExecutableFileExtensions());
                });
            });

            runner.testGroup("getFoldersInPathEnvironmentVariable(EnvironmentVariables,FileSystem)", () ->
            {
                runner.test("with null environmentVariables", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = null;
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertThrows(() -> RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem),
                        new PreConditionFailure("environmentVariables cannot be null."));
                });

                runner.test("with null fileSystem", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create();
                    final InMemoryFileSystem fileSystem = null;
                    test.assertThrows(() -> RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem),
                        new PreConditionFailure("fileSystem cannot be null."));
                });

                runner.test("with no PATH environment variable", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem));
                });

                runner.test("with empty PATH environment variable", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATH", "");
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem));
                });

                runner.test("with empty PATH environment variable parts", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATH", ";");
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem));
                });

                runner.test("with non-empty PATH environment variable parts", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATH", "/hello/;/there");
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(
                            "/hello/",
                            "/there/"),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem).map(Folder::toString));
                });

                runner.test("with empty PATH environment variable part", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATH", "/hello/;;/there");
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(
                            "/hello/",
                            "/there/"),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem).map(Folder::toString));
                });

                runner.test("with PATH environment variable part that resolves outside of the root", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATH", "/hello/../../");
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem).map(Folder::toString));
                });

                runner.test("with PATH environment variable part that contains an environment variable reference", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("ABC", "/def")
                        .set("PATH", "%ABC%/hello/");
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual(
                        Iterable.create(
                            "/def/hello/"),
                        RealChildProcessRunner.getFoldersInPathEnvironmentVariable(environmentVariables, fileSystem).map(Folder::toString));
                });
            });

            runner.testGroup("getExecutableFileExtensions(EnvironmentVariables)", () ->
            {
                runner.test("with null environmentVariables", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = null;
                    test.assertThrows(() -> RealChildProcessRunner.getExecutableFileExtensions(environmentVariables),
                        new PreConditionFailure("environmentVariables cannot be null."));
                });

                runner.test("with no PATHEXT environment variable", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create();
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getExecutableFileExtensions(environmentVariables));
                });

                runner.test("with empty PATH environment variable", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATHEXT", "");
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getExecutableFileExtensions(environmentVariables));
                });

                runner.test("with empty PATH environment variable parts", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATHEXT", ";");
                    test.assertEqual(
                        Iterable.create(),
                        RealChildProcessRunner.getExecutableFileExtensions(environmentVariables));
                });

                runner.test("with non-empty PATH environment variable parts", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATHEXT", "/hello/;/there");
                    test.assertEqual(
                        Iterable.create(
                            "/hello/",
                            "/there"),
                        RealChildProcessRunner.getExecutableFileExtensions(environmentVariables));
                });

                runner.test("with empty PATH environment variable part", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("PATHEXT", "/hello/;;/there");
                    test.assertEqual(
                        Iterable.create(
                            "/hello/",
                            "/there"),
                        RealChildProcessRunner.getExecutableFileExtensions(environmentVariables));
                });

                runner.test("with PATH environment variable part that contains an environment variable reference", (Test test) ->
                {
                    final EnvironmentVariables environmentVariables = EnvironmentVariables.create()
                        .set("ABC", "/def")
                        .set("PATHEXT", "%ABC%/hello/");
                    test.assertEqual(
                        Iterable.create(
                            "/def/hello/"),
                        RealChildProcessRunner.getExecutableFileExtensions(environmentVariables));
                });
            });

            runner.testGroup("findExecutableFile(Path,Folder)", () ->
            {
                runner.test("with null executablePath",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = null;
                    final Folder workingFolder = process.getCurrentFolder();
                    test.assertThrows(() -> processRunner.findExecutableFile(executablePath, workingFolder).await(),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null workingFolder",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("fake");
                    final Folder workingFolder = null;
                    test.assertThrows(() -> processRunner.findExecutableFile(executablePath, workingFolder).await(),
                        new PreConditionFailure("workingFolder cannot be null."));
                });

                runner.test("with rooted executablePath",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("/fake/file");
                    final Folder workingFolder = process.getCurrentFolder();
                    test.assertThrows(() -> processRunner.findExecutableFile(executablePath, workingFolder),
                        new PreConditionFailure("executablePath.isRooted() cannot be true."));
                });

                runner.test("with executablePath with more than one segment that doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("fake/file");
                    final Folder workingFolder = process.getCurrentFolder();
                    test.assertThrows(() -> processRunner.findExecutableFile(executablePath, workingFolder).await(),
                        new FileNotFoundException(workingFolder.getFile(executablePath).await()));
                });

                runner.test("with executablePath with more than one segment that exists (/)",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("fake/file");
                    final Folder workingFolder = process.getCurrentFolder();
                    final File executableFile = workingFolder.createFile(executablePath).await();
                    final File result = processRunner.findExecutableFile(executablePath, workingFolder).await();
                    test.assertEqual(executableFile, result);
                });

                runner.test("with executablePath with more than one segment that exists (\\)",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("fake\\file");
                    final Folder workingFolder = process.getCurrentFolder();
                    final File executableFile = workingFolder.createFile(executablePath).await();
                    final File result = processRunner.findExecutableFile(executablePath, workingFolder).await();
                    test.assertEqual(executableFile, result);
                });

                runner.test("with executablePath that ends with '.' and executable file extension that doesn't end with '.'",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    process.getEnvironmentVariables().set("PATHEXT", "exe");
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("fake\\file.");
                    final Folder workingFolder = process.getCurrentFolder();
                    final File executableFile = workingFolder.createFile("fake/file.exe").await();
                    final File result = processRunner.findExecutableFile(executablePath, workingFolder).await();
                    test.assertEqual(executableFile, result);
                });

                runner.test("with executablePath that doesn't end with '.' and executable file extension that doesn't end with '.'",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    process.getEnvironmentVariables().set("PATHEXT", "exe");
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("fake\\file");
                    final Folder workingFolder = process.getCurrentFolder();
                    final File executableFile = workingFolder.createFile("fake/file.exe").await();
                    final File result = processRunner.findExecutableFile(executablePath, workingFolder).await();
                    test.assertEqual(executableFile, result);
                });
            });

            runner.testGroup("run(String,String...)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final String executablePath = "doesntExist";
                    test.assertThrows(() -> processRunner.run(executablePath).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run("javac").await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run("javac", "spam").await();
                    test.assertEqual(1, exitCode);
                });
            });

            runner.testGroup("run(String,Iterable<String>)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final String executablePath = "doesntExist";
                    test.assertThrows(() -> processRunner.run(executablePath, Iterable.create()).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run("javac", Iterable.create()).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run("javac", Iterable.create("spam")).await();
                    test.assertEqual(1, exitCode);
                });
            });

            runner.testGroup("run(Path,String...)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("doesntExist");
                    test.assertThrows(() -> processRunner.run(executablePath).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run(Path.parse("javac")).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run(Path.parse("javac"), "spam").await();
                    test.assertEqual(1, exitCode);
                });
            });

            runner.testGroup("run(Path,Iterable<String>)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("doesntExist");
                    test.assertThrows(() -> processRunner.run(executablePath, Iterable.create()).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run(Path.parse("javac"), Iterable.create()).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Integer exitCode = processRunner.run(Path.parse("javac"), Iterable.create("spam")).await();
                    test.assertEqual(1, exitCode);
                });
            });

            runner.testGroup("run(File,String...)", () ->
            {
                runner.test("with not found executableFile",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = process.getCurrentFolder().getFile("doesntExist").await();
                    test.assertThrows(() -> processRunner.run(executableFile).await(),
                        new FileNotFoundException(executableFile));
                });

                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final Integer exitCode = processRunner.run(executableFile).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final Integer exitCode = processRunner.run(executableFile, "spam").await();
                    test.assertEqual(1, exitCode);
                });
            });

            runner.testGroup("run(File,Iterable<String>)", () ->
            {
                runner.test("with not found executableFile",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = process.getCurrentFolder().getFile("doesntExist").await();
                    test.assertThrows(() -> processRunner.run(executableFile, Iterable.create()).await(),
                        new FileNotFoundException(executableFile));
                });

                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final Integer exitCode = processRunner.run(executableFile, Iterable.create()).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final Integer exitCode = processRunner.run(executableFile, Iterable.create("spam")).await();
                    test.assertEqual(1, exitCode);
                });
            });

            runner.testGroup("run(ChildProcessParameters)", () ->
            {
                runner.test("with working folder that doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcessParameters parameters = ChildProcessParameters.create("javac")
                        .setWorkingFolderPath("/i/dont/exist/");
                    test.assertThrows(() -> processRunner.run(parameters).await(),
                        new FolderNotFoundException("/i/dont/exist/"));
                });

                runner.test("with rooted executable path that doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcessParameters parameters = ChildProcessParameters.create("/i/dont/exist");
                    test.assertThrows(() -> processRunner.run(parameters).await(),
                        new FileNotFoundException("/i/dont/exist"));
                });

                runner.test("with working folder and multi-segment relative executable path",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);

                    final QubFolder qubFolder = process.getQubFolder().await();
                    final QubProjectVersionFolder jdkFolder = qubFolder.getLatestProjectVersionFolder("openjdk", "jdk").await();
                    final ChildProcessParameters parameters = ChildProcessParameters.create("bin/javac.exe")
                        .setWorkingFolder(jdkFolder);
                    final Integer exitCode = processRunner.run(parameters).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with working folder and multi-segment relative executable path without file extension",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);

                    final QubFolder qubFolder = process.getQubFolder().await();
                    final QubProjectVersionFolder jdkFolder = qubFolder.getLatestProjectVersionFolder("openjdk", "jdk").await();
                    final ChildProcessParameters parameters = ChildProcessParameters.create("bin\\javac")
                        .setWorkingFolder(jdkFolder);
                    final Integer exitCode = processRunner.run(parameters).await();
                    test.assertEqual(2, exitCode);
                });

                runner.test("with redirected output and error streams",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    final ChildProcessParameters parameters = ChildProcessParameters.create("javac")
                        .redirectOutputTo(output)
                        .redirectErrorTo(error);
                    final Integer exitCode = processRunner.run(parameters).await();
                    test.assertEqual(2, exitCode);
                    final String outputText = output.getText().await();
                    test.assertContains(outputText, "Usage: javac <options> <source files>");
                    test.assertContains(outputText, "where possible options include:");
                    test.assertEqual("", error.getText().await());
                });
            });

            runner.testGroup("start(String,String...)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final String executablePath = "doesntExist";
                    test.assertThrows(() -> processRunner.start(executablePath).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start("javac").await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(2, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start("javac", "spam").await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(1, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });
            });

            runner.testGroup("start(String,Iterable<String>)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final String executablePath = "doesntExist";
                    test.assertThrows(() -> processRunner.start(executablePath, Iterable.create()).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start("javac", Iterable.create()).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(2, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start("javac", Iterable.create("spam")).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(1, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });
            });

            runner.testGroup("start(Path,String...)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("doesntExist");
                    test.assertThrows(() -> processRunner.start(executablePath).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start(Path.parse("javac")).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(2, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start(Path.parse("javac"), "spam").await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(1, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });
            });

            runner.testGroup("start(Path,Iterable<String>)", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final Path executablePath = Path.parse("doesntExist");
                    test.assertThrows(() -> processRunner.start(executablePath, Iterable.create()).await(),
                        new FileNotFoundException(process.getCurrentFolder().getFile(executablePath).await()));
                });

                runner.test("with executable found on PATH",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start(Path.parse("javac"), Iterable.create()).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(2, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final ChildProcess childProcess = processRunner.start(Path.parse("javac"), Iterable.create("spam")).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(1, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });
            });

            runner.testGroup("start(File,String...)", () ->
            {
                runner.test("with not found executableFile",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = process.getCurrentFolder().getFile("doesntExist").await();
                    test.assertThrows(() -> processRunner.start(executableFile).await(),
                        new FileNotFoundException(executableFile));
                });

                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final ChildProcess childProcess = processRunner.start(executableFile).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(2, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final ChildProcess childProcess = processRunner.start(executableFile, "spam").await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(1, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });
            });

            runner.testGroup("start(File,Iterable<String>)", () ->
            {
                runner.test("with not found executableFile",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = process.getCurrentFolder().getFile("doesntExist").await();
                    test.assertThrows(() -> processRunner.start(executableFile, Iterable.create()).await(),
                        new FileNotFoundException(executableFile));
                });

                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final ChildProcess childProcess = processRunner.start(executableFile, Iterable.create()).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(2, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });

                runner.test("with arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcess()),
                    (Test test, DesktopProcess process) ->
                {
                    final RealChildProcessRunner processRunner = RealChildProcessRunner.create(process);
                    final File executableFile = processRunner.findExecutableFile(Path.parse("javac"), process.getCurrentFolder()).await();
                    final ChildProcess childProcess = processRunner.start(executableFile, Iterable.create("spam")).await();
                    test.assertOneOf(Iterable.create(ProcessState.NotRunning, ProcessState.Running), childProcess.getState());
                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(1, childProcess.await());
                        test.assertEqual(ProcessState.NotRunning, childProcess.getState());
                    }
                });
            });
        });
    }
}
