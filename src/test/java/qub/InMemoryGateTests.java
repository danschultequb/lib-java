package qub;

import org.junit.Test;

import static junit.framework.TestCase.*;

public class InMemoryGateTests
{
    @Test
    public void constructorWithTrue()
    {
        final InMemoryGate gate = new InMemoryGate(true);
        assertTrue(gate.isOpen());
    }

    @Test
    public void constructorWithFalse()
    {
        final InMemoryGate gate = new InMemoryGate(false);
        assertFalse(gate.isOpen());
    }

    @Test
    public void openWhenAlreadyOpen()
    {
        final InMemoryGate gate = new InMemoryGate(true);
        assertFalse(gate.open());
        assertTrue(gate.isOpen());
    }

    @Test
    public void openWhenClosed()
    {
        final InMemoryGate gate = new InMemoryGate(false);
        assertTrue(gate.open());
        assertTrue(gate.isOpen());
    }

    @Test
    public void closeWhenAlreadyClosed()
    {
        final InMemoryGate gate = new InMemoryGate(false);
        assertFalse(gate.close());
        assertFalse(gate.isOpen());
    }

    @Test
    public void closeWhenOpen()
    {
        final InMemoryGate gate = new InMemoryGate(true);
        assertTrue(gate.close());
        assertFalse(gate.isOpen());
    }
}
