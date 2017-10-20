package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class StackTests
{
    @Test
    public void constructor()
    {
        final Stack<Double> stack = new Stack<>();
        assertFalse(stack.any());
        assertEquals(0, stack.getCount());
    }

    @Test
    public void push()
    {
        final Stack<Double> stack = new Stack<>();
        stack.push(5.6);
        assertTrue(stack.any());
        assertEquals(1, stack.getCount());
        assertEquals(5.6, stack.peek().doubleValue(), 0);
    }

    @Test
    public void pop()
    {
        final Stack<Double> stack = new Stack<>();
        assertNull(stack.pop());

        stack.push(1.2);
        stack.push(3.4);
        assertEquals(3.4, stack.pop(), 0);
        assertEquals(1, stack.getCount());

        assertEquals(1.2, stack.pop(), 0);
        assertEquals(0, stack.getCount());

        assertNull(stack.pop());
    }

    @Test
    public void peek()
    {
        final Stack<Double> stack = new Stack<>();
        assertNull(stack.peek());

        stack.push(1.2);
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(1.2, stack.peek(), 0);
        }

        stack.push(3.4);
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(3.4, stack.peek(), 0);
        }

        stack.pop();
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(1.2, stack.peek(), 0);
        }
    }
}
