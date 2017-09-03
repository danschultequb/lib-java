package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueueTests
{
    @Test
    public void constructor()
    {
        final Queue<Boolean> queue = new Queue<>();
        assertEquals(0, queue.getCount());
        assertFalse(queue.any());
    }

    @Test
    public void enqueue()
    {
        final Queue<Boolean> queue = new Queue<>();
        assertNull(queue.dequeue());
        assertFalse(queue.any());

        queue.enqueue(false);
        assertTrue(queue.any());
        assertEquals(1, queue.getCount());

        queue.enqueue(true);
        assertTrue(queue.any());
        assertEquals(2, queue.getCount());

        assertFalse(queue.dequeue());
        assertTrue(queue.any());
        assertEquals(1, queue.getCount());

        assertTrue(queue.dequeue());
        assertFalse(queue.any());
        assertEquals(0, queue.getCount());

        assertNull(queue.dequeue());
        assertFalse(queue.any());
        assertEquals(0, queue.getCount());
    }

    @Test
    public void peek()
    {
        final Queue<Boolean> queue = new Queue<>();
        assertNull(queue.peek());

        queue.enqueue(false);
        assertFalse(queue.peek());

        queue.enqueue(true);
        assertFalse(queue.peek());

        assertEquals(false, queue.dequeue());
        assertEquals(true, queue.peek());
    }

    @Test
    public void iterate()
    {
        final Queue<Boolean> queue = new Queue<>();
        final Iterator<Boolean> iterator1 = queue.iterate();
        assertFalse(iterator1.hasStarted());
        assertFalse(iterator1.hasCurrent());
        assertNull(iterator1.getCurrent());

        assertFalse(iterator1.next());
        assertTrue(iterator1.hasStarted());
        assertFalse(iterator1.hasCurrent());
        assertNull(iterator1.getCurrent());
    }
}
