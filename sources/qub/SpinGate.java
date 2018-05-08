package qub;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Gate concurrency primitive that can be closed (all threads block) or opened (all threads pass).
 */
public class SpinGate implements Gate
{
    private final AtomicBoolean open;

    /**
     * Create a new SpinGate with the provided initial state.
     * @param open Whether or not this SpinGate is initialized to open or closed.
     */
    public SpinGate(boolean open)
    {
        this.open = new AtomicBoolean(open);
    }

    @Override
    public boolean isOpen()
    {
        return open.get();
    }

    @Override
    public boolean open()
    {
        return open.compareAndSet(false, true);
    }

    @Override
    public boolean close()
    {
        return open.compareAndSet(true, false);
    }

    @Override
    public void passThrough()
    {
        while (!open.get())
        {
        }
    }
}
