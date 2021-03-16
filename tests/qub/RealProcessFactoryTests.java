package qub;

public interface RealProcessFactoryTests
{
    static RealProcessFactory create(Process process)
    {
        final AsyncRunner parallelAsyncRunner = process.getParallelAsyncRunner();
        final EnvironmentVariables environmentVariables = process.getEnvironmentVariables();
        final Folder currentFolder = process.getCurrentFolder();
        return new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder);
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(RealProcessFactory.class, () ->
        {
            ProcessFactoryTests.test(runner, RealProcessFactoryTests::create);

            runner.testGroup("constructor", () ->
            {
                runner.test("with null parallelAsyncRunner", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AsyncRunner parallelAsyncRunner = null;
                        final EnvironmentVariables environmentVariables = process.getEnvironmentVariables();
                        final Folder currentFolder = process.getCurrentFolder();
                        test.assertThrows(() -> new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder),
                            new PreConditionFailure("parallelAsyncRunner cannot be null."));
                    }
                });

                runner.test("with null environmentVariables", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AsyncRunner parallelAsyncRunner = process.getParallelAsyncRunner();
                        final EnvironmentVariables environmentVariables = null;
                        final Folder currentFolder = process.getCurrentFolder();
                        test.assertThrows(() -> new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder),
                            new PreConditionFailure("environmentVariables cannot be null."));
                    }
                });

                runner.test("with null currentFolder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AsyncRunner parallelAsyncRunner = process.getParallelAsyncRunner();
                        final EnvironmentVariables environmentVariables = process.getEnvironmentVariables();
                        final Folder currentFolder = null;
                        test.assertThrows(() -> new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder),
                            new PreConditionFailure("currentFolder cannot be null."));
                    }
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AsyncRunner parallelAsyncRunner = process.getParallelAsyncRunner();
                        final EnvironmentVariables environmentVariables = process.getEnvironmentVariables();
                        final Folder currentFolder = process.getCurrentFolder();
                        final RealProcessFactory factory = new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder);
                        test.assertNotNull(factory);
                    }
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with not found relative executablePath",
                    (TestResources resources) -> Tuple.create(
                        resources.getParallelAsyncRunner(),
                        resources.getEnvironmentVariables(),
                        resources.getCurrentFolder()),
                    (Test test, AsyncRunner parallelAsyncRunner, EnvironmentVariables environmentVariables, Folder currentFolder) ->
                {
                    final RealProcessFactory factory = new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder);
                    final Path executablePath = Path.parse("doesntExist");
                    final Iterable<String> arguments = Iterable.create();
                    final Path currentFolderPath = currentFolder.getPath();
                    test.assertThrows(() -> factory.run(executablePath, arguments, currentFolderPath, null, null, null, null).await(),
                        new FileNotFoundException(executablePath));
                });
            });
        });
    }
}
