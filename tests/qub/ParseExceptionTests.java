package qub;

public interface ParseExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ParseException.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with null message", (Test test) ->
                {
                    test.assertThrows(() -> new ParseException(null),
                        new PreConditionFailure("message cannot be null."));
                });

                runner.test("with empty message", (Test test) ->
                {
                    test.assertThrows(() -> new ParseException(""),
                        new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with non-empty message", (Test test) ->
                {
                    final ParseException error = new ParseException("hello");
                    test.assertEqual("hello", error.getMessage());
                });
            });
        });
    }
}
