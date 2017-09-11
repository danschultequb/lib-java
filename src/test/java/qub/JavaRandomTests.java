package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JavaRandomTests
{
    @Test
    public void getInteger()
    {
        final JavaRandom random = new JavaRandom();
        assertNotEquals(random.getInteger(), random.getInteger());
    }
}
