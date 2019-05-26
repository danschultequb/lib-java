package qub;

/**
 * A Gate concurrency primitive that can be closed (all threads block) or opened (all threads pass).
 */
public class SpinGate implements Gate
{
    private final Clock clock;
    private final java.util.concurrent.atomic.AtomicBoolean open;

    /**
     * Create a new SpinGate with the provided initial state.
     * @param open Whether or not this SpinGate is initialized to open or closed.
     */
    public SpinGate(boolean open)
    {
        this(null, open);
    }

    /**
     * Create a new SpinGate with the provided initial state.
     * @param open Whether or not this SpinGate is initialized to open or closed.
     */
    public SpinGate(Clock clock, boolean open)
    {
        this.clock = clock;
        this.open = new java.util.concurrent.atomic.AtomicBoolean(open);
    }

    @Override
    public Clock getClock()
    {
        return clock;
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
    public Result<Void> passThrough()
    {
        return Result.create(() ->
        {
            while (!open.get())
            {
            }
        });
    }

    @Override
    public Result<Void> passThrough(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(getClock(), "getClock()");

        final Clock clock = getClock();
        Result<Void> result = null;
        while (result == null)
        {
            if (timeout.lessThanOrEqualTo(clock.getCurrentDateTime()))
            {
                result = Result.error(new TimeoutException());
            }
            else if (open.get())
            {
                result = Result.success();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
