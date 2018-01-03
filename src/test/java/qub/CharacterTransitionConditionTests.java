package qub;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterTransitionConditionTests
{
    @Test
    public void matches()
    {
        final CharacterTransitionCondition condition = new CharacterTransitionCondition('a');
        assertTrue(condition.matches('a'));
        assertFalse(condition.matches('b'));
    }
}
