package qub;

public class JSONTokenizerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JSONTokenizer", new Action0()
        {
            @Override
            public void run()
            {
                final Action3<String,JSONToken[],Issue[]> tokenizerTest = new Action3<String, JSONToken[], Issue[]>()
                {
                    @Override
                    public void run(final String text, final JSONToken[] expectedTokens, final Issue[] expectedIssues)
                    {
                        runner.test("with " + runner.escapeAndQuote(text) + " and no issues", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final JSONTokenizer tokenizer = new JSONTokenizer(text);
                                final Array<JSONToken> actualTokens = Array.fromValues(tokenizer);
                                test.assertEqual(Array.fromValues(expectedTokens), actualTokens);
                            }
                        });
                        
                        runner.test("with " + runner.escapeAndQuote(text) + " and issues", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final List<Issue> actualIssues = new ArrayList<>();
                                final JSONTokenizer tokenizer = new JSONTokenizer(text, actualIssues);
                                final Array<JSONToken> actualTokens = Array.fromValues(tokenizer);
                                test.assertEqual(Array.fromValues(expectedTokens), actualTokens);
                                test.assertEqual(Array.fromValues(expectedIssues), actualIssues);
                            }
                        });
                    }
                };

                tokenizerTest.run(
                    null,
                    new JSONToken[0],
                    new Issue[0]);

                tokenizerTest.run(
                    "",
                    new JSONToken[0],
                    new Issue[0]);

                tokenizerTest.run(
                    "{",
                    new JSONToken[] { JSONToken.leftCurlyBracket(0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "}",
                    new JSONToken[] { JSONToken.rightCurlyBracket(0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "[",
                    new JSONToken[] { JSONToken.leftSquareBracket(0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "]",
                    new JSONToken[] { JSONToken.rightSquareBracket(0) },
                    new Issue[0]);

                tokenizerTest.run(
                    ":",
                    new JSONToken[] { JSONToken.colon(0) },
                    new Issue[0]);

                tokenizerTest.run(
                    ",",
                    new JSONToken[] { JSONToken.comma(0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "true",
                    new JSONToken[] { JSONToken.trueToken("true", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "TRUE",
                    new JSONToken[] { JSONToken.trueToken("TRUE", 0) },
                    new Issue[] { JSONIssues.shouldBeLowercased(0, 4) });

                tokenizerTest.run(
                    "falSE",
                    new JSONToken[] { JSONToken.falseToken("falSE", 0) },
                    new Issue[] { JSONIssues.shouldBeLowercased(0, 5) });

                tokenizerTest.run(
                    "false",
                    new JSONToken[] { JSONToken.falseToken("false", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "null",
                    new JSONToken[] { JSONToken.nullToken("null", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "NULL",
                    new JSONToken[] { JSONToken.nullToken("NULL", 0) },
                    new Issue[] { JSONIssues.shouldBeLowercased(0, 4) });

                tokenizerTest.run(
                    "\'",
                    new JSONToken[] { JSONToken.quotedString("\'", 0, false), },
                    new Issue[] { JSONIssues.missingEndQuote("\'", 0, 1) });

                tokenizerTest.run(
                    "\'test",
                    new JSONToken[] { JSONToken.quotedString("\'test", 0, false) },
                    new Issue[] { JSONIssues.missingEndQuote("\'", 0, 5) });

                tokenizerTest.run(
                    "\'\\\'",
                    new JSONToken[] { JSONToken.quotedString("\'\\\'", 0, false) },
                    new Issue[] { JSONIssues.missingEndQuote("\'", 0, 3) });

                tokenizerTest.run(
                    "\'\'",
                    new JSONToken[] { JSONToken.quotedString("\'\'", 0, true) },
                    new Issue[0]);

                tokenizerTest.run(
                    "\"",
                    new JSONToken[] { JSONToken.quotedString("\"", 0, false) },
                    new Issue[] { JSONIssues.missingEndQuote("\"", 0, 1) });

                tokenizerTest.run(
                    "\"\"",
                    new JSONToken[] { JSONToken.quotedString("\"\"", 0, true) },
                    new Issue[0]);

                tokenizerTest.run(
                    " ",
                    new JSONToken[] { JSONToken.whitespace(" ", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "\t",
                    new JSONToken[] { JSONToken.whitespace("\t", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "\r",
                    new JSONToken[] { JSONToken.whitespace("\r", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    " \t\r",
                    new JSONToken[] { JSONToken.whitespace(" \t\r", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "\n",
                    new JSONToken[] { JSONToken.newLine("\n", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "\r\n",
                    new JSONToken[] { JSONToken.newLine("\r\n", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "-",
                    new JSONToken[] { JSONToken.number("-", 0) },
                    new Issue[] { JSONIssues.missingWholeNumberDigits(0, 1) });

                tokenizerTest.run(
                    "-5",
                    new JSONToken[] { JSONToken.number("-5", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "-.",
                    new JSONToken[] { JSONToken.number("-.", 0) },
                    new Issue[]
                    {
                        JSONIssues.expectedWholeNumberDigits(1, 1),
                        JSONIssues.missingFractionalNumberDigits(1, 1)
                    });
                tokenizerTest.run(
                    "12.",
                    new JSONToken[] { JSONToken.number("12.", 0) },
                    new Issue[] { JSONIssues.missingFractionalNumberDigits(2, 1) });

                tokenizerTest.run(
                    "12.3",
                    new JSONToken[] { JSONToken.number("12.3", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "12.a",
                    new JSONToken[]
                    {
                        JSONToken.number("12.", 0),
                        JSONToken.unrecognized("a", 3)
                    },
                    new Issue[] { JSONIssues.expectedFractionalNumberDigits(3, 1) });

                tokenizerTest.run(
                    "10e",
                    new JSONToken[] { JSONToken.number("10e", 0) },
                    new Issue[] { JSONIssues.missingExponentNumberDigits(2, 1) });

                tokenizerTest.run(
                    "10e{",
                    new JSONToken[]
                    {
                        JSONToken.number("10e", 0),
                        JSONToken.leftCurlyBracket(3)
                    },
                    new Issue[] { JSONIssues.expectedExponentNumberDigits(3, 1) });

                tokenizerTest.run(
                    "10e5",
                    new JSONToken[] { JSONToken.number("10e5", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "10e-",
                    new JSONToken[] { JSONToken.number("10e-", 0) },
                    new Issue[] { JSONIssues.missingExponentNumberDigits(3, 1) });

                tokenizerTest.run(
                    "10e-5",
                    new JSONToken[] { JSONToken.number("10e-5", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "10e+",
                    new JSONToken[] { JSONToken.number("10e+", 0) },
                    new Issue[] { JSONIssues.missingExponentNumberDigits(3, 1) });

                tokenizerTest.run(
                    "10e+5",
                    new JSONToken[] { JSONToken.number("10e+5", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "10E+5",
                    new JSONToken[] { JSONToken.number("10E+5", 0) },
                    new Issue[] { JSONIssues.shouldBeLowercased(2, 1) });

                tokenizerTest.run(
                    "/",
                    new JSONToken[] { JSONToken.unrecognized("/", 0) },
                    new Issue[] { JSONIssues.missingCommentSlashOrAsterisk(0, 1) });

                tokenizerTest.run(
                    "//",
                    new JSONToken[] { JSONToken.lineComment("//", 0) },
                    new Issue[0]);

                tokenizerTest.run(
                    "/*",
                    new JSONToken[] { JSONToken.blockComment("/*", 0, false) },
                    new Issue[0]);

                tokenizerTest.run(
                    "/**",
                    new JSONToken[] { JSONToken.blockComment("/**", 0, false) },
                    new Issue[0]);

                tokenizerTest.run(
                    "/**/",
                    new JSONToken[] { JSONToken.blockComment("/**/", 0, true) },
                    new Issue[0]);

                tokenizerTest.run(
                    "/*\n*/",
                    new JSONToken[] { JSONToken.blockComment("/*\n*/", 0, true) },
                    new Issue[0]);

                tokenizerTest.run(
                    "/*/",
                    new JSONToken[] { JSONToken.blockComment("/*/", 0, false) },
                    new Issue[0]);

                tokenizerTest.run(
                    "/a",
                    new JSONToken[]
                        {
                            JSONToken.unrecognized("/", 0),
                            JSONToken.unrecognized("a", 1)
                        },
                    new Issue[] { JSONIssues.expectedCommentSlashOrAsterisk(1, 1) });

                tokenizerTest.run(
                    "&",
                    new JSONToken[] { JSONToken.unrecognized("&", 0) },
                    new Issue[0]);
            }
        });
    }
}
