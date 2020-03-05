package qub;

/**
 * An event that will be run with one argument.
 */
public interface RunnableEvent1<T> extends Event1<T>, Action1<T>
{
    /**
     * Create a new event.
     */
    static <T> BasicRunnableEvent1<T> create()
    {
        return BasicRunnableEvent1.create();
    }

    /**
     * Run the callbacks that have been registered to this event.
     */
    void run(T arg);
}
