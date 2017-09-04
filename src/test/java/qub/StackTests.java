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

    @Test
    public void iterate()
    {
        final Stack<Double> stack = new Stack<>();
        final Iterator<Double> iterator1 = stack.iterate();
        assertFalse(iterator1.hasStarted());
        assertFalse(iterator1.hasCurrent());
        assertNull(iterator1.getCurrent());

        assertFalse(iterator1.next());
        assertTrue(iterator1.hasStarted());
        assertFalse(iterator1.hasCurrent());
        assertNull(iterator1.getCurrent());

        stack.push(1.2);
        stack.push(3.4);
        stack.push(5.6);

        final Iterator<Double> iterator2 = stack.iterate();
        assertFalse(iterator2.hasStarted());
        assertFalse(iterator2.hasCurrent());
        assertNull(iterator2.getCurrent());

        assertTrue(iterator2.next());
        assertTrue(iterator2.hasStarted());
        assertTrue(iterator2.hasCurrent());
        assertEquals(5.6, iterator2.getCurrent(), 0);

        assertTrue(iterator2.next());
        assertTrue(iterator2.hasStarted());
        assertTrue(iterator2.hasCurrent());
        assertEquals(3.4, iterator2.getCurrent(), 0);

        assertTrue(iterator2.next());
        assertTrue(iterator2.hasStarted());
        assertTrue(iterator2.hasCurrent());
        assertEquals(1.2, iterator2.getCurrent(), 0);

        assertFalse(iterator2.next());
        assertTrue(iterator2.hasStarted());
        assertFalse(iterator2.hasCurrent());
        assertNull(iterator2.getCurrent());
    }
}
