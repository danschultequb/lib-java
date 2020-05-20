package qub;

public interface RealProcessFactoryTests
{
    static RealProcessFactory create(Test test)
    {
        final AsyncRunner parallelAsyncRunner = test.getParallelAsyncRunner();
        final EnvironmentVariables environmentVariables = test.getProcess().getEnvironmentVariables();
        final Folder currentFolder = test.getProcess().getCurrentFolder();
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
                    final AsyncRunner parallelAsyncRunner = null;
                    final EnvironmentVariables environmentVariables = test.getProcess().getEnvironmentVariables();
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    test.assertThrows(() -> new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder),
                        new PreConditionFailure("parallelAsyncRunner cannot be null."));
                });

                runner.test("with null environmentVariables", (Test test) ->
                {
                    final AsyncRunner parallelAsyncRunner = test.getParallelAsyncRunner();
                    final EnvironmentVariables environmentVariables = null;
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    test.assertThrows(() -> new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder),
                        new PreConditionFailure("environmentVariables cannot be null."));
                });

                runner.test("with null currentFolder", (Test test) ->
                {
                    final AsyncRunner parallelAsyncRunner = test.getParallelAsyncRunner();
                    final EnvironmentVariables environmentVariables = test.getProcess().getEnvironmentVariables();
                    final Folder currentFolder = null;
                    test.assertThrows(() -> new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder),
                        new PreConditionFailure("currentFolder cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final AsyncRunner parallelAsyncRunner = test.getParallelAsyncRunner();
                    final EnvironmentVariables environmentVariables = test.getProcess().getEnvironmentVariables();
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final RealProcessFactory factory = new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder);
                    test.assertNotNull(factory);
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with not found executablePath", (Test test) ->
                {
                    final RealProcessFactory factory = RealProcessFactoryTests.create(test);
                    test.assertThrows(() -> factory.run(Path.parse("doesntExist"), Iterable.create(), test.getProcess().getCurrentFolderPath(), null, null, null, null).await(),
                        new FileNotFoundException("doesntExist"));
                });
            });
        });
    }
}
