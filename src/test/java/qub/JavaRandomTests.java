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
    }
}
