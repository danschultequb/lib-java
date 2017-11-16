package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTests
{
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
