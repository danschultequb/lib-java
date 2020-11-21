package qub;

public interface VisualVMProcessBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(VisualVMProcessBuilder.class, () ->
        {
            runner.testGroup("get(Process)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> VisualVMProcessBuilder.get(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with no QUB_HOME environment variable", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables());

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No environment variable named \"QUB_HOME\" found."));
                    }
                });

                runner.test("with no QubFolder", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        process.setFileSystem(fileSystem);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no oracle publisher folder", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        fileSystem.createFolder("/qub/").await();
                        process.setFileSystem(fileSystem);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no visualvm project folder", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        fileSystem.createFolder("/qub/oracle/").await();
                        process.setFileSystem(fileSystem);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no versions folder", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        fileSystem.createFolder("/qub/oracle/visualvm/").await();
                        process.setFileSystem(fileSystem);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no version folder", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        fileSystem.createFolder("/qub/oracle/visualvm/versions").await();
                        process.setFileSystem(fileSystem);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no published files", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        fileSystem.createFolder("/qub/oracle/visualvm/versions/1.2.3/").await();
                        process.setFileSystem(fileSystem);

                        final VisualVMProcessBuilder processBuilder = VisualVMProcessBuilder.get(process).await();
                        test.assertNotNull(processBuilder);
                        test.assertEqual("/qub/oracle/visualvm/versions/1.2.3/bin/visualvm.exe", processBuilder.getExecutablePath().toString());
                    }
                });

                runner.test("with multiple versions", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        process.setEnvironmentVariables(new EnvironmentVariables()
                            .set("QUB_HOME", "/qub/"));

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("/").await();
                        fileSystem.createFolder("/qub/oracle/visualvm/versions/1/").await();
                        fileSystem.createFolder("/qub/oracle/visualvm/versions/2/").await();
                        process.setFileSystem(fileSystem);

                        final VisualVMProcessBuilder processBuilder = VisualVMProcessBuilder.get(process).await();
                        test.assertNotNull(processBuilder);
                        test.assertEqual("/qub/oracle/visualvm/versions/2/bin/visualvm.exe", processBuilder.getExecutablePath().toString());
                    }
                });
            });
        });
    }
}
