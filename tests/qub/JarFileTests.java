package qub;

public interface JarFileTests
{
    static void test(TestRunner runner)
    {
        final Path jarFilePath = Path.parse("C:/qub/qub/pack-java/versions/20/pack-java.jar");

        runner.testGroup(JarFile.class, () ->
        {
            runner.testGroup("open()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JarFile.open(null),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file",
                    (TestResources resources) -> Tuple.create(resources.getCurrentFolder()),
                    (Test test, Folder currentFolder) ->
                {
                    final File fakeFile = currentFolder.getFile("fakeFile.txt").await();
                    test.assertThrows(() -> JarFile.open(fakeFile).await(),
                        new FileNotFoundException(fakeFile));
                });

                runner.test("with existing file",
                    (TestResources resources) -> Tuple.create(resources.getQubFolder()),
                    (Test test, QubFolder qubFolder) ->
                {
                    final File file = qubFolder.getProjectFolder("qub", "lib-java").await()
                        .getLatestProjectVersionFolder().await()
                        .getFile("lib-java.jar").await();
                    try (final JarFile jarFile = JarFile.open(file).await())
                    {
                        test.assertNotNull(jarFile);
                        test.assertFalse(jarFile.isDisposed());
                        test.assertEqual(file.getPath(), jarFile.getPath());
                    }
                });

                runner.test("with existing non-jar file",
                    (TestResources resources) -> Tuple.create(resources.getCurrentFolder()),
                    (Test test, Folder currentFolder) ->
                {
                    final File projectJsonFile = currentFolder.getFile("project.json").await();
                    test.assertThrows(() -> JarFile.open(projectJsonFile).await(),
                        new java.util.zip.ZipException("zip END header not found"));
                });

                runner.test("with existing jar file with different extension",
                    (TestResources resources) -> Tuple.create(resources.getQubFolder()),
                    (Test test, QubFolder qubFolder) ->
                {
                    final File file = qubFolder.getProjectFolder("qub", "lib-java").await()
                        .getLatestProjectVersionFolder().await()
                        .getFile("lib-java.jar").await();
                    final Path fooFilePath = file.getPath().changeFileExtension(".foo");
                    try (final TemporaryFile fooFile = TemporaryFile.get(file.getFileSystem().getFile(fooFilePath).await()))
                    {
                        file.copyTo(fooFile).await();

                        try (final JarFile jarFile = JarFile.open(fooFile).await())
                        {
                            test.assertNotNull(jarFile);
                            test.assertFalse(jarFile.isDisposed());
                            test.assertEqual(fooFile.getPath(), jarFile.getPath());
                        }
                    }
                });
            });

            runner.test("dispose()",
                (TestResources resources) -> Tuple.create(resources.getQubFolder()),
                (Test test, QubFolder qubFolder) ->
            {
                final File file = qubFolder.getProjectFolder("qub", "lib-java").await()
                    .getLatestProjectVersionFolder().await()
                    .getFile("lib-java.jar").await();
                try (final JarFile jarFile = JarFile.open(file).await())
                {
                    test.assertTrue(jarFile.dispose().await());
                    test.assertTrue(jarFile.isDisposed());

                    test.assertFalse(jarFile.dispose().await());
                    test.assertTrue(jarFile.isDisposed());
                }
            });

            runner.test("getEntries()",
                (TestResources resources) -> Tuple.create(resources.getQubFolder()),
                (Test test, QubFolder qubFolder) ->
            {
                final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("qub", "pack-java", "20").await();
                final File file = projectVersionFolder.getFile("pack-java.jar").await();
                try (final JarFile jarFile = JarFile.open(file).await())
                {
                    final Iterable<JarFileEntry> entries = jarFile.getEntries();
                    test.assertNotNull(entries);
                    test.assertEqual(
                        Iterable.create(
                            "META-INF/",
                            "META-INF/MANIFEST.MF",
                            "qub/FakeJarProcessRun.class",
                            "qub/JarArguments.class",
                            "qub/JarProcessBuilder.class",
                            "qub/PackJSON.class",
                            "qub/PackJSONFile.class",
                            "qub/QubPack.class",
                            "qub/QubPackParameters.class"),
                        entries.map(entry -> entry.getRelativePath().toString()));
                }
            });

            runner.test("getClassFileEntries()",
                (TestResources resources) -> Tuple.create(resources.getQubFolder()),
                (Test test, QubFolder qubFolder) ->
            {
                final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("qub", "pack-java", "20").await();
                final File file = projectVersionFolder.getFile("pack-java.jar").await();
                try (final JarFile jarFile = JarFile.open(file).await())
                {
                    final Iterable<JarFileEntry> entries = jarFile.getClassFileEntries();
                    test.assertNotNull(entries);
                    test.assertEqual(
                        Iterable.create(
                            "qub/FakeJarProcessRun.class",
                            "qub/JarArguments.class",
                            "qub/JarProcessBuilder.class",
                            "qub/PackJSON.class",
                            "qub/PackJSONFile.class",
                            "qub/QubPack.class",
                            "qub/QubPackParameters.class"),
                        entries.map(entry -> entry.getRelativePath().toString()));
                }
            });
        });
    }
}
