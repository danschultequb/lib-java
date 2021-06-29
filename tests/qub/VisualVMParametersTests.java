package qub;

public interface VisualVMParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(VisualVMParameters.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final VisualVMParameters parameters = VisualVMParameters.create();
                test.assertNotNull(parameters);
                test.assertEqual(Path.parse("visualvm"), parameters.getExecutablePath());
                test.assertNull(parameters.getWorkingFolderPath());
                test.assertEqual(Iterable.create(), parameters.getArguments());
                test.assertNull(parameters.getInputStream());
                test.assertNull(parameters.getOutputStreamHandler());
                test.assertNull(parameters.getErrorStreamHandler());
            });

            runner.testGroup("create(Path)", () ->
            {
                final Action2<Path,Throwable> createErrorTest = (Path visualVmPath, Throwable expected) ->
                {
                    runner.test("with " + visualVmPath, (Test test) ->
                    {
                        test.assertThrows(() -> VisualVMParameters.create(visualVmPath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("visualVmPath cannot be null."));
                createErrorTest.run(Path.parse("visualvm/"), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                createErrorTest.run(Path.parse("visualvm\\"), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));

                final Action1<Path> createTest = (Path visualVmPath) ->
                {
                    runner.test("with " + visualVmPath, (Test test) ->
                    {
                        final VisualVMParameters parameters = VisualVMParameters.create(visualVmPath);
                        test.assertNotNull(parameters);
                        test.assertEqual(visualVmPath, parameters.getExecutablePath());
                        test.assertNull(parameters.getWorkingFolderPath());
                        test.assertEqual(Iterable.create(), parameters.getArguments());
                        test.assertNull(parameters.getInputStream());
                        test.assertNull(parameters.getOutputStreamHandler());
                        test.assertNull(parameters.getErrorStreamHandler());
                    });
                };

                createTest.run(Path.parse("visualvm"));
                createTest.run(Path.parse("visualvm.exe"));
                createTest.run(Path.parse("hello"));
                createTest.run(Path.parse("visual/vm"));
                createTest.run(Path.parse("visual\\vm"));
                createTest.run(Path.parse("/visual/vm.exe"));
                createTest.run(Path.parse("\\visual\\vm.exe"));
            });

            runner.testGroup("create(QubFolder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> VisualVMParameters.create((QubFolder)null),
                        new PreConditionFailure("qubFolder cannot be null."));
                });

                runner.test("with no oracle publisher folder",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final InMemoryFileSystem fileSystem = process.getFileSystem();
                    final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                    final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                    fakeJavaCompiledSourcesJarFile.create().await();

                    process.getTypeLoader()
                        .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                    test.assertThrows(() -> VisualVMParameters.create(qubFolder).await(),
                        new NotFoundException("No project named oracle/visualvm has been published."));
                });

                runner.test("with no visualvm project folder",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final InMemoryFileSystem fileSystem = process.getFileSystem();
                    final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                    final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                    fakeJavaCompiledSourcesJarFile.create().await();
                    qubFolder.getPublisherFolder("oracle").await()
                        .create().await();

                    process.getTypeLoader()
                        .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                    test.assertThrows(() -> VisualVMParameters.create(qubFolder).await(),
                        new NotFoundException("No project named oracle/visualvm has been published."));
                });

                runner.test("with no versions folder",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final InMemoryFileSystem fileSystem = process.getFileSystem();
                    final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                    final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                    fakeJavaCompiledSourcesJarFile.create().await();
                    qubFolder.getProjectFolder("oracle", "visualvm").await()
                        .create().await();

                    process.getTypeLoader()
                        .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                    test.assertThrows(() -> VisualVMParameters.create(qubFolder).await(),
                        new NotFoundException("No project named oracle/visualvm has been published."));
                });

                runner.test("with no version folder",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
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

                    test.assertThrows(() -> VisualVMParameters.create(qubFolder).await(),
                        new NotFoundException("No project named oracle/visualvm has been published."));
                });

                runner.test("with no published files",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final InMemoryFileSystem fileSystem = process.getFileSystem();
                    final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                    final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                    fakeJavaCompiledSourcesJarFile.create().await();
                    qubFolder.getProjectVersionFolder("oracle", "visualvm", "1.2.3").await().create().await();

                    process.getTypeLoader()
                        .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                    final VisualVMParameters parameters = VisualVMParameters.create(qubFolder).await();
                    test.assertNotNull(parameters);
                    test.assertEqual("/qub/oracle/visualvm/versions/1.2.3/bin/visualvm.exe", parameters.getExecutablePath().toString());
                    test.assertNull(parameters.getWorkingFolderPath());
                    test.assertEqual(Iterable.create(), parameters.getArguments());
                    test.assertNull(parameters.getInputStream());
                    test.assertNull(parameters.getOutputStreamHandler());
                    test.assertNull(parameters.getErrorStreamHandler());
                });

                runner.test("with multiple versions",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final InMemoryFileSystem fileSystem = process.getFileSystem();
                    final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                    final File fakeJavaCompiledSourcesJarFile = qubFolder.getCompiledSourcesFile("qub", "fake-java", "50").await();
                    fakeJavaCompiledSourcesJarFile.create().await();
                    qubFolder.getProjectVersionFolder("oracle", "visualvm", "1").await().create().await();
                    qubFolder.getProjectVersionFolder("oracle", "visualvm", "2").await().create().await();

                    process.getTypeLoader()
                        .addTypeContainer("fake.MainClassFullName", fakeJavaCompiledSourcesJarFile);

                    final VisualVMParameters parameters = VisualVMParameters.create(qubFolder).await();
                    test.assertNotNull(parameters);
                    test.assertEqual("/qub/oracle/visualvm/versions/2/bin/visualvm.exe", parameters.getExecutablePath().toString());
                    test.assertNull(parameters.getWorkingFolderPath());
                    test.assertEqual(Iterable.create(), parameters.getArguments());
                    test.assertNull(parameters.getInputStream());
                    test.assertNull(parameters.getOutputStreamHandler());
                    test.assertNull(parameters.getErrorStreamHandler());
                });
            });
        });
    }
}
