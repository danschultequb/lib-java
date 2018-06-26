package qub;

public class StringsTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Strings.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Strings());
            });

            runner.testGroup("containsAny(String,char[])", () ->
            {
                final Action3<String,char[],Boolean> containsAnyTest = (String text, char[] characters, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + (characters == null ? "null" : Array.fromValues(characters).toString()), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.containsAny(text, characters));
                    });
                };

                containsAnyTest.run(null, null, false);
                containsAnyTest.run(null, new char[0], false);
                containsAnyTest.run(null, new char[] { 'a' }, false);

                containsAnyTest.run("", null, false);
                containsAnyTest.run("", new char[0], false);
                containsAnyTest.run("", new char[] { 'a' }, false);

                containsAnyTest.run("apples", null, false);
                containsAnyTest.run("apples", new char[0], false);
                containsAnyTest.run("apples", new char[] { 'b' }, false);
                containsAnyTest.run("apples", new char[] { 'a' }, true);
            });

            runner.testGroup("escape(String)", () ->
            {
                final Action2<String,String> escapeTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.escape(text));
                    });
                };

                escapeTest.run(null, null);
                escapeTest.run("", "");
                escapeTest.run("abc", "abc");
                escapeTest.run("\b\f\n\r\t\'\"", "\\b\\f\\n\\r\\t\\\'\\\"");
            });

            runner.testGroup("quote(String)", () ->
            {
                final Action2<String,String> quoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.quote(text));
                    });
                };

                quoteTest.run(null, null);
                quoteTest.run("", "\"\"");
                quoteTest.run("abc", "\"abc\"");
                quoteTest.run("\"", "\"\"\"");
            });

            runner.testGroup("escapeAndQuote(String)", () ->
            {
                final Action2<String,String> escapeAndQuoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.escapeAndQuote(text));
                    });
                };

                escapeAndQuoteTest.run(null, null);
                escapeAndQuoteTest.run("", "\"\"");
                escapeAndQuoteTest.run("abc", "\"abc\"");
                escapeAndQuoteTest.run("\b\f\n\r\t", "\"\\b\\f\\n\\r\\t\"");
                escapeAndQuoteTest.run("\"", "\"\\\"\"");
            });

            runner.testGroup("repeat(String,int)", () ->
            {
                runner.test("with \"\" and 10", (Test test) ->
                {
                    test.assertEqual("", Strings.repeat("", 10));
                });

                runner.test("with \"a\" and -1", (Test test) ->
                {
                    test.assertThrows(() -> Strings.repeat("a", -1));
                });

                runner.test("with \"a\" and 0", (Test test) ->
                {
                    test.assertEqual("", Strings.repeat("a", 0));
                });

                runner.test("with \"a\" and 1", (Test test) ->
                {
                    test.assertEqual("a", Strings.repeat("a", 1));
                });

                runner.test("with \"a\" and 2", (Test test) ->
                {
                    test.assertEqual("aa", Strings.repeat("a", 2));
                });

                runner.test("with \"a\" and 3", (Test test) ->
                {
                    test.assertEqual("aaa", Strings.repeat("a", 3));
                });
                runner.test("with \"ab\" and 1", (Test test) ->
                {
                    test.assertEqual("ab", Strings.repeat("ab", 1));
                });

                runner.test("with \"ab\" and 2", (Test test) ->
                {
                    test.assertEqual("abab", Strings.repeat("ab", 2));
                });

                runner.test("with \"ab\" and 3", (Test test) ->
                {
                    test.assertEqual("ababab", Strings.repeat("ab", 3));
                });
            });
        });
    }
}
