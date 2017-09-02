package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class MathTests
{
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
