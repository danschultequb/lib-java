package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTests
{
    private static void singleLexTest(char character, Lex expectedLex)
    {
        singleLexTest(Character.toString(character), expectedLex);
    }

    private static void singleLexTest(String text, Lex expectedLex)
    {
        final Lexer lexer = new Lexer(text);
        assertLexer(lexer, false, null);

        assertTrue(lexer.next());
        assertLexer(lexer, true, expectedLex);

        assertFalse(lexer.next());
        assertLexer(lexer, true, null);
    }

    @Test
    public void withSingleCharacters()
    {
        singleLexTest('{', Lex.leftCurlyBracket(0));
        singleLexTest('}', Lex.rightCurlyBracket(0));
        singleLexTest('[', Lex.leftSquareBracket(0));
        singleLexTest(']', Lex.rightSquareBracket(0));
        singleLexTest('<', Lex.leftAngleBracket(0));
        singleLexTest('>', Lex.rightAngleBracket(0));
        singleLexTest('(', Lex.leftParenthesis(0));
        singleLexTest(')', Lex.rightParenthesis(0));
        singleLexTest("xyz", Lex.letters("xyz", 0));
        singleLexTest('\'', Lex.singleQuote(0));
        singleLexTest('\"', Lex.doubleQuote(0));
        singleLexTest("0123456789", Lex.digits("0123456789", 0));
        singleLexTest(',', Lex.comma(0));
        singleLexTest(':', Lex.colon(0));
        singleLexTest(';', Lex.semicolon(0));
        singleLexTest('!', Lex.exclamationPoint(0));
        singleLexTest('\\', Lex.backslash(0));
        singleLexTest('/', Lex.forwardSlash(0));
        singleLexTest('?', Lex.questionMark(0));
        singleLexTest('-', Lex.dash(0));
        singleLexTest('+', Lex.plus(0));
        singleLexTest('=', Lex.equalsSign(0));
        singleLexTest('.', Lex.period(0));
        singleLexTest('_', Lex.underscore(0));
        singleLexTest('&', Lex.ampersand(0));
        singleLexTest('|', Lex.verticalBar(0));
        singleLexTest(' ', Lex.space(0));
        singleLexTest('\t', Lex.tab(0));
        singleLexTest('\r', Lex.carriageReturn(0));
        singleLexTest('\n', Lex.newLine(0));
        singleLexTest("\r\n", Lex.carriageReturnNewLine(0));
        singleLexTest('*', Lex.asterisk(0));
        singleLexTest('%', Lex.percent(0));
        singleLexTest('#', Lex.hash(0));
        singleLexTest('^', Lex.unrecognized('^', 0));
    }

    @Test
    public void withRightCurlyBracket()
    {
        final Lexer lexer = new Lexer("}");
        assertLexer(lexer, false, null);

        assertTrue(lexer.next());
        assertLexer(lexer, true, Lex.rightCurlyBracket(0));

        assertFalse(lexer.next());
        assertLexer(lexer, true, null);
    }

    private static void assertLexer(Lexer lexer, boolean hasStarted, Lex current)
    {
        assertEquals(hasStarted, lexer.hasStarted());
        assertEquals(current != null, lexer.hasCurrent());
        assertEquals(current, lexer.getCurrent());
    }
}
