package qub;

/**
 * A factory class that is used to create synchronization objects.
 */
public class Synchronization
{
    private Synchronization()
    {
    }

    /**
     * Create a new {@link Synchronization} factory.
     */
    public static Synchronization create()
    {
        return new Synchronization();
    }

    /**
     * Create a new Gate object with the provided initial open state.
     * @param isOpen Whether or not the created Gate will be open.
     * @return The created Gate.
     */
    public Gate createGate(boolean isOpen)
    {
        return SpinGate.create(isOpen);
    }

    /**
     * Create a new Gate object with the provided initial open state.
     * @param isOpen Whether or not the created Gate will be open.
     * @return The created Gate.
     */
    public Gate createGate(Clock clock, boolean isOpen)
    {
        PreCondition.assertNotNull(clock, "clock");

        return SpinGate.create(clock, isOpen);
    }
}
