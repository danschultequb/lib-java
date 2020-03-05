package qub;

/**
 * An event that will be run with two arguments.
 */
public interface RunnableEvent2<T1,T2> extends Event2<T1,T2>, Action2<T1,T2>
{
    /**
     * Create a new event.
     */
    static <T1,T2> RunnableEvent2<T1,T2> create()
    {
        return BasicRunnableEvent2.create();
    }

    /**
     * Run the callbacks that have been registered to this event.
     */
    void run(T1 arg1, T2 arg2);
}
