package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONIssuesTests
{
    @Test
    public void constructor()
    {
        final JSONIssues issues = new JSONIssues();
        assertNotNull(issues);
    }

    @Test
    public void expectedEndOfFile()
    {
        assertEquals(
            Issue.error("Expected end of file.", 10, 1),
            JSONIssues.expectedEndOfFile(10, 1));
    }

    @Test
    public void missingClosingRightSquareBracket()
    {
        assertEquals(
            Issue.error("Missing closing right square bracket (']').", 11, 1),
            JSONIssues.missingClosingRightSquareBracket(11, 1));
    }

    @Test
    public void expectedCommaOrClosingRightSquareBracket()
    {
        assertEquals(
            Issue.error("Expected comma (',') or closing right square bracket (']').", 12, 4),
            JSONIssues.expectedCommaOrClosingRightSquareBracket(12, 4));
    }

    @Test
    public void expectedArrayElementOrClosingRightSquareBracket()
    {
        assertEquals(
            Issue.error("Expected array element or closing right square bracket (']').", 13, 1),
            JSONIssues.expectedArrayElementOrClosingRightSquareBracket(13, 1));
    }

    @Test
    public void expectedArrayElement()
    {
        assertEquals(
            Issue.error("Expected array element.", 14, 1),
            JSONIssues.expectedArrayElement(14, 1));
    }

    @Test
    public void expectedCommaOrClosingRightCurlyBracket()
    {
        assertEquals(
            Issue.error("Expected comma (',') or closing right curly bracket ('}').", 15, 3),
            JSONIssues.expectedCommaOrClosingRightCurlyBracket(15, 3));
    }

    @Test
    public void expectedPropertyName()
    {
        assertEquals(
            Issue.error("Expected property name.", 16, 4),
            JSONIssues.expectedPropertyName(16, 4));
    }

    @Test
    public void expectedPropertyNameOrClosingRightCurlyBracket()
    {
        assertEquals(
            Issue.error("Expected property name or closing right curly bracket ('}').", 17, 1),
            JSONIssues.expectedPropertyNameOrClosingRightCurlyBracket(17, 1));
    }

    @Test
    public void expectedColon()
    {
        assertEquals(
            Issue.error("Expected colon (':').", 18, 2),
            JSONIssues.expectedColon(18, 2));
    }

    @Test
    public void expectedPropertyValue()
    {
        assertEquals(
            Issue.error("Expected property value.", 19, 1),
            JSONIssues.expectedPropertyValue(19, 1));
    }
}
