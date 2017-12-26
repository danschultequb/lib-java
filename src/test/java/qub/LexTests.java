package qub;

import org.junit.Test;
import static org.junit.Assert.*;

public class LexTests
{
    @Test
    public void constructor()
    {
        final Lex lex = new Lex("LexText", 20, LexType.Letters);
        assertEquals("LexText", lex.toString());
        assertEquals(20, lex.getStartIndex());
        assertEquals(LexType.Letters, lex.getType());
    }

    private static void getLengthTest(String text)
    {
        final Lex lex = getLex(text);
        final int expectedLength = text == null ? 0 : text.length();
        final int actualLength = lex.getLength();
        assertEquals("Lex.getLength() with \"" + text + "\"", expectedLength, actualLength);
    }

    @Test
    public void getLength()
    {
        getLengthTest(null);
        getLengthTest("");
        getLengthTest("123");
    }

    private static void getAfterEndIndexTest(String text, int startIndex)
    {
        final Lex lex = getLex(text, startIndex);
        final int expectedAfterEndIndex = startIndex + (text == null ? 0 : text.length());
        final int actualAfterEndIndex = lex.getAfterEndIndex();
        assertEquals("Lex.getAfterEndIndex() with \"" + text + "\" and " + startIndex, expectedAfterEndIndex, actualAfterEndIndex);
    }

    @Test
    public void getAfterEndIndex()
    {
        getAfterEndIndexTest(null, -10);
        getAfterEndIndexTest(null, 0);
        getAfterEndIndexTest(null, 3);
        getAfterEndIndexTest("", -9);
        getAfterEndIndexTest("", 0);
        getAfterEndIndexTest("", 4);
        getAfterEndIndexTest("testing", -8);
        getAfterEndIndexTest("testing", 0);
        getAfterEndIndexTest("testing", 5);
    }

    private static void getEndIndexTest(String text, int startIndex)
    {
        final Lex lex = getLex(text, startIndex);
        final int expectedEndIndex = startIndex + (text == null || text.length() <= 1 ? 0 : text.length() - 1);
        final int actualEndIndex = lex.getEndIndex();
        assertEquals("Lex.getEndIndex() with \"" + text + "\" and " + startIndex, expectedEndIndex, actualEndIndex);
    }

    @Test
    public void getEndIndex()
    {
        getEndIndexTest(null, -5);
        getEndIndexTest(null, 0);
        getEndIndexTest(null, 15);
        getEndIndexTest("", -6);
        getEndIndexTest("", 0);
        getEndIndexTest("", 16);
        getEndIndexTest("a", -7);
        getEndIndexTest("a", 0);
        getEndIndexTest("a", 17);
        getEndIndexTest("ab", -8);
        getEndIndexTest("ab", 0);
        getEndIndexTest("ab", 18);
    }

    private static void getSpanTest(String text, int startIndex)
    {
        final Lex lex = getLex(text, startIndex);
        final Span expectedSpan = new Span(startIndex, text == null ? 0 : text.length());
        final Span actualSpan = lex.getSpan();
        assertEquals("Lex.getSpan() with \"" + text + "\" and " + startIndex, expectedSpan, actualSpan);
    }

    @Test
    public void getSpan()
    {
        getSpanTest(null, -20);
        getSpanTest(null, 0);
        getSpanTest(null, 2);
        getSpanTest("", -30);
        getSpanTest("", 0);
        getSpanTest("", 3);
        getSpanTest("grapes", -40);
        getSpanTest("grapes", 0);
        getSpanTest("grapes", 4);
    }

    @Test
    public void equals()
    {
        final Lex lex = Lex.space(17);
        assertFalse(lex.equals((Object)null));
        assertFalse(lex.equals((Lex)null));
        assertFalse(lex.equals("lex"));
        assertFalse(lex.equals(Lex.leftCurlyBracket(17)));
        assertFalse(lex.equals(Lex.space(18)));
        assertTrue(lex.equals(lex));
        assertTrue(lex.equals(Lex.space(17)));
    }

    private static void isWhitespaceTest(LexType type, boolean expected)
    {
        final Lex lex = getLex(type);
        assertEquals("Lex.isWhitespace() with " + type, expected, lex.isWhitespace());
    }

    @Test
    public void isWhitespace()
    {
        isWhitespaceTest(LexType.LeftCurlyBracket, false);
        isWhitespaceTest(LexType.RightCurlyBracket, false);
        isWhitespaceTest(LexType.LeftSquareBracket, false);
        isWhitespaceTest(LexType.RightSquareBracket, false);
        isWhitespaceTest(LexType.LeftAngleBracket, false);
        isWhitespaceTest(LexType.RightAngleBracket, false);
        isWhitespaceTest(LexType.LeftParenthesis, false);
        isWhitespaceTest(LexType.RightParenthesis, false);
        isWhitespaceTest(LexType.Letters, false);
        isWhitespaceTest(LexType.SingleQuote, false);
        isWhitespaceTest(LexType.DoubleQuote, false);
        isWhitespaceTest(LexType.Digits, false);
        isWhitespaceTest(LexType.Comma, false);
        isWhitespaceTest(LexType.Colon, false);
        isWhitespaceTest(LexType.Semicolon, false);
        isWhitespaceTest(LexType.ExclamationPoint, false);
        isWhitespaceTest(LexType.Backslash, false);
        isWhitespaceTest(LexType.ForwardSlash, false);
        isWhitespaceTest(LexType.QuestionMark, false);
        isWhitespaceTest(LexType.Dash, false);
        isWhitespaceTest(LexType.Plus, false);
        isWhitespaceTest(LexType.EqualsSign, false);
        isWhitespaceTest(LexType.Period, false);
        isWhitespaceTest(LexType.Underscore, false);
        isWhitespaceTest(LexType.Ampersand, false);
        isWhitespaceTest(LexType.VerticalBar, false);
        isWhitespaceTest(LexType.Space, true);
        isWhitespaceTest(LexType.Tab, true);
        isWhitespaceTest(LexType.CarriageReturn, true);
        isWhitespaceTest(LexType.NewLine, false);
        isWhitespaceTest(LexType.CarriageReturnNewLine, false);
        isWhitespaceTest(LexType.Asterisk, false);
        isWhitespaceTest(LexType.Percent, false);
        isWhitespaceTest(LexType.Hash, false);
        isWhitespaceTest(LexType.Unrecognized, false);
    }

    private static void isNewLineTest(LexType type, boolean expected)
    {
        final Lex lex = getLex(type);
        assertEquals("Lex.isNewLine() with " + type, expected, lex.isNewLine());
    }

    @Test
    public void isNewLine()
    {
        isNewLineTest(LexType.LeftCurlyBracket, false);
        isNewLineTest(LexType.RightCurlyBracket, false);
        isNewLineTest(LexType.LeftSquareBracket, false);
        isNewLineTest(LexType.RightSquareBracket, false);
        isNewLineTest(LexType.LeftAngleBracket, false);
        isNewLineTest(LexType.RightAngleBracket, false);
        isNewLineTest(LexType.LeftParenthesis, false);
        isNewLineTest(LexType.RightParenthesis, false);
        isNewLineTest(LexType.Letters, false);
        isNewLineTest(LexType.SingleQuote, false);
        isNewLineTest(LexType.DoubleQuote, false);
        isNewLineTest(LexType.Digits, false);
        isNewLineTest(LexType.Comma, false);
        isNewLineTest(LexType.Colon, false);
        isNewLineTest(LexType.Semicolon, false);
        isNewLineTest(LexType.ExclamationPoint, false);
        isNewLineTest(LexType.Backslash, false);
        isNewLineTest(LexType.ForwardSlash, false);
        isNewLineTest(LexType.QuestionMark, false);
        isNewLineTest(LexType.Dash, false);
        isNewLineTest(LexType.Plus, false);
        isNewLineTest(LexType.EqualsSign, false);
        isNewLineTest(LexType.Period, false);
        isNewLineTest(LexType.Underscore, false);
        isNewLineTest(LexType.Ampersand, false);
        isNewLineTest(LexType.VerticalBar, false);
        isNewLineTest(LexType.Space, false);
        isNewLineTest(LexType.Tab, false);
        isNewLineTest(LexType.CarriageReturn, false);
        isNewLineTest(LexType.NewLine, true);
        isNewLineTest(LexType.CarriageReturnNewLine, true);
        isNewLineTest(LexType.Asterisk, false);
        isNewLineTest(LexType.Percent, false);
        isNewLineTest(LexType.Hash, false);
        isNewLineTest(LexType.Unrecognized, false);
    }

    @Test
    public void leftCurlyBracket()
    {
        assertLex(Lex.leftCurlyBracket(0), "{", 0, LexType.LeftCurlyBracket);
    }

    @Test
    public void rightCurlyBracket()
    {
        assertLex(Lex.rightCurlyBracket(1), "}", 1, LexType.RightCurlyBracket);
    }

    @Test
    public void leftSquareBracket()
    {
        assertLex(Lex.leftSquareBracket(2), "[", 2, LexType.LeftSquareBracket);
    }

    @Test
    public void rightSquareBracket()
    {
        assertLex(Lex.rightSquareBracket(3), "]", 3, LexType.RightSquareBracket);
    }

    @Test
    public void leftParenthesis()
    {
        assertLex(Lex.leftParenthesis(4), "(", 4, LexType.LeftParenthesis);
    }

    @Test
    public void rightParenthesis()
    {
        assertLex(Lex.rightParenthesis(5), ")", 5, LexType.RightParenthesis);
    }

    @Test
    public void leftAngleBracket()
    {
        assertLex(Lex.leftAngleBracket(6), "<", 6, LexType.LeftAngleBracket);
    }

    @Test
    public void rightAngleBracket()
    {
        assertLex(Lex.rightAngleBracket(7), ">", 7, LexType.RightAngleBracket);
    }

    @Test
    public void doubleQuote()
    {
        assertLex(Lex.doubleQuote(8), "\"", 8, LexType.DoubleQuote);
    }

    @Test
    public void singleQuote()
    {
        assertLex(Lex.singleQuote(9), "\'", 9, LexType.SingleQuote);
    }

    @Test
    public void dash()
    {
        assertLex(Lex.dash(10), "-", 10, LexType.Dash);
    }

    @Test
    public void plus()
    {
        assertLex(Lex.plus(11), "+", 11, LexType.Plus);
    }

    @Test
    public void comma()
    {
        assertLex(Lex.comma(12), ",", 12, LexType.Comma);
    }

    @Test
    public void colon()
    {
        assertLex(Lex.colon(13), ":", 13, LexType.Colon);
    }

    @Test
    public void semicolon()
    {
        assertLex(Lex.semicolon(14), ";", 14, LexType.Semicolon);
    }

    @Test
    public void exclamationPoint()
    {
        assertLex(Lex.exclamationPoint(15), "!", 15, LexType.ExclamationPoint);
    }

    @Test
    public void backslash()
    {
        assertLex(Lex.backslash(16), "\\", 16, LexType.Backslash);
    }

    @Test
    public void forwardSlash()
    {
        assertLex(Lex.forwardSlash(17), "/", 17, LexType.ForwardSlash);
    }

    @Test
    public void questionMark()
    {
        assertLex(Lex.questionMark(18), "?", 18, LexType.QuestionMark);
    }

    @Test
    public void equalsSign()
    {
        assertLex(Lex.equalsSign(19), "=", 19, LexType.EqualsSign);
    }

    @Test
    public void period()
    {
        assertLex(Lex.period(20), ".", 20, LexType.Period);
    }

    @Test
    public void underscore()
    {
        assertLex(Lex.underscore(21), "_", 21, LexType.Underscore);
    }

    @Test
    public void ampersand()
    {
        assertLex(Lex.ampersand(22), "&", 22, LexType.Ampersand);
    }

    @Test
    public void space()
    {
        assertLex(Lex.space(23), " ", 23, LexType.Space);
    }

    @Test
    public void tab()
    {
        assertLex(Lex.tab(24), "\t", 24, LexType.Tab);
    }

    @Test
    public void carriageReturn()
    {
        assertLex(Lex.carriageReturn(25), "\r", 25, LexType.CarriageReturn);
    }

    @Test
    public void carriageReturnNewLine()
    {
        assertLex(Lex.carriageReturnNewLine(26), "\r\n", 26, LexType.CarriageReturnNewLine);
    }

    @Test
    public void newLine()
    {
        assertLex(Lex.newLine(27), "\n", 27, LexType.NewLine);
    }

    @Test
    public void asterisk()
    {
        assertLex(Lex.asterisk(28), "*", 28, LexType.Asterisk);
    }

    @Test
    public void percent()
    {
        assertLex(Lex.percent(29), "%", 29, LexType.Percent);
    }

    @Test
    public void verticalBar()
    {
        assertLex(Lex.verticalBar(30), "|", 30, LexType.VerticalBar);
    }

    @Test
    public void hash()
    {
        assertLex(Lex.hash(31), "#", 31, LexType.Hash);
    }

    @Test
    public void letters()
    {
        assertLex(Lex.letters("abcdef", 32), "abcdef", 32, LexType.Letters);
    }

    @Test
    public void digits()
    {
        assertLex(Lex.digits("123456", 33), "123456", 33, LexType.Digits);
    }

    @Test
    public void unrecognized()
    {
        assertLex(Lex.unrecognized('$', 34), "$", 34, LexType.Unrecognized);
    }

    @Test
    public void isLetter()
    {
        assertFalse(Lex.isLetter('0'));
        assertFalse(Lex.isLetter('.'));

        assertTrue(Lex.isLetter('a'));
        assertTrue(Lex.isLetter('m'));
        assertTrue(Lex.isLetter('z'));
        assertTrue(Lex.isLetter('A'));
        assertTrue(Lex.isLetter('N'));
        assertTrue(Lex.isLetter('Z'));

        assertFalse(Lex.isLetter.run('0'));
        assertFalse(Lex.isLetter.run('.'));

        assertTrue(Lex.isLetter.run('a'));
        assertTrue(Lex.isLetter.run('m'));
        assertTrue(Lex.isLetter.run('z'));
        assertTrue(Lex.isLetter.run('A'));
        assertTrue(Lex.isLetter.run('N'));
        assertTrue(Lex.isLetter.run('Z'));
    }

    @Test
    public void isDigit()
    {
        assertFalse(Lex.isDigit('a'));
        assertFalse(Lex.isDigit(' '));
        assertFalse(Lex.isDigit('-'));
        assertFalse(Lex.isDigit('.'));

        assertTrue(Lex.isDigit('0'));
        assertTrue(Lex.isDigit('1'));
        assertTrue(Lex.isDigit('2'));
        assertTrue(Lex.isDigit('3'));
        assertTrue(Lex.isDigit('4'));
        assertTrue(Lex.isDigit('5'));
        assertTrue(Lex.isDigit('6'));
        assertTrue(Lex.isDigit('7'));
        assertTrue(Lex.isDigit('8'));
        assertTrue(Lex.isDigit('9'));

        assertFalse(Lex.isDigit.run('a'));
        assertFalse(Lex.isDigit.run(' '));
        assertFalse(Lex.isDigit.run('-'));
        assertFalse(Lex.isDigit.run('.'));

        assertTrue(Lex.isDigit.run('0'));
        assertTrue(Lex.isDigit.run('1'));
        assertTrue(Lex.isDigit.run('2'));
        assertTrue(Lex.isDigit.run('3'));
        assertTrue(Lex.isDigit.run('4'));
        assertTrue(Lex.isDigit.run('5'));
        assertTrue(Lex.isDigit.run('6'));
        assertTrue(Lex.isDigit.run('7'));
        assertTrue(Lex.isDigit.run('8'));
        assertTrue(Lex.isDigit.run('9'));
    }

    private static void assertLex(Lex lex, String text, int startIndex, LexType type)
    {
        assertNotNull(lex);
        assertEquals(text, lex.toString());
        assertEquals(startIndex, lex.getStartIndex());
        assertEquals(type, lex.getType());
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
