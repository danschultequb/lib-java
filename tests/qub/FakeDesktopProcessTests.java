package qub;

public interface FakeDesktopProcessTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDesktopProcess.class, () ->
        {
            final Function0<FakeDesktopProcess> creator = () ->
            {
                final FakeDesktopProcess process = FakeDesktopProcess.create();

                process.getEnvironmentVariables()
                    .set("path", "fake-path");

                final InMemoryFileSystem fileSystem = process.getFileSystem();
                final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                final File compiledSourcesFile = qubFolder.getCompiledSourcesFile("fake", "main-java", "13").await();
                compiledSourcesFile.create().await();

                process.getTypeLoader()
                    .addTypeContainer("fake.MainClassFullName", compiledSourcesFile.toString());

                return process;
            };

            DesktopProcessTests.test(runner, creator);
        });
    }
}
