package qub;

public class JSONTokenTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JSONToken", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final JSONToken token = new JSONToken("{", 17, JSONTokenType.LeftCurlyBracket);
                        test.assertEqual("{", token.toString());
                        test.assertEqual(17, token.getStartIndex());
                        test.assertEqual(1, token.getLength());
                        test.assertEqual(18, token.getAfterEndIndex());
                        test.assertEqual(new Span(17, 1), token.getSpan());
                        test.assertEqual(JSONTokenType.LeftCurlyBracket, token.getType());
                    }
                });

                runner.test("leftCurlyBracket()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.leftCurlyBracket(0), "{", 0, 1, JSONTokenType.LeftCurlyBracket);
                    }
                });

                runner.test("rightCurlyBracket()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.rightCurlyBracket(1), "}", 1, 1, JSONTokenType.RightCurlyBracket);
                    }
                });

                runner.test("leftSquareBracket()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.leftSquareBracket(2), "[", 2, 1, JSONTokenType.LeftSquareBracket);
                    }
                });

                runner.test("rightSquareBracket()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.rightSquareBracket(3), "]", 3, 1, JSONTokenType.RightSquareBracket);
                    }
                });

                runner.test("colon()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.colon(4), ":", 4, 1, JSONTokenType.Colon);
                    }
                });

                runner.test("comma()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.comma(5), ",", 5, 1, JSONTokenType.Comma);
                    }
                });

                runner.test("trueToken()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.trueToken("true", 5), "true", 5, 4, JSONTokenType.True);
                    }
                });

                runner.test("falseToken()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.falseToken("false", 6), "false", 6, 5, JSONTokenType.False);
                    }
                });

                runner.test("nullToken()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.nullToken("null", 7), "null", 7, 4, JSONTokenType.Null);
                    }
                });

                runner.test("newLine()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.newLine("\n", 8), "\n", 8, 1, JSONTokenType.NewLine);
                        assertToken(test, JSONToken.newLine("\r\n", 9), "\r\n", 9, 2, JSONTokenType.NewLine);
                    }
                });

                runner.test("quotedString()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertCloseableToken(test, JSONToken.quotedString("\'hello", 10, false), "\'hello", 10, 6, JSONTokenType.QuotedString, false);
                        assertCloseableToken(test, JSONToken.quotedString("\'hello\'", 11, true), "\'hello\'", 11, 7, JSONTokenType.QuotedString, true);
                        assertCloseableToken(test, JSONToken.quotedString("\"hello", 12, false), "\"hello", 12, 6, JSONTokenType.QuotedString, false);
                        assertCloseableToken(test, JSONToken.quotedString("\"hello\"", 13, true), "\"hello\"", 13, 7, JSONTokenType.QuotedString, true);
                    }
                });

                runner.test("number()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.number("123", 14), "123", 14, 3, JSONTokenType.Number);
                    }
                });

                runner.test("whitespace()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.whitespace("  \t", 15), "  \t", 15, 3, JSONTokenType.Whitespace);
                    }
                });

                runner.test("lineComment()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.lineComment("//", 16), "//", 16, 2, JSONTokenType.LineComment);
                    }
                });

                runner.test("blockComment()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertCloseableToken(test, JSONToken.blockComment("/*", 17, false), "/*", 17, 2, JSONTokenType.BlockComment, false);
                    }
                });

                runner.test("unrecognized()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertToken(test, JSONToken.unrecognized("&", 18), "&", 18, 1, JSONTokenType.Unrecognized);
                    }
                });
            }
        });
    }

    private static void assertToken(Test test, JSONToken token, String text, int startIndex, int length, JSONTokenType type)
    {
        test.assertEqual(text, token.toString());
        test.assertEqual(new Span(startIndex, length), token.getSpan());
        test.assertEqual(type, token.getType());
    }

    private static void assertCloseableToken(Test test, JSONCloseableToken token, String text, int startIndex, int length, JSONTokenType type, boolean closed)
    {
        assertToken(test, token, text, startIndex, length, type);
        test.assertEqual(closed, token.isClosed());
    }
}
