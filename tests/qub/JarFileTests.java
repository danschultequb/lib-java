package qub;

public interface JarFileTests
{
    static void test(TestRunner runner)
    {
        final Path jarFilePath = Path.parse("C:/qub/qub/qub-java-pack/10/qub-java-pack.jar");

        runner.testGroup(JarFile.class, () ->
        {
            runner.testGroup("open()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JarFile.open(null),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
                    final File fakeFile = currentFolder.getFile("fake.jar").await();
                    test.assertThrows(() -> JarFile.open(fakeFile).await(),
                        new FileNotFoundException(fakeFile));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final FileSystem fileSystem = test.getProcess().getFileSystem();
                    final File file = fileSystem.getFile(jarFilePath).await();
                    try (final JarFile jarFile = JarFile.open(file).await())
                    {
                        test.assertNotNull(jarFile);
                        test.assertFalse(jarFile.isDisposed());
                        test.assertEqual(file.getPath(), jarFile.getPath());
                    }
                });

                runner.test("with existing non-jar file", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
                    final File projectJsonFile = currentFolder.getFile("project.json").await();
                    test.assertThrows(() -> JarFile.open(projectJsonFile).await(),
                        new java.util.zip.ZipException("error in opening zip file"));
                });

                runner.test("with existing jar file with different extension", (Test test) ->
                {
                    final FileSystem fileSystem = test.getProcess().getFileSystem();
                    final File file = fileSystem.getFile(jarFilePath).await();
                    final File fooFile = fileSystem.getFile(file.getPath().changeFileExtension(".foo")).await();
                    file.copyTo(fooFile).await();
                    try
                    {
                        try (final JarFile jarFile = JarFile.open(fooFile).await())
                        {
                            test.assertNotNull(jarFile);
                            test.assertFalse(jarFile.isDisposed());
                            test.assertEqual(fooFile.getPath(), jarFile.getPath());
                        }
                    }
                    finally
                    {
                        fooFile.delete()
                            .catchError()
                            .await();
                    }
                });
            });

            runner.test("dispose()", (Test test) ->
            {
                final FileSystem fileSystem = test.getProcess().getFileSystem();
                final File file = fileSystem.getFile(jarFilePath).await();
                try (final JarFile jarFile = JarFile.open(file).await())
                {
                    test.assertTrue(jarFile.dispose().await());
                    test.assertTrue(jarFile.isDisposed());

                    test.assertFalse(jarFile.dispose().await());
                    test.assertTrue(jarFile.isDisposed());
                }
            });

            runner.test("getEntries()", (Test test) ->
            {
                final FileSystem fileSystem = test.getProcess().getFileSystem();
                final File file = fileSystem.getFile(jarFilePath).await();
                try (final JarFile jarFile = JarFile.open(file).await())
                {
                    final Iterable<JarFileEntry> entries = jarFile.getEntries();
                    test.assertNotNull(entries);
                    test.assertEqual(
                        Iterable.create(
                            "META-INF/",
                            "META-INF/MANIFEST.MF",
                            "qub/FakeJarCreator.class",
                            "qub/JarCreator.class",
                            "qub/JavaJarCreator.class",
                            "qub/QubPack.class"),
                        entries.map(entry -> entry.getRelativePath().toString()));
                }
            });

            runner.test("getClassFileEntries()", (Test test) ->
            {
                final FileSystem fileSystem = test.getProcess().getFileSystem();
                final File file = fileSystem.getFile(jarFilePath).await();
                try (final JarFile jarFile = JarFile.open(file).await())
                {
                    final Iterable<JarFileEntry> entries = jarFile.getClassFileEntries();
                    test.assertNotNull(entries);
                    test.assertEqual(
                        Iterable.create(
                            "qub/FakeJarCreator.class",
                            "qub/JarCreator.class",
                            "qub/JavaJarCreator.class",
                            "qub/QubPack.class"),
                        entries.map(entry -> entry.getRelativePath().toString()));
                }
            });
        });
    }
}
