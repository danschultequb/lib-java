package qub;

public interface PreConditionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(PreCondition.class, () ->
        {
            runner.testGroup("assertEqual(char,char,String)", () ->
            {
                final Action4<Character,Character,String,Throwable> assertEqualErrorTest = (Character expected, Character value, String expressionName, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEqual(expected.charValue(), value.charValue(), expressionName),
                            expectedError);
                    });
                };

                assertEqualErrorTest.run('a', 'b', "c", new PreConditionFailure("c (b) must be a."));
                assertEqualErrorTest.run('a', 'A', "d", new PreConditionFailure("d (A) must be a."));

                final Action3<Character,Character,String> assertEqualTest = (Character expected, Character value, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEqual(expected.charValue(), value.charValue(), expressionName);
                    });
                };

                assertEqualTest.run('a', 'a', "myVariable");
            });

            runner.testGroup("assertEqual(char,Character,String)", () ->
            {
                final Action4<Character,Character,String,Throwable> assertEqualErrorTest = (Character expected, Character value, String expressionName, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEqual(expected.charValue(), value, expressionName),
                            expectedError);
                    });
                };

                assertEqualErrorTest.run('a', null, "c", new PreConditionFailure("c (null) must be a."));
                assertEqualErrorTest.run('a', 'b', "c", new PreConditionFailure("c (b) must be a."));
                assertEqualErrorTest.run('a', 'A', "d", new PreConditionFailure("d (A) must be a."));

                final Action3<Character,Character,String> assertEqualTest = (Character expected, Character value, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEqual(expected.charValue(), value, expressionName);
                    });
                };

                assertEqualTest.run('a', 'a', "myVariable");
            });

            runner.testGroup("assertEqual(Character,char,String)", () ->
            {
                final Action4<Character,Character,String,Throwable> assertEqualErrorTest = (Character expected, Character value, String expressionName, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEqual(expected, value.charValue(), expressionName),
                            expectedError);
                    });
                };

                assertEqualErrorTest.run(null, 'b', "c", new PreConditionFailure("c (b) must be null."));
                assertEqualErrorTest.run('a', 'b', "c", new PreConditionFailure("c (b) must be a."));
                assertEqualErrorTest.run('a', 'A', "d", new PreConditionFailure("d (A) must be a."));

                final Action3<Character,Character,String> assertEqualTest = (Character expected, Character value, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEqual(expected, value.charValue(), expressionName);
                    });
                };

                assertEqualTest.run('a', 'a', "myVariable");
            });

            runner.testGroup("assertStartsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> assertStartsWithErrorTest = (String value, String prefix, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartsWith(value, prefix, expressionName),
                            expected);
                    });
                };

                assertStartsWithErrorTest.run(null, null, null, new PreConditionFailure("Expected null (null) to start with null."));
                assertStartsWithErrorTest.run("", "", "", new PreConditionFailure("Expected  (\"\") to start with \"\"."));
                assertStartsWithErrorTest.run("a", "b", "c", new PreConditionFailure("Expected c (\"a\") to start with \"b\"."));
                assertStartsWithErrorTest.run("hello", "H", "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to start with \"H\"."));

                final Action3<String,String,String> assertStartsWithTest = (String value, String prefix, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertStartsWith(value, prefix, expressionName);
                    });
                };

                assertStartsWithTest.run("hello", "h", "myVariable");
                assertStartsWithTest.run("hello", "hel", "myVariable");
                assertStartsWithTest.run("hello", "hello", "myVariable");
            });

            runner.testGroup("assertStartsWith(String,String,CharacterComparer,String)", () ->
            {
                final Action5<String,String,CharacterComparer,String,Throwable> assertStartsWithErrorTest = (String value, String prefix, CharacterComparer comparer, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartsWith(value, prefix, comparer, expressionName),
                            expected);
                    });
                };

                assertStartsWithErrorTest.run("a", "b", null, "c", new PreConditionFailure("characterComparer cannot be null."));
                assertStartsWithErrorTest.run(null, null, CharacterComparer.CaseInsensitive, null, new PreConditionFailure("Expected null (null) to start with null."));
                assertStartsWithErrorTest.run("", "", CharacterComparer.CaseInsensitive, "", new PreConditionFailure("Expected  (\"\") to start with \"\"."));
                assertStartsWithErrorTest.run("a", "b", CharacterComparer.CaseInsensitive, "c", new PreConditionFailure("Expected c (\"a\") to start with \"b\"."));
                assertStartsWithErrorTest.run("hello", "H", CharacterComparer.Exact, "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to start with \"H\"."));

                final Action4<String,String,CharacterComparer,String> assertStartsWithTest = (String value, String prefix, CharacterComparer comparer, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertStartsWith(value, prefix, comparer, expressionName);
                    });
                };

                assertStartsWithTest.run("hello", "h", CharacterComparer.Exact, "myVariable");
                assertStartsWithTest.run("hello", "hel", CharacterComparer.Exact, "myVariable");
                assertStartsWithTest.run("hello", "hello", CharacterComparer.Exact, "myVariable");

                assertStartsWithTest.run("hello", "h", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "hel", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "hello", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "H", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "HEL", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "HELLO", CharacterComparer.CaseInsensitive, "myVariable");
            });

            runner.testGroup("assertEndsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> assertEndsWithErrorTest = (String value, String prefix, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEndsWith(value, prefix, expressionName),
                            expected);
                    });
                };

                assertEndsWithErrorTest.run(null, null, null, new PreConditionFailure("Expected null (null) to end with null."));
                assertEndsWithErrorTest.run("", "", "", new PreConditionFailure("Expected  (\"\") to end with \"\"."));
                assertEndsWithErrorTest.run("a", "b", "c", new PreConditionFailure("Expected c (\"a\") to end with \"b\"."));
                assertEndsWithErrorTest.run("hello", "O", "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to end with \"O\"."));

                final Action3<String,String,String> assertEndsWithTest = (String value, String prefix, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEndsWith(value, prefix, expressionName);
                    });
                };

                assertEndsWithTest.run("hello", "o", "myVariable");
                assertEndsWithTest.run("hello", "llo", "myVariable");
                assertEndsWithTest.run("hello", "hello", "myVariable");
            });
        });
    }
}
