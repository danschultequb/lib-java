package qub;

public interface RuntimeClassLoaderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RuntimeClassLoader.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader())
                    {
                        test.assertFalse(classLoader.isDisposed());
                        test.assertEqual(Iterable.create(), classLoader.getClassSources());
                    }
                });

                runner.test("with Folder and Jar file arguments", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Folder classFolder = fileSystem.getFolder("/class/source/").await();
                    final File jarFile = fileSystem.getFile("/example.jar").await();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(classFolder, jarFile))
                    {
                        test.assertFalse(classLoader.isDisposed());
                        test.assertEqual(Iterable.create(classFolder, jarFile), classLoader.getClassSources());
                    }
                });

                runner.test("with null class sources array", (Test test) ->
                {
                    test.assertThrows(() -> new RuntimeClassLoader((FileSystemEntry[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null class source", (Test test) ->
                {
                    test.assertThrows(() -> new RuntimeClassLoader((FileSystemEntry)null),
                        new PreConditionFailure("classSource cannot be null."));
                });

                runner.test("with null class sources Iterable", (Test test) ->
                {
                    test.assertThrows(() -> new RuntimeClassLoader((Iterable<FileSystemEntry>)null),
                        new PreConditionFailure("classSources cannot be null."));
                });

                runner.test("with Iterable with null class source", (Test test) ->
                {
                    test.assertThrows(() -> new RuntimeClassLoader(Iterable.create((FileSystemEntry)null)),
                        new PreConditionFailure("classSource cannot be null."));
                });

                runner.test("with null parentClassLoader", (Test test) ->
                {
                    test.assertThrows(() -> new RuntimeClassLoader(Iterable.create(), null),
                        new PreConditionFailure("parentClassLoader cannot be null."));
                });
            });

            runner.testGroup("loadClass()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final Folder nonExistingFolder = currentFolder.getFolder("idontexist").await();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(nonExistingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass(null),
                            new PreConditionFailure("fullClassName cannot be null."));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final Folder nonExistingFolder = currentFolder.getFolder("idontexist").await();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(nonExistingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass(""),
                            new PreConditionFailure("fullClassName cannot be empty."));
                    }
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final Folder nonExistingFolder = currentFolder.getFolder("idontexist").await();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(nonExistingFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass("not.a.FakeClass"),
                            new ClassNotFoundException("Could not load a class with the name \"not.a.FakeClass\" from [" + nonExistingFolder + "]."));
                    }
                });

                runner.test("when folder does exist, but the class file doesn't exist", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(currentFolder))
                    {
                        test.assertThrows(() -> classLoader.loadClass("not.a.FakeClass"),
                            new ClassNotFoundException("Could not load a class with the name \"not.a.FakeClass\" from [" + currentFolder + "]."));
                    }
                });

                runner.test("when folder and class file exists", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final Folder outputFolder = currentFolder.getFolder("outputs").await();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(outputFolder))
                    {
                        final Class<?> loadedClass = classLoader.loadClass("qub.RuntimeClassLoaderTests");
                        test.assertNotNull(loadedClass);
                        test.assertEqual("qub.RuntimeClassLoaderTests", Types.getFullTypeName(loadedClass));
                        test.assertSame(classLoader.getParent(), loadedClass.getClassLoader());
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder();
                    final Folder outputFolder = currentFolder.getFolder("outputs").await();
                    try (final RuntimeClassLoader classLoader = new RuntimeClassLoader(outputFolder))
                    {
                        test.assertTrue(classLoader.dispose().await());
                        test.assertTrue(classLoader.isDisposed());
                        test.assertFalse(classLoader.dispose().await());
                        test.assertTrue(classLoader.isDisposed());

                        test.assertThrows(() -> classLoader.loadClass("qub.RuntimeClassLoaderTests"),
                            new PreConditionFailure("isDisposed() cannot be true."));
                    }
                });
            });
        });
    }
}
