package qub;

/**
 * A factory class that is used to create synchronization objects.
 */
public class Synchronization
{
    /**
     * Create a new Synchronization factory.
     */
    public Synchronization()
    {
    }

    /**
     * Create a new Gate object with the provided initial open state.
     * @param isOpen Whether or not the created Gate will be open.
     * @return The created Gate.
     */
    public Gate createGate(boolean isOpen)
    {
        return new SpinGate(isOpen);
    }
}
