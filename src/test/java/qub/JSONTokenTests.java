package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTokenTests
{
    @Test
    public void JSONTokenConstructor()
    {
        final JSONToken token = new JSONToken("{", 17, JSONTokenType.LeftCurlyBracket);
        assertEquals("{", token.toString());
        assertEquals(17, token.getStartIndex());
        assertEquals(1, token.getLength());
        assertEquals(18, token.getAfterEndIndex());
        assertEquals(new Span(17, 1), token.getSpan());
        assertEquals(JSONTokenType.LeftCurlyBracket, token.getType());
    }

    @Test
    public void JSONTokenLeftCurlyBracket()
    {
        assertToken(JSONToken.leftCurlyBracket(0), "{", 0, 1, JSONTokenType.LeftCurlyBracket);
    }

    @Test
    public void TokenRightCurlyBracket()
    {
        assertToken(JSONToken.rightCurlyBracket(1), "}", 1, 1, JSONTokenType.RightCurlyBracket);
    }

    @Test
    public void TokenLeftSquareBracket()
    {
        assertToken(JSONToken.leftSquareBracket(2), "[", 2, 1, JSONTokenType.LeftSquareBracket);
    }

    @Test
    public void TokenRightSquareBracket()
    {
        assertToken(JSONToken.rightSquareBracket(3), "]", 3, 1, JSONTokenType.RightSquareBracket);
    }

    @Test
    public void TokenColon()
    {
        assertToken(JSONToken.colon(4), ":", 4, 1, JSONTokenType.Colon);
    }

    @Test
    public void TokenComma()
    {
        assertToken(JSONToken.comma(5), ",", 5, 1, JSONTokenType.Comma);
    }

    @Test
    public void TokenTrueToken()
    {
        assertToken(JSONToken.trueToken("true", 5), "true", 5, 4, JSONTokenType.True);
    }

    @Test
    public void TokenFalseToken()
    {
        assertToken(JSONToken.falseToken("false", 6), "false", 6, 5, JSONTokenType.False);
    }

    @Test
    public void TokenNullToken()
    {
        assertToken(JSONToken.nullToken("null", 7), "null", 7, 4, JSONTokenType.Null);
    }

    @Test
    public void TokenNewLine()
    {
        assertToken(JSONToken.newLine("\n", 8), "\n", 8, 1, JSONTokenType.NewLine);
        assertToken(JSONToken.newLine("\r\n", 9), "\r\n", 9, 2, JSONTokenType.NewLine);
    }

    @Test
    public void TokenQuotedString()
    {
        assertCloseableToken(JSONToken.quotedString("\'hello", 10, false), "\'hello", 10, 6, JSONTokenType.QuotedString, false);
        assertCloseableToken(JSONToken.quotedString("\'hello\'", 11, true), "\'hello\'", 11, 7, JSONTokenType.QuotedString, true);
        assertCloseableToken(JSONToken.quotedString("\"hello", 12, false), "\"hello", 12, 6, JSONTokenType.QuotedString, false);
        assertCloseableToken(JSONToken.quotedString("\"hello\"", 13, true), "\"hello\"", 13, 7, JSONTokenType.QuotedString, true);
    }

    @Test
    public void TokenNumber()
    {
        assertToken(JSONToken.number("123", 14), "123", 14, 3, JSONTokenType.Number);
    }

    @Test
    public void TokenWhitespace()
    {
        assertToken(JSONToken.whitespace("  \t", 15), "  \t", 15, 3, JSONTokenType.Whitespace);
    }

    @Test
    public void TokenLineComment()
    {
        assertToken(JSONToken.lineComment("//", 16), "//", 16, 2, JSONTokenType.LineComment);
    }

    @Test
    public void TokenBlockComment()
    {
        assertCloseableToken(JSONToken.blockComment("/*", 17, false), "/*", 17, 2, JSONTokenType.BlockComment, false);
    }

    @Test
    public void TokenUnrecognized()
    {
        assertToken(JSONToken.unrecognized("&", 18), "&", 18, 1, JSONTokenType.Unrecognized);
    }

    private static void assertToken(JSONToken token, String text, int startIndex, int length, JSONTokenType type)
    {
        assertEquals(text, token.toString());
        assertEquals(new Span(startIndex, length), token.getSpan());
        assertEquals(type, token.getType());
    }

    private static void assertCloseableToken(JSONCloseableToken token, String text, int startIndex, int length, JSONTokenType type, boolean closed)
    {
        assertToken(token, text, startIndex, length, type);
        assertEquals(closed, token.isClosed());
    }
}
