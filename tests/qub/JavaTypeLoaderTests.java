package qub;

public interface JavaTypeLoaderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaTypeLoader.class, () ->
        {
            TypeLoaderTests.test(runner, JavaTypeLoader::create);

            runner.testGroup("getTypeContainerPathString(String)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final String typeContainerPath = typeLoader.getTypeContainerPathString("qub.TypeLoader").await();
                    test.assertNotNullAndNotEmpty(typeContainerPath);

                    final FileSystem fileSystem = test.getFileSystem();
                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });

                runner.test("with " + Strings.escapeAndQuote(String.class), (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    typeLoader.getTypeContainerPathString(String.class).await();
                });
            });

            runner.testGroup("getTypeContainerPathString(Class<?>)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final String typeContainerPath = typeLoader.getTypeContainerPathString(TypeLoader.class).await();
                    test.assertNotNullAndNotEmpty(typeContainerPath);

                    final FileSystem fileSystem = test.getFileSystem();
                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });
            });

            runner.testGroup("getTypeContainerPath(String)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final Path typeContainerPath = typeLoader.getTypeContainerPath("qub.TypeLoader").await();
                    test.assertNotNull(typeContainerPath);
                    test.assertTrue(typeContainerPath.isRooted());

                    final FileSystem fileSystem = test.getFileSystem();
                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });
            });

            runner.testGroup("getTypeContainerPath(Class<?>)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final Path typeContainerPath = typeLoader.getTypeContainerPath(TypeLoader.class).await();
                    test.assertNotNull(typeContainerPath);
                    test.assertTrue(typeContainerPath.isRooted());

                    final FileSystem fileSystem = test.getFileSystem();
                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });
            });
        });
    }
}
