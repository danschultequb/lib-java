package qub;

/**
 * An interface for a function that takes no arguments and doesn't return anything.
 */
public interface Action0 extends Runnable
{
    /**
     * The function to run.
     */
    void run();

    /**
     * An Action0 that does nothing.
     */
    Action0 empty = () -> {};
}
