package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTests
{
    @Test
    public void JSONConstructor()
    {
        final JSON json = new JSON();
        assertNotNull(json);
    }

    @Test
    public void TokenConstructor()
    {
        final JSON.Token token = new JSON.Token("{", 17, JSON.Token.Type.LeftCurlyBracket);
        assertEquals("{", token.toString());
        assertEquals(17, token.getStartIndex());
        assertEquals(1, token.getLength());
        assertEquals(18, token.getAfterEndIndex());
        assertEquals(new Span(17, 1), token.getSpan());
        assertEquals(JSON.Token.Type.LeftCurlyBracket, token.getType());
    }

    @Test
    public void TokenLeftCurlyBracket()
    {
        assertToken(JSON.Token.leftCurlyBracket(0), "{", 0, 1, JSON.Token.Type.LeftCurlyBracket);
    }

    @Test
    public void TokenRightCurlyBracket()
    {
        assertToken(JSON.Token.rightCurlyBracket(1), "}", 1, 1, JSON.Token.Type.RightCurlyBracket);
    }

    @Test
    public void TokenLeftSquareBracket()
    {
        assertToken(JSON.Token.leftSquareBracket(2), "[", 2, 1, JSON.Token.Type.LeftSquareBracket);
    }

    @Test
    public void TokenRightSquareBracket()
    {
        assertToken(JSON.Token.rightSquareBracket(3), "]", 3, 1, JSON.Token.Type.RightSquareBracket);
    }

    @Test
    public void TokenColon()
    {
        assertToken(JSON.Token.colon(4), ":", 4, 1, JSON.Token.Type.Colon);
    }

    @Test
    public void TokenComma()
    {
        assertToken(JSON.Token.comma(5), ",", 5, 1, JSON.Token.Type.Comma);
    }

    @Test
    public void TokenTrueToken()
    {
        assertToken(JSON.Token.trueToken(5), "true", 5, 4, JSON.Token.Type.True);
    }

    @Test
    public void TokenFalseToken()
    {
        assertToken(JSON.Token.falseToken(6), "false", 6, 5, JSON.Token.Type.False);
    }

    @Test
    public void TokenNullToken()
    {
        assertToken(JSON.Token.nullToken(7), "null", 7, 4, JSON.Token.Type.Null);
    }

    @Test
    public void TokenNewLine()
    {
        assertToken(JSON.Token.newLine("\n", 8), "\n", 8, 1, JSON.Token.Type.NewLine);
        assertToken(JSON.Token.newLine("\r\n", 9), "\r\n", 9, 2, JSON.Token.Type.NewLine);
    }

    @Test
    public void TokenQuotedString()
    {
        assertCloseableToken(JSON.Token.quotedString("\'hello", 10, false), "\'hello", 10, 6, JSON.Token.Type.QuotedString, false);
        assertCloseableToken(JSON.Token.quotedString("\'hello\'", 11, true), "\'hello\'", 11, 7, JSON.Token.Type.QuotedString, true);
        assertCloseableToken(JSON.Token.quotedString("\"hello", 12, false), "\"hello", 12, 6, JSON.Token.Type.QuotedString, false);
        assertCloseableToken(JSON.Token.quotedString("\"hello\"", 13, true), "\"hello\"", 13, 7, JSON.Token.Type.QuotedString, true);
    }

    @Test
    public void TokenNumber()
    {
        assertToken(JSON.Token.number("123", 14), "123", 14, 3, JSON.Token.Type.Number);
    }

    @Test
    public void TokenWhitespace()
    {
        assertToken(JSON.Token.whitespace("  \t", 15), "  \t", 15, 3, JSON.Token.Type.Whitespace);
    }

    @Test
    public void TokenLineComment()
    {
        assertToken(JSON.Token.lineComment("//", 16), "//", 16, 2, JSON.Token.Type.LineComment);
    }

    @Test
    public void TokenBlockComment()
    {
        assertCloseableToken(JSON.Token.blockComment("/*", 17, false), "/*", 17, 2, JSON.Token.Type.BlockComment, false);
    }

    @Test
    public void TokenUnrecognized()
    {
        assertToken(JSON.Token.unrecognized("&", 18), "&", 18, 1, JSON.Token.Type.Unrecognized);
    }

    @Test
    public void Tokenizer()
    {

    }

    private static void assertToken(JSON.Token token, String text, int startIndex, int length, JSON.Token.Type type)
    {
        assertEquals(text, token.toString());
        assertEquals(new Span(startIndex, length), token.getSpan());
        assertEquals(type, token.getType());
    }

    private static void assertCloseableToken(JSON.CloseableToken token, String text, int startIndex, int length, JSON.Token.Type type, boolean closed)
    {
        assertToken(token, text, startIndex, length, type);
        assertEquals(closed, token.isClosed());
    }

    private static void assertTokenizer(String text, JSON.Token[] expectedTokens)
    {
        final JSON.Tokenizer tokenizer = new JSON.Tokenizer(text);
        final Array<JSON.Token> actualTokens = Array.fromValues(tokenizer);
        final int
    }
}
