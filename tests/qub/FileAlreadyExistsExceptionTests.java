package qub;

public interface FileAlreadyExistsExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FileAlreadyExistsException.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                final Action2<String,Throwable> constructorErrorTest = (String filePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        test.assertThrows(() -> new FileAlreadyExistsException(filePath), expected);
                    });
                };

                constructorErrorTest.run(null, new PreConditionFailure("filePath cannot be null."));
                constructorErrorTest.run("", new PreConditionFailure("filePath cannot be empty."));
                constructorErrorTest.run("hello", new PreConditionFailure("filePath.isRooted() cannot be false."));
            });
        });
    }
}
