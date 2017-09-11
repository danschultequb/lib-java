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
            assertEquals(7, random.getRandomInteger());
        }
    }

    @Test
    public void getRandomIntegerBetween()
    {
        final FixedRandom random = new FixedRandom(0);
        assertEquals(0, random.getRandomIntegerBetween(0, 9));
        assertEquals(1, random.getRandomIntegerBetween(1, 3));
        assertEquals(11, random.getRandomIntegerBetween(11, 11));

        assertEquals(7, random.getRandomIntegerBetween(10, 7));
    }
}
