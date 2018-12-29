package qub;

public class AlreadyExistsExceptionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(AlreadyExistsException.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null message", (Test test) ->
                {
                    test.assertThrows(() -> new AlreadyExistsException(null), new PreConditionFailure("message cannot be null."));
                });

                runner.test("with empty message", (Test test) ->
                {
                    test.assertThrows(() -> new AlreadyExistsException(""), new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with non-empty message", (Test test) ->
                {
                    final AlreadyExistsException exception = new AlreadyExistsException("abc");
                    test.assertEqual("abc", exception.getMessage());
                    test.assertNull(exception.getCause());
                });
            });
        });
    }
}
