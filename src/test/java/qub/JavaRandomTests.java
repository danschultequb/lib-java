package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JavaRandomTests
{
    @Test
    public void getRandomInteger()
    {
        final JavaRandom random = new JavaRandom();
        assertNotEquals(random.getRandomInteger(), random.getRandomInteger());

        for (int i = 0; i < 100; ++i)
        {
            assertTrue(0 <= random.getRandomInteger());
        }
    }
}
