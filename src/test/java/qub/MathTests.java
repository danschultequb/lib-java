package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class MathTests
{
    @Test
    public void constructor()
    {
        new Math();
    }

    @Test
    public void minimum()
    {
        assertEquals(-10, Math.minimum(-10, -5));
        assertEquals(-10, Math.minimum(-5, -10));

        assertEquals(-3, Math.minimum(-3, -3));

        assertEquals(-1, Math.minimum(-1, 0));
        assertEquals(-1, Math.minimum(0, -1));
    }

    @Test
    public void maximum()
    {
        assertEquals(-5, Math.maximum(-10, -5));
        assertEquals(-5, Math.maximum(-5, -10));

        assertEquals(-3, Math.maximum(-3, -3));

        assertEquals(0, Math.maximum(-1, 0));
        assertEquals(0, Math.maximum(0, -1));
    }

    @Test
    public void isOdd() {
        assertTrue(Math.isOdd(1));
        assertTrue(Math.isOdd(3));
        assertTrue(Math.isOdd(-1));

        assertFalse(Math.isOdd(0));
        assertFalse(Math.isOdd(2));
        assertFalse(Math.isOdd(-2));

        assertFalse(Math.isOdd.run(null));

        assertTrue(Math.isOdd.run(1));
        assertTrue(Math.isOdd.run(3));
        assertTrue(Math.isOdd.run(-1));

        assertFalse(Math.isOdd.run(0));
        assertFalse(Math.isOdd.run(2));
        assertFalse(Math.isOdd.run(-2));
    }

    @Test
    public void isEven() {
        assertFalse(Math.isEven(1));
        assertFalse(Math.isEven(3));
        assertFalse(Math.isEven(-1));

        assertTrue(Math.isEven(0));
        assertTrue(Math.isEven(2));
        assertTrue(Math.isEven(-2));

        assertFalse(Math.isEven.run(null));

        assertFalse(Math.isEven.run(1));
        assertFalse(Math.isEven.run(3));
        assertFalse(Math.isEven.run(-1));

        assertTrue(Math.isEven.run(0));
        assertTrue(Math.isEven.run(2));
        assertTrue(Math.isEven.run(-2));
    }
}
