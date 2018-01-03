package qub;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathSeparatorTransitionConditionTests
{
    @Test
    public void matches()
    {
        final PathSeparatorTransitionCondition condition = new PathSeparatorTransitionCondition();
        assertTrue(condition.matches('/'));
        assertTrue(condition.matches('\\'));
        assertFalse(condition.matches('a'));
    }
}
