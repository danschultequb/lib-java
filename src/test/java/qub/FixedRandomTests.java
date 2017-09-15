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
        final FixedRandom r0 = new FixedRandom(0);
        assertEquals(0, r0.getRandomIntegerBetween(0, 9));
        assertEquals(1, r0.getRandomIntegerBetween(1, 3));
        assertEquals(11, r0.getRandomIntegerBetween(11, 11));
        assertEquals(7, r0.getRandomIntegerBetween(10, 7));

        final FixedRandom r10 = new FixedRandom(10);
        assertEquals(0, r0.getRandomIntegerBetween(0, 9));
        assertEquals(1, r0.getRandomIntegerBetween(1, 10));
        assertEquals(1, r0.getRandomIntegerBetween(1, 3));
        assertEquals(11, r0.getRandomIntegerBetween(11, 11));
        assertEquals(7, r0.getRandomIntegerBetween(10, 7));
    }
}
