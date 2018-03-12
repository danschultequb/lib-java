package qub;

public class JSONTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JSON", () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final JSON json = new JSON();
                test.assertNotNull(json);
            });

            runner.testGroup("parse()", () ->
            {
                final Action3<String,JSONSegment[],Issue[]> parseTest = (String text, JSONSegment[] expectedDocumentSegments, Issue[] expectedIssues) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final JSONDocument expectedDocument = new JSONDocument(Array.fromValues(expectedDocumentSegments));
                        final JSONDocument actualDocumentWithoutErrors = JSON.parse(text);
                        test.assertEqual(expectedDocument.getSegments(), actualDocumentWithoutErrors.getSegments());

                        final List<Issue> actualIssues = new ArrayList<>();
                        final JSONDocument actualDocumentWithErrors = JSON.parse(text, actualIssues);
                        test.assertEqual(expectedDocument.getSegments(), actualDocumentWithErrors.getSegments());
                        test.assertEqual(Array.fromValues(expectedIssues), actualIssues);
                    });
                };

                parseTest.run(null,
                    new JSONSegment[0],
                    new Issue[0]);
                parseTest.run("",
                    new JSONSegment[0],
                    new Issue[0]);

                parseTest.run(" ",
                    new JSONSegment[]
                        {
                            JSONToken.whitespace(" ", 0)
                        },
                    new Issue[0]);
                parseTest.run("\t",
                    new JSONSegment[]
                        {
                            JSONToken.whitespace("\t", 0)
                        },
                    new Issue[0]);
                parseTest.run("\r",
                    new JSONSegment[]
                        {
                            JSONToken.whitespace("\r", 0)
                        },
                    new Issue[0]);
                parseTest.run("true",
                    new JSONSegment[]
                        {
                            JSONToken.trueToken("true", 0)
                        },
                    new Issue[0]);
                parseTest.run("false",
                    new JSONSegment[]
                        {
                            JSONToken.falseToken("false", 0)
                        },
                    new Issue[0]);
                parseTest.run("123.456e-10",
                    new JSONSegment[]
                        {
                            JSONToken.number("123.456e-10", 0)
                        },
                    new Issue[0]);
                parseTest.run("// hello",
                    new JSONSegment[]
                        {
                            JSONToken.lineComment("// hello", 0)
                        },
                    new Issue[0]);
                parseTest.run("/* hello */",
                    new JSONSegment[]
                        {
                            JSONToken.blockComment("/* hello */", 0, true)
                        },
                    new Issue[0]);
                parseTest.run("{",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{", 0)
                        },
                    new Issue[]
                        {
                            JSONIssues.missingClosingRightCurlyBracket(0, 1)
                        });
                parseTest.run("123{}",
                    new JSONSegment[]
                        {
                            JSONToken.number("123"),
                            JSON.parseObject("{}", 3)
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedEndOfFile(3, 2)
                        });
                parseTest.run("{}123",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{}"),
                            JSONToken.number("123", 2)
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedEndOfFile(2, 3)
                        });
                parseTest.run("{\"a\":0\"b\":1}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\":0\"b\":1}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedCommaOrClosingRightCurlyBracket(6, 3)
                        });
                parseTest.run("{\"a\":0,}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\":0,}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedPropertyName(7, 1)
                        });
                parseTest.run("{,\"a\":0}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{,\"a\":0}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedPropertyNameOrClosingRightCurlyBracket(1, 1)
                        });
                parseTest.run("{,,}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{,,}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedPropertyNameOrClosingRightCurlyBracket(1, 1),
                            JSONIssues.expectedPropertyName(2, 1),
                            JSONIssues.expectedPropertyName(3, 1)
                        });
                parseTest.run("{123}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{123}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedPropertyNameOrClosingRightCurlyBracket(1, 3)
                        });
                parseTest.run("{\"a\":1,false}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\":1,false}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedPropertyName(7, 5)
                        });
                parseTest.run("{\"a\":1false}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\":1false}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedCommaOrClosingRightCurlyBracket(6, 5)
                        });
                parseTest.run("{\"a\"1}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\"1}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedColon(4, 1),
                            JSONIssues.expectedCommaOrClosingRightCurlyBracket(4, 1)
                        });
                parseTest.run("{\"a\":[]}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\":[]}")
                        },
                    new Issue[0]);
                parseTest.run("{\"a\"::}",
                    new JSONSegment[]
                        {
                            JSON.parseObject("{\"a\"::}")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedPropertyValue(5, 1),
                            JSONIssues.expectedCommaOrClosingRightCurlyBracket(5, 1)
                        });
                parseTest.run("[",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[")
                        },
                    new Issue[]
                        {
                            JSONIssues.missingClosingRightSquareBracket(0, 1)
                        });
                parseTest.run("[]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[]")
                        },
                    new Issue[0]);
                parseTest.run("[false true]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[false true]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedCommaOrClosingRightSquareBracket(7, 4)
                        });
                parseTest.run("[{}]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[{}]")
                        },
                    new Issue[0]);
                parseTest.run("[false{}]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[false{}]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedCommaOrClosingRightSquareBracket(6, 1)
                        });
                parseTest.run("[[]]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[[]]")
                        },
                    new Issue[0]);
                parseTest.run("[false[]]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[false[]]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedCommaOrClosingRightSquareBracket(6, 1)
                        });
                parseTest.run("[,]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[,]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedArrayElementOrClosingRightSquareBracket(1, 1),
                            JSONIssues.expectedArrayElement(2, 1)
                        });
                parseTest.run("[,,]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[,,]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedArrayElementOrClosingRightSquareBracket(1, 1),
                            JSONIssues.expectedArrayElement(2, 1),
                            JSONIssues.expectedArrayElement(3, 1)
                        });
                parseTest.run("[$]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[$]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedArrayElementOrClosingRightSquareBracket(1, 1)
                        });
                parseTest.run("[true$]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[true$]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedCommaOrClosingRightSquareBracket(5, 1)
                        });
                parseTest.run("[true,$]",
                    new JSONSegment[]
                        {
                            JSON.parseArray("[true,$]")
                        },
                    new Issue[]
                        {
                            JSONIssues.expectedArrayElement(6, 1)
                        });
            });
        });
    }
}
