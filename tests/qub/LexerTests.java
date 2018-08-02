package qub;

public class LexerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Lexer", () ->
        {
            runner.testGroup("next()", () ->
            {
                final Action2<String,Lex[]> nextTest = (String text, Lex[] expectedLexes) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final Lexer lexer = new Lexer(text);
                        assertLexer(test, lexer, false, null);

                        for (final Lex expectedLex : expectedLexes)
                        {
                            test.assertTrue(lexer.next());
                            assertLexer(test, lexer, true, expectedLex);
                        }

                        for (int i = 0; i < 2; ++i)
                        {
                            test.assertFalse(lexer.next());
                            assertLexer(test, lexer, true, null);
                        }
                    });
                };
                
                nextTest.run(null, new Lex[0]);
                nextTest.run("", new Lex[0]);

                nextTest.run("{", new Lex[] { Lex.leftCurlyBracket(0) });
                nextTest.run("}", new Lex[] { Lex.rightCurlyBracket(0) });
                nextTest.run("[", new Lex[] { Lex.leftSquareBracket(0) });
                nextTest.run("]", new Lex[] { Lex.rightSquareBracket(0) });
                nextTest.run("<", new Lex[] { Lex.leftAngleBracket(0) });
                nextTest.run(">", new Lex[] { Lex.rightAngleBracket(0) });
                nextTest.run("(", new Lex[] { Lex.leftParenthesis(0) });
                nextTest.run(")", new Lex[] { Lex.rightParenthesis(0) });
                nextTest.run("xyz", new Lex[] { Lex.letters("xyz", 0) });
                nextTest.run("\'", new Lex[] { Lex.singleQuote(0) });
                nextTest.run("\"", new Lex[] { Lex.doubleQuote(0) });
                nextTest.run("0123456789", new Lex[] { Lex.digits("0123456789", 0) });
                nextTest.run(",", new Lex[] { Lex.comma(0) });
                nextTest.run(":", new Lex[] { Lex.colon(0) });
                nextTest.run(";", new Lex[] { Lex.semicolon(0) });
                nextTest.run("!", new Lex[] { Lex.exclamationPoint(0) });
                nextTest.run("\\", new Lex[] { Lex.backslash(0) });
                nextTest.run("/", new Lex[] { Lex.forwardSlash(0) });
                nextTest.run("?", new Lex[] { Lex.questionMark(0) });
                nextTest.run("-", new Lex[] { Lex.dash(0) });
                nextTest.run("+", new Lex[] { Lex.plus(0) });
                nextTest.run("=", new Lex[] { Lex.equalsSign(0) });
                nextTest.run(".", new Lex[] { Lex.period(0) });
                nextTest.run("_", new Lex[] { Lex.underscore(0) });
                nextTest.run("&", new Lex[] { Lex.ampersand(0) });
                nextTest.run("|", new Lex[] { Lex.verticalBar(0) });
                nextTest.run(" ", new Lex[] { Lex.space(0) });
                nextTest.run("\t", new Lex[] { Lex.tab(0) });
                nextTest.run("\r", new Lex[] { Lex.carriageReturn(0) });
                nextTest.run("\n", new Lex[] { Lex.newLine(0) });
                nextTest.run("\r\n", new Lex[] { Lex.carriageReturnNewLine(0) });
                nextTest.run("\r ", new Lex[] { Lex.carriageReturn(0), Lex.space(1) });
                nextTest.run("*", new Lex[] { Lex.asterisk(0) });
                nextTest.run("%", new Lex[] { Lex.percent(0) });
                nextTest.run("#", new Lex[] { Lex.hash(0) });
                nextTest.run("^", new Lex[] { Lex.unrecognized('^', 0) });

                nextTest.run("abc123", new Lex[] { Lex.letters("abc", 0), Lex.digits("123", 3) });
                nextTest.run("5.6.7", new Lex[] { Lex.digits("5", 0), Lex.period(1), Lex.digits("6", 2), Lex.period(3), Lex.digits("7", 4) });
            });
        });
    }

    private static void assertLexer(Test test, Lexer lexer, boolean hasStarted, Lex current)
    {
        test.assertEqual(hasStarted, lexer.hasStarted());
        test.assertEqual(current != null, lexer.hasCurrent());
        test.assertEqual(current, lexer.getCurrent());
    }
}
