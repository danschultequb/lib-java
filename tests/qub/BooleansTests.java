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
                final Action2<String,Throwable> parseErrorTest = (String value, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertThrows(() -> Booleans.parse(value).await(),
                            expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("value cannot be null."));
                parseErrorTest.run("", new PreConditionFailure("value cannot be empty."));
                parseErrorTest.run("True", new ParseException("Expected the value (\"True\") to be either \"true\" or \"false\"."));
                parseErrorTest.run("falSE", new ParseException("Expected the value (\"falSE\") to be either \"true\" or \"false\"."));
                parseErrorTest.run("apple", new ParseException("Expected the value (\"apple\") to be either \"true\" or \"false\"."));

                final Action2<String,Boolean> parseTest = (String value, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Booleans.parse(value).await());
                    });
                };

                parseTest.run("true", true);
                parseTest.run("false", false);
            });

            runner.testGroup("parse(String,boolean)", () ->
            {
                final Action3<String,Boolean,Throwable> parseErrorTest = (String value, Boolean caseSensitive, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), caseSensitive), (Test test) ->
                    {
                        test.assertThrows(() -> Booleans.parse(value, caseSensitive).await(),
                            expected);
                    });
                };

                parseErrorTest.run(null, true, new PreConditionFailure("value cannot be null."));
                parseErrorTest.run("", true, new PreConditionFailure("value cannot be empty."));
                parseErrorTest.run("True", true, new ParseException("Expected the value (\"True\") to be either \"true\" or \"false\"."));
                parseErrorTest.run("falSE", true, new ParseException("Expected the value (\"falSE\") to be either \"true\" or \"false\"."));
                parseErrorTest.run("apple", true, new ParseException("Expected the value (\"apple\") to be either \"true\" or \"false\"."));
                parseErrorTest.run("apple", false, new ParseException("Expected the value (\"apple\") to be either \"true\" or \"false\"."));

                final Action3<String,Boolean,Boolean> parseTest = (String value, Boolean caseSensitive, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), caseSensitive), (Test test) ->
                    {
                        test.assertEqual(expected, Booleans.parse(value, caseSensitive).await());
                    });
                };

                parseTest.run("true", true, true);
                parseTest.run("true", false, true);
                parseTest.run("TrUe", false, true);
                parseTest.run("false", true, false);
                parseTest.run("false", false, false);
                parseTest.run("FalsE", false, false);
            });
        });
    }
}
