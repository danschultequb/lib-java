package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTokenizerTests
{
    @Test
    public void constructor()
    {
        assertTokenizer(null);
        assertTokenizer("");
        assertTokenizer("{", JSONToken.leftCurlyBracket(0));
        assertTokenizer("}", JSONToken.rightCurlyBracket(0));
        assertTokenizer("[", JSONToken.leftSquareBracket(0));
        assertTokenizer("]", JSONToken.rightSquareBracket(0));
        assertTokenizer(":", JSONToken.colon(0));
        assertTokenizer(",", JSONToken.comma(0));
        assertTokenizer("true", JSONToken.trueToken("true", 0));
        assertTokenizer("TRUE",
                JSONToken.trueToken("TRUE", 0),
                JSONIssues.shouldBeLowercased(0, 4));
        assertTokenizer("falSE",
                JSONToken.falseToken("falSE", 0),
                JSONIssues.shouldBeLowercased(0, 5));
        assertTokenizer("false", JSONToken.falseToken("false", 0));
        assertTokenizer("null", JSONToken.nullToken("null", 0));
        assertTokenizer("NULL",
                JSONToken.nullToken("NULL", 0),
                JSONIssues.shouldBeLowercased(0, 4));
        assertTokenizer("\'",
                JSONToken.quotedString("\'", 0, false),
                JSONIssues.missingEndQuote("\'", 0, 1));
        assertTokenizer("\'test",
                JSONToken.quotedString("\'test", 0, false),
                JSONIssues.missingEndQuote("\'", 0, 5));
        assertTokenizer("\'\\\'",
                JSONToken.quotedString("\'\\\'", 0, false),
                JSONIssues.missingEndQuote("\'", 0, 3));
        assertTokenizer("\'\'", JSONToken.quotedString("\'\'", 0, true));
        assertTokenizer("\"",
                JSONToken.quotedString("\"", 0, false),
                JSONIssues.missingEndQuote("\"", 0, 1));
        assertTokenizer("\"\"", JSONToken.quotedString("\"\"", 0, true));
        assertTokenizer(" ", JSONToken.whitespace(" ", 0));
        assertTokenizer("\t", JSONToken.whitespace("\t", 0));
        assertTokenizer("\r", JSONToken.whitespace("\r", 0));
        assertTokenizer(" \t\r", JSONToken.whitespace(" \t\r", 0));
        assertTokenizer("\n", JSONToken.newLine("\n", 0));
        assertTokenizer("\r\n", JSONToken.newLine("\r\n", 0));
        assertTokenizer("-",
                JSONToken.number("-", 0),
                JSONIssues.missingWholeNumberDigits(0, 1));
        assertTokenizer("-5", JSONToken.number("-5", 0));
        assertTokenizer("-.",
                JSONToken.number("-.", 0),
                new Issue[]
                        {
                                JSONIssues.expectedWholeNumberDigits(1, 1),
                                JSONIssues.missingFractionalNumberDigits(1, 1)
                        });
        assertTokenizer("12.",
                JSONToken.number("12.", 0),
                JSONIssues.missingFractionalNumberDigits(2, 1));
        assertTokenizer("12.3", JSONToken.number("12.3", 0));
        assertTokenizer("12.a",
                new JSONToken[]
                        {
                                JSONToken.number("12.", 0),
                                JSONToken.unrecognized("a", 3)
                        },
                JSONIssues.expectedFractionalNumberDigits(3, 1));
        assertTokenizer("10e",
                JSONToken.number("10e", 0),
                JSONIssues.missingExponentNumberDigits(2, 1));
        assertTokenizer("10e{",
                new JSONToken[]
                        {
                                JSONToken.number("10e", 0),
                                JSONToken.leftCurlyBracket(3)
                        },
                JSONIssues.expectedExponentNumberDigits(3, 1));
        assertTokenizer("10e5", JSONToken.number("10e5", 0));
        assertTokenizer("10e-",
                JSONToken.number("10e-", 0),
                JSONIssues.missingExponentNumberDigits(3, 1));
        assertTokenizer("10e-5", JSONToken.number("10e-5", 0));
        assertTokenizer("10e+",
                JSONToken.number("10e+", 0),
                JSONIssues.missingExponentNumberDigits(3, 1));
        assertTokenizer("10e+5", JSONToken.number("10e+5", 0));
        assertTokenizer("10E+5",
                JSONToken.number("10E+5", 0),
                JSONIssues.shouldBeLowercased(2, 1));
        assertTokenizer("/",
                JSONToken.unrecognized("/", 0),
                JSONIssues.missingCommentSlashOrAsterisk(0, 1));
        assertTokenizer("//", JSONToken.lineComment("//", 0));
        assertTokenizer("/*", JSONToken.blockComment("/*", 0, false));
        assertTokenizer("/**", JSONToken.blockComment("/**", 0, false));
        assertTokenizer("/**/", JSONToken.blockComment("/**/", 0, true));
        assertTokenizer("/*\n*/", JSONToken.blockComment("/*\n*/", 0, true));
        assertTokenizer("/*/", JSONToken.blockComment("/*/", 0, false));

        assertTokenizer("/a",
                new JSONToken[]
                        {
                                JSONToken.unrecognized("/", 0),
                                JSONToken.unrecognized("a", 1)
                        },
                JSONIssues.expectedCommentSlashOrAsterisk(1, 1));

        assertTokenizer("&", JSONToken.unrecognized("&", 0));
    }

    private static void assertTokenizer(String text)
    {
        assertTokenizer(text, new JSONToken[0]);
    }

    private static void assertTokenizer(String text, JSONToken expectedToken)
    {
        assertTokenizer(text, expectedToken, new Issue[0]);
    }

    private static void assertTokenizer(String text, JSONToken expectedToken, Issue expectedIssue)
    {
        assertTokenizer(text, new JSONToken[] { expectedToken }, new Issue[] { expectedIssue });
    }

    private static void assertTokenizer(String text, JSONToken expectedToken, Issue[] expectedIssues)
    {
        assertTokenizer(text, new JSONToken[] { expectedToken }, expectedIssues);
    }

    private static void assertTokenizer(String text, JSONToken[] expectedTokens)
    {
        assertTokenizer(text, expectedTokens, new Issue[0]);
    }

    private static void assertTokenizer(String text, JSONToken[] expectedTokens, Issue expectedIssue)
    {
        assertTokenizer(text, expectedTokens, new Issue[] { expectedIssue });
    }

    private static void assertTokenizer(String text, JSONToken[] expectedTokens, Issue[] expectedIssues)
    {
        assertTokenizerWithoutIssues(text, expectedTokens);
        assertTokenizerWithIssues(text, expectedTokens, expectedIssues);
    }

    private static void assertTokenizerWithoutIssues(String text, JSONToken[] expectedTokens)
    {
        final JSONTokenizer tokenizer = new JSONTokenizer(text);
        final Array<JSONToken> actualTokens = Array.fromValues(tokenizer);
        final int tokensToCompare = Math.minimum(expectedTokens.length, actualTokens.getCount());
        for (int i = 0; i < tokensToCompare; ++i)
        {
            final JSONToken expectedToken = expectedTokens[i];
            final JSONToken actualToken = actualTokens.get(i);
            assertEquals(expectedToken, actualToken);
        }
        assertEquals(expectedTokens.length, actualTokens.getCount());
    }

    private static void assertTokenizerWithIssues(String text, JSONToken[] expectedTokens, Issue[] expectedIssues)
    {
        final List<Issue> actualIssues = new ArrayList<>();
        final JSONTokenizer tokenizer = new JSONTokenizer(text, actualIssues);
        final Array<JSONToken> actualTokens = Array.fromValues(tokenizer);

        final int tokensToCompare = Math.minimum(expectedTokens.length, actualTokens.getCount());
        for (int i = 0; i < tokensToCompare; ++i)
        {
            final JSONToken expectedToken = expectedTokens[i];
            final JSONToken actualToken = actualTokens.get(i);
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
