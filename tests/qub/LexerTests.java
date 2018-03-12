package qub;

public class LexerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Lexer", () ->
        {
            runner.testGroup("single lex tests", () ->
            {
                final Action2<String,Lex> singleLexTest = (String text, Lex expectedLex) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final Lexer lexer = new Lexer(text);
                        assertLexer(test, lexer, false, null);

                        test.assertTrue(lexer.next());
                        assertLexer(test, lexer, true, expectedLex);

                        test.assertFalse(lexer.next());
                        assertLexer(test, lexer, true, null);
                    });
                };

                singleLexTest.run("{", Lex.leftCurlyBracket(0));
                singleLexTest.run("}", Lex.rightCurlyBracket(0));
                singleLexTest.run("[", Lex.leftSquareBracket(0));
                singleLexTest.run("]", Lex.rightSquareBracket(0));
                singleLexTest.run("<", Lex.leftAngleBracket(0));
                singleLexTest.run(">", Lex.rightAngleBracket(0));
                singleLexTest.run("(", Lex.leftParenthesis(0));
                singleLexTest.run(")", Lex.rightParenthesis(0));
                singleLexTest.run("xyz", Lex.letters("xyz", 0));
                singleLexTest.run("\'", Lex.singleQuote(0));
                singleLexTest.run("\"", Lex.doubleQuote(0));
                singleLexTest.run("0123456789", Lex.digits("0123456789", 0));
                singleLexTest.run(",", Lex.comma(0));
                singleLexTest.run(":", Lex.colon(0));
                singleLexTest.run(";", Lex.semicolon(0));
                singleLexTest.run("!", Lex.exclamationPoint(0));
                singleLexTest.run("\\", Lex.backslash(0));
                singleLexTest.run("/", Lex.forwardSlash(0));
                singleLexTest.run("?", Lex.questionMark(0));
                singleLexTest.run("-", Lex.dash(0));
                singleLexTest.run("+", Lex.plus(0));
                singleLexTest.run("=", Lex.equalsSign(0));
                singleLexTest.run(".", Lex.period(0));
                singleLexTest.run("_", Lex.underscore(0));
                singleLexTest.run("&", Lex.ampersand(0));
                singleLexTest.run("|", Lex.verticalBar(0));
                singleLexTest.run(" ", Lex.space(0));
                singleLexTest.run("\t", Lex.tab(0));
                singleLexTest.run("\r", Lex.carriageReturn(0));
                singleLexTest.run("\n", Lex.newLine(0));
                singleLexTest.run("\r\n", Lex.carriageReturnNewLine(0));
                singleLexTest.run("*", Lex.asterisk(0));
                singleLexTest.run("%", Lex.percent(0));
                singleLexTest.run("#", Lex.hash(0));
                singleLexTest.run("^", Lex.unrecognized('^', 0));
            });

            runner.test("with right curly bracket", (Test test) ->
            {
                final Lexer lexer = new Lexer("}");
                assertLexer(test, lexer, false, null);

                test.assertTrue(lexer.next());
                assertLexer(test, lexer, true, Lex.rightCurlyBracket(0));

                test.assertFalse(lexer.next());
                assertLexer(test, lexer, true, null);
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
