package qub;

public class AlreadyExistsExceptionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(AlreadyExistsException.class, () ->
        {
            runner.testGroup("constructor(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final AlreadyExistsException exception = new AlreadyExistsException(null);
                    test.assertEqual(null, exception.getValue());
                    test.assertEqual("The value null already exists.", exception.getMessage());
                    test.assertNull(exception.getCause());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AlreadyExistsException exception = new AlreadyExistsException("apples");
                    test.assertEqual("apples", exception.getValue());
                    test.assertEqual("The value apples already exists.", exception.getMessage());
                    test.assertNull(exception.getCause());
                });
            });

            runner.testGroup("constructor(Object,String)", () ->
            {
                runner.test("with null value and null message", (Test test) ->
                {
                    test.assertThrows(() -> new AlreadyExistsException(null, null), new PreConditionFailure("message cannot be null."));
                });

                runner.test("with non-null value and null message", (Test test) ->
                {
                    test.assertThrows(() -> new AlreadyExistsException("apples", null), new PreConditionFailure("message cannot be null."));
                });

                runner.test("with null value and empty message", (Test test) ->
                {
                    test.assertThrows(() -> new AlreadyExistsException(null, ""), new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with non-null value and empty message", (Test test) ->
                {
                    test.assertThrows(() -> new AlreadyExistsException("apples", ""), new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with null value and non-empty message", (Test test) ->
                {
                    final AlreadyExistsException exception = new AlreadyExistsException(null, "abc");
                    test.assertEqual(null, exception.getValue());
                    test.assertEqual("abc", exception.getMessage());
                    test.assertNull(exception.getCause());
                });

                runner.test("with non-null value and non-empty message", (Test test) ->
                {
                    final AlreadyExistsException exception = new AlreadyExistsException(5, "xyz");
                    test.assertEqual(5, exception.getValue());
                    test.assertEqual("xyz", exception.getMessage());
                    test.assertNull(exception.getCause());
                });
            });
        });
    }
}
