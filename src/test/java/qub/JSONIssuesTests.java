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
}
