package qub;

public interface PreConditionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(PreCondition.class, () ->
        {
            runner.testGroup("assertStartsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> startsWithErrorTest = (String value, String prefix, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartsWith(value, prefix, expressionName),
                            expected);
                    });
                };

                startsWithErrorTest.run(null, null, null, new PreConditionFailure("Expected null (null) to start with null."));
                startsWithErrorTest.run("", "", "", new PreConditionFailure("Expected  (\"\") to start with \"\"."));
                startsWithErrorTest.run("a", "b", "c", new PreConditionFailure("Expected c (\"a\") to start with \"b\"."));
                startsWithErrorTest.run("hello", "H", "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to start with \"H\"."));

                final Action3<String,String,String> startsWithTest = (String value, String prefix, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertStartsWith(value, prefix, expressionName);
                    });
                };

                startsWithTest.run("hello", "h", "myVariable");
                startsWithTest.run("hello", "hel", "myVariable");
                startsWithTest.run("hello", "hello", "myVariable");
            });

            runner.testGroup("assertStartsWith(String,String,CharacterComparer,String)", () ->
            {
                final Action5<String,String,CharacterComparer,String,Throwable> startsWithErrorTest = (String value, String prefix, CharacterComparer comparer, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartsWith(value, prefix, comparer, expressionName),
                            expected);
                    });
                };

                startsWithErrorTest.run("a", "b", null, "c", new PreConditionFailure("characterComparer cannot be null."));
                startsWithErrorTest.run(null, null, CharacterComparer.CaseInsensitive, null, new PreConditionFailure("Expected null (null) to start with null."));
                startsWithErrorTest.run("", "", CharacterComparer.CaseInsensitive, "", new PreConditionFailure("Expected  (\"\") to start with \"\"."));
                startsWithErrorTest.run("a", "b", CharacterComparer.CaseInsensitive, "c", new PreConditionFailure("Expected c (\"a\") to start with \"b\"."));
                startsWithErrorTest.run("hello", "H", CharacterComparer.Exact, "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to start with \"H\"."));

                final Action4<String,String,CharacterComparer,String> startsWithTest = (String value, String prefix, CharacterComparer comparer, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertStartsWith(value, prefix, comparer, expressionName);
                    });
                };

                startsWithTest.run("hello", "h", CharacterComparer.Exact, "myVariable");
                startsWithTest.run("hello", "hel", CharacterComparer.Exact, "myVariable");
                startsWithTest.run("hello", "hello", CharacterComparer.Exact, "myVariable");

                startsWithTest.run("hello", "h", CharacterComparer.CaseInsensitive, "myVariable");
                startsWithTest.run("hello", "hel", CharacterComparer.CaseInsensitive, "myVariable");
                startsWithTest.run("hello", "hello", CharacterComparer.CaseInsensitive, "myVariable");
                startsWithTest.run("hello", "H", CharacterComparer.CaseInsensitive, "myVariable");
                startsWithTest.run("hello", "HEL", CharacterComparer.CaseInsensitive, "myVariable");
                startsWithTest.run("hello", "HELLO", CharacterComparer.CaseInsensitive, "myVariable");
            });

            runner.testGroup("assertEndsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> endsWithErrorTest = (String value, String prefix, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEndsWith(value, prefix, expressionName),
                            expected);
                    });
                };

                endsWithErrorTest.run(null, null, null, new PreConditionFailure("Expected null (null) to end with null."));
                endsWithErrorTest.run("", "", "", new PreConditionFailure("Expected  (\"\") to end with \"\"."));
                endsWithErrorTest.run("a", "b", "c", new PreConditionFailure("Expected c (\"a\") to end with \"b\"."));
                endsWithErrorTest.run("hello", "O", "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to end with \"O\"."));

                final Action3<String,String,String> endsWithTest = (String value, String prefix, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEndsWith(value, prefix, expressionName);
                    });
                };

                endsWithTest.run("hello", "o", "myVariable");
                endsWithTest.run("hello", "llo", "myVariable");
                endsWithTest.run("hello", "hello", "myVariable");
            });
        });
    }
}
