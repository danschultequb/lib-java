package qub;

public interface StringsTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Strings.class, () ->
        {
            runner.testGroup("iterable(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual(Iterable.create(), Strings.iterable((String)null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(Iterable.create(), Strings.iterable(""));
                });

                runner.test("with one character", (Test test) ->
                {
                    test.assertEqual(Iterable.create('a'), Strings.iterable("a"));
                });

                runner.test("with two characters", (Test test) ->
                {
                    test.assertEqual(Iterable.create('a', 'b'), Strings.iterable("ab"));
                });
            });

            runner.testGroup("startsWith(String,String)", () ->
            {
                final Action3<String,String,Boolean> startsWithTest = (String value, String prefix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(value, prefix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.startsWith(value, prefix));
                    });
                };

                startsWithTest.run(null, null, false);
                startsWithTest.run(null, "", false);
                startsWithTest.run(null, "a", false);
                startsWithTest.run(null, "abc", false);

                startsWithTest.run("", null, false);
                startsWithTest.run("", "", false);
                startsWithTest.run("", "a", false);
                startsWithTest.run("", "abc", false);

                startsWithTest.run("a", null, false);
                startsWithTest.run("a", "", false);
                startsWithTest.run("a", "a", true);
                startsWithTest.run("a", "A", false);
                startsWithTest.run("a", "abc", false);

                startsWithTest.run("abcdef", null, false);
                startsWithTest.run("abcdef", "", false);
                startsWithTest.run("abcdef", "a", true);
                startsWithTest.run("abcdef", "A", false);
                startsWithTest.run("abcdef", "abc", true);
            });

            runner.testGroup("startsWith(String,String,CharacterComparer)", () ->
            {
                runner.test("with null characterComparer", (Test test) ->
                {
                    test.assertThrows(() -> Strings.startsWith("a", "b", null),
                        new PreConditionFailure("characterComparer cannot be null."));
                });

                runner.testGroup("with CharacterComparer.Exact", () ->
                {
                    final Action3<String,String,Boolean> startsWithTest = (String value, String prefix, Boolean expected) ->
                    {
                        runner.test("with " + English.andList(Iterable.create(value, prefix).map(Strings::escapeAndQuote)), (Test test) ->
                        {
                            test.assertEqual(expected, Strings.startsWith(value, prefix, CharacterComparer.Exact));
                        });
                    };

                    startsWithTest.run(null, null, false);
                    startsWithTest.run(null, "", false);
                    startsWithTest.run(null, "a", false);
                    startsWithTest.run(null, "abc", false);

                    startsWithTest.run("", null, false);
                    startsWithTest.run("", "", false);
                    startsWithTest.run("", "a", false);
                    startsWithTest.run("", "abc", false);

                    startsWithTest.run("a", null, false);
                    startsWithTest.run("a", "", false);
                    startsWithTest.run("a", "a", true);
                    startsWithTest.run("a", "A", false);
                    startsWithTest.run("a", "abc", false);

                    startsWithTest.run("abcdef", null, false);
                    startsWithTest.run("abcdef", "", false);
                    startsWithTest.run("abcdef", "a", true);
                    startsWithTest.run("abcdef", "A", false);
                    startsWithTest.run("abcdef", "abc", true);
                });

                runner.testGroup("with CharacterComparer.CaseInsensitive", () ->
                {
                    final Action3<String,String,Boolean> startsWithTest = (String value, String prefix, Boolean expected) ->
                    {
                        runner.test("with " + English.andList(Iterable.create(value, prefix).map(Strings::escapeAndQuote)), (Test test) ->
                        {
                            test.assertEqual(expected, Strings.startsWith(value, prefix, CharacterComparer.CaseInsensitive));
                        });
                    };

                    startsWithTest.run(null, null, false);
                    startsWithTest.run(null, "", false);
                    startsWithTest.run(null, "a", false);
                    startsWithTest.run(null, "abc", false);

                    startsWithTest.run("", null, false);
                    startsWithTest.run("", "", false);
                    startsWithTest.run("", "a", false);
                    startsWithTest.run("", "abc", false);

                    startsWithTest.run("a", null, false);
                    startsWithTest.run("a", "", false);
                    startsWithTest.run("a", "a", true);
                    startsWithTest.run("a", "A", true);
                    startsWithTest.run("a", "abc", false);

                    startsWithTest.run("abcdef", null, false);
                    startsWithTest.run("abcdef", "", false);
                    startsWithTest.run("abcdef", "a", true);
                    startsWithTest.run("abcdef", "A", true);
                    startsWithTest.run("abcdef", "abc", true);
                });
            });

            runner.testGroup("endsWith(String,char)", () ->
            {
                final Action3<String,String,Throwable> endsWithErrorTest = (String text, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.endsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, "b", new PreConditionFailure("text cannot be null."));

                final Action3<String,Character,Boolean> endsWithTest = (String text, Character suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.endsWith(text, suffix));
                    });
                };

                endsWithTest.run("", 'b', false);
                endsWithTest.run("a", 'b', false);
                endsWithTest.run("b", 'b', true);
                endsWithTest.run("ab", 'b', true);
            });

            runner.testGroup("endsWith(String,String)", () ->
            {
                final Action3<String,String,Throwable> endsWithErrorTest = (String text, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.endsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, "b", new PreConditionFailure("text cannot be null."));
                endsWithErrorTest.run("a", null, new PreConditionFailure("suffix cannot be null."));
                endsWithErrorTest.run("a", "", new PreConditionFailure("suffix cannot be empty."));

                final Action3<String,String,Boolean> endsWithTest = (String text, String suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.endsWith(text, suffix));
                    });
                };

                endsWithTest.run("", "b", false);
                endsWithTest.run("a", "b", false);
                endsWithTest.run("b", "b", true);
                endsWithTest.run("ab", "b", true);
                endsWithTest.run("ab", "bc", false);
                endsWithTest.run("ac", "bc", false);
                endsWithTest.run("abc", "bc", true);
            });

            runner.testGroup("ensureEndsWith(String,char)", () ->
            {
                final Action3<String,Character,Throwable> endsWithErrorTest = (String text, Character suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.ensureEndsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, 'b', new PreConditionFailure("text cannot be null."));

                final Action3<String,Character,String> ensureEndsWithTest = (String text, Character suffix, String expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.ensureEndsWith(text, suffix));
                    });
                };

                ensureEndsWithTest.run("", 'b', "b");
                ensureEndsWithTest.run("a", 'b', "ab");
                ensureEndsWithTest.run("b", 'b', "b");
                ensureEndsWithTest.run("ab", 'b', "ab");
            });

            runner.testGroup("ensureEndsWith(String,String)", () ->
            {
                final Action3<String,String,Throwable> endsWithErrorTest = (String text, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.ensureEndsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, "b", new PreConditionFailure("text cannot be null."));
                endsWithErrorTest.run("a", null, new PreConditionFailure("suffix cannot be null."));
                endsWithErrorTest.run("a", "", new PreConditionFailure("suffix cannot be empty."));

                final Action3<String,String,String> ensureEndsWithTest = (String text, String suffix, String expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.ensureEndsWith(text, suffix));
                    });
                };

                ensureEndsWithTest.run("", "b", "b");
                ensureEndsWithTest.run("a", "b", "ab");
                ensureEndsWithTest.run("b", "b", "b");
                ensureEndsWithTest.run("ab", "b", "ab");
                ensureEndsWithTest.run("ab", "bc", "abbc");
                ensureEndsWithTest.run("ac", "bc", "acbc");
                ensureEndsWithTest.run("abc", "bc", "abc");
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
                escapeTest.run("\b\f\n\r\t'\"", "\\b\\f\\n\\r\\t'\\\"");
            });

            runner.testGroup("unescape(String)", () ->
            {
                final Action2<String,String> unescapeTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.unescape(text));
                    });
                };

                unescapeTest.run(null, null);
                unescapeTest.run("", "");
                unescapeTest.run("abc", "abc");
                unescapeTest.run("\b\f\n\r\t'\"", "\b\f\n\r\t'\"");
                unescapeTest.run("\\b\\f\\n\\r\\t'\\\"", "\b\f\n\r\t'\"");
            });

            runner.testGroup("isQuoted(String)", () ->
            {
                final Action2<String,Boolean> isQuotedTest = (String text, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.isQuoted(text));
                    });
                };

                isQuotedTest.run(null, false);
                isQuotedTest.run("", false);
                isQuotedTest.run("\"\"", true);
                isQuotedTest.run("''", true);
                isQuotedTest.run("``", false);
                isQuotedTest.run("hello", false);
                isQuotedTest.run("Todd's", false);
                isQuotedTest.run("\"hey", false);
                isQuotedTest.run("hey\"", false);
                isQuotedTest.run("\"hey'", false);
                isQuotedTest.run("\"hey\"", true);
                isQuotedTest.run("'hey\"", false);
                isQuotedTest.run("'hey'", true);
                isQuotedTest.run("\"hey\\\"", false);
                isQuotedTest.run("\"hey\\\\\"", true);
                isQuotedTest.run("\"hey\\\\\\\"", false);
                isQuotedTest.run("\\\"hey\"", false);
            });

            runner.testGroup("unquote(String)", () ->
            {
                final Action2<String,String> unquoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.unquote(text));
                    });
                };

                unquoteTest.run(null, null);
                unquoteTest.run("", "");
                unquoteTest.run("\"\"", "");
                unquoteTest.run("''", "");
                unquoteTest.run("``", "``");
                unquoteTest.run("hello", "hello");
                unquoteTest.run("Todd's", "Todd's");
                unquoteTest.run("\"hey", "\"hey");
                unquoteTest.run("hey\"", "hey\"");
                unquoteTest.run("\"hey'", "\"hey'");
                unquoteTest.run("\"hey\"", "hey");
                unquoteTest.run("'hey\"", "'hey\"");
                unquoteTest.run("'hey'", "hey");
                unquoteTest.run("\"hey\\\"", "\"hey\\\"");
                unquoteTest.run("\"hey\\\\\"", "hey\\\\");
                unquoteTest.run("\"hey\\\\\\\"", "\"hey\\\\\\\"");
                unquoteTest.run("\\\"hey\"", "\\\"hey\"");
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

            runner.testGroup("unescapeAndUnquote(String)", () ->
            {
                final Action2<String,String> unescapeAndUnquoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.unescapeAndUnquote(text));
                    });
                };

                unescapeAndUnquoteTest.run(null, null);
                unescapeAndUnquoteTest.run("", "");
                unescapeAndUnquoteTest.run("\"\"", "");
                unescapeAndUnquoteTest.run("abc", "abc");
                unescapeAndUnquoteTest.run("\"abc\"", "abc");
                unescapeAndUnquoteTest.run("\b\f\n\r\t", "\b\f\n\r\t");
                unescapeAndUnquoteTest.run("\"\\b\\f\\n\\r\\t\"", "\b\f\n\r\t");
                unescapeAndUnquoteTest.run("\"", "\"");
                unescapeAndUnquoteTest.run("\"\\\"\"", "\"");
            });

            runner.testGroup("repeat(String,int)", () ->
            {
                runner.test("with \"\" and 10", (Test test) ->
                {
                    test.assertEqual("", Strings.repeat("", 10));
                });

                runner.test("with \"a\" and -1", (Test test) ->
                {
                    test.assertThrows(() -> Strings.repeat("a", -1), new PreConditionFailure("repetitions (-1) must be greater than or equal to 0."));
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

            runner.testGroup("concatenate(String,String)", () ->
            {
                final Action3<String,String,String> concatenateTest = (String lhs, String rhs, String expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.concatenate(lhs, rhs));
                    });
                };

                concatenateTest.run(null, null, "");
                concatenateTest.run(null, "", "");
                concatenateTest.run(null, "def", "def");

                concatenateTest.run("", null, "");
                concatenateTest.run("", "", "");
                concatenateTest.run("", "def", "def");

                concatenateTest.run("abc", null, "abc");
                concatenateTest.run("abc", "", "abc");
                concatenateTest.run("abc", "def", "abcdef");
            });

            runner.testGroup("join(java.lang.Iterable<String>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join(null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join(Iterable.create()));
                });

                runner.test("with non-empty values", (Test test) ->
                {
                    test.assertEqual("abc", Strings.join(Iterable.create("a", "b", "c")));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("ac", Strings.join(Iterable.create("a", null, "c")));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("ac", Strings.join(Iterable.create("a", "", "c")));
                });
            });

            runner.testGroup("join(char,java.lang.Iterable<String>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join('+', null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join('-', Iterable.create()));
                });

                runner.test("with non-empty values", (Test test) ->
                {
                    test.assertEqual("a+b+c", Strings.join('+', Iterable.create("a", "b", "c")));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("a--c", Strings.join('-', Iterable.create("a", null, "c")));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("a,,c", Strings.join(',', Iterable.create("a", "", "c")));
                });
            });

            runner.testGroup("join(String,java.lang.Iterable<String>)", () ->
            {
                runner.test("with null separator", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join(null, Iterable.create()),
                        new PreConditionFailure("separator cannot be null."));
                });

                runner.test("with empty separator and empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join("", Iterable.create()));
                });

                runner.test("with empty separator and non-empty values", (Test test) ->
                {
                    test.assertEqual("ab", Strings.join("", Iterable.create("a", "b")));
                });

                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join("test", null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join(" - ", Iterable.create()));
                });

                runner.test("with non-empty values", (Test test) ->
                {
                    test.assertEqual("a and b and c", Strings.join(" and ", Iterable.create("a", "b", "c")));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("a--c", Strings.join("-", Iterable.create("a", null, "c")));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("a,,c", Strings.join(",", Iterable.create("a", "", "c")));
                });
            });

            runner.testGroup("equal(String,String)", () ->
            {
                final Action3<String,String,Boolean> equalTest = (String lhs, String rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.equal(lhs, rhs));
                    });
                };

                equalTest.run(null, null, true);
                equalTest.run(null, "", false);
                equalTest.run(null, "a", false);
                equalTest.run(null, "A", false);
                equalTest.run(null, "abc", false);

                equalTest.run("", null, false);
                equalTest.run("", "", true);
                equalTest.run("", "a", false);
                equalTest.run("", "A", false);
                equalTest.run("", "abc", false);

                equalTest.run("a", null, false);
                equalTest.run("a", "", false);
                equalTest.run("a", "a", true);
                equalTest.run("a", "A", false);
                equalTest.run("a", "abc", false);

                equalTest.run("abc", null, false);
                equalTest.run("abc", "", false);
                equalTest.run("abc", "a", false);
                equalTest.run("abc", "A", false);
                equalTest.run("abc", "abc", true);
            });

            runner.testGroup("format(String,Object...)", () ->
            {
                final Action3<String,Object[],Throwable> formatErrorTest = (String formattedString, Object[] formattedStringArguments, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(formattedString), Array.toString(formattedStringArguments)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.format(formattedString, formattedStringArguments),
                            expected);
                    });
                };

                formatErrorTest.run("hello %f", new Object[] { 1 }, new java.util.IllegalFormatConversionException('f', java.lang.Integer.class));

                final Action3<String,Object[],String> formatTest = (String formattedString, Object[] formattedStringArguments, String expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(formattedString), Array.toString(formattedStringArguments)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.format(formattedString, formattedStringArguments));
                    });
                };

                formatTest.run(null, null, null);
                formatTest.run(null, new Object[0], null);
                formatTest.run(null, new Object[] { 1 }, null);
                formatTest.run("", null, "");
                formatTest.run("", new Object[0], "");
                formatTest.run("", new Object[] { 1 }, "");
                formatTest.run("hello", null, "hello");
                formatTest.run("hello", new Object[0], "hello");
                formatTest.run("hello", new Object[] { 1 }, "hello");
                formatTest.run("hello %d", null, "hello %d");
                formatTest.run("hello %d", new Object[0], "hello %d");
                formatTest.run("hello %d", new Object[] { 1 }, "hello 1");
                formatTest.run("hello %f", null, "hello %f");
                formatTest.run("hello %f", new Object[0], "hello %f");
                formatTest.run("hello %f", new Object[] { 1.2 }, "hello 1.200000");
                formatTest.run("hello %.3f", new Object[] { 1.2 }, "hello 1.200");
                formatTest.run("hello %s", null, "hello %s");
                formatTest.run("hello %s", new Object[0], "hello %s");
                formatTest.run("hello %s", new Object[] { "there" }, "hello there");
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
                    test.assertThrows(() -> Strings.isOneOf(null, (String[])null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null and null String", (Test test) ->
                {
                    test.assertTrue(Strings.isOneOf(null, (String)null));
                });
            });

            runner.testGroup("iterateWords(String)", () ->
            {
                final Action2<String,Iterable<String>> iterateWordsTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.iterateWords(value).toList());
                    });
                };

                iterateWordsTest.run(null, Iterable.create());
                iterateWordsTest.run("", Iterable.create());
                iterateWordsTest.run("     ", Iterable.create());
                iterateWordsTest.run("./\\\"*", Iterable.create());
                iterateWordsTest.run("a", Iterable.create("a"));
                iterateWordsTest.run("abc", Iterable.create("abc"));
                iterateWordsTest.run("a.a", Iterable.create("a", "a"));
                iterateWordsTest.run("Disposable.create()", Iterable.create("Disposable", "create"));
                iterateWordsTest.run("a a", Iterable.create("a", "a"));
                iterateWordsTest.run("a b", Iterable.create("a", "b"));
                iterateWordsTest.run("a1 b", Iterable.create("a1", "b"));
            });

            runner.testGroup("compare(String,String)", () ->
            {
                final Action3<String,String,Comparison> compareTest = (String lhs, String rhs, Comparison expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.compare(lhs, rhs));
                    });
                };

                compareTest.run(null, null, Comparison.Equal);
                compareTest.run(null, "", Comparison.LessThan);
                compareTest.run(null, "a", Comparison.LessThan);
                compareTest.run(null, "abc", Comparison.LessThan);
                compareTest.run(null, "defg", Comparison.LessThan);

                compareTest.run("", null, Comparison.GreaterThan);
                compareTest.run("", "", Comparison.Equal);
                compareTest.run("", "a", Comparison.LessThan);
                compareTest.run("", "abc", Comparison.LessThan);
                compareTest.run("", "defg", Comparison.LessThan);

                compareTest.run("a", null, Comparison.GreaterThan);
                compareTest.run("a", "", Comparison.GreaterThan);
                compareTest.run("a", "a", Comparison.Equal);
                compareTest.run("a", "abc", Comparison.LessThan);
                compareTest.run("a", "defg", Comparison.LessThan);

                compareTest.run("abc", null, Comparison.GreaterThan);
                compareTest.run("abc", "", Comparison.GreaterThan);
                compareTest.run("abc", "a", Comparison.GreaterThan);
                compareTest.run("abc", "abc", Comparison.Equal);
                compareTest.run("abc", "defg", Comparison.LessThan);
            });

            runner.testGroup("lessThan(String,String)", () ->
            {
                final Action3<String,String,Boolean> lessThanTest = (String lhs, String rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.lessThan(lhs, rhs));
                    });
                };

                lessThanTest.run(null, null, false);
                lessThanTest.run(null, "", true);
                lessThanTest.run(null, "a", true);
                lessThanTest.run(null, "abc", true);
                lessThanTest.run(null, "defg", true);

                lessThanTest.run("", null, false);
                lessThanTest.run("", "", false);
                lessThanTest.run("", "a", true);
                lessThanTest.run("", "abc", true);
                lessThanTest.run("", "defg", true);

                lessThanTest.run("a", null, false);
                lessThanTest.run("a", "", false);
                lessThanTest.run("a", "a", false);
                lessThanTest.run("a", "abc", true);
                lessThanTest.run("a", "defg", true);

                lessThanTest.run("abc", null, false);
                lessThanTest.run("abc", "", false);
                lessThanTest.run("abc", "a", false);
                lessThanTest.run("abc", "abc", false);
                lessThanTest.run("abc", "defg", true);
            });

            runner.testGroup("greaterThan(String,String)", () ->
            {
                final Action3<String,String,Boolean> greaterThanTest = (String lhs, String rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.greaterThan(lhs, rhs));
                    });
                };

                greaterThanTest.run(null, null, false);
                greaterThanTest.run(null, "", false);
                greaterThanTest.run(null, "a", false);
                greaterThanTest.run(null, "abc", false);
                greaterThanTest.run(null, "defg", false);

                greaterThanTest.run("", null, true);
                greaterThanTest.run("", "", false);
                greaterThanTest.run("", "a", false);
                greaterThanTest.run("", "abc", false);
                greaterThanTest.run("", "defg", false);

                greaterThanTest.run("a", null, true);
                greaterThanTest.run("a", "", true);
                greaterThanTest.run("a", "a", false);
                greaterThanTest.run("a", "abc", false);
                greaterThanTest.run("a", "defg", false);

                greaterThanTest.run("abc", null, true);
                greaterThanTest.run("abc", "", true);
                greaterThanTest.run("abc", "a", true);
                greaterThanTest.run("abc", "abc", false);
                greaterThanTest.run("abc", "defg", false);
            });

            runner.testGroup("iterateLines(String)", () ->
            {
                final Action2<String,Iterable<String>> iterateLinesTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.iterateLines(value).toList());
                    });
                };

                iterateLinesTest.run(null, Iterable.create());
                iterateLinesTest.run("", Iterable.create());
                iterateLinesTest.run("   ", Iterable.create("   "));
                iterateLinesTest.run("abcd", Iterable.create("abcd"));
                iterateLinesTest.run("\n\n\n", Iterable.create("", "", ""));
                iterateLinesTest.run("\r\n\n\r", Iterable.create("", "", "\r"));
                iterateLinesTest.run("a\nb\r\nc\rd", Iterable.create("a", "b", "c\rd"));
            });

            runner.testGroup("iterateLines(String,boolean)", () ->
            {
                final Action3<String,Boolean,Iterable<String>> iterateLinesTest = (String value, Boolean includeNewLines, Iterable<String> expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), includeNewLines), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.iterateLines(value, includeNewLines).toList());
                    });
                };

                iterateLinesTest.run(null, false, Iterable.create());
                iterateLinesTest.run("", false, Iterable.create());
                iterateLinesTest.run("   ", false, Iterable.create("   "));
                iterateLinesTest.run("abcd", false, Iterable.create("abcd"));
                iterateLinesTest.run("\n\n\n", false, Iterable.create("", "", ""));
                iterateLinesTest.run("\r\n\n\r", false, Iterable.create("", "", "\r"));
                iterateLinesTest.run("a\nb\r\nc\rd", false, Iterable.create("a", "b", "c\rd"));

                iterateLinesTest.run(null, true, Iterable.create());
                iterateLinesTest.run("", true, Iterable.create());
                iterateLinesTest.run("   ", true, Iterable.create("   "));
                iterateLinesTest.run("abcd", true, Iterable.create("abcd"));
                iterateLinesTest.run("\n\n\n", true, Iterable.create("\n", "\n", "\n"));
                iterateLinesTest.run("\r\n\n\r", true, Iterable.create("\r\n", "\n", "\r"));
                iterateLinesTest.run("a\nb\r\nc\rd", true, Iterable.create("a\n", "b\r\n", "c\rd"));
            });

            runner.testGroup("iterateSubstrings(String,char...)", () ->
            {
                final Action3<String,Iterable<Character>,Iterable<String>> iterateSubstringsTest = (String value, Iterable<Character> separators, Iterable<String> expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), separators.map(Characters::escapeAndQuote)), (Test test) ->
                    {
                        final Iterator<String> iterateSubstringsResult = Strings.iterateSubstrings(value, Array.toCharArray(separators).await());
                        IteratorTests.assertIterator(test, iterateSubstringsResult, false, null);

                        test.assertEqual(expected, iterateSubstringsResult.toList());
                        IteratorTests.assertIterator(test, iterateSubstringsResult, true, null);
                    });
                };

                iterateSubstringsTest.run(null, Iterable.create(), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create(), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create(), Iterable.create("hello"));

                iterateSubstringsTest.run(null, Iterable.create('a'), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create('a'), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create('a'), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", Iterable.create('a'), Iterable.create("bc"));
                iterateSubstringsTest.run("abaca", Iterable.create('a'), Iterable.create("b", "c"));
                iterateSubstringsTest.run("a", Iterable.create('a'), Iterable.create());
                iterateSubstringsTest.run("aa", Iterable.create('a'), Iterable.create());
                iterateSubstringsTest.run("aaa", Iterable.create('a'), Iterable.create());
                iterateSubstringsTest.run("hello there my friend", Iterable.create('l', 'e', ' '), Iterable.create("h", "o", "th", "r", "my", "fri", "nd"));

                iterateSubstringsTest.run("a\nb\nc", Iterable.create('\r', '\n'), Iterable.create("a", "b", "c"));
                iterateSubstringsTest.run("\ra\n\nc\r", Iterable.create('\r', '\n'), Iterable.create("a", "c"));
            });

            runner.testGroup("iterateSubstrings(String,String...)", () ->
            {
                final Action3<String,Iterable<String>,Iterable<String>> iterateSubstringsTest = (String value, Iterable<String> separators, Iterable<String> expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), separators.map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final String[] separatorStrings = new String[separators.getCount()];
                        Array.toArray(separators, separatorStrings);
                        final Iterator<String> iterateSubstringsResult = Strings.iterateSubstrings(value, separatorStrings);
                        IteratorTests.assertIterator(test, iterateSubstringsResult, false, null);

                        test.assertEqual(expected, iterateSubstringsResult.toList());
                        IteratorTests.assertIterator(test, iterateSubstringsResult, true, null);
                    });
                };

                iterateSubstringsTest.run(null, Iterable.create(), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create(), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create(), Iterable.create("hello"));

                iterateSubstringsTest.run(null, Iterable.create((String)null), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create((String)null), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create((String)null), Iterable.create("hello"));

                iterateSubstringsTest.run(null, Iterable.create(""), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create(""), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create(""), Iterable.create("hello"));

                iterateSubstringsTest.run(null, Iterable.create("a"), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create("a"), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create("a"), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", Iterable.create("a"), Iterable.create("bc"));
                iterateSubstringsTest.run("abaca", Iterable.create("a"), Iterable.create("b", "c"));
                iterateSubstringsTest.run("a", Iterable.create("a"), Iterable.create());
                iterateSubstringsTest.run("aa", Iterable.create("a"), Iterable.create());
                iterateSubstringsTest.run("aaa", Iterable.create("a"), Iterable.create());
                iterateSubstringsTest.run("hello there my friend", Iterable.create("l", "e", " "), Iterable.create("h", "o", "th", "r", "my", "fri", "nd"));

                iterateSubstringsTest.run(null, Iterable.create("a", "a"), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create("a", "a"), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create("a", "a"), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", Iterable.create("a", "a"), Iterable.create("bc"));
                iterateSubstringsTest.run("abaca", Iterable.create("a", "a"), Iterable.create("b", "c"));
                iterateSubstringsTest.run("a", Iterable.create("a", "a"), Iterable.create());
                iterateSubstringsTest.run("aa", Iterable.create("a", "a"), Iterable.create());
                iterateSubstringsTest.run("aaa", Iterable.create("a", "a"), Iterable.create());
                iterateSubstringsTest.run("hello there my friend", Iterable.create("l", "e", " "), Iterable.create("h", "o", "th", "r", "my", "fri", "nd"));

                iterateSubstringsTest.run(null, Iterable.create("aa"), Iterable.create());
                iterateSubstringsTest.run("", Iterable.create("aa"), Iterable.create());
                iterateSubstringsTest.run("hello", Iterable.create("aa"), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", Iterable.create("aa"), Iterable.create("abc"));
                iterateSubstringsTest.run("abaca", Iterable.create("aa"), Iterable.create("abaca"));
                iterateSubstringsTest.run("a", Iterable.create("aa"), Iterable.create("a"));
                iterateSubstringsTest.run("aa", Iterable.create("aa"), Iterable.create());
                iterateSubstringsTest.run("aaa", Iterable.create("aa"), Iterable.create("a"));
                iterateSubstringsTest.run("hello there my friend", Iterable.create("ll", "ere", "ie"), Iterable.create("he", "o th", " my fr", "nd"));
                iterateSubstringsTest.run("hello there my friend", Iterable.create("l", "ere", "ie"), Iterable.create("he", "o th", " my fr", "nd"));

                iterateSubstringsTest.run("a\nb\nc\r\n", Iterable.create("\r", "\n", "\r\n"), Iterable.create("a", "b", "c"));
                iterateSubstringsTest.run("\ra\n\nc\rd\r\ne", Iterable.create("\r", "\n", "\r\n"), Iterable.create("a", "c", "d", "e"));
            });

            runner.testGroup("iterateSubstrings(String,IterateSubstringsOptions)", () ->
            {
                final Action3<String,IterateSubstringsOptions,Iterable<String>> iterateSubstringsTest = (String value, IterateSubstringsOptions options, Iterable<String> expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), options), (Test test) ->
                    {
                        final Iterator<String> iterateSubstringsResult = Strings.iterateSubstrings(value, options);
                        IteratorTests.assertIterator(test, iterateSubstringsResult, false, null);

                        test.assertEqual(expected, iterateSubstringsResult.toList());
                        IteratorTests.assertIterator(test, iterateSubstringsResult, true, null);
                    });
                };

                iterateSubstringsTest.run(null, IterateSubstringsOptions.create(), Iterable.create());
                iterateSubstringsTest.run("", IterateSubstringsOptions.create(), Iterable.create());
                iterateSubstringsTest.run("hello", IterateSubstringsOptions.create(), Iterable.create("hello"));

                iterateSubstringsTest.run(null, IterateSubstringsOptions.create().setSeparators("a"), Iterable.create());
                iterateSubstringsTest.run("", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create());
                iterateSubstringsTest.run("hello", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create("bc"));
                iterateSubstringsTest.run("abaca", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create("b", "c"));
                iterateSubstringsTest.run("a", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create());
                iterateSubstringsTest.run("aa", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create());
                iterateSubstringsTest.run("aaa", IterateSubstringsOptions.create().setSeparators("a"), Iterable.create());
                iterateSubstringsTest.run("hello there my friend", IterateSubstringsOptions.create().setSeparators("l", "e", " "), Iterable.create("h", "o", "th", "r", "my", "fri", "nd"));
                iterateSubstringsTest.run("a\nb\nc\r\n", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n"), Iterable.create("a", "b", "c"));
                iterateSubstringsTest.run("\ra\n\nc\rd\r\ne", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n"), Iterable.create("a", "c", "d", "e"));

                iterateSubstringsTest.run(null, IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create());
                iterateSubstringsTest.run("", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create());
                iterateSubstringsTest.run("hello", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create("", "bc"));
                iterateSubstringsTest.run("abaca", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create("", "b", "c", ""));
                iterateSubstringsTest.run("a", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create("", ""));
                iterateSubstringsTest.run("aa", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create("", "", ""));
                iterateSubstringsTest.run("aaa", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true), Iterable.create("", "", "", ""));
                iterateSubstringsTest.run("hello there my friend", IterateSubstringsOptions.create().setSeparators("l", "e", " ").setIncludeEmptySubstrings(true), Iterable.create("h", "", "", "o", "th", "r", "", "my", "fri", "nd"));
                iterateSubstringsTest.run("a\nb\nc\r\n", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n").setIncludeEmptySubstrings(true), Iterable.create("a", "b", "c", ""));
                iterateSubstringsTest.run("\ra\n\nc\rd\r\ne", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n").setIncludeEmptySubstrings(true), Iterable.create("", "a", "", "c", "d", "e"));

                iterateSubstringsTest.run(null, IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("hello", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create("bc"));
                iterateSubstringsTest.run("abaca", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create("ba", "ca"));
                iterateSubstringsTest.run("a", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("aa", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("aaa", IterateSubstringsOptions.create().setSeparators("a").setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("hello there my friend", IterateSubstringsOptions.create().setSeparators("l", "e", " ").setIncludeSeparators(true), Iterable.create("he", "o ", "the", "re", "my ", "frie", "nd"));
                iterateSubstringsTest.run("a\nb\nc\r\n", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n").setIncludeSeparators(true), Iterable.create("a\n", "b\n", "c\r\n"));
                iterateSubstringsTest.run("\ra\n\nc\rd\r\ne", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n").setIncludeSeparators(true), Iterable.create("a\n", "c\r", "d\r\n", "e"));

                iterateSubstringsTest.run(null, IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create());
                iterateSubstringsTest.run("hello", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("a", "bc"));
                iterateSubstringsTest.run("abaca", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("a", "ba", "ca", ""));
                iterateSubstringsTest.run("a", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("a", ""));
                iterateSubstringsTest.run("aa", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("a", "a", ""));
                iterateSubstringsTest.run("aaa", IterateSubstringsOptions.create().setSeparators("a").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("a", "a", "a", ""));
                iterateSubstringsTest.run("hello there my friend", IterateSubstringsOptions.create().setSeparators("l", "e", " ").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("he", "l", "l", "o ", "the", "re", " ", "my ", "frie", "nd"));
                iterateSubstringsTest.run("a\nb\nc\r\n", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("a\n", "b\n", "c\r\n", ""));
                iterateSubstringsTest.run("\ra\n\nc\rd\r\ne", IterateSubstringsOptions.create().setSeparators("\r", "\n", "\r\n").setIncludeEmptySubstrings(true).setIncludeSeparators(true), Iterable.create("\r", "a\n", "\n", "c\r", "d\r\n", "e"));

                iterateSubstringsTest.run(null, IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create());
                iterateSubstringsTest.run("", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create());
                iterateSubstringsTest.run("hello", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create("hello"));
                iterateSubstringsTest.run("abc", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create("abc"));
                iterateSubstringsTest.run("abaca", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create("abaca"));
                iterateSubstringsTest.run("a", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create("a"));
                iterateSubstringsTest.run("aa", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create());
                iterateSubstringsTest.run("aaa", IterateSubstringsOptions.create().setSeparators("aa"), Iterable.create("a"));
                iterateSubstringsTest.run("hello there my friend", IterateSubstringsOptions.create().setSeparators("ll", "ere", "ie"), Iterable.create("he", "o th", " my fr", "nd"));
                iterateSubstringsTest.run("hello there my friend", IterateSubstringsOptions.create().setSeparators("l", "ere", "ie"), Iterable.create("he", "o th", " my fr", "nd"));
            });

            runner.testGroup("toLowerCase(String)", () ->
            {
                final Action2<String,String> toLowerCaseTest = (String value, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.toLowerCase(value));
                    });
                };

                toLowerCaseTest.run(null, null);
                toLowerCaseTest.run("", "");
                toLowerCaseTest.run("a", "a");
                toLowerCaseTest.run("A", "a");
                toLowerCaseTest.run("b", "b");
                toLowerCaseTest.run("B", "b");
            });

            runner.testGroup("toUpperCase(String)", () ->
            {
                final Action2<String,String> toUpperCaseTest = (String value, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.toUpperCase(value));
                    });
                };

                toUpperCaseTest.run(null, null);
                toUpperCaseTest.run("", "");
                toUpperCaseTest.run("a", "A");
                toUpperCaseTest.run("A", "A");
                toUpperCaseTest.run("b", "B");
                toUpperCaseTest.run("B", "B");
            });
        });
    }
}
