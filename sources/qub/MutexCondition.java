package qub;

public interface MutexCondition
{
    /**
     * Release this condition's mutex and wait to be signaled that this condition is satisfied.
     * @return The result of awaiting for this condition.
     */
    Result<Void> await();

    /**
     * Release this condition's mutex and wait to be signaled that this condition is satisfied.
     * @param timeout The maximum amount of time to wait for the condition to be signaled.
     * @return The result of awaiting for this condition.
     */
    Result<Void> await(Duration timeout);

    /**
     * Release this condition's mutex and wait to be signaled that this condition is satisfied.
     * @param timeout The DateTime at which this thread will abort waiting for this condition to be
     *                signaled.
     * @return The result of awaiting for this condition.
     */
    Result<Void> await(DateTime timeout);

    /**
     * Signal any awaiting threads that this condition is now satisfied.
     */
    void signalAll();
}
