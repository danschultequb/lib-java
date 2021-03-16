package qub;

/**
 * A Gate concurrency primitive that can be closed (all threads block) or opened (all threads pass).
 */
public class SpinGate implements Gate
{
    private final Clock clock;
    private final BooleanValue open;

    private SpinGate(Clock clock, boolean isOpen)
    {
        this.clock = clock;
        this.open = BooleanValue.create(isOpen);
    }

    /**
     * Create a new SpinGate that cannot time out while attempting to pass through. The returned
     * SpinGate will be closed.
     * @return The new SpinGate.
     */
    public static SpinGate create()
    {
        return SpinGate.create(null);
    }

    /**
     * Create a new SpinGate that will use the provided clock to determine whether or not a pass
     * through attempt has timed out. The returned SpinGate will be closed.
     * @param clock The clock to use when determining whether or not a pass through attempt has
     *              timed out.
     */
    public static SpinGate create(Clock clock)
    {
        return SpinGate.create(clock, false);
    }

    /**
     * Create a new SpinGate that will be created in the provided open state.
     * @param isOpen Whether or not the returned SpinGate will be open.
     */
    public static SpinGate create(boolean isOpen)
    {
        return SpinGate.create(null, isOpen);
    }

    /**
     * Create a new SpinGate that will use the provided clock to determine whether or not a pass
     * through attempt has timed out.
     * @param clock The clock to use when determining whether or not a pass through attempt has
     *              timed out.
     * @param isOpen Whether or not the returned SpinGate will be open.
     */
    public static SpinGate create(Clock clock, boolean isOpen)
    {
        return new SpinGate(clock, isOpen);
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
        return this.passThrough(Action0.empty);
    }

    /**
     * Attempt to go through this Gate. If the Gate is closed, then any thread that attempts to go
     * through it will block. The provided Action0 will be invoked after each failed attempt to go
     * through the gate.
     * @param onWait The action to invoke after each failed attempt to go through the gate.
     */
    public Result<Void> passThrough(Action0 onWait)
    {
        PreCondition.assertNotNull(onWait, "onWait");

        return Result.create(() ->
        {
            while (!open.get())
            {
                onWait.run();
                java.lang.Thread.yield();
            }
        });
    }

    /**
     * Attempt to go through this Gate for the provided timeout duration. If the Gate is closed,
     * then any thread that attempts to go through it will block. The provided Action0 will be
     * invoked after each failed attempt to go through the gate.
     * @param timeout The amount of time to wait before this thread will abort waiting for this Gate
     *                to open.
     * @param onWait The action to invoke after each failed attempt to go through the gate.
     */
    public Result<Void> passThrough(Duration timeout, Action0 onWait)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(this.getClock(), "this.getClock()");
        PreCondition.assertNotNull(onWait, "onWait");

        final DateTime dateTimeTimeout = this.getClock().getCurrentDateTime().plus(timeout);
        return this.passThrough(dateTimeTimeout, onWait);
    }

    @Override
    public Result<Void> passThrough(DateTime timeout)
    {
        return this.passThrough(timeout, Action0.empty);
    }

    /**
     * Attempt to go through this Gate until the provided timeout DateTime. If the Gate is closed,
     * then any thread that attempts to go through it will block. The provided Action0 will be
     * invoked after each failed attempt to go through the gate.
     * @param timeout The DateTime at which this thread will abort waiting for this Gate to open.
     * @param onWait The action to invoke after each failed attempt to go through the gate.
     */
    public Result<Void> passThrough(DateTime timeout, Action0 onWait)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(this.getClock(), "this.getClock()");
        PreCondition.assertNotNull(onWait, "onWait");

        final Clock clock = this.getClock();
        return Result.create(() ->
        {
            while (true)
            {
                if (timeout.lessThanOrEqualTo(clock.getCurrentDateTime()))
                {
                    throw new TimeoutException();
                }
                else if (open.get())
                {
                    break;
                }
                else
                {
                    onWait.run();
                    java.lang.Thread.yield();
                }
            }
        });
    }
}
