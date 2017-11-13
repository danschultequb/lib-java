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
        assertToken(JSON.Token.trueToken("true", 5), "true", 5, 4, JSON.Token.Type.True);
    }

    @Test
    public void TokenFalseToken()
    {
        assertToken(JSON.Token.falseToken("false", 6), "false", 6, 5, JSON.Token.Type.False);
    }

    @Test
    public void TokenNullToken()
    {
        assertToken(JSON.Token.nullToken("null", 7), "null", 7, 4, JSON.Token.Type.Null);
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
        assertTokenizer(null);
        assertTokenizer("");
        assertTokenizer("{", JSON.Token.leftCurlyBracket(0));
        assertTokenizer("}", JSON.Token.rightCurlyBracket(0));
        assertTokenizer("[", JSON.Token.leftSquareBracket(0));
        assertTokenizer("]", JSON.Token.rightSquareBracket(0));
        assertTokenizer(":", JSON.Token.colon(0));
        assertTokenizer(",", JSON.Token.comma(0));
        assertTokenizer("true", JSON.Token.trueToken("true", 0));
        assertTokenizer("TRUE",
            JSON.Token.trueToken("TRUE", 0),
            JSON.Issues.shouldBeLowercased(0, 4));
        assertTokenizer("falSE",
            JSON.Token.falseToken("falSE", 0),
            JSON.Issues.shouldBeLowercased(0, 5));
        assertTokenizer("false", JSON.Token.falseToken("false", 0));
        assertTokenizer("null", JSON.Token.nullToken("null", 0));
        assertTokenizer("NULL",
            JSON.Token.nullToken("NULL", 0),
            JSON.Issues.shouldBeLowercased(0, 4));
        assertTokenizer("\'",
            JSON.Token.quotedString("\'", 0, false),
            JSON.Issues.missingEndQuote("\'", 0, 1));
        assertTokenizer("\'test",
            JSON.Token.quotedString("\'test", 0, false),
            JSON.Issues.missingEndQuote("\'", 0, 5));
        assertTokenizer("\'\\\'",
            JSON.Token.quotedString("\'\\\'", 0, false),
            JSON.Issues.missingEndQuote("\'", 0, 3));
        assertTokenizer("\'\'", JSON.Token.quotedString("\'\'", 0, true));
        assertTokenizer("\"",
            JSON.Token.quotedString("\"", 0, false),
            JSON.Issues.missingEndQuote("\"", 0, 1));
        assertTokenizer("\"\"", JSON.Token.quotedString("\"\"", 0, true));
        assertTokenizer(" ", JSON.Token.whitespace(" ", 0));
        assertTokenizer("\t", JSON.Token.whitespace("\t", 0));
        assertTokenizer("\r", JSON.Token.whitespace("\r", 0));
        assertTokenizer(" \t\r", JSON.Token.whitespace(" \t\r", 0));
        assertTokenizer("\n", JSON.Token.newLine("\n", 0));
        assertTokenizer("\r\n", JSON.Token.newLine("\r\n", 0));
        assertTokenizer("-",
            JSON.Token.number("-", 0),
            JSON.Issues.missingWholeNumberDigits(0, 1));
        assertTokenizer("-5", JSON.Token.number("-5", 0));
        assertTokenizer("-.",
            JSON.Token.number("-.", 0),
            new Issue[]
            {
                JSON.Issues.expectedWholeNumberDigits(1, 1),
                JSON.Issues.missingFractionalNumberDigits(1, 1)
            });
        assertTokenizer("12.",
            JSON.Token.number("12.", 0),
            JSON.Issues.missingFractionalNumberDigits(2, 1));
        assertTokenizer("12.3", JSON.Token.number("12.3", 0));
        assertTokenizer("12.a",
            new JSON.Token[]
            {
                JSON.Token.number("12.", 0),
                JSON.Token.unrecognized("a", 3)
            },
            JSON.Issues.expectedFractionalNumberDigits(3, 1));
        assertTokenizer("10e",
            JSON.Token.number("10e", 0),
            JSON.Issues.missingExponentNumberDigits(2, 1));
        assertTokenizer("10e{",
            new JSON.Token[]
            {
                JSON.Token.number("10e", 0),
                JSON.Token.leftCurlyBracket(3)
            },
            JSON.Issues.expectedExponentNumberDigits(3, 1));
        assertTokenizer("10e5", JSON.Token.number("10e5", 0));
        assertTokenizer("10e-",
            JSON.Token.number("10e-", 0),
            JSON.Issues.missingExponentNumberDigits(3, 1));
        assertTokenizer("10e-5", JSON.Token.number("10e-5", 0));
        assertTokenizer("10e+",
            JSON.Token.number("10e+", 0),
            JSON.Issues.missingExponentNumberDigits(3, 1));
        assertTokenizer("10e+5", JSON.Token.number("10e+5", 0));
        assertTokenizer("10E+5",
            JSON.Token.number("10E+5", 0),
            JSON.Issues.shouldBeLowercased(2, 1));
        assertTokenizer("/",
            JSON.Token.unrecognized("/", 0),
            JSON.Issues.missingCommentSlashOrAsterisk(0, 1));
        assertTokenizer("//", JSON.Token.lineComment("//", 0));
        assertTokenizer("/*", JSON.Token.blockComment("/*", 0, false));
        assertTokenizer("/**", JSON.Token.blockComment("/**", 0, false));
        assertTokenizer("/**/", JSON.Token.blockComment("/**/", 0, true));
        assertTokenizer("/*\n*/", JSON.Token.blockComment("/*\n*/", 0, true));
        assertTokenizer("/*/", JSON.Token.blockComment("/*/", 0, false));

        assertTokenizer("/a",
            new JSON.Token[]
            {
                JSON.Token.unrecognized("/", 0),
                JSON.Token.unrecognized("a", 1)
            },
            JSON.Issues.expectedCommentSlashOrAsterisk(1, 1));

        assertTokenizer("&", JSON.Token.unrecognized("&", 0));
    }

    @Test
    public void IssuesConstructor()
    {
        final JSON.Issues issues = new JSON.Issues();
        assertNotNull(issues);
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

    private static void assertTokenizer(String text)
    {
        assertTokenizer(text, new JSON.Token[0]);
    }

    private static void assertTokenizer(String text, JSON.Token expectedToken)
    {
        assertTokenizer(text, expectedToken, new Issue[0]);
    }

    private static void assertTokenizer(String text, JSON.Token expectedToken, Issue expectedIssue)
    {
        assertTokenizer(text, new JSON.Token[] { expectedToken }, new Issue[] { expectedIssue });
    }

    private static void assertTokenizer(String text, JSON.Token expectedToken, Issue[] expectedIssues)
    {
        assertTokenizer(text, new JSON.Token[] { expectedToken }, expectedIssues);
    }

    private static void assertTokenizer(String text, JSON.Token[] expectedTokens)
    {
        assertTokenizer(text, expectedTokens, new Issue[0]);
    }

    private static void assertTokenizer(String text, JSON.Token[] expectedTokens, Issue expectedIssue)
    {
        assertTokenizer(text, expectedTokens, new Issue[] { expectedIssue });
    }

    private static void assertTokenizer(String text, JSON.Token[] expectedTokens, Issue[] expectedIssues)
    {
        assertTokenizerWithoutIssues(text, expectedTokens);
        assertTokenizerWithIssues(text, expectedTokens, expectedIssues);
    }

    private static void assertTokenizerWithoutIssues(String text, JSON.Token[] expectedTokens)
    {
        final JSON.Tokenizer tokenizer = new JSON.Tokenizer(text);
        final Array<JSON.Token> actualTokens = Array.fromValues(tokenizer);
        final int tokensToCompare = Math.minimum(expectedTokens.length, actualTokens.getCount());
        for (int i = 0; i < tokensToCompare; ++i)
        {
            final JSON.Token expectedToken = expectedTokens[i];
            final JSON.Token actualToken = actualTokens.get(i);
            assertEquals(expectedToken, actualToken);
        }
        assertEquals(expectedTokens.length, actualTokens.getCount());
    }

    private static void assertTokenizerWithIssues(String text, JSON.Token[] expectedTokens, Issue[] expectedIssues)
    {
        final List<Issue> actualIssues = new ArrayList<>();
        final JSON.Tokenizer tokenizer = new JSON.Tokenizer(text, actualIssues);
        final Array<JSON.Token> actualTokens = Array.fromValues(tokenizer);

        final int tokensToCompare = Math.minimum(expectedTokens.length, actualTokens.getCount());
        for (int i = 0; i < tokensToCompare; ++i)
        {
            final JSON.Token expectedToken = expectedTokens[i];
            final JSON.Token actualToken = actualTokens.get(i);
            assertEquals(expectedToken, actualToken);
        }
        assertEquals(expectedTokens.length, actualTokens.getCount());

        final int issuesToCompare = Math.minimum(expectedIssues.length, actualIssues.getCount());
        for (int i = 0; i < issuesToCompare; ++i)
        {
            final Issue expectedIssue = expectedIssues[i];
            final Issue actualIssue = actualIssues.get(i);
            assertEquals(expectedIssue, actualIssue);
        }
        assertEquals(expectedIssues.length, actualIssues.getCount());
    }
}
