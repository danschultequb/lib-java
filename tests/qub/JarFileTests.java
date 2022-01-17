package qub;

public interface JarFileTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JarFile.class,
            (TestResources resources) -> Tuple.create(resources.getQubFolder()),
            (QubFolder qubFolder) ->
        {
            final Folder libJavaFolder = qubFolder.getLatestProjectVersionFolder("qub", "lib-java").await();
            final File libJavaJarFile = libJavaFolder.getFile("lib-java.jar").await();

            final Folder csvJavaFolder = qubFolder.getLatestProjectVersionFolder("qub", "csv-java").await();
            final File csvJavaJarFile = csvJavaFolder.getFile("csv-java.jar").await();

            runner.testGroup("open()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JarFile.open(null),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final File fakeFile = libJavaFolder.getFile("fakeFile.txt").await();
                    test.assertThrows(() -> JarFile.open(fakeFile).await(),
                        new FileNotFoundException(fakeFile));
                });

                runner.test("with existing file", (Test test) ->
                {
                    try (final JarFile jarFile = JarFile.open(libJavaJarFile).await())
                    {
                        test.assertNotNull(jarFile);
                        test.assertFalse(jarFile.isDisposed());
                        test.assertEqual(libJavaJarFile.getPath(), jarFile.getPath());
                    }
                });

                runner.test("with existing non-jar file", (Test test) ->
                {
                    final File projectJsonFile = libJavaFolder.getFile("project.json").await();
                    test.assertThrows(() -> JarFile.open(projectJsonFile).await(),
                        new java.util.zip.ZipException("zip END header not found"));
                });

                runner.test("with existing jar file with different extension", (Test test) ->
                {
                    final Path fooFilePath = libJavaJarFile.getPath().changeFileExtension(".foo").relativeTo(libJavaFolder);
                    try (final TemporaryFile fooFile = TemporaryFile.get(libJavaFolder.getFile(fooFilePath).await()))
                    {
                        libJavaJarFile.copyTo(fooFile).await();

                        try (final JarFile jarFile = JarFile.open(fooFile).await())
                        {
                            test.assertNotNull(jarFile);
                            test.assertFalse(jarFile.isDisposed());
                            test.assertEqual(fooFile.getPath(), jarFile.getPath());
                        }
                    }
                });
            });

            runner.test("dispose()", (Test test) ->
            {
                try (final JarFile jarFile = JarFile.open(libJavaJarFile).await())
                {
                    test.assertTrue(jarFile.dispose().await());
                    test.assertTrue(jarFile.isDisposed());

                    test.assertFalse(jarFile.dispose().await());
                    test.assertTrue(jarFile.isDisposed());
                }
            });

            runner.test("getEntries()", (Test test) ->
            {
                try (final JarFile jarFile = JarFile.open(csvJavaJarFile).await())
                {
                    final Iterable<JarFileEntry> entries = jarFile.getEntries();
                    test.assertNotNull(entries);
                    test.assertEqual(
                        Iterable.create(
                            "META-INF/",
                            "META-INF/MANIFEST.MF",
                            "qub/CSV.class",
                            "qub/CSVDocument.class",
                            "qub/CSVFormat.class",
                            "qub/CSVRow.class"),
                        entries.map(entry -> entry.getRelativePath().toString()));
                }
            });

            runner.test("getClassFileEntries()", (Test test) ->
            {
                try (final JarFile jarFile = JarFile.open(csvJavaJarFile).await())
                {
                    final Iterable<JarFileEntry> entries = jarFile.getClassFileEntries();
                    test.assertNotNull(entries);
                    test.assertEqual(
                        Iterable.create(
                            "qub/CSV.class",
                            "qub/CSVDocument.class",
                            "qub/CSVFormat.class",
                            "qub/CSVRow.class"),
                        entries.map(entry -> entry.getRelativePath().toString()));
                }
            });
        });
    }
}
