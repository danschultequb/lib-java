package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexTypeTests
{
    @Test
    public void valueOf()
    {
        assertEquals(LexType.LeftCurlyBracket, LexType.valueOf("LeftCurlyBracket"));
    }
}
