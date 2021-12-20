package qub;

public interface RootAlreadyExistsExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RootAlreadyExistsException.class, () ->
        {
            runner.testGroup("constructor(Path)", () ->
            {
                final Action2<Path,Throwable> constructorErrorTest = (Path rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(Objects.toString(rootPath)), (Test test) ->
                    {
                        test.assertThrows(() -> new RootAlreadyExistsException(rootPath),
                            expected);
                    });
                };

                constructorErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                constructorErrorTest.run(Path.parse("hello"), new PreConditionFailure("rootPath.isRooted() cannot be false."));

                final Action1<Path> constructorTest = (Path rootPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath.toString()), (Test test) ->
                    {
                        final RootAlreadyExistsException e = new RootAlreadyExistsException(rootPath);
                        test.assertNotNull(e);
                        test.assertEqual(rootPath, e.getRootPath());
                        test.assertNull(e.getCause());
                        test.assertEqual("The root at " + Strings.escapeAndQuote(rootPath.toString()) + " already exists.", e.getMessage());
                    });
                };

                constructorTest.run(Path.parse("/"));
                constructorTest.run(Path.parse("\\"));
                constructorTest.run(Path.parse("C:/"));
                constructorTest.run(Path.parse("/hello/there"));
            });
        });
    }
}
