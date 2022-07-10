package qub;

public interface CharactersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Characters.class, () ->
        {
            runner.testGroup("toString(char)", () ->
            {
                final Action2<Character,String> toStringTest = (Character character, String expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expected, Characters.toString(character));
                    });
                };

                toStringTest.run('a', "a");
                toStringTest.run('A', "A");
            });

            runner.testGroup("isQuote(char)", () ->
            {
                final Action2<Character,Boolean> isQuoteTest = (Character character, Boolean expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expected, Characters.isQuote(character));
                    });
                };

                isQuoteTest.run('a', false);
                isQuoteTest.run('A', false);
                isQuoteTest.run('"', true);
                isQuoteTest.run('\'', true);
                isQuoteTest.run('`', false);
                isQuoteTest.run('0', false);
            });

            runner.testGroup("isWhitespace(char)", () ->
            {
                final Action2<Character,Boolean> isWhitespaceTest = (Character character, Boolean expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expected, Characters.isWhitespace(character));
                    });
                };

                isWhitespaceTest.run('a', false);
                isWhitespaceTest.run('A', false);
                isWhitespaceTest.run('"', false);
                isWhitespaceTest.run('\'', false);
                isWhitespaceTest.run('`', false);
                isWhitespaceTest.run('0', false);
                isWhitespaceTest.run(' ', true);
                isWhitespaceTest.run('\n', true);
                isWhitespaceTest.run('\r', true);
                isWhitespaceTest.run('\t', true);
                isWhitespaceTest.run('\f', false);
                isWhitespaceTest.run('\b', false);
                isWhitespaceTest.run('\0', false);
            });

            runner.testGroup("escapeAndQuote(char)", () ->
            {
                final Action2<Character,String> escapeAndQuoteTest = (Character character, String expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expected, Characters.escapeAndQuote(character));
                    });
                };

                escapeAndQuoteTest.run('a', "\"a\"");
                escapeAndQuoteTest.run('A', "\"A\"");
                escapeAndQuoteTest.run(' ', "\" \"");
                escapeAndQuoteTest.run('\b', "\"\\b\"");
                escapeAndQuoteTest.run('\f', "\"\\f\"");
                escapeAndQuoteTest.run('\n', "\"\\n\"");
                escapeAndQuoteTest.run('\r', "\"\\r\"");
                escapeAndQuoteTest.run('\t', "\"\\t\"");
                escapeAndQuoteTest.run('\'', "\"'\"");
                escapeAndQuoteTest.run('\"', "\"\\\"\"");
                escapeAndQuoteTest.run((char)0xD800, "\"\\u+D800\"");
                escapeAndQuoteTest.run((char)0xD801, "\"\\u+D801\"");
                escapeAndQuoteTest.run((char)0xDFFE, "\"\\u+DFFE\"");
                escapeAndQuoteTest.run((char)0xDFFF, "\"\\u+DFFF\"");
            });

            runner.testGroup("escape(char)", () ->
            {
                final Action2<Character,String> escapeTest = (Character character, String expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expected, Characters.escape(character.charValue()));
                    });
                };

                escapeTest.run('a', "a");
                escapeTest.run('A', "A");
                escapeTest.run(' ', " ");
                escapeTest.run('\b', "\\b");
                escapeTest.run('\f', "\\f");
                escapeTest.run('\n', "\\n");
                escapeTest.run('\r', "\\r");
                escapeTest.run('\t', "\\t");
                escapeTest.run('\'', "'");
                escapeTest.run('\"', "\\\"");
                escapeTest.run((char)0xD800, "\\u+D800");
                escapeTest.run((char)0xD801, "\\u+D801");
                escapeTest.run((char)0xDFFE, "\\u+DFFE");
                escapeTest.run((char)0xDFFF, "\\u+DFFF");

                runner.testGroup("speed tests", () ->
                {
                    final Action4<Character,Integer,Duration,String> escapeSpeedTest = (Character character, Integer iterations, Duration maximumDuration, String expected) ->
                    {
                        runner.speedTest("with " + Characters.escapeAndQuote(character) + " " + iterations + " times", maximumDuration, (Test test) ->
                        {
                            final int iterationsInt = iterations.intValue();
                            for (int i = 0; i < iterationsInt; i++)
                            {
                                test.assertEqual(expected, Characters.escape(character.charValue()));
                            }
                        });
                    };

                    escapeSpeedTest.run('a', 5000000, Duration.seconds(0.25), "a");
                    escapeSpeedTest.run('\t', 5000000, Duration.seconds(0.25), "\\t");
                    escapeSpeedTest.run('\'', 5000000, Duration.seconds(0.25), "'");
                    escapeSpeedTest.run((char)0xD800, 4000000, Duration.seconds(0.4), "\\u+D800");
                });
            });

            runner.testGroup("escape(Character)", () ->
            {
                final Action2<Character,String> escapeTest = (Character character, String expected) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(character), (Test test) ->
                    {
                        test.assertEqual(expected, Characters.escape(character));
                    });
                };

                escapeTest.run(null, "null");
                escapeTest.run('a', "a");
                escapeTest.run('A', "A");
                escapeTest.run(' ', " ");
                escapeTest.run('\b', "\\b");
                escapeTest.run('\f', "\\f");
                escapeTest.run('\n', "\\n");
                escapeTest.run('\r', "\\r");
                escapeTest.run('\t', "\\t");
                escapeTest.run('\'', "'");
                escapeTest.run('\"', "\\\"");
                escapeTest.run((char)0xD800, "\\u+D800");
                escapeTest.run((char)0xD801, "\\u+D801");
                escapeTest.run((char)0xDFFE, "\\u+DFFE");
                escapeTest.run((char)0xDFFF, "\\u+DFFF");
            });

            runner.testGroup("unescapeNextCharacter(SaveableIterator<Character>)", () ->
            {
                final Action3<String,Throwable,String> unescapeNextCharacterErrorTest = (String characters, Throwable expected, String expectedRemainder) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(characters), (Test test) ->
                    {
                        final SaveableIterator<Character> characterIterator = characters == null ? null : SaveableIterator.create(Strings.iterate(characters).start());
                        test.assertThrows(() -> Characters.unescapeNextCharacter(characterIterator),
                            expected);
                        if (characterIterator != null)
                        {
                            test.assertEqual(expectedRemainder, Characters.join(characterIterator));
                        }
                    });
                };

                unescapeNextCharacterErrorTest.run(null, new PreConditionFailure("characters cannot be null."), "");
                unescapeNextCharacterErrorTest.run("", new PreConditionFailure("characters.hasCurrent() cannot be false."), "");

                final Action3<String,Character,String> unescapeNextCharacterTest = (String characters, Character expected, String expectedRemainder) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(characters), (Test test) ->
                    {
                        final SaveableIterator<Character> characterIterator = SaveableIterator.create(Strings.iterate(characters).start());
                        test.assertEqual(expected, Characters.unescapeNextCharacter(characterIterator));
                        test.assertEqual(expectedRemainder, Characters.join(characterIterator));
                    });
                };

                unescapeNextCharacterTest.run("a", 'a', "");
                unescapeNextCharacterTest.run("b\\n", 'b', "\\n");
                unescapeNextCharacterTest.run("\\b", '\b', "");
                unescapeNextCharacterTest.run("\b", '\b', "");
                unescapeNextCharacterTest.run("\\f", '\f', "");
                unescapeNextCharacterTest.run("\f", '\f', "");
                unescapeNextCharacterTest.run("\\n", '\n', "");
                unescapeNextCharacterTest.run("\n", '\n', "");
                unescapeNextCharacterTest.run("\\r", '\r', "");
                unescapeNextCharacterTest.run("\r", '\r', "");
                unescapeNextCharacterTest.run("\\t", '\t', "");
                unescapeNextCharacterTest.run("\t", '\t', "");
                unescapeNextCharacterTest.run("\\u", '\\', "u");
                unescapeNextCharacterTest.run("\\u-", '\\', "u-");
                unescapeNextCharacterTest.run("\\u+", '\\', "u+");
                unescapeNextCharacterTest.run("\\u+D", '\\', "u+D");
                unescapeNextCharacterTest.run("\\u+D800", (char)0xD800, "");
                unescapeNextCharacterTest.run((char)0xD800 + "", (char)0xD800, "");
                unescapeNextCharacterTest.run("\\u+DF23", (char)0xDF23, "");
                unescapeNextCharacterTest.run((char)0xDF23 + "", (char)0xDF23, "");
                unescapeNextCharacterTest.run("\\u+DF235", (char)0xDF23, "5");
                unescapeNextCharacterTest.run("\\'", '\'', "");
                unescapeNextCharacterTest.run("'", '\'', "");
                unescapeNextCharacterTest.run("\\\"", '\"', "");
                unescapeNextCharacterTest.run("\"", '\"', "");
                unescapeNextCharacterTest.run("\\\\", '\\', "");
                unescapeNextCharacterTest.run("\\", '\\', "");
                unescapeNextCharacterTest.run("\\h", '\\', "h");
            });

            runner.testGroup("join(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Characters.join((Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Characters.join(Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc", Characters.join(Iterable.create('a', 'b', 'c')));
                });
            });

            runner.testGroup("join(char,Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Characters.join(' ', (Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Characters.join(' ', Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("a b c", Characters.join(' ', Iterable.create('a', 'b', 'c')));
                });
            });

            runner.testGroup("join(String,Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Characters.join("--", (Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Characters.join("--", Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("a--b--c", Characters.join("--", Iterable.create('a', 'b', 'c')));
                });
            });
        });
    }
}
