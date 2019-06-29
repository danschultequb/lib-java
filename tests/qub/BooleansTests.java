package qub;

public interface BooleansTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Booleans.class, () ->
        {
            runner.testGroup("isTrue(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Booleans.isTrue(null));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertFalse(Booleans.isTrue(false));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertTrue(Booleans.isTrue(true));
                });
            });

            runner.testGroup("isFalse(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Booleans.isFalse(null));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertTrue(Booleans.isFalse(false));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertFalse(Booleans.isFalse(true));
                });
            });

            runner.testGroup("toString(boolean)", () ->
            {
                runner.test("with true", (Test test) ->
                {
                    test.assertEqual("true", Booleans.toString(true));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertEqual("false", Booleans.toString(false));
                });
            });

            runner.testGroup("toString(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.toString(null),
                        new PreConditionFailure("value cannot be null."));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertEqual("true", Booleans.toString(Boolean.valueOf(true)));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertEqual("false", Booleans.toString(Boolean.valueOf(false)));
                });
            });

            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.parse(null),
                        new PreConditionFailure("value cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.parse(""),
                        new PreConditionFailure("value cannot be empty."));
                });

                runner.test("with \"true\"", (Test test) ->
                {
                    test.assertTrue(Booleans.parse("true").await());
                });

                runner.test("with \"false\"", (Test test) ->
                {
                    test.assertFalse(Booleans.parse("false").await());
                });

                runner.test("with \"True\"", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.parse("True").await(),
                        new ParseException("Expected the value (\"True\") to be either \"true\" or \"false\"."));
                });

                runner.test("with \"falSE\"", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.parse("falSE").await(),
                        new ParseException("Expected the value (\"falSE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with \"apple\"", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.parse("apple").await(),
                        new ParseException("Expected the value (\"apple\") to be either \"true\" or \"false\"."));
                });
            });
        });
    }
}
