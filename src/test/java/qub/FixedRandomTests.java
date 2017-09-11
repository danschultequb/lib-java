package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FixedRandomTests
{
    @Test
    public void constructor()
    {
        final FixedRandom random = new FixedRandom(7);
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(7, random.getInteger());
        }
    }
}
