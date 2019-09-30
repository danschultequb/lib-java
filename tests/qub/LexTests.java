package qub;

public class LexTests
{
    public static void run(final TestRunner runner)
    {
        runner.testGroup(Lex.class, () ->
        {
            runner.test("constructor", (Test test) ->
            {
                final Lex lex = new Lex("LexText", 20, LexType.Letters);
                test.assertEqual("LexText", lex.toString());
                test.assertEqual(20, lex.getStartIndex());
                test.assertEqual(LexType.Letters, lex.getType());
            });

            runner.testGroup("getLength()", () ->
            {
                final Action1<String> getLengthTest = text ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final Lex lex = getLex(text);
                        final int expectedLength = text == null ? 0 : text.length();
                        final int actualLength = lex.getLength();
                        test.assertEqual(expectedLength, actualLength);
                    });
                };

                getLengthTest.run(null);
                getLengthTest.run("");
                getLengthTest.run("123");
            });

            runner.testGroup("getAfterEndIndex()", () ->
            {
                final Action2<String,Integer> getAfterEndIndexTest = (String text, Integer startIndex) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + startIndex, (Test test) ->
                    {
                        final Lex lex = getLex(text, startIndex);
                        final int expectedAfterEndIndex = startIndex + (text == null ? 0 : text.length());
                        final int actualAfterEndIndex = lex.getAfterEndIndex();
                        test.assertEqual(expectedAfterEndIndex, actualAfterEndIndex);
                    });
                };

                getAfterEndIndexTest.run(null, -10);
                getAfterEndIndexTest.run(null, 0);
                getAfterEndIndexTest.run(null, 3);
                getAfterEndIndexTest.run("", -9);
                getAfterEndIndexTest.run("", 0);
                getAfterEndIndexTest.run("", 4);
                getAfterEndIndexTest.run("testing", -8);
                getAfterEndIndexTest.run("testing", 0);
                getAfterEndIndexTest.run("testing", 5);
            });

            runner.testGroup("getEndIndex()", () ->
            {
                final Action2<String,Integer> getEndIndexTest = (String text, Integer startIndex) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + startIndex, (Test test) ->
                    {
                        final Lex lex = getLex(text, startIndex);
                        final int expectedEndIndex = startIndex + (text == null || text.length() <= 1 ? 0 : text.length() - 1);
                        final int actualEndIndex = lex.getEndIndex();
                        test.assertEqual(expectedEndIndex, actualEndIndex);
                    });
                };

                getEndIndexTest.run(null, -5);
                getEndIndexTest.run(null, 0);
                getEndIndexTest.run(null, 15);
                getEndIndexTest.run("", -6);
                getEndIndexTest.run("", 0);
                getEndIndexTest.run("", 16);
                getEndIndexTest.run("a", -7);
                getEndIndexTest.run("a", 0);
                getEndIndexTest.run("a", 17);
                getEndIndexTest.run("ab", -8);
                getEndIndexTest.run("ab", 0);
                getEndIndexTest.run("ab", 18);
            });

            runner.testGroup("getSpan()", () ->
            {
                final Action2<String,Integer> getSpanTest = (String text, Integer startIndex) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + startIndex, (Test test) ->
                    {
                        final Lex lex = getLex(text, startIndex);
                        final Span expectedSpan = new Span(startIndex, text == null ? 0 : text.length());
                        final Span actualSpan = lex.getSpan();
                        test.assertEqual(expectedSpan, actualSpan);
                    });
                };

                getSpanTest.run(null, -20);
                getSpanTest.run(null, 0);
                getSpanTest.run(null, 2);
                getSpanTest.run("", -30);
                getSpanTest.run("", 0);
                getSpanTest.run("", 3);
                getSpanTest.run("grapes", -40);
                getSpanTest.run("grapes", 0);
                getSpanTest.run("grapes", 4);
            });

            runner.test("equals()", (Test test) ->
            {
                final Lex lex = Lex.space(17);
                test.assertFalse(lex.equals((Object)null));
                test.assertFalse(lex.equals((Lex)null));
                test.assertFalse(lex.equals("lex"));
                test.assertFalse(lex.equals(Lex.leftCurlyBracket(17)));
                test.assertFalse(lex.equals(Lex.space(18)));
                test.assertTrue(lex.equals(lex));
                test.assertTrue(lex.equals(Lex.space(17)));
            });

            runner.testGroup("isWhitespace()", () ->
            {
                final Action2<LexType,Boolean> isWhitespaceTest = (LexType type, Boolean expected) ->
                {
                    runner.test("with " + type, (Test test) ->
                    {
                        final Lex lex = getLex(type);
                        test.assertEqual(expected, lex.isWhitespace());
                    });
                };

                isWhitespaceTest.run(LexType.LeftCurlyBracket, false);
                isWhitespaceTest.run(LexType.RightCurlyBracket, false);
                isWhitespaceTest.run(LexType.LeftSquareBracket, false);
                isWhitespaceTest.run(LexType.RightSquareBracket, false);
                isWhitespaceTest.run(LexType.LeftAngleBracket, false);
                isWhitespaceTest.run(LexType.RightAngleBracket, false);
                isWhitespaceTest.run(LexType.LeftParenthesis, false);
                isWhitespaceTest.run(LexType.RightParenthesis, false);
                isWhitespaceTest.run(LexType.Letters, false);
                isWhitespaceTest.run(LexType.SingleQuote, false);
                isWhitespaceTest.run(LexType.DoubleQuote, false);
                isWhitespaceTest.run(LexType.Digits, false);
                isWhitespaceTest.run(LexType.Comma, false);
                isWhitespaceTest.run(LexType.Colon, false);
                isWhitespaceTest.run(LexType.Semicolon, false);
                isWhitespaceTest.run(LexType.ExclamationPoint, false);
                isWhitespaceTest.run(LexType.Backslash, false);
                isWhitespaceTest.run(LexType.ForwardSlash, false);
                isWhitespaceTest.run(LexType.QuestionMark, false);
                isWhitespaceTest.run(LexType.Dash, false);
                isWhitespaceTest.run(LexType.Plus, false);
                isWhitespaceTest.run(LexType.EqualsSign, false);
                isWhitespaceTest.run(LexType.Period, false);
                isWhitespaceTest.run(LexType.Underscore, false);
                isWhitespaceTest.run(LexType.Ampersand, false);
                isWhitespaceTest.run(LexType.VerticalBar, false);
                isWhitespaceTest.run(LexType.Space, true);
                isWhitespaceTest.run(LexType.Tab, true);
                isWhitespaceTest.run(LexType.CarriageReturn, true);
                isWhitespaceTest.run(LexType.NewLine, false);
                isWhitespaceTest.run(LexType.CarriageReturnNewLine, false);
                isWhitespaceTest.run(LexType.Asterisk, false);
                isWhitespaceTest.run(LexType.Percent, false);
                isWhitespaceTest.run(LexType.Hash, false);
                isWhitespaceTest.run(LexType.Unrecognized, false);
            });

            runner.testGroup("isNewLine()", () ->
            {
                final Action2<LexType,Boolean> isNewLineTest = (LexType type, Boolean expected) ->
                {
                    runner.test("with " + type, (Test test) ->
                    {
                        final Lex lex = getLex(type);
                        test.assertEqual(expected, lex.isNewLine());
                    });
                };

                isNewLineTest.run(LexType.LeftCurlyBracket, false);
                isNewLineTest.run(LexType.RightCurlyBracket, false);
                isNewLineTest.run(LexType.LeftSquareBracket, false);
                isNewLineTest.run(LexType.RightSquareBracket, false);
                isNewLineTest.run(LexType.LeftAngleBracket, false);
                isNewLineTest.run(LexType.RightAngleBracket, false);
                isNewLineTest.run(LexType.LeftParenthesis, false);
                isNewLineTest.run(LexType.RightParenthesis, false);
                isNewLineTest.run(LexType.Letters, false);
                isNewLineTest.run(LexType.SingleQuote, false);
                isNewLineTest.run(LexType.DoubleQuote, false);
                isNewLineTest.run(LexType.Digits, false);
                isNewLineTest.run(LexType.Comma, false);
                isNewLineTest.run(LexType.Colon, false);
                isNewLineTest.run(LexType.Semicolon, false);
                isNewLineTest.run(LexType.ExclamationPoint, false);
                isNewLineTest.run(LexType.Backslash, false);
                isNewLineTest.run(LexType.ForwardSlash, false);
                isNewLineTest.run(LexType.QuestionMark, false);
                isNewLineTest.run(LexType.Dash, false);
                isNewLineTest.run(LexType.Plus, false);
                isNewLineTest.run(LexType.EqualsSign, false);
                isNewLineTest.run(LexType.Period, false);
                isNewLineTest.run(LexType.Underscore, false);
                isNewLineTest.run(LexType.Ampersand, false);
                isNewLineTest.run(LexType.VerticalBar, false);
                isNewLineTest.run(LexType.Space, false);
                isNewLineTest.run(LexType.Tab, false);
                isNewLineTest.run(LexType.CarriageReturn, false);
                isNewLineTest.run(LexType.NewLine, true);
                isNewLineTest.run(LexType.CarriageReturnNewLine, true);
                isNewLineTest.run(LexType.Asterisk, false);
                isNewLineTest.run(LexType.Percent, false);
                isNewLineTest.run(LexType.Hash, false);
                isNewLineTest.run(LexType.Unrecognized, false);
            });

            runner.test("leftCurlyBracket()", (Test test) ->
            {
                assertLex(test, Lex.leftCurlyBracket(0), "{", 0, LexType.LeftCurlyBracket);
            });

            runner.test("rightCurlyBracket()", (Test test) ->
            {
                assertLex(test, Lex.rightCurlyBracket(1), "}", 1, LexType.RightCurlyBracket);
            });

            runner.test("leftSquareBracket()", (Test test) ->
            {
                assertLex(test, Lex.leftSquareBracket(2), "[", 2, LexType.LeftSquareBracket);
            });

            runner.test("rightSquareBracket()", (Test test) ->
            {
                assertLex(test, Lex.rightSquareBracket(3), "]", 3, LexType.RightSquareBracket);
            });

            runner.test("leftParenthesis()", (Test test) ->
            {
                assertLex(test, Lex.leftParenthesis(4), "(", 4, LexType.LeftParenthesis);
            });

            runner.test("rightParenthesis()", (Test test) ->
            {
                assertLex(test, Lex.rightParenthesis(5), ")", 5, LexType.RightParenthesis);
            });

            runner.test("leftAngleBracket()", (Test test) -> 
            {
                assertLex(test, Lex.leftAngleBracket(6), "<", 6, LexType.LeftAngleBracket);
            });

            runner.test("rightAngleBracket()", (Test test) ->
            {
                assertLex(test, Lex.rightAngleBracket(7), ">", 7, LexType.RightAngleBracket);
            });

            runner.test("doubleQuote()", (Test test) ->
            {
                assertLex(test, Lex.doubleQuote(8), "\"", 8, LexType.DoubleQuote);
            });

            runner.test("singleQuote()", (Test test) ->
            {
                assertLex(test, Lex.singleQuote(9), "\'", 9, LexType.SingleQuote);
            });

            runner.test("dash()", (Test test) ->
            {
                assertLex(test, Lex.dash(10), "-", 10, LexType.Dash);
            });

            runner.test("plus()", (Test test) ->
            {
                assertLex(test, Lex.plus(11), "+", 11, LexType.Plus);
            });

            runner.test("comma()", (Test test) ->
            {
                assertLex(test, Lex.comma(12), ",", 12, LexType.Comma);
            });

            runner.test("colon()", (Test test) ->
            {
                assertLex(test, Lex.colon(13), ":", 13, LexType.Colon);
            });

            runner.test("semicolon", (Test test) ->
            {
                assertLex(test, Lex.semicolon(14), ";", 14, LexType.Semicolon);
            });

            runner.test("exclamationPoint()", (Test test) ->
            {
                assertLex(test, Lex.exclamationPoint(15), "!", 15, LexType.ExclamationPoint);
            });

            runner.test("backslash()", (Test test) ->
            {
                assertLex(test, Lex.backslash(16), "\\", 16, LexType.Backslash);
            });

            runner.test("forwardSlash()", (Test test) ->
            {
                assertLex(test, Lex.forwardSlash(17), "/", 17, LexType.ForwardSlash);
            });

            runner.test("questionMark()", (Test test) ->
            {
                assertLex(test, Lex.questionMark(18), "?", 18, LexType.QuestionMark);
            });

            runner.test("equalsSign()", (Test test) ->
            {
                assertLex(test, Lex.equalsSign(19), "=", 19, LexType.EqualsSign);
            });

            runner.test("period()", (Test test) ->
            {
                assertLex(test, Lex.period(20), ".", 20, LexType.Period);
            });

            runner.test("underscore()", (Test test) ->
            {
                assertLex(test, Lex.underscore(21), "_", 21, LexType.Underscore);
            });

            runner.test("ampersand()", (Test test) ->
            {
                assertLex(test, Lex.ampersand(22), "&", 22, LexType.Ampersand);
            });

            runner.test("space()", (Test test) ->
            {
                assertLex(test, Lex.space(23), " ", 23, LexType.Space);
            });

            runner.test("tab()", (Test test) ->
            {
                assertLex(test, Lex.tab(24), "\t", 24, LexType.Tab);
            });

            runner.test("carriageReturn()", (Test test) ->
            {
                assertLex(test, Lex.carriageReturn(25), "\r", 25, LexType.CarriageReturn);
            });

            runner.test("carriageReturnNewLine()", (Test test) ->
            {
                assertLex(test, Lex.carriageReturnNewLine(26), "\r\n", 26, LexType.CarriageReturnNewLine);
            });

            runner.test("newLine()", (Test test) ->
            {
                assertLex(test, Lex.newLine(27), "\n", 27, LexType.NewLine);
            });

            runner.test("asterisk()", (Test test) ->
            {
                assertLex(test, Lex.asterisk(28), "*", 28, LexType.Asterisk);
            });

            runner.test("percent()", (Test test) ->
            {
                assertLex(test, Lex.percent(29), "%", 29, LexType.Percent);
            });

            runner.test("verticalBar()", (Test test) ->
            {
                assertLex(test, Lex.verticalBar(30), "|", 30, LexType.VerticalBar);
            });

            runner.test("hash()", (Test test) ->
            {
                assertLex(test, Lex.hash(31), "#", 31, LexType.Hash);
            });

            runner.test("letters()", (Test test) ->
            {
                assertLex(test, Lex.letters("abcdef", 32), "abcdef", 32, LexType.Letters);
            });

            runner.test("digits()", (Test test) ->
            {
                assertLex(test, Lex.digits("123456", 33), "123456", 33, LexType.Digits);
            });

            runner.test("unrecognized()", (Test test) ->
            {
                assertLex(test, Lex.unrecognized('$', 34), "$", 34, LexType.Unrecognized);
            });

            runner.test("isLetter()", (Test test) ->
            {
                test.assertFalse(Lex.isLetter('0'));
                test.assertFalse(Lex.isLetter('.'));

                test.assertTrue(Lex.isLetter('a'));
                test.assertTrue(Lex.isLetter('m'));
                test.assertTrue(Lex.isLetter('z'));
                test.assertTrue(Lex.isLetter('A'));
                test.assertTrue(Lex.isLetter('N'));
                test.assertTrue(Lex.isLetter('Z'));
            });

            runner.test("isDigit()", (Test test) ->
            {
                test.assertFalse(Lex.isDigit('a'));
                test.assertFalse(Lex.isDigit(' '));
                test.assertFalse(Lex.isDigit('-'));
                test.assertFalse(Lex.isDigit('.'));

                test.assertTrue(Lex.isDigit('0'));
                test.assertTrue(Lex.isDigit('1'));
                test.assertTrue(Lex.isDigit('2'));
                test.assertTrue(Lex.isDigit('3'));
                test.assertTrue(Lex.isDigit('4'));
                test.assertTrue(Lex.isDigit('5'));
                test.assertTrue(Lex.isDigit('6'));
                test.assertTrue(Lex.isDigit('7'));
                test.assertTrue(Lex.isDigit('8'));
                test.assertTrue(Lex.isDigit('9'));
            });
        });
    }

    private static void assertLex(Test test, Lex lex, String text, int startIndex, LexType type)
    {
        test.assertNotNull(lex);
        test.assertEqual(text, lex.toString());
        test.assertEqual(startIndex, lex.getStartIndex());
        test.assertEqual(type, lex.getType());
    }

    private static Lex getLex(String text)
    {
        return getLex(text, 15);
    }

    private static Lex getLex(String text, int startIndex)
    {
        return getLex(text, startIndex, LexType.Unrecognized);
    }

    private static Lex getLex(LexType type)
    {
        return getLex(null, 15, type);
    }

    private static Lex getLex(String text, int startIndex, LexType type)
    {
        return new Lex(text, startIndex, type);
    }
}
