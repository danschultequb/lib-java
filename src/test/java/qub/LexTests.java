package qub;

import org.junit.Test;
import static org.junit.Assert.*;

public class LexTests
{
    @Test
    public void constructor()
    {
        final Lex lex = new Lex("LexText", 20, LexType.Letters);
        assertEquals("LexText", lex.getText());
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
