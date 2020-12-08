package qub;

public interface VisualVMProcessBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(VisualVMProcessBuilder.class, () ->
        {
            runner.testGroup("get(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> VisualVMProcessBuilder.get(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with no oracle publisher folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                        fakeJavaCompiledSourcesJarFile.create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no visualvm project folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                        fakeJavaCompiledSourcesJarFile.create().await();
                        qubFolder.getPublisherFolder("oracle").await()
                            .create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no versions folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                        fakeJavaCompiledSourcesJarFile.create().await();
                        qubFolder.getProjectFolder("oracle", "visualvm").await()
                            .create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no version folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                        fakeJavaCompiledSourcesJarFile.create().await();
                        qubFolder.getProjectFolder("oracle", "visualvm").await()
                            .getProjectVersionsFolder().await()
                            .create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                        test.assertThrows(() -> VisualVMProcessBuilder.get(process).await(),
                            new NotFoundException("No project named oracle/visualvm has been published."));
                    }
                });

                runner.test("with no published files", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                        fakeJavaCompiledSourcesJarFile.create().await();
                        qubFolder.getProjectVersionFolder("oracle", "visualvm", "1.2.3").await().create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                        final VisualVMProcessBuilder processBuilder = VisualVMProcessBuilder.get(process).await();
                        test.assertNotNull(processBuilder);
                        test.assertEqual("/qub/oracle/visualvm/versions/1.2.3/bin/visualvm.exe", processBuilder.getExecutablePath().toString());
                    }
                });

                runner.test("with multiple versions", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                        fakeJavaCompiledSourcesJarFile.create().await();
                        qubFolder.getProjectVersionFolder("oracle", "visualvm", "1").await().create().await();
                        qubFolder.getProjectVersionFolder("oracle", "visualvm", "2").await().create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                        final VisualVMProcessBuilder processBuilder = VisualVMProcessBuilder.get(process).await();
                        test.assertNotNull(processBuilder);
                        test.assertEqual("/qub/oracle/visualvm/versions/2/bin/visualvm.exe", processBuilder.getExecutablePath().toString());
                    }
                });
            });
        });
    }
}
