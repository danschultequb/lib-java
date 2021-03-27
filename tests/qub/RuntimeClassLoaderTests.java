package qub;

public interface RuntimeClassLoaderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RuntimeClassLoader.class, () ->
        {
            runner.testGroup("create()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create())
                    {
                        test.assertFalse(classLoader.isDisposed());
                        test.assertEqual(Iterable.create(), classLoader.getClassSources());
                    }
                });

                runner.test("with Folder and Jar file arguments", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder classFolder = fileSystem.getFolder("/class/source/").await();
                    final File jarFile = fileSystem.getFile("/example.jar").await();
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(classFolder, jarFile))
                    {
                        test.assertFalse(classLoader.isDisposed());
                        test.assertEqual(Iterable.create(classFolder, jarFile), classLoader.getClassSources());
                    }
                });

                runner.test("with null class sources array", (Test test) ->
                {
                    test.assertThrows(() -> RuntimeClassLoader.create((FileSystemEntry[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null class source", (Test test) ->
                {
                    test.assertThrows(() -> RuntimeClassLoader.create((FileSystemEntry)null),
                        new PreConditionFailure("classSource cannot be null."));
                });

                runner.test("with null class sources Iterable", (Test test) ->
                {
                    test.assertThrows(() -> RuntimeClassLoader.create((Iterable<FileSystemEntry>)null),
                        new PreConditionFailure("classSources cannot be null."));
                });

                runner.test("with Iterable with null class source", (Test test) ->
                {
                    test.assertThrows(() -> RuntimeClassLoader.create(Iterable.create((FileSystemEntry)null)),
                        new PreConditionFailure("classSource cannot be null."));
                });

                runner.test("with null parentClassLoader", (Test test) ->
                {
                    test.assertThrows(() -> RuntimeClassLoader.create(Iterable.create(), null),
                        new PreConditionFailure("parentClassLoader cannot be null."));
                });
            });

            runner.testGroup("loadClass()", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                    (Test test, Folder nonExistingFolder) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(nonExistingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass(null),
                            new PreConditionFailure("fullClassName cannot be null."));
                    }
                });

                runner.test("with empty",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                    (Test test, Folder nonExistingFolder) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(nonExistingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass(""),
                            new PreConditionFailure("fullClassName cannot be empty."));
                    }
                });

                runner.test("when folder doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                    (Test test, Folder nonExistingFolder) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(nonExistingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass("not.a.FakeClass"),
                            new ClassNotFoundException("Could not load a class with the name \"not.a.FakeClass\" from [" + nonExistingFolder + "]."));
                    }
                });

                runner.test("when folder does exist, but the class file doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder existingFolder) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(existingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass("not.a.FakeClass"),
                            new ClassNotFoundException("Could not load a class with the name \"not.a.FakeClass\" from [" + existingFolder + "]."));
                    }
                });

                runner.test("when folder and class file exists",
                    (TestResources resources) -> Tuple.create(resources.getCurrentFolder().getFolder("outputs").await()),
                    (Test test, Folder outputsFolder) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(outputsFolder))
                    {
                        final Class<?> loadedClass = classLoader.loadClass("qub.RuntimeClassLoaderTests");
                        test.assertNotNull(loadedClass);
                        test.assertEqual("qub.RuntimeClassLoaderTests", Types.getFullTypeName(loadedClass));
                        test.assertSame(classLoader.getParent(), loadedClass.getClassLoader());
                    }
                });

                runner.test("when disposed",
                    (TestResources resources) -> Tuple.create(resources.getCurrentFolder().getFolder("outputs").await()),
                    (Test test, Folder outputsFolder) ->
                {
                    try (final RuntimeClassLoader classLoader = RuntimeClassLoader.create(outputsFolder))
                    {
                        test.assertTrue(classLoader.dispose().await());
                        test.assertTrue(classLoader.isDisposed());

                        test.assertFalse(classLoader.dispose().await());
                        test.assertTrue(classLoader.isDisposed());

                        test.assertThrows(() -> classLoader.loadClass("qub.RuntimeClassLoaderTests"),
                            new PreConditionFailure("this.isDisposed() cannot be true."));
                    }
                });
            });
        });
    }
}
