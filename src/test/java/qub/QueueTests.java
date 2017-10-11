package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class QueueTests
{
    protected abstract Queue<Integer> createQueue();

    @Test
    public void enqueue()
    {
        final Queue<Integer> queue = createQueue();
        assertNull(queue.dequeue());
        assertFalse(queue.any());

        queue.enqueue(0);
        assertTrue(queue.any());
        assertEquals(1, queue.getCount());

        queue.enqueue(1);
        assertTrue(queue.any());
        assertEquals(2, queue.getCount());

        assertEquals(0, queue.dequeue().intValue());
        assertTrue(queue.any());
        assertEquals(1, queue.getCount());

        assertEquals(1, queue.dequeue().intValue());
        assertFalse(queue.any());
        assertEquals(0, queue.getCount());

        assertNull(queue.dequeue());
        assertFalse(queue.any());
        assertEquals(0, queue.getCount());
    }

    @Test
    public void peek()
    {
        final Queue<Integer> queue = createQueue();
        assertNull(queue.peek());

        queue.enqueue(20);
        assertEquals(20, queue.peek().intValue());

        queue.enqueue(21);
        assertEquals(20, queue.peek().intValue());

        assertEquals(20, queue.dequeue().intValue());
        assertEquals(21, queue.peek().intValue());
    }
}
