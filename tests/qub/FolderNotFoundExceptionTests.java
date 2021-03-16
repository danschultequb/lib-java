package qub;

public interface FolderNotFoundExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FolderNotFoundException.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                final Action2<String,Throwable> constructorErrorTest = (String folderPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        test.assertThrows(() -> new FolderNotFoundException(folderPath), expected);
                    });
                };

                constructorErrorTest.run(null, new PreConditionFailure("folderPath cannot be null."));
                constructorErrorTest.run("", new PreConditionFailure("folderPath cannot be empty."));
                constructorErrorTest.run("hello", new PreConditionFailure("folderPath.isRooted() cannot be false."));

                final Action1<String> constructorTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FolderNotFoundException exception = new FolderNotFoundException(folderPath);
                        test.assertNotNull(exception, "exception");
                        test.assertEqual(Path.parse(folderPath), exception.getFolderPath());
                        test.assertEqual("The folder at " + Strings.escapeAndQuote(folderPath) + " doesn't exist.", exception.getMessage());
                    });
                };

                constructorTest.run("/");
                constructorTest.run("/hello");
                constructorTest.run("/hello/");
                constructorTest.run("\\");
                constructorTest.run("\\there");
                constructorTest.run("\\there\\");
            });

            runner.testGroup("constructor(Path)", () ->
            {
                final Action2<Path,Throwable> constructorErrorTest = (Path folderPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        test.assertThrows(() -> new FolderNotFoundException(folderPath), expected);
                    });
                };

                constructorErrorTest.run(null, new PreConditionFailure("folderPath cannot be null."));
                constructorErrorTest.run(Path.parse("hello"), new PreConditionFailure("folderPath.isRooted() cannot be false."));

                final Action1<String> constructorTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FolderNotFoundException exception = new FolderNotFoundException(Path.parse(folderPath));
                        test.assertNotNull(exception, "exception");
                        test.assertEqual(Path.parse(folderPath), exception.getFolderPath());
                        test.assertEqual("The folder at " + Strings.escapeAndQuote(folderPath) + " doesn't exist.", exception.getMessage());
                    });
                };

                constructorTest.run("/");
                constructorTest.run("/hello");
                constructorTest.run("/hello/");
                constructorTest.run("\\");
                constructorTest.run("\\there");
                constructorTest.run("\\there\\");
            });

            runner.testGroup("constructor(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new FolderNotFoundException((Folder)null),
                        new PreConditionFailure("folder cannot be null."));
                });

                final Action2<String,String> constructorTest = (String folderPath, String expectedFolderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot("/").await();
                        final FolderNotFoundException exception = new FolderNotFoundException(fileSystem.getFolder(folderPath).await());
                        test.assertNotNull(exception, "exception");
                        test.assertEqual(Path.parse(expectedFolderPath), exception.getFolderPath());
                        test.assertEqual("The folder at " + Strings.escapeAndQuote(expectedFolderPath) + " doesn't exist.", exception.getMessage());
                    });
                };

                constructorTest.run("/", "/");
                constructorTest.run("/hello", "/hello/");
                constructorTest.run("/hello/", "/hello/");
                constructorTest.run("\\", "/");
                constructorTest.run("\\there", "/there/");
                constructorTest.run("\\there\\", "/there/");
            });
        });
    }
}
