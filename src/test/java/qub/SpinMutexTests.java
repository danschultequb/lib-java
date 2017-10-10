package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpinMutexTests
{
    @Test
    public void constructor()
    {
        final SpinMutex mutex = new SpinMutex();
        assertFalse(mutex.isAcquired());
    }

    @Test
    public void acquireWhenNotLocked()
    {
        final SpinMutex mutex = new SpinMutex();
        mutex.acquire();
        assertTrue(mutex.isAcquired());
    }

    @Test
    public void tryAcquireWhenNotLocked()
    {
        final SpinMutex mutex = new SpinMutex();
        assertTrue(mutex.tryAcquire());
        assertTrue(mutex.isAcquired());
    }

    @Test
    public void tryAcquireWhenLocked()
    {
        final SpinMutex mutex = new SpinMutex();
        mutex.acquire();
        assertFalse(mutex.tryAcquire());
        assertTrue(mutex.isAcquired());
    }

    @Test
    public void releaseWhenNotLocked()
    {
        final SpinMutex mutex = new SpinMutex();
        assertFalse(mutex.release());
        assertFalse(mutex.isAcquired());
    }

    @Test
    public void releaseWhenLocked()
    {
        final SpinMutex mutex = new SpinMutex();
        mutex.acquire();
        assertTrue(mutex.release());
        assertFalse(mutex.isAcquired());
    }
}
