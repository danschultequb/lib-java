package qub;

public interface EnumsTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Enums.class, () ->
        {
            runner.testGroup("parse()", () ->
            {
                runner.test("with null enumType", (Test test) ->
                {
                    test.assertThrows(() -> Enums.parse(null, "hello"),
                        new PreConditionFailure("enumType cannot be null."));
                });

                runner.test("with null text", (Test test) ->
                {
                    test.assertThrows(() -> Enums.parse(Comparison.class, null),
                        new PreConditionFailure("text cannot be null."));
                });

                runner.test("with empty text", (Test test) ->
                {
                    test.assertThrows(() -> Enums.parse(Comparison.class, ""),
                        new PreConditionFailure("text cannot be empty."));
                });

                runner.test("with non-matching text", (Test test) ->
                {
                    test.assertThrows(() -> Enums.parse(Comparison.class, "oops").await(),
                        new NotFoundException("No enum value for qub.Comparison matches \"oops\"."));
                });

                runner.test("with matching text", (Test test) ->
                {
                    test.assertEqual(Comparison.GreaterThan, Enums.parse(Comparison.class, Comparison.GreaterThan.toString()).await());
                });

                runner.test("with different-cased matching text", (Test test) ->
                {
                    test.assertEqual(Comparison.LessThan, Enums.parse(Comparison.class, "LESSTHAN").await());
                });
            });
        });
    }
}
