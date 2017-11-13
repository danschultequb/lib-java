package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class IssueTests
{
    @Test
    public void constructor()
    {
        final Issue issue = new Issue("test message", new Span(1, 2), Issue.Type.Warning);
        assertEquals("test message", issue.getMessage());
        assertEquals(new Span(1, 2), issue.getSpan());
        assertEquals(Issue.Type.Warning, issue.getType());
        assertEquals("Warning [1, 3): \"test message\"", issue.toString());
    }

    @Test
    public void warning()
    {
        final Issue issue = Issue.warning("abc", 7, 10);
        assertEquals("abc", issue.getMessage());
        assertEquals(new Span(7, 10), issue.getSpan());
        assertEquals(Issue.Type.Warning, issue.getType());
    }

    @Test
    public void error()
    {
        final Issue issue = Issue.error("abc", 7, 10);
        assertEquals("abc", issue.getMessage());
        assertEquals(new Span(7, 10), issue.getSpan());
        assertEquals(Issue.Type.Error, issue.getType());
    }
}
