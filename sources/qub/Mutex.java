package qub;

/**
 * A synchronization primitive that permits a thread to lock a section of code so that only a single
 * thread will enter at a time.
 */
public interface Mutex
{
    /**
     * Get whether or not this SpinMutex is currently acquired.
     * @return Whether or not this SpinMutex is currently acquired.
     */
    boolean isAcquired();

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    void acquire();

    /**
     * Attempt to acquire this SpinMutex and return whether or not it was acquired.
     * @return Whether or not the SpinMutex was acquired.
     */
    boolean tryAcquire();

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    boolean release();

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    void criticalSection(Action0 action);

    /**
     * Run the provided function after this Mutex has been acquired and automatically release the
     * Mutex when the function completes.
     * @param function The function to run after acquiring this Mutex.
     * @return The return value from the function.
     */
    <T> T criticalSection(Function0<T> function);

    /**
     * Acquire this mutex and return a Disposable that will release this mutex when it is disposed.
     * @return
     */
    Disposable criticalSection();
}
