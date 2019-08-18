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

//            runner.testGroup("loadClass()", () ->
//            {
//                runner.test("with null", (Test test) ->
//                {
//                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
//                    final Folder nonExistingFolder = currentFolder.getFolder("idontexist").await();
//                    try (final DynamicClassLoader classFolder = new DynamicClassLoader(nonExistingFolder))
//                    {
//                        test.assertThrows(() -> classFolder.loadClass(null),
//                            new PreConditionFailure("fullClassName cannot be null."));
//                    }
//                });
//
//                runner.test("with empty", (Test test) ->
//                {
//                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
//                    final Folder nonExistingFolder = currentFolder.getFolder("idontexist").await();
//                    try (final DynamicClassLoader classFolder = new DynamicClassLoader(nonExistingFolder))
//                    {
//                        test.assertThrows(() -> classFolder.loadClass(""),
//                            new PreConditionFailure("fullClassName cannot be empty."));
//                    }
//                });
//
//                runner.test("when folder doesn't exist", (Test test) ->
//                {
//                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
//                    final Folder nonExistingFolder = currentFolder.getFolder("idontexist").await();
//                    try (final DynamicClassLoader classFolder = new DynamicClassLoader(nonExistingFolder))
//                    {
//                        test.assertThrows(() -> classFolder.loadClass("not.a.FakeClass").await(),
//                            new NotFoundException("Could not find a class named \"not.a.FakeClass\" in the class folder " + nonExistingFolder + "."));
//                    }
//                });
//
//                runner.test("when folder does exist, but the class file doesn't exist", (Test test) ->
//                {
//                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
//                    try (final DynamicClassLoader classFolder = new DynamicClassLoader(currentFolder))
//                    {
//                        test.assertThrows(() -> classFolder.loadClass("not.a.FakeClass").await(),
//                            new NotFoundException("Could not find a class named \"not.a.FakeClass\" in the class folder " + currentFolder + "."));
//                    }
//                });
//
//                runner.test("when folder and class file exists", (Test test) ->
//                {
//                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
//                    final Folder outputFolder = currentFolder.getFolder("outputs").await();
//                    try (final DynamicClassLoader classFolder = new DynamicClassLoader(outputFolder))
//                    {
//                        final Class<?> loadedClass = classFolder.loadClass("qub.ClassFolderTests").await();
//                        test.assertNotNull(loadedClass);
//                        test.assertEqual("qub.ClassFolderTests", Types.getFullTypeName(loadedClass));
//                        test.assertSame()
//                        test.assertNotSame(ClassFolderTests.class, loadedClass);
//                        loadedClass.
//                        test.assertEqual(ClassFolderTests.class, loadedClass);
//                    }
//                });
//            });
        });
    }
}
