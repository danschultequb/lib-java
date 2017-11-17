package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTests
{
    @Test
    public void constructor()
    {
        final JSON json = new JSON();
        assertNotNull(json);
    }

    @Test
    public void parse()
    {
        parseTest(null);
        parseTest("");

        parseTest(" ",
            new JSONSegment[]
            {
                JSONToken.whitespace(" ", 0)
            });
        parseTest("\t",
            new JSONSegment[]
                {
                    JSONToken.whitespace("\t", 0)
                });
        parseTest("\r",
            new JSONSegment[]
                {
                    JSONToken.whitespace("\r", 0)
                });
        parseTest("true",
            new JSONSegment[]
            {
                JSONToken.trueToken("true", 0)
            });
        parseTest("false",
            new JSONSegment[]
            {
                JSONToken.falseToken("false", 0)
            });
        parseTest("123.456e-10",
            new JSONSegment[]
            {
               JSONToken.number("123.456e-10", 0)
            });
        parseTest("// hello",
            new JSONSegment[]
            {
                JSONToken.lineComment("// hello", 0)
            });
        parseTest("/* hello */",
            new JSONSegment[]
            {
                JSONToken.blockComment("/* hello */", 0, true)
            });
        parseTest("{",
            new JSONSegment[]
            {
                JSON.parseObject("{", 0)
            },
            new Issue[]
            {
                JSONIssues.missingClosingRightCurlyBracket(0, 1)
            });
        parseTest("123{}",
            new JSONSegment[]
            {
                JSONToken.number("123"),
                JSON.parseObject("{}", 3)
            },
            new Issue[]
            {
               JSONIssues.expectedEndOfFile(3, 2)
            });
        parseTest("{}123",
            new JSONSegment[]
            {
                JSON.parseObject("{}"),
                JSONToken.number("123", 2)
            },
            new Issue[]
            {
                JSONIssues.expectedEndOfFile(2, 3)
            });
        parseTest("{\"a\":0\"b\":1}",
            new JSONSegment[]
            {
               JSON.parseObject("{\"a\":0\"b\":1}")
            },
            new Issue[]
            {
                JSONIssues.expectedCommaOrClosingRightCurlyBracket(6, 3)
            });
        parseTest("{\"a\":0,}",
            new JSONSegment[]
            {
                JSON.parseObject("{\"a\":0,}")
            },
            new Issue[]
            {
                JSONIssues.expectedPropertyName(7, 1)
            });
        parseTest("{,\"a\":0}",
            new JSONSegment[]
            {
                JSON.parseObject("{,\"a\":0}")
            },
            new Issue[]
            {
                JSONIssues.expectedPropertyNameOrClosingRightCurlyBracket(1, 1)
            });
        parseTest("{,,}",
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
        parseTest("{123}",
            new JSONSegment[]
            {
                JSON.parseObject("{123}")
            },
            new Issue[]
            {
                JSONIssues.expectedPropertyNameOrClosingRightCurlyBracket(1, 3)
            });
        parseTest("{\"a\":1,false}",
            new JSONSegment[]
            {
                JSON.parseObject("{\"a\":1,false}")
            },
            new Issue[]
            {
                JSONIssues.expectedPropertyName(7, 5)
            });
        parseTest("{\"a\":1false}",
            new JSONSegment[]
            {
                JSON.parseObject("{\"a\":1false}")
            },
            new Issue[]
            {
                JSONIssues.expectedCommaOrClosingRightCurlyBracket(6, 5)
            });
        parseTest("{\"a\"1}",
            new JSONSegment[]
            {
                JSON.parseObject("{\"a\"1}")
            },
            new Issue[]
            {
                JSONIssues.expectedColon(4, 1),
                JSONIssues.expectedCommaOrClosingRightCurlyBracket(4, 1)
            });
        parseTest("{\"a\":[]}",
            new JSONSegment[]
            {
                JSON.parseObject("{\"a\":[]}")
            });
        parseTest("{\"a\"::}",
            new JSONSegment[]
            {
                JSON.parseObject("{\"a\"::}")
            },
            new Issue[]
            {
                JSONIssues.expectedPropertyValue(5, 1),
                JSONIssues.expectedCommaOrClosingRightCurlyBracket(5, 1)
            });
        parseTest("[",
            new JSONSegment[]
            {
                JSON.parseArray("[")
            },
            new Issue[]
            {
               JSONIssues.missingClosingRightSquareBracket(0, 1)
            });
        parseTest("[]",
            new JSONSegment[]
            {
                JSON.parseArray("[]")
            });
        parseTest("[false true]",
            new JSONSegment[]
            {
                JSON.parseArray("[false true]")
            },
            new Issue[]
            {
               JSONIssues.expectedCommaOrClosingRightSquareBracket(7, 4)
            });
        parseTest("[{}]",
            new JSONSegment[]
            {
                JSON.parseArray("[{}]")
            });
        parseTest("[false{}]",
            new JSONSegment[]
            {
                JSON.parseArray("[false{}]")
            },
            new Issue[]
            {
                JSONIssues.expectedCommaOrClosingRightSquareBracket(6, 1)
            });
        parseTest("[[]]",
            new JSONSegment[]
            {
                JSON.parseArray("[[]]")
            });
        parseTest("[false[]]",
            new JSONSegment[]
            {
                JSON.parseArray("[false[]]")
            },
            new Issue[]
            {
                JSONIssues.expectedCommaOrClosingRightSquareBracket(6, 1)
            });
        parseTest("[,]",
            new JSONSegment[]
            {
                JSON.parseArray("[,]")
            },
            new Issue[]
            {
                JSONIssues.expectedArrayElementOrClosingRightSquareBracket(1, 1),
                JSONIssues.expectedArrayElement(2, 1)
            });
        parseTest("[,,]",
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
        parseTest("[$]",
            new JSONSegment[]
            {
                JSON.parseArray("[$]")
            },
            new Issue[]
            {
                JSONIssues.expectedArrayElementOrClosingRightSquareBracket(1, 1)
            });
        parseTest("[true$]",
            new JSONSegment[]
            {
                JSON.parseArray("[true$]")
            },
            new Issue[]
            {
                JSONIssues.expectedCommaOrClosingRightSquareBracket(5, 1)
            });
        parseTest("[true,$]",
            new JSONSegment[]
            {
                JSON.parseArray("[true,$]")
            },
            new Issue[]
            {
                JSONIssues.expectedArrayElement(6, 1)
            });
    }

    private static void parseTest(String text)
    {
        parseTest(text, new JSONSegment[0]);
    }

    private static void parseTest(String text, Issue[] expectedIssues)
    {
        parseTest(text, new JSONSegment[0], expectedIssues);
    }

    private static void parseTest(String text, JSONSegment[] expectedDocumentSegments)
    {
        parseTest(text, expectedDocumentSegments, new Issue[0]);
    }

    private static void parseTest(String text, JSONSegment[] expectedDocumentSegments, Issue[] expectedIssues)
    {
        final JSONDocument expectedDocument = new JSONDocument(Array.fromValues(expectedDocumentSegments));
        final JSONDocument actualDocumentWithoutErrors = JSON.parse(text);
        assertEqualDocuments(expectedDocument, actualDocumentWithoutErrors);

        final List<Issue> actualIssues = new ArrayList<>();
        final JSONDocument actualDocumentWithErrors = JSON.parse(text, actualIssues);
        assertEqualDocuments(expectedDocument, actualDocumentWithErrors);
        assertEqualIterables(Array.fromValues(expectedIssues), actualIssues);
    }

    private static void assertEqualDocuments(JSONDocument expectedDocument, JSONDocument actualDocument)
    {
        assertEqualIterables(expectedDocument.getSegments(), actualDocument.getSegments());
    }

    private static <T> void assertEqualIterables(Iterable<T> lhs, Iterable<T> rhs)
    {
        final Iterator<T> lhsIterator = lhs.iterate();
        final Iterator<T> rhsIterator = rhs.iterate();
        while (true)
        {
            lhsIterator.next();
            rhsIterator.next();
            if (!lhsIterator.hasCurrent() || !rhsIterator.hasCurrent())
            {
                break;
            }
            else
            {
                assertEquals(lhsIterator.getCurrent(), rhsIterator.getCurrent());
            }
        }
        assertEquals("Expected " + lhs.getCount() + " elements, but found " + rhs.getCount() + ".",
            lhsIterator.hasCurrent(),
            rhsIterator.hasCurrent());
    }
}
