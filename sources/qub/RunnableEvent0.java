package qub;

/**
 * An event that will be run with no arguments.
 */
public interface RunnableEvent0 extends Event0, Action0
{
    /**
     * Create a new runnable event.
     * @return A new runnable event.
     */
    static RunnableEvent0 create()
    {
        return BasicRunnableEvent0.create();
    }

    /**
     * Run the callbacks that have been registered to this event.
     */
    void run();
}
