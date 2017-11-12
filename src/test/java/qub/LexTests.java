package qub;

import org.junit.Test;
import static org.junit.Assert.*;

public class LexTests
{
    @Test
    public void constructor()
    {
        final Lex lex = new Lex("LexText", 20, Lex.Type.Letters);
        assertEquals("LexText", lex.getText());
        assertEquals(20, lex.getStartIndex());
        assertEquals(Lex.Type.Letters, lex.getType());
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

    private static void isWhitespaceTest(Lex.Type type, boolean expected)
    {
        final Lex lex = getLex(type);
        assertEquals("Lex.isWhitespace() with " + type, expected, lex.isWhitespace());
    }

    @Test
    public void isWhitespace()
    {
        isWhitespaceTest(Lex.Type.LeftCurlyBracket, false);
        isWhitespaceTest(Lex.Type.RightCurlyBracket, false);
        isWhitespaceTest(Lex.Type.LeftSquareBracket, false);
        isWhitespaceTest(Lex.Type.RightSquareBracket, false);
        isWhitespaceTest(Lex.Type.LeftAngleBracket, false);
        isWhitespaceTest(Lex.Type.RightAngleBracket, false);
        isWhitespaceTest(Lex.Type.LeftParenthesis, false);
        isWhitespaceTest(Lex.Type.RightParenthesis, false);
        isWhitespaceTest(Lex.Type.Letters, false);
        isWhitespaceTest(Lex.Type.SingleQuote, false);
        isWhitespaceTest(Lex.Type.DoubleQuote, false);
        isWhitespaceTest(Lex.Type.Digits, false);
        isWhitespaceTest(Lex.Type.Comma, false);
        isWhitespaceTest(Lex.Type.Colon, false);
        isWhitespaceTest(Lex.Type.Semicolon, false);
        isWhitespaceTest(Lex.Type.ExclamationPoint, false);
        isWhitespaceTest(Lex.Type.Backslash, false);
        isWhitespaceTest(Lex.Type.ForwardSlash, false);
        isWhitespaceTest(Lex.Type.QuestionMark, false);
        isWhitespaceTest(Lex.Type.Dash, false);
        isWhitespaceTest(Lex.Type.Plus, false);
        isWhitespaceTest(Lex.Type.EqualsSign, false);
        isWhitespaceTest(Lex.Type.Period, false);
        isWhitespaceTest(Lex.Type.Underscore, false);
        isWhitespaceTest(Lex.Type.Ampersand, false);
        isWhitespaceTest(Lex.Type.VerticalBar, false);
        isWhitespaceTest(Lex.Type.Space, true);
        isWhitespaceTest(Lex.Type.Tab, true);
        isWhitespaceTest(Lex.Type.CarriageReturn, true);
        isWhitespaceTest(Lex.Type.NewLine, false);
        isWhitespaceTest(Lex.Type.CarriageReturnNewLine, false);
        isWhitespaceTest(Lex.Type.Asterisk, false);
        isWhitespaceTest(Lex.Type.Percent, false);
        isWhitespaceTest(Lex.Type.Hash, false);
        isWhitespaceTest(Lex.Type.Unrecognized, false);
    }

    private static void isNewLineTest(Lex.Type type, boolean expected)
    {
        final Lex lex = getLex(type);
        assertEquals("Lex.isNewLine() with " + type, expected, lex.isNewLine());
    }

    @Test
    public void isNewLine()
    {
        isNewLineTest(Lex.Type.LeftCurlyBracket, false);
        isNewLineTest(Lex.Type.RightCurlyBracket, false);
        isNewLineTest(Lex.Type.LeftSquareBracket, false);
        isNewLineTest(Lex.Type.RightSquareBracket, false);
        isNewLineTest(Lex.Type.LeftAngleBracket, false);
        isNewLineTest(Lex.Type.RightAngleBracket, false);
        isNewLineTest(Lex.Type.LeftParenthesis, false);
        isNewLineTest(Lex.Type.RightParenthesis, false);
        isNewLineTest(Lex.Type.Letters, false);
        isNewLineTest(Lex.Type.SingleQuote, false);
        isNewLineTest(Lex.Type.DoubleQuote, false);
        isNewLineTest(Lex.Type.Digits, false);
        isNewLineTest(Lex.Type.Comma, false);
        isNewLineTest(Lex.Type.Colon, false);
        isNewLineTest(Lex.Type.Semicolon, false);
        isNewLineTest(Lex.Type.ExclamationPoint, false);
        isNewLineTest(Lex.Type.Backslash, false);
        isNewLineTest(Lex.Type.ForwardSlash, false);
        isNewLineTest(Lex.Type.QuestionMark, false);
        isNewLineTest(Lex.Type.Dash, false);
        isNewLineTest(Lex.Type.Plus, false);
        isNewLineTest(Lex.Type.EqualsSign, false);
        isNewLineTest(Lex.Type.Period, false);
        isNewLineTest(Lex.Type.Underscore, false);
        isNewLineTest(Lex.Type.Ampersand, false);
        isNewLineTest(Lex.Type.VerticalBar, false);
        isNewLineTest(Lex.Type.Space, false);
        isNewLineTest(Lex.Type.Tab, false);
        isNewLineTest(Lex.Type.CarriageReturn, false);
        isNewLineTest(Lex.Type.NewLine, true);
        isNewLineTest(Lex.Type.CarriageReturnNewLine, true);
        isNewLineTest(Lex.Type.Asterisk, false);
        isNewLineTest(Lex.Type.Percent, false);
        isNewLineTest(Lex.Type.Hash, false);
        isNewLineTest(Lex.Type.Unrecognized, false);
    }

    @Test
    public void leftCurlyBracket()
    {
        assertLex(Lex.leftCurlyBracket(0), "{", 0, Lex.Type.LeftCurlyBracket);
    }

    @Test
    public void rightCurlyBracket()
    {
        assertLex(Lex.rightCurlyBracket(1), "}", 1, Lex.Type.RightCurlyBracket);
    }

    @Test
    public void leftSquareBracket()
    {
        assertLex(Lex.leftSquareBracket(2), "[", 2, Lex.Type.LeftSquareBracket);
    }

    @Test
    public void rightSquareBracket()
    {
        assertLex(Lex.rightSquareBracket(3), "]", 3, Lex.Type.RightSquareBracket);
    }

    @Test
    public void leftParenthesis()
    {
        assertLex(Lex.leftParenthesis(4), "(", 4, Lex.Type.LeftParenthesis);
    }

    @Test
    public void rightParenthesis()
    {
        assertLex(Lex.rightParenthesis(5), ")", 5, Lex.Type.RightParenthesis);
    }

    @Test
    public void leftAngleBracket()
    {
        assertLex(Lex.leftAngleBracket(6), "<", 6, Lex.Type.LeftAngleBracket);
    }

    @Test
    public void rightAngleBracket()
    {
        assertLex(Lex.rightAngleBracket(7), ">", 7, Lex.Type.RightAngleBracket);
    }

    @Test
    public void doubleQuote()
    {
        assertLex(Lex.doubleQuote(8), "\"", 8, Lex.Type.DoubleQuote);
    }

    @Test
    public void singleQuote()
    {
        assertLex(Lex.singleQuote(9), "\'", 9, Lex.Type.SingleQuote);
    }

    @Test
    public void dash()
    {
        assertLex(Lex.dash(10), "-", 10, Lex.Type.Dash);
    }

    @Test
    public void plus()
    {
        assertLex(Lex.plus(11), "+", 11, Lex.Type.Plus);
    }

    @Test
    public void comma()
    {
        assertLex(Lex.comma(12), ",", 12, Lex.Type.Comma);
    }

    @Test
    public void colon()
    {
        assertLex(Lex.colon(13), ":", 13, Lex.Type.Colon);
    }

    @Test
    public void semicolon()
    {
        assertLex(Lex.semicolon(14), ";", 14, Lex.Type.Semicolon);
    }

    @Test
    public void exclamationPoint()
    {
        assertLex(Lex.exclamationPoint(15), "!", 15, Lex.Type.ExclamationPoint);
    }

    @Test
    public void backslash()
    {
        assertLex(Lex.backslash(16), "\\", 16, Lex.Type.Backslash);
    }

    @Test
    public void forwardSlash()
    {
        assertLex(Lex.forwardSlash(17), "/", 17, Lex.Type.ForwardSlash);
    }

    @Test
    public void questionMark()
    {
        assertLex(Lex.questionMark(18), "?", 18, Lex.Type.QuestionMark);
    }

    @Test
    public void equalsSign()
    {
        assertLex(Lex.equalsSign(19), "=", 19, Lex.Type.EqualsSign);
    }

    @Test
    public void period()
    {
        assertLex(Lex.period(20), ".", 20, Lex.Type.Period);
    }

    @Test
    public void underscore()
    {
        assertLex(Lex.underscore(21), "_", 21, Lex.Type.Underscore);
    }

    @Test
    public void ampersand()
    {
        assertLex(Lex.ampersand(22), "&", 22, Lex.Type.Ampersand);
    }

    @Test
    public void space()
    {
        assertLex(Lex.space(23), " ", 23, Lex.Type.Space);
    }

    @Test
    public void tab()
    {
        assertLex(Lex.tab(24), "\t", 24, Lex.Type.Tab);
    }

    @Test
    public void carriageReturn()
    {
        assertLex(Lex.carriageReturn(25), "\r", 25, Lex.Type.CarriageReturn);
    }

    @Test
    public void carriageReturnNewLine()
    {
        assertLex(Lex.carriageReturnNewLine(26), "\r\n", 26, Lex.Type.CarriageReturnNewLine);
    }

    @Test
    public void newLine()
    {
        assertLex(Lex.newLine(27), "\n", 27, Lex.Type.NewLine);
    }

    @Test
    public void asterisk()
    {
        assertLex(Lex.asterisk(28), "*", 28, Lex.Type.Asterisk);
    }

    @Test
    public void percent()
    {
        assertLex(Lex.percent(29), "%", 29, Lex.Type.Percent);
    }

    @Test
    public void verticalBar()
    {
        assertLex(Lex.verticalBar(30), "|", 30, Lex.Type.VerticalBar);
    }

    @Test
    public void hash()
    {
        assertLex(Lex.hash(31), "#", 31, Lex.Type.Hash);
    }

    @Test
    public void letters()
    {
        assertLex(Lex.letters("abcdef", 32), "abcdef", 32, Lex.Type.Letters);
    }

    @Test
    public void digits()
    {
        assertLex(Lex.digits("123456", 33), "123456", 33, Lex.Type.Digits);
    }

    @Test
    public void unrecognized()
    {
        assertLex(Lex.unrecognized('$', 34), "$", 34, Lex.Type.Unrecognized);
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

    private static void assertLex(Lex lex, String text, int startIndex, Lex.Type type)
    {
        assertNotNull(lex);
        assertEquals(text, lex.getText());
        assertEquals(startIndex, lex.getStartIndex());
        assertEquals(type, lex.getType());
    }

    private static Lex getLex(String text)
    {
        return getLex(text, 15);
    }

    private static Lex getLex(String text, int startIndex)
    {
        return getLex(text, startIndex, Lex.Type.Unrecognized);
    }

    private static Lex getLex(Lex.Type type)
    {
        return getLex(null, 15, type);
    }

    private static Lex getLex(String text, int startIndex, Lex.Type type)
    {
        return new Lex(text, startIndex, type);
    }
}
