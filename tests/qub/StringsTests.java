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
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + (characters == null ? "null" : Array.create(characters).toString()), (Test test) ->
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

            runner.testGroup("join(java.lang.Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join((java.lang.Iterable<Character>)null), new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Strings.join(new Array<>(0)));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc", Strings.join(Array.create('a', 'b', 'c')));
                });
            });

            runner.testGroup("padLeft()", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padLeft(null, 2, 'a'));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padLeft("", 2, 'a'));
                });

                runner.test("with non-empty value smaller than the minimum length", (Test test) ->
                {
                    test.assertEqual("zzzab", Strings.padLeft("ab", 5, 'z'));
                });

                runner.test("with non-empty value equal to the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", 3, 'z'));
                });

                runner.test("with non-empty value greater than the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", 1, 'z'));
                });

                runner.test("with negative minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", -1, 'z'));
                });

                runner.test("with zero minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", 0, 'z'));
                });
            });

            runner.testGroup("padRight()", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padRight(null, 2, 'a'));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padRight("", 2, 'a'));
                });

                runner.test("with non-empty value smaller than the minimum length", (Test test) ->
                {
                    test.assertEqual("abzzz", Strings.padRight("ab", 5, 'z'));
                });

                runner.test("with non-empty value equal to the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", 3, 'z'));
                });

                runner.test("with non-empty value greater than the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", 1, 'z'));
                });

                runner.test("with negative minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", -1, 'z'));
                });

                runner.test("with zero minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", 0, 'z'));
                });
            });

            runner.testGroup("isOneOf(String,String...)", () ->
            {
                runner.test("with null and null String[]", (Test test) ->
                {
                    test.assertThrows(() -> Strings.isOneOf(null, (String[])null));
                });

                runner.test("with null and null String", (Test test) ->
                {
                    test.assertTrue(Strings.isOneOf(null, (String)null));
                });
            });

            runner.testGroup("getWords(String)", () ->
            {
                final Action2<String,Iterable<String>> getWordsTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.getWords(value));
                    });
                };

                getWordsTest.run(null, Iterable.empty());
                getWordsTest.run("", Iterable.empty());
                getWordsTest.run("     ", Iterable.empty());
                getWordsTest.run("./\\\"*", Iterable.empty());
                getWordsTest.run("a", Iterable.create("a"));
                getWordsTest.run("abc", Iterable.create("abc"));
                getWordsTest.run("a.a", Iterable.create("a"));
                getWordsTest.run("a a", Iterable.create("a"));
                getWordsTest.run("a b", Iterable.create("a", "b"));
                getWordsTest.run("a1 b", Iterable.create("a1", "b"));
            });
        });
    }
}
