package qub;

/**
 * A class that wraps an object with critical-section-only-access behavior.
 * @param <T> The type of object that this class wraps.
 */
public class Locked<T>
{
    private final T value;
    private final Mutex mutex;

    /**
     * Create a new Locked wrapper around the provided value.
     * @param value The value to lock.
     */
    public Locked(T value)
    {
        this(value, new SpinMutex());
    }

    /**
     * Create a new Locked wrapper around the provided value.
     * @param value The value to lock.
     * @param mutex The mutex that will be used to lock access to the value.
     */
    public Locked(T value, Mutex mutex)
    {
        PreCondition.assertNotNull(mutex, "mutex");

        this.value = value;
        this.mutex = mutex;
    }

    /**
     * Perform the provided action with this Locked object's value within a critical section.
     * @param action The action to perform with this Locked object's value within a critical
     *               section.
     */
    public void get(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        mutex.criticalSection(() -> action.run(value));
    }

    /**
     * Perform the provided function with this Locked object's value within a critical section.
     * @param function The function to perform with this Locked object's value within a critical
     *               section.
     */
    public <U> U get(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return mutex.criticalSection(() -> function.run(value));
    }
}
